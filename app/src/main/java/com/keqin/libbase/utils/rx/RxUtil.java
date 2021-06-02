package com.keqin.libbase.utils.rx;

import com.keqin.libbase.utils.logger.L;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava方法组合
 *
 * @author Created by gold on 2017/5/19 17:25
 */
public final class RxUtil {

    private static final String TAG = "RxUtil";

    private RxUtil() {
    }

    /**
     * 等待time毫秒后在主线程返回
     *
     * @param time 时间 ms
     */
    public static Observable<Long> waitMain(long time) {
        return Observable
                .timer(time, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 倒计时，主线程返回
     *
     * @param time 最大时间 seconds
     */
    public static Observable<Long> countdown(long time) {
        return Observable
                .interval(1, TimeUnit.SECONDS)
                .take(time)
                .map(aLong -> time - aLong - 1)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 定时器，主线程返回
     *
     * @param time 时间 seconds 间隔多少时间执行一次
     */
    public static Observable<Long> countUp(long time) {
        return Observable
                .interval(time, TimeUnit.SECONDS)
                .take(Integer.MAX_VALUE)
                .map(aLong -> aLong + 1)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 应用调度器io_main
     *
     * @param <T> 类型
     */
    public static <T> ObservableTransformer<T, T> applyScheduler() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 将返回值包装成rxJava
     *
     * @param callable 调用
     */
    public static <T> Observable<T> wrap(Callable<T> callable) {
        return Observable.defer(() -> {
            T result;
            try {
                result = callable.call();
            } catch (Throwable e) {
                return Observable.error(e);
            }
            return Observable.just(result);
        });
    }

    /**
     * 运行不用返回的Observable
     *
     * @param observable Observable
     */
    public static <T> void runNotObservable(Observable<T> observable) {
        runNotObservable(observable, TAG);
    }

    /**
     * 运行不用返回的Observable
     *
     * @param observable Observable
     * @param tag        出现错误时的打印tag
     */
    public static <T> void runNotObservable(Observable<T> observable, String tag) {
        observable.subscribe(t -> {
        }, throwable -> {
            L.e(tag, throwable);
        });
    }
}
