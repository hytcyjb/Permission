package com.example.administrator.testpressiom;

/**
 * Interface used by {@linkOnShowRationale} methods to allow for continuation
 * or cancellation of a permission request.
 */
public interface PermissionRequest {

    void proceed();

    void cancel();
}
