package com.ben.android.learnopengl.filter;

import android.content.Context;

public abstract class Filter {
    // protected String TAG = this.getClass().getSimpleName();
    private final String TAG = "opengl Filter";
    
    protected Context context;
    protected int width;
    protected int height;
    protected String vertexShader;
    protected String fragmentShader;

    public Filter(Context context) {
        this.context = context;
        vertexShader = getVertexShader(context);     // 顶点着色器程序
        fragmentShader = getFragmentShader(context); // 片元着色器程序
        initialize();
    }

    protected abstract String getVertexShader(Context context);
    protected abstract String getFragmentShader(Context context);

    public void onSurfaceChanged(int width,int height){
        this.width = width;
        this.height = height;
    }

    protected void initialize() { }
    protected void release() { }
    
    public int render(int texture, float [] matrix){
        return texture;
    }
}
