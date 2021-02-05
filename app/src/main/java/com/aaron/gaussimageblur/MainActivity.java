package com.aaron.gaussimageblur;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aaron.gaussblur.GaussBlur;
import com.aaron.gaussblur.ColorFilter;

public class MainActivity extends AppCompatActivity {
    public static final float DEFAULT_BLUR_RADIUS = 10.0f;
    private ImageView ivBlurImage;
    private Button btnBlur, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        ivBlurImage = findViewById(R.id.iv_example_image);
        btnBlur = findViewById(R.id.btn_blur);
        btnReset = findViewById(R.id.btn_reset);
        btnBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                blurImage(R.drawable.example_image, DEFAULT_BLUR_RADIUS, ScaleValue.SCALE_VALUE_8);
                Bitmap oriBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.example_image);
                ivBlurImage.setImageBitmap(colorFilterImage(oriBitmap));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBlurImage.setImageResource(R.drawable.example_image);
            }
        });
    }

    public void blurImage(int resId, float radius, float scale){
        if (!isValidRadius(radius)){
            return;
        }
        Bitmap bitmap = GaussBlur.with(this)
                .bitmap(resId)
                .blurRadius(radius)
                .scale(scale)
                .blur();
        ivBlurImage.setImageBitmap(bitmap);
    }

    public Bitmap colorFilterImage(Bitmap oriBitmap){
        return ColorFilter.with()
                .red(76f)
                .saturation(2f)
                .colorFilter(oriBitmap);
    }

    private boolean isValidRadius(float radius){
        return radius > 0f && radius <= 25.0f;
    }
}