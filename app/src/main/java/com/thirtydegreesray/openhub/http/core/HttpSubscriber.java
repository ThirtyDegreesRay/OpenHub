

package com.thirtydegreesray.openhub.http.core;

import retrofit2.Response;
import rx.Subscriber;

/**
 * 网络请求订阅者，处理网络请求的返回，判断数据有效性，同时处理网络请求异常情况<br>
 * Created by ThirtyDegreesRay on 2016/7/15 11:19
 */
public class HttpSubscriber<T> extends Subscriber<Response<T>> {

    private HttpObserver<T> mObserver;

    public HttpSubscriber() {
    }

    public HttpSubscriber(HttpObserver<T> observer) {
        mObserver = observer;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (mObserver != null)
            mObserver.onError(e);
    }

    @Override
    public void onNext(Response<T> r) {
        if (mObserver != null)
            mObserver.onSuccess(new HttpResponse<>(r));
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
