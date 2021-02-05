package com.aaron.gaussblur;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import androidx.annotation.FloatRange;

/**
 * @author aaron
 * @date 2021/02/04
 */
public class ColorFilter {
    /**
     * 颜色轴，分别代表红绿蓝
     */
    public static final int RED_AXIS = 0;
    public static final int GREEN_AXIS = 1;
    public static final int BLUE_AXIS = 2;
    private static final ColorFilter sInstance = new ColorFilter();
    /**
     * 色调：红色
     */
    private float mRedHue = 0;
    /**
     * 色调：绿色
     */
    private float mGreenHue = 0;
    /**
     * 色调：蓝色
     */
    private float mBlueHue = 0;
    /**
     * 饱和度，值越大表示颜色越鲜艳。最大为1；最小为0时，呈现黑白效果
     */
    private float mSaturation = 1f;
    /**
     * 亮度，值越大越亮（白），默认是1，为原图亮度
     */
    private float mBrightness = 1f;
    private Paint paint;

    private ColorFilter() {
        paint = new Paint();
    }

    public static ColorFilter with() {
        return sInstance;
    }

    public ColorFilter red(float redHue){
        this.mRedHue = redHue;
        return this;
    }

    public ColorFilter green(float greenHue){
        this.mGreenHue = greenHue;
        return this;
    }

    public ColorFilter blue(float blueHue){
        this.mBlueHue = blueHue;
        return this;
    }

    public ColorFilter saturation(float saturation){
        this.mSaturation = saturation;
        return this;
    }

    public ColorFilter brightness(float brightness){
        this.mBrightness = brightness;
        return this;
    }

    public Bitmap colorFilter(Bitmap oriBitmap){
        if (oriBitmap == null){
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(oriBitmap.getWidth(), oriBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        setMatrix();
        canvas.drawBitmap(oriBitmap, 0, 0, paint);
        return bitmap;
    }

    private void setMatrix(){
        ColorMatrix hueMatrix = new ColorMatrix();
        if (mRedHue > 0){
            hueMatrix.setRotate(RED_AXIS, mRedHue);
        }
        if (mGreenHue > 0){
            hueMatrix.setRotate(GREEN_AXIS, mGreenHue);
        }
        if (mBlueHue > 0){
            hueMatrix.setRotate(BLUE_AXIS, mBlueHue);
        }
        ColorMatrix saturationMatrix = new ColorMatrix();
        if (mSaturation <= 1.0f || mSaturation >= 0) {
            saturationMatrix.setSaturation(mSaturation);
        }
        ColorMatrix brightnessMatrix = new ColorMatrix();
        brightnessMatrix.setScale(mBrightness, mBrightness, mBrightness, 1);

        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(brightnessMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
    }
    /**
     * 通过setRotate方法设置色调，实质是将传入色调值作为角度，计算弧长。
     * 然后计算cos和sin值，构造颜色变换矩阵。cos和sin都是周期2π的周期函数
     * @param hue
     * @return
     */
    public float handleHueValue(float hue){
        return 0;
    }
}
