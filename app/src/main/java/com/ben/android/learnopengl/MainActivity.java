package com.ben.android.learnopengl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.ben.android.learnopengl.filter.BigEyesFilter;
import com.ben.android.learnopengl.util.AndroidUtilities;
import com.ben.android.learnopengl.widgets.CameraView;
import com.ben.android.learnopengl.widgets.RecordButton;

// fbo的实现可以参考：
// https://lneway.github.io/2017/10/23/Android%E5%AE%9E%E6%97%B6%E6%BB%A4%E9%95%9C%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86/
public class MainActivity extends AppCompatActivity {
    private final String TAG = "opengl MainActivity";

    private RecordButton recordButton;
    private CameraView cameraView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = findViewById(R.id.cameraView);
        recordButton = findViewById(R.id.recordButton);
        recordButton.setOnLongClickListener(new RecordButton.OnLongClickListener() {
            @Override
            public void onLongClick() {
                Log.d(TAG, "onLongClick() ");
                cameraView.startRecord();
            }
            @Override
            public void onNoMinRecord(int currentTime) {
                Log.d(TAG, "onNoMinRecord() ");
            }
            @Override
            public void onRecordFinishedListener() {
                Log.d(TAG, "onRecordFinishedListener() ");
                cameraView.stopRecord();
            }
        });
    }
}
