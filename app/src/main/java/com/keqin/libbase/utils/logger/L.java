package com.keqin.libbase.utils.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.keqin.libservice.server.impl.ServerConfig;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 打印日志
 *
 * @author Created by gold on 2018/3/6 11:52
 */
public class L {

    private static final int JSON_INDENT = 2;
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";

    private L() {
    }

    public static void d(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.d(message, args);
    }

    public static void d(@NonNull String tag, @NonNull Object object) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.d(object);
    }

    public static void e(@NonNull String tag, @NonNull Throwable throwable) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        String message = throwable.getMessage();
        Logger.e(throwable, TextUtils.isEmpty(message) ? "null message" : message);
    }

    public static void e(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.e(message, args);
    }

    public static void e(@NonNull String tag, @Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.e(throwable, message, args);
    }

    public static void w(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.w(message, args);
    }

    public static void i(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.i(message, args);
    }

    public static void v(@NonNull String tag, String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.v(message, args);
    }

    public static void wtf(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.wtf(message, args);
    }

    public static void json(@NonNull String tag, @NonNull String json) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.json(json);
    }

    public static void json(@NonNull String tag, @Nullable String message, @NonNull String... jsons) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);

        try {
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(message)) {
                sb.append(message)
                        .append("\n")
                        .append(SINGLE_DIVIDER)
                        .append("\n");
            }

            for (int i = 0, count = jsons.length; i < count; i++) {
                String json = jsons[i];
                if (TextUtils.isEmpty(json)) {
                    continue;
                }

                if (json.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(json);
                    String formatJson = jsonObject.toString(JSON_INDENT);
                    sb.append(formatJson);
                } else if (json.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(json);
                    String formatJson = jsonArray.toString(JSON_INDENT);
                    sb.append(formatJson);
                }
                if (i < count - 1) {
                    sb.append("\n")
                            .append(SINGLE_DIVIDER)
                            .append("\n");
                }
            }
            if (sb.length() > 0) {
                Logger.d(sb.toString());
                return;
            }
            Logger.e("Invalid Json");
        } catch (JSONException e) {
            Logger.e("Invalid Json");
        }
    }

    public static void xml(@NonNull String tag, @NonNull String xml) {
        if (ServerConfig.isRelease()) return;
        Logger.t(tag);
        Logger.xml(xml);
    }

    public static void is(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Log.i(tag, createMessage(message, args));
    }

    public static void vs(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Log.v(tag, createMessage(message, args));
    }

    public static void ds(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Log.d(tag, createMessage(message, args));
    }

    public static void ws(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Log.w(tag, createMessage(message, args));
    }

    public static void es(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        if (ServerConfig.isRelease()) return;
        Log.e(tag, createMessage(message, args));
    }

    private static String createMessage(String message, Object... args) {
        return args == null || args.length == 0 ? message : String.format(message, args);
    }

}
