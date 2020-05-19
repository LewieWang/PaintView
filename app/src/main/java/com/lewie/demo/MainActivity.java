package com.lewie.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.lewie.paintview.ImageUtils;
import com.lewie.paintview.PaintView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String imgName =  System.currentTimeMillis() + "img.jpg";

    private final String imgPath = Environment.getExternalStorageDirectory() + "/DCIM/img/";

    private PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paint_view);
        paintView.setEraserWidth(20f)
                .setPaintColor(Color.RED)
                .setPaintWidth(20f)
                .setBackgroundResource(R.mipmap.timg);
    }


    public void modePen(View view){
        paintView.setMode(PaintView.MODE_PEN);
    }

    public void modeEraser(View view){
        paintView.setMode(PaintView.MODE_ERASER);
    }

    public void undo(View view){
        paintView.undo();
    }

    public void redo(View view){
        paintView.redo();
    }

    public void clear(View view){
        paintView.clear();
    }

    public void save(View view){
        Bitmap bitmap = ImageUtils.view2Bitmap(paintView);

        ImageUtils.save(bitmap,new File(imgPath + imgName ),Bitmap.CompressFormat.JPEG);
    }
}
