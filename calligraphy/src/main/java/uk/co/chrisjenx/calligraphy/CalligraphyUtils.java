package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public final class CalligraphyUtils {

    private static final int[] R_styleable_TextView = new int[]{
        /* 0 */android.R.attr.fontFamily,
    };
    private static final int TextView_fontFamily = 0;

    public static final boolean applyFontToTextView(final TextView textView, final Typeface typeface) {
        if (textView == null || typeface == null) return false;
        textView.setTypeface(typeface);
        return true;
    }

    public static final boolean applyFontToTextView(final Context context, final TextView textView, final String filePath) {
        if (textView == null || context == null) return false;
        final AssetManager assetManager = context.getAssets();
        final Typeface typeface = TypefaceUtils.load(assetManager, filePath);
        return applyFontToTextView(textView, typeface);
    }

    public static final void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig config) {
        if (context == null || textView == null || config == null) return;
        if (!config.isFontSet()) return;
        applyFontToTextView(context, textView, config.getFontPath());
    }

    public static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig config, final String textViewFont) {
        if (context == null || textView == null || config == null) return;
        if (!TextUtils.isEmpty(textViewFont) && applyFontToTextView(context, textView, textViewFont)) {
            return;
        }
        applyFontToTextView(context, textView, config);
    }

    /**
     * Pulls out the fontFamily from the attributes to see if the user has set a custom font
     *
     * @return
     */
    static final String pullFontFamily(Context context, AttributeSet attrs) {
        if (context == null || attrs == null) return null;
        final TypedArray a = context.obtainStyledAttributes(attrs, R_styleable_TextView);
        // Use the thickness specified, zero being the default
        final String fontFamily = a.getString(TextView_fontFamily);
        a.recycle();

        return fontFamily;
    }

    private CalligraphyUtils() {
    }
}
