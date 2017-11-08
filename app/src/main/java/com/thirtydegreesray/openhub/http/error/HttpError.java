

package com.thirtydegreesray.openhub.http.error;

/**
 * 网络请求错误
 * Created on 2016/10/19.
 *
 * @author ThirtyDegreesRay
 */

public class HttpError extends Error {

    private int errorCode = -1;

    public HttpError(int errorCode) {
        super(HttpErrorCode.getErrorMsg(errorCode));
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
