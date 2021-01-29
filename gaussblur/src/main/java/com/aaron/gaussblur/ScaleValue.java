package com.aaron.gaussblur;

import androidx.annotation.FloatRange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@FloatRange(from = 0.0f, to = 1.0f)
public @interface ScaleValue {
    float SCALE_VALUE_2 = 1 / 2f;
    float SCALE_VALUE_4 = 1 / 4f;
    float SCALE_VALUE_8 = 1 / 8f;
    float SCALE_VALUE_16 = 1 / 16f;
    float SCALE_VALUE_32 = 1 / 32f;
}
