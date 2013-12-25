package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public final class CalligraphyUtils {

    public static boolean applyFontToTextView(final TextView textView, final Typeface typeface) {
        if (textView == null || typeface == null) return false;
        textView.setTypeface(typeface);
        return true;
    }

    public static boolean applyFontToTextView(final Context context, final TextView textView, final String filePath) {
        if (textView == null || context == null) return false;
        final AssetManager assetManager = context.getAssets();
        final Typeface typeface = TypefaceUtils.load(assetManager, filePath);
        return applyFontToTextView(textView, typeface);
    }

    public static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig config) {
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

    static String pullFontFamily(Context context, int attributeId, AttributeSet attrs) {
        final String attributeName = context.getResources().getResourceEntryName(attributeId);
        final int stringResourceId = attrs.getAttributeResourceValue(null, attributeName, -1);
        return stringResourceId > 0
            ? context.getString(stringResourceId)
            : attrs.getAttributeValue(null, attributeName);
    }

    static String pullFontFamilyFromStyle(Context context, int attributeId, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{attributeId});
        try {
            return typedArray.getString(0);
        }finally {
            typedArray.recycle();
        }
    }

    static String pullFontFamilyFromTheme(Context context, int attributeId, int styleId) {
        final Resources.Theme theme = context.getTheme();
        final TypedValue value = new TypedValue();

        theme.resolveAttribute(styleId, value, true);
        final TypedArray typedArray = theme.obtainStyledAttributes(value.resourceId, new int[]{attributeId});
        try {
            return typedArray.getString(0);
        }finally {
            typedArray.recycle();
        }
    }

    private CalligraphyUtils() {
    }
}
