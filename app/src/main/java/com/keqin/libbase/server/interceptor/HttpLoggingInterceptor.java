package com.keqin.libbase.server.interceptor;

import android.text.TextUtils;

import com.keqin.libbase.server.ServerManager;
import com.keqin.libbase.utils.logger.L;

import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * 日志拦截器
 *
 * @author Created by gold on 2018/4/26 09:55
 */
public final class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public HttpLoggingInterceptor() {
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        LinkedHashMap<String, String> params = new LinkedHashMap<>();

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();

        String method = request.method();
        HttpUrl httpUrl = request.url();

        String url = httpUrl.toString();
        url = String.format("%s%s", url.substring(0, url.indexOf('?')),
                (connection != null ? " " + connection.protocol() : ""));

        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                params.put("Content-Type", String.valueOf(requestBody.contentType()));
            }
            if (requestBody.contentLength() != -1) {
                params.put("Content-Length", requestBody.contentLength() + "-byte");
            }
        }

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                params.put(name, headers.value(i));
            }
        }

        String requestHeaderJson = mapToJson(params);
        String requestBodyParams = httpUrl.queryParameter("params");
        String requestBodyJson = null;

        if (!TextUtils.isEmpty(requestBodyParams)) {
            requestBodyJson = ServerManager.get().getServerEncrypt().decrypt(requestBodyParams);
        }

        if (hasRequestBody) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (TextUtils.isEmpty(requestBodyParams) && isPlaintext(buffer) && charset != null) {
                requestBodyParams = buffer.readString(charset);
                params.put("params", requestBodyParams);
                requestBodyJson = mapToJson(params);
            }
        }

        //request结束

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        boolean hasResponseBody = responseBody != null;

        long contentLength = hasResponseBody ? responseBody.contentLength() : 0;

        if (hasResponseBody) {
            if (responseBody.contentType() != null) {
                params.put("Content-Type", String.valueOf(responseBody.contentType()));
            }
            params.put("Content-Length", contentLength + "-byte");
        }

        Headers responseHeaders = response.headers();
        for (int i = 0, count = responseHeaders.size(); i < count; i++) {
            String name = responseHeaders.name(i);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                params.put(name, responseHeaders.value(i));
            }
        }

        String responseHeaderJson = null;
        String responseBodyJson = null;

        if (hasResponseBody) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Long gzippedLength = null;
            if ("gzip".equalsIgnoreCase(responseHeaders.get("Content-Encoding"))) {
                gzippedLength = buffer.size();
                GzipSource gzippedResponseBody = null;
                try {
                    gzippedResponseBody = new GzipSource(buffer.clone());
                    buffer = new Buffer();
                    buffer.writeAll(gzippedResponseBody);
                } finally {
                    if (gzippedResponseBody != null) {
                        gzippedResponseBody.close();
                    }
                }
            }

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (!isPlaintext(buffer)) {
                return response;
            }

            if (gzippedLength != null) {
                params.put("Content-Length", buffer.size() + "-byte, " + gzippedLength + "-gzipped-byte body");
            } else {
                params.put("Content-Length", buffer.size() + "-byte");
            }

            responseHeaderJson = mapToJson(params);

            if (contentLength != 0 && charset != null) {
                responseBodyJson = buffer.clone().readString(charset);
            }
        }

        String formatUrl = String.format(Locale.getDefault(), "%s：%s (%d ms)", method, url, tookMs);
        if (!TextUtils.isEmpty(requestBodyParams)) {
            formatUrl += "\nParams：" + requestBodyParams;
        }
        L.json("Http", formatUrl, requestHeaderJson, requestBodyJson, responseHeaderJson, responseBodyJson);

        return response;
    }

    private String mapToJson(Map<String, String> param) {
        if (param.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(param);
        param.clear();
        return jsonObject.toString();
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }
}
