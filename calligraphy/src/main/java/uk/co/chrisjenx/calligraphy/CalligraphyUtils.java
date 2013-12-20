package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public final class CalligraphyUtils {

    public static final void applyFontToTextView(final TextView textView, final Typeface typeface) {
        if (textView == null || typeface == null) return;
        textView.setTypeface(typeface);
    }

    public static final void applyFontToTextView(final Context context, final TextView textView, final String filePath) {
        if(textView == null || context == null) return;
        final AssetManager assetManager = context.getAssets();
        final Typeface typeface = TypefaceUtils.load(assetManager, filePath);
        applyFontToTextView(textView, typeface);
    }

    public static final void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig config) {
        if(context == null || textView == null || config == null) return;
        if(!config.isFontSet()) return;
        applyFontToTextView(context, textView, config.getFontPath());
    }

    private CalligraphyUtils() {
    }
}
