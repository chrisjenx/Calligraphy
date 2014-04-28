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

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public final class CalligraphyUtils {

    private static Integer sActionBarTitleId = null;
    private static Integer sActionBarSubTitleId = null;

    /**
     * Applies a Typeface to a TextView
     *
     * @param textView Not null, TextView or child of.
     * @param typeface Not null, Typeface to apply to the TextView.
     * @return true if applied otherwise false.
     */
    public static boolean applyFontToTextView(final TextView textView, final Typeface typeface) {
        if (textView == null || typeface == null) return false;
        textView.setTypeface(typeface);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);

        if (isTextViewInActionBar(textView))
            textView.post(new Runnable() {
                @Override
                public void run() {
                    textView.setTypeface(typeface);
                }
            });

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
        String attributeName;
        try {
            attributeName = context.getResources().getResourceEntryName(attributeId);
        } catch (Resources.NotFoundException e) {
            // invalid attribute ID
            return null;
        }
        final int stringResourceId = attrs.getAttributeResourceValue(null, attributeName, -1);
        return stringResourceId > 0
                ? context.getString(stringResourceId)
                : attrs.getAttributeValue(null, attributeName);
    }

    static final String pullFontPathFromStyle(Context context, AttributeSet attrs, int attributeId) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{attributeId});
        try {
            return typedArray.getString(0);
        } catch (Exception ignore) {
            // Failed for some reason.
            return null;
        } finally {
            typedArray.recycle();
        }
    }

    static final String pullFontPathFromTheme(Context context, int styleId, int attributeId) {
        final Resources.Theme theme = context.getTheme();
        final TypedValue value = new TypedValue();

        theme.resolveAttribute(styleId, value, true);
        final TypedArray typedArray = theme.obtainStyledAttributes(value.resourceId, new int[]{attributeId});
        try {
            return typedArray.getString(0);
        } catch (Exception ignore) {
            // Failed for some reason.
            return null;
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Does a dirty search to see if the TextView is part of the ActionBar.
     * This could fail on some devices...
     *
     * @param textView checks this textview.
     */
    @SuppressWarnings("ConstantConditions")
    static final boolean isTextViewInActionBar(TextView textView) {
        if (textView == null) return false;
        if (sActionBarTitleId == null)
            sActionBarTitleId = textView.getResources().getIdentifier("action_bar_title", "id", "android");
        if (sActionBarSubTitleId == null)
            sActionBarSubTitleId = textView.getResources().getIdentifier("action_bar_subtitle", "id", "android");

        return sActionBarTitleId != null && textView.getId() == sActionBarTitleId ||
                sActionBarSubTitleId != null && textView.getId() == sActionBarSubTitleId;
    }

    private CalligraphyUtils() {
    }

}
