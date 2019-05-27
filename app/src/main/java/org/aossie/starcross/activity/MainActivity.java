package org.aossie.starcross.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import org.aossie.starcross.R;
import org.aossie.starcross.renderer.SkyRenderer;
import org.aossie.starcross.util.MiscUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MiscUtil.getTag(MainActivity.class);
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glSurfaceView = findViewById(R.id.surface);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new SkyRenderer());
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "OnPause");

        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "OnResume");

        super.onResume();
        glSurfaceView.onResume();
    }

}
