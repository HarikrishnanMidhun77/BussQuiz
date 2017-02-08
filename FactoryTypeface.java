package com.quizapp.hp.quiz;

/**
 * Created by Bineesh P Babu on 06-09-2015.
 */
import android.content.Context;
import android.graphics.Typeface;

/**
 * Typeface creation.
 *
 * @author Sotti https://plus.google.com/+PabloCostaTirado/about
 */
public class FactoryTypeface
{
    public static Typeface createTypeface(Context context, int typeface)
    {
        return Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s.ttf", context.getString(typeface)));
    }
}