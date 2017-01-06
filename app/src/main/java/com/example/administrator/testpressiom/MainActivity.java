package com.example.administrator.testpressiom;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    /****
     * 分4个步骤
	 * 详细分解，说明
     * 2016年5月24日12:52:09
     * yjbo
     */
    private String Tag = "MainPress";
    int REQUEST_CODE_REQUEST_PERMISSION = 0x0001;
    private BitmapView bitmapView = null;
    private static final String[] PERMISSION_SHOWCAMERA = new String[] {"android.permission.CAMERA"};
    private  final int REQUEST_SHOWCAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_camear).setOnClickListener(this);
//        bitmapView = new BitmapView(this);
//        setContentView(bitmapView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_camear:
                insertDataToSDCard();
//                initCamear();
                break;
        }
    }


    /**
     * 向SD卡插入数据
     */
    private void insertDataToSDCard() {
        if (PermissionUtils.hasSelfPermissions(this, PERMISSION_SHOWCAMERA)) {
            showCamera();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(this, PERMISSION_SHOWCAMERA)) {
                showRationaleForCamera(new ShowCameraPermissionRequest(this));
            } else {
                ActivityCompat.requestPermissions(this, PERMISSION_SHOWCAMERA, REQUEST_CODE_REQUEST_PERMISSION);
                show("00====正常的相机请求权限");
            }
        }
    }
    //1---1.如果权限申请成功就走这里，同时也是操作这里的时候请求的
    private void showCamera() {
        show("11---相机有了权限了");
    }
    //3.拒绝之后调用：（小米的直接走这里不走第2步，华为的可以先走2，）
    private void onCameraDenied() {
        show("33相机启动失败");
    }
    //4.拒绝之后，再次请求，小米不会调用这里，华为可以走这里
    private void onCameraNeverAskAgain() {
        show("44相机再次请求，弹出对话框");
    }
    //2.2.拒绝之后，再次请求，小米不会调用这里，华为可以走这里，这是弹出一个弹窗，然后可以允许，允许之后就再次跳出系统的请求权限的对话框
    private void showRationaleForCamera(PermissionRequest request) {
        //2.拒绝之后，再次请求，小米不会调用这里，华为可以走这里，这是弹出一个弹窗，然后可以允许，允许之后就再次跳出系统的请求权限的对话框
        showRationaleDialog(R.string.requset_camear, request);
        show("22====相机请求权限对话框");
    }

    private void show(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_REQUEST_PERMISSION) {
            if (PermissionUtils.getTargetSdkVersion(this) < 23 && !PermissionUtils.hasSelfPermissions(this, PERMISSION_SHOWCAMERA)) {
                onCameraDenied();
                return;
            }
            if (PermissionUtils.verifyPermissions(grantResults)) {
                showCamera();
            } else {
                if (!PermissionUtils.shouldShowRequestPermissionRationale(this, PERMISSION_SHOWCAMERA)) {
                    onCameraNeverAskAgain();
//                    show("44相机再次请求，弹出对话框");
                } else {
                    onCameraDenied();
//                    show("33-22相机启动失败");
                }
            }
        }
    }



    private  final class ShowCameraPermissionRequest implements PermissionRequest {
        private final WeakReference<Activity> weakTarget;

        private ShowCameraPermissionRequest(Activity target) {
            this.weakTarget = new WeakReference<>(target);
        }

        @Override
        public void proceed() {
            Activity target = weakTarget.get();
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);
        }

        @Override
        public void cancel() {
            Activity target = weakTarget.get();
            if (target == null) return;
//            target.onCameraDenied();
        }
    }


    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("不允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }


    //打开相机
    private static final int CAMERA_ID = 0;

    private CameraPreview mPreview;
    private Camera mCamera;
    private void initCamear() {
        // Open an instance of the first camera and retrieve its info.
        mCamera = getCameraInstance(CAMERA_ID);
        Camera.CameraInfo cameraInfo = null;

        if (mCamera != null) {
            // Get camera info only if the camera is available
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }


        // Get the rotation of the screen to adjust the preview image accordingly.
        final int displayRotation = getWindowManager().getDefaultDisplay()
                .getRotation();

        // Create the Preview view and set it as the content of this Activity.
        mPreview = new CameraPreview(MainActivity.this, mCamera, cameraInfo, displayRotation);
//        FrameLayout preview = (FrameLayout) root.findViewById(R.id.camera_preview);
//        preview.addView(mPreview);

    }
    /** A safe way to get an instance of the Camera object. */
    public  Camera getCameraInstance(int cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(Tag, "Camera " + cameraId + " is not available: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}
