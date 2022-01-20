package com.ben.android.learnopengl.util;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

public class CameraHelper implements Camera.PreviewCallback {
    private static final String TAG = "opengl CameraHelper";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private int mCameraId;
    private Camera mCamera;
    private byte[] buffer; // 存放onPreviewFrame()中预览数据的缓冲区 ？
    private Camera.PreviewCallback mPreviewCallback;
    private SurfaceTexture mSurfaceTexture;

    public CameraHelper(int cameraId) {
        mCameraId = cameraId;
    }

    public void startPreview(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = surfaceTexture;
        try {
            // 获得camera对象
            mCamera = Camera.open(mCameraId);
            // 配置camera的属性
            Camera.Parameters parameters = mCamera.getParameters();
            // 设置预览数据格式为nv21
            parameters.setPreviewFormat(ImageFormat.NV21);
            // 这是摄像头宽、高
            parameters.setPreviewSize(WIDTH HEIGHT);
            //  设置摄像头 图像传感器的角度、方向
            mCamera.setParameters(parameters); // 将这些参数配置给相机使用

            buffer = new byte[WIDTH * HEIGHT * 3 / 2]; // 存放onPreviewFrame()中预览数据的缓冲区 ？ 这块儿还是比较绕，要多想一下
            // 数据缓存区
            mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(this); // 为相机设置这样一个回调
            // 它跟setPreviewCallback的工作方式一样，但是要求指定一个字节数组作为缓冲区，用于预览帧数据，这样能够更好的管理预览帧数据时使用的内存。
            // 在调用Camera.startPreview()接口前，我们需要setPreviewCallbackWithBuffer，
            // 而setPreviewCallbackWithBuffer之前我们需要重新addCallbackBuffer，
            // 因为setPreviewCallbackWithBuffer 使用时需要指定一个字节数组作为缓冲区，用于预览图像数据 即addCallbackBuffer，然后你在onPerviewFrame中的data才会有值；
            // setPreviewCallbackWithBuffer需要在startPreview()之前调用，
            // 因为setPreviewCallbackWithBuffer使用时需要指定一个字节数组作为缓冲区，用于预览帧数据，
            // 所以我们需要在setPreviewCallbackWithBuffer之前调用addCallbackBuffer，这样onPreviewFrame的data才有值
            // 设置预览画面
            mCamera.setPreviewTexture(mSurfaceTexture); // 相面所预览的内容显示在这样一块纹理上
            mCamera.startPreview();
            // 我们设置addCallbackBuffer的地方有两个，一个是在startPreview之前，一个是在onPreviewFrame中，
            // 这两个都需要调用，如果在onPreviewFrame中不调用，那么，就无法继续回调到onPreviewFrame中了。
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 首先定义一个类实现Camera.PreviewCallback接口，然后在它的onPreviewFrame(byte[] data, Camera camera)方法中即可接收到每一帧的预览数据，也就是参数data。
    // 然后使用 setPreviewCallback()、setOneShotPreviewCallback 或 setPreviewCallbackWithBuffer()注册回调接口
    public void setPreviewCallback(Camera.PreviewCallback previewCallback) { // 提供 为相机设置一个onPreview回调 的接口
        // 一旦使用此方法注册预览回调接口，onPreviewFrame()方法会一直被调用，直到camera preview销毁
        // onPreviewFrame()方法跟Camera.open()是运行于同一个线程，所以为了防止onPreviewFrame()会阻塞UI线程，将Camera.open()放置在子线程中运行。
        mPreviewCallback = previewCallback;
    }
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //  data数据依然是倒的
        if (mPreviewCallback != null) 
            mPreviewCallback.onPreviewFrame(data, camera); // 让回调拿这些数据去渲染吧。。。
        camera.addCallbackBuffer(buffer); // 将此缓冲区添加到预览回调缓冲区队列中
        // 添加一个预分配的缓冲区到预览回调缓冲区队列中。应用程序可一添加一个或多个缓冲器到这个队列中。
        // 当预览帧数据到达时并且缓冲区队列仍然有至少一个可用的缓冲区时，这个 缓冲区将会被消耗掉然后从队列中移除，然后这个缓冲区会调用预览回调接口。
        // 如果预览帧数据到达时没有剩余的缓冲区，这帧数据将会被丢弃。当缓冲区中的数据处理完成后，应用程序应该将这个缓冲区添加回缓冲区队列中。
    }
    // void setOneShotPreviewCallback (Camera.PreviewCallback cb) {}
    // 使用此方法注册预览回调接口时，会将下一帧数据回调给onPreviewFrame()方法，调用完成后这个回调接口将被销毁。也就是只会回调一次预览帧数据。

    public void switchCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        stopPreview();
        startPreview(mSurfaceTexture);
    }
    public int getCameraId() {
        return mCameraId;
    }
    public void stopPreview() {
        if (mCamera != null) {
            // 预览数据回调接口
            mCamera.setPreviewCallback(null); 
            // 停止预览
            mCamera.stopPreview();
            // 释放摄像头
            mCamera.release();
            mCamera = null;
        }
    }
}
