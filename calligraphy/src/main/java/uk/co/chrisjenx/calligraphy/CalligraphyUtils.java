package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public final class CalligraphyUtils {

    public static final boolean applyFontToTextView(final TextView textView, final Typeface typeface) {
        if (textView == null || typeface == null) return false;
        textView.setTypeface(typeface);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
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

    static final String pullFontPath(Context context, AttributeSet attrs, int attributeId) {
        final String attributeName = context.getResources().getResourceEntryName(attributeId);
        final int stringResourceId = attrs.getAttributeResourceValue(null, attributeName, -1);
        return stringResourceId > 0
                ? context.getString(stringResourceId)
                : attrs.getAttributeValue(null, attributeName);
    }

    static final String pullFontPathFromStyle(Context context, AttributeSet attrs, int attributeId) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{attributeId, android.R.attr.textAppearance});
        try {
            // First defined attribute
            String fontFromAttribute = typedArray.getString(0);
            Log.i("CAL", "font style " + fontFromAttribute);
            fontFromAttribute = null;
            if (TextUtils.isEmpty(fontFromAttribute)) {
                // Not in the style look in Attributes, second in the attrs[]
                fontFromAttribute = pullFontPathFromTextAppearance(context, typedArray.getResourceId(1, -1), attributeId);
            }
            return fontFromAttribute;
        } finally {
            typedArray.recycle();
        }
    }

    static final String pullFontPathFromTextAppearance(final Context context, final int appearanceResId, final int attributeId) {
//        Log.i("CAL", "font appearance id " + String.valueOf(appearanceResId));
//        if (appearanceResId != -1) {
//            final Resources.Theme theme = context.getTheme();
//            theme.obtainStyledAttributes()
//            final TypedArray appearance = context.obtainStyledAttributes(appearanceResId, new int[]{attributeId, android.R.attr.fontFamily, android.R.attr.textSize});
//            Log.i("CAL", "font appearance array " + String.valueOf(appearance) + " ");
//            for (int i = 0; i < appearance.getIndexCount(); i++) {
//                Log.i("CAL", "font appearance name " + appearance.getString(i));
//                Log.i("CAL", "font appearance name " + appearance.getResourceId(i, -1));
//            }
//            try {
//                return appearance.getString(0);
//            } finally {
//                appearance.recycle();
//            }
//        }
        //TODO: Fix
        return null;
    }

    static final String pullFontPathFromTheme(Context context, int styleId, int attributeId) {
        final Resources.Theme theme = context.getTheme();
        final TypedValue value = new TypedValue();

        theme.resolveAttribute(styleId, value, true);
        final TypedArray typedArray = theme.obtainStyledAttributes(value.resourceId, new int[]{attributeId});
        try {
            return typedArray.getString(0);
        } finally {
            typedArray.recycle();
        }
    }

    private CalligraphyUtils() {
    }
}
