package com.example.aslapp;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final int CLASSIFY_INTERVAL = 20;
    private static final String TAG = "RecognitionActivity";

    private Classifier classifier;
    private Mat frame;
    private Mat mRGBA;
    private JavaCameraView openCvCameraView;

    private TextView resultTextView;
    private AlertDialog dialog;

    private Boolean isDebug = false;
    private Boolean isEdge = false;

    private String text = "";
    private int counter = 0;

    private BaseLoaderCallback baseloadercallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            if (status == BaseLoaderCallback.SUCCESS)
                openCvCameraView.enableView();
            else
                super.onManagerConnected(status);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FrameLayout layout = new FrameLayout(this);
        layout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(layout);

        int mCameraIndex = 0;
        openCvCameraView = new JavaCameraView(this, mCameraIndex);
        openCvCameraView.setCvCameraViewListener(CameraActivity.this);
        openCvCameraView.setVisibility(SurfaceView.VISIBLE);
        openCvCameraView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        layout.addView(openCvCameraView);

        resultTextView = new TextView(this);
        resultTextView.setTextColor(Color.WHITE);
        resultTextView.setBackgroundColor(Color.BLACK);
        resultTextView.setTextSize(20f);
        resultTextView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL));
        layout.addView(resultTextView);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "Connected camera.");
            baseloadercallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.d(TAG, "Camera not connected.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseloadercallback);
        }

        int windowVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(windowVisibility);

        try {
            classifier = new Classifier(this);
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize classifier", e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (openCvCameraView != null) {
            openCvCameraView.disableView();
        }
        if (classifier != null) {
            classifier.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (openCvCameraView != null) {
            openCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        if (mRGBA != null) mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        float mh = mRGBA.height();
        float cw = (float) Resources.getSystem().getDisplayMetrics().widthPixels;
        float scale = mh / cw * 0.7f;

        mRGBA = inputFrame.rgba();
        frame = classifier.processMat(mRGBA);

        if (!isDebug) {
            if (counter == CLASSIFY_INTERVAL) {
                runInterpreter();
                counter = 0;
            } else {
                counter++;
            }
        }

        Imgproc.rectangle(mRGBA,
                new Point(mRGBA.cols() / 2f - (mRGBA.cols() * scale / 2),
                        mRGBA.rows() / 2f - (mRGBA.cols() * scale / 2)),
                new Point(mRGBA.cols() / 2f + (mRGBA.cols() * scale / 2),
                        mRGBA.rows() / 2f + (mRGBA.cols() * scale / 2)),
                new Scalar(0, 255, 0), 1);
        if (isEdge) {
            mRGBA = classifier.debugMat(mRGBA);
        }

        System.gc();
        return mRGBA;
    }

    private void runInterpreter() {
        if (classifier != null) {
            classifier.classifyMat(frame);
            switch (classifier.getResult()) {
                case "SPACE":
                    text += " ";
                    break;
                case "BACKSPACE":
                    text = text.substring(0, text.length() - 1);
                    break;
                case "NOTHING":
                    break;
                default:
                    text += classifier.getResult();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultTextView.setText(text);
                }
            });
        }
    }
}