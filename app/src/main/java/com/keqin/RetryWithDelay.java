package com.keqin;

import android.support.annotation.IntRange;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    public static final int INFINITE_RETRY = -1;

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    /**
     * 构造方法
     *
     * @param maxRetries       最大重试次数 -1为无限重试
     * @param retryDelayMillis 每次重试时间
     */
    public RetryWithDelay(@IntRange(from = -1) int maxRetries, @IntRange(from = 1) int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) {
        return observable.flatMap(throwable -> {
            if (maxRetries == INFINITE_RETRY || ++retryCount <= maxRetries) {
                return Observable.timer(retryCount * retryDelayMillis, TimeUnit.MILLISECONDS);
            }
            return Observable.error(throwable);
        });
    }
}