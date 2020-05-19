package com.lewie.paintview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class PaintView extends View {

    public static final int MODE_PEN = 0;

    public static final int MODE_ERASER = 1;

    public static final int MODE_TEXT = 2;

    private static final float TOUCH_TOLERANCE = 4;

    private int mode = MODE_PEN;

    private Canvas mCanvas;

    private Path mPath;

    private Paint mPaint;

    private Paint mEraserPaint;

    private Paint mDrawPaint;

    private Bitmap mBitmap;

    private ArrayList<PathData> savePath;

    private ArrayList<PathData> deletePath;

    private PathData pd;

    private float mX, mY;

    public PaintView(Context c) {
        super(c);
        initCanvas();
        savePath = new ArrayList<>();
        deletePath = new ArrayList<>();

    }

    public PaintView(Context c, AttributeSet attrs) {
        super(c, attrs);
        initCanvas();
        savePath = new ArrayList<>();
        deletePath = new ArrayList<>();
    }


    public void initCanvas() {

        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setColor(Color.RED);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(10);


        mPaint = mDrawPaint;

        mEraserPaint = new Paint();
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeWidth(10);
        mEraserPaint.setColor(Color.WHITE);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mPath = new Path();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mCanvas == null) {
            mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }



    class PathData {
        Path path;
        Paint paint;
    }


    /**
     * 撤销
     */
    public void undo() {

        if (savePath != null && savePath.size() > 0) {

            mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

            PathData drawPath = savePath.get(savePath.size() - 1);

            deletePath.add(drawPath);

            savePath.remove(savePath.size() - 1);

            for (PathData pathData : savePath) {
                mCanvas.drawPath(pathData.path, pathData.paint);
            }
            invalidate();
        }
    }

    /**
     * 反撤销
     */
    public void redo() {
        if (deletePath.size() > 0) {

            PathData dp = deletePath.get(deletePath.size() - 1);

            savePath.add(dp);

            mCanvas.drawPath(dp.path, dp.paint);

            deletePath.remove(deletePath.size() - 1);

            invalidate();
        }
    }

    /**
     * 清空画板
     */
    public void clear() {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        savePath.clear();
        deletePath.clear();
        invalidate();
    }

    /**
     * 设置画笔颜色
     * @param color
     */
    public PaintView setPaintColor(int color){
        mEraserPaint.setColor(color);
        return this;
    }

    /**
     * 设置画笔宽度
     * @param width
     * @return
     */
    public PaintView setPaintWidth(float width){
        mDrawPaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置橡皮檫宽度
     * @param width
     * @return
     */
    public PaintView setEraserWidth(float width){
        mEraserPaint.setStrokeWidth(width);
        return this;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mPath = new Path();
                pd = new PathData();
                pd.path = mPath;
                pd.paint = mPaint;

                mPath.moveTo(x, y);
                mX = x;
                mY = y;
//                invalidate(); //清屏
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

                    mCanvas.drawPath(mPath, mPaint);

//                    mPath.lineTo(mX, mY);

                    mX = x;
                    mY = y;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPath.lineTo(mX, mY);

                savePath.add(pd);

                mPath = null;

                invalidate();
                break;
        }
        return true;
    }

    public void setMode(int m) {
        switch (m) {
            case MODE_PEN:
                mode = MODE_PEN;
                mPaint = mDrawPaint;
                break;

            case MODE_ERASER:
                mode = MODE_ERASER;
                mPaint = mEraserPaint;
                break;

            case MODE_TEXT:
                mode = MODE_TEXT;
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setTextSize(22);
                break;
        }
    }

    public Bitmap getBitmap(){
        return this.mBitmap;
    }

}
 