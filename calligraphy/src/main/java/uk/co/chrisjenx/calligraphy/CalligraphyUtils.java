package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import static uk.co.chrisjenx.calligraphy.ReflectionUtils.getStaticFieldValue;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public final class CalligraphyUtils {

    static final int[] R_Styleable_TextView;
    static final int[] R_Styleable_TextAppearance;
    static final int R_Styleable_TextView_textAppearance;
    static final int R_Styleable_TextAppearance_fontFamily;

    static {
        Class<?> styleableClass = ReflectionUtils.getClass("com.android.internal.R$styleable");
        if (styleableClass != null) {
            R_Styleable_TextView = getStaticFieldValue(styleableClass, "TextView", null);
            R_Styleable_TextAppearance = getStaticFieldValue(styleableClass, "TextAppearance", null);
            R_Styleable_TextView_textAppearance = getStaticFieldValue(styleableClass, "TextView_textAppearance", -1);
            R_Styleable_TextAppearance_fontFamily = getStaticFieldValue(styleableClass, "TextAppearance_fontFamily", -1);
        } else {
            R_Styleable_TextView = null;
            R_Styleable_TextAppearance = null;
            R_Styleable_TextView_textAppearance = -1;
            R_Styleable_TextAppearance_fontFamily = -1;
        }
    }

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
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{attributeId});
        if (typedArray != null) {
            try {
                // First defined attribute
                String fontFromAttribute = typedArray.getString(0);
                if (!TextUtils.isEmpty(fontFromAttribute)) {
                    return fontFromAttribute;
                }
            } finally {
                typedArray.recycle();
            }
        }
        return pullFontPathFromTextAppearance(context, attrs);
    }

    static final String pullFontPathFromTextAppearance(final Context context, AttributeSet attrs) {
        if (R_Styleable_TextView == null || R_Styleable_TextAppearance == null
                || R_Styleable_TextView_textAppearance == -1 || R_Styleable_TextAppearance_fontFamily == -1) {
            return null;
        }

        TypedArray textViewAttrs = context.obtainStyledAttributes(attrs, R_Styleable_TextView);
        if (textViewAttrs == null) {
            return null;
        }

        int textAppearanceId = textViewAttrs.getResourceId(R_Styleable_TextView_textAppearance, -1);
        TypedArray textAppearanceAttrs = context.obtainStyledAttributes(textAppearanceId, R_Styleable_TextAppearance);
        if (textAppearanceAttrs == null) {
            return null;
        }

        return textAppearanceAttrs.getString(R_Styleable_TextAppearance_fontFamily);
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
