package com.blm.qiubopay.helpers.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.blm.qiubopay.R;
import com.blm.qiubopay.tools.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

//import com.example.android.multidex.myCApplication.R;

public class HSignatureView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private boolean moved = false;
    private List<PointF> pointList;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public HSignatureView(Context context, AttributeSet attr) {
        super(context, attr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF0000ee);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4);

        mBitmap = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();

        pointList = new ArrayList<PointF>();

        super.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath, mPaint);
        if(pointList.size() > 0){
            for(PointF pointF:pointList){
                canvas.drawPoint(pointF.x, pointF.y, mPaint);
            }
        }
    }

    private void TouchStart(float x, float y) {

        moved = false;
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

        if(this.isDrawingCacheEnabled()){
            this.setDrawingCacheEnabled(false);
        }
    }

    private void TouchMove(float x, float y) {

        moved = true;
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void TouchUp() {

        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        if(!moved){
            PointF pointF = new PointF(mX, mY);
            pointList.add(pointF);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                TouchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                TouchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                TouchUp();
                invalidate();
                break;
        }

        return true;
    }

    public void ClearCanvas() {
        mPath = new Path();
        pointList.clear();
        invalidate();
    }

    public byte[] getBytes() {
        Bitmap b = getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public Bitmap getBitmap() {
        /*View v = (View) this.getParent();
        Bitmap b = Bitmap.createBitmap(v.getHeight(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);*/

        if(isDrawingCacheEnabled()){
            setDrawingCacheEnabled(false);
        }

        setDrawingCacheEnabled(true);
        Bitmap sign = blackNwhite(getDrawingCache());

        return sign;
    }

    public String base64(){

        if(isDrawingCacheEnabled()){
            setDrawingCacheEnabled(false);
        }

        setDrawingCacheEnabled(true);
        Bitmap sign = blackNwhite(getDrawingCache());

        return  ImageUtil.convert(sign);
    }

    public Bitmap blackNwhite(Bitmap bitmap)
    {

        Bitmap bmpMonochrome = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bmpMonochrome;

    }

}
