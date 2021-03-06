package com.example.aslapp;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.util.List;

class Classifier {
    private static final String TAG = "Tflite";
    private static final String MODEL = "mobilenet.tflite";
    private static final String LABEL = "markers.txt";


    private static final int DIM_HEIGHT = 80;
    private static final int DIM_WIDTH = 80;
    private static final int BYTES = 4;


    private static String result;
    private Interpreter tflite;
    private List<String> markers;
    private ByteBuffer imgData;
    private float[][] a1;

    Classifier(Activity activity) throws IOException {
        MappedByteBuffer tfliteModel = FileUtil.loadMappedFile(activity, MODEL);
        Interpreter.Options tfliteOptions = new Interpreter.Options();
        tfliteOptions.setNumThreads(4);
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        markers = FileUtil.loadLabels(activity, LABEL);
        imgData = ByteBuffer.allocateDirect(DIM_HEIGHT * DIM_WIDTH * BYTES);
        imgData.order(ByteOrder.nativeOrder());
        a1 = new float[1][markers.size()];
    }

    void classifyMat(Mat mat) {
        if (tflite != null) {
            convertMatToByteBuffer(mat);
            runInterface();
        }
    }

    String getResult() {
        return result;
    }

    void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
    }

    Mat processMat(Mat mat) {
        float mh = mat.height();
        float cw = (float) Resources.getSystem().getDisplayMetrics().widthPixels;
        float scale = mh / cw * 0.7f;
        Rect roi = new Rect((int) (mat.cols() / 2 - (mat.cols() * scale / 2)),
                (int) (mat.rows() / 2 - (mat.cols() * scale / 2)),
                (int) (mat.cols() * scale),
                (int) (mat.cols() * scale));
        Mat sub = mat.submat(roi);
        sub.copyTo(mat.submat(roi));

        Mat edges = new Mat(sub.size(), CvType.CV_8UC1);
        Imgproc.Canny(sub, edges, 50, 200);

        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(3, 3));
        Imgproc.dilate(edges, edges, element1);

        Core.rotate(edges, edges, Core.ROTATE_90_CLOCKWISE);
        Imgproc.resize(edges, edges, new Size(DIM_WIDTH, DIM_HEIGHT));
        return edges;
    }

    Mat debugMat(Mat mat) {

        Mat edges = new Mat(mat.size(), CvType.CV_8UC1);
        Imgproc.Canny(mat, edges, 50, 200);

        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(3, 3));
        Imgproc.dilate(edges, edges, element1);

        return edges;
    }

    private void convertMatToByteBuffer(Mat mat) {
        imgData.rewind();
        for (int i = 0; i < DIM_HEIGHT; ++i) {
            for (int j = 0; j < DIM_WIDTH; ++j) {
                Log.d(TAG, "" + mat.get(i, j)[0]);
                imgData.putFloat((float) mat.get(i, j)[0] / 255.0f);
            }
        }
    }

    private void runInterface() {
        if (imgData != null) {
            tflite.run(imgData, a1);
        }
        processResults(a1[0]);
        for (int i = 0; i < markers.size(); i++) {
            Log.d(TAG, markers.get(i) + ": " + a1[0][i]);
        }
        Log.d(TAG, "Guess: " + getResult());
    }

    private void processResults(float[] prob) {
        int max = 0;
        for (int i = 0; i < prob.length; ++i) {
            if (prob[i] > prob[max]) {
                max = i;
            }
        }
        if (prob[max] > 0.8f) {
            result = markers.get(max);
        } else {
            result = "NOTHING";

        }
    }
}
