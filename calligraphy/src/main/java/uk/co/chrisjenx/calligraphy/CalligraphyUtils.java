package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public final class CalligraphyUtils {

    /**
     * Applies a custom typeface span to the text.
     *
     * @param s        text to apply it too.
     * @param typeface typeface to apply.
     * @return Either the passed in Object or new Spannable with the typeface span applied.
     */
    public static CharSequence applyTypefaceSpan(CharSequence s, Typeface typeface) {
        if (s != null && s.length() > 0) {
            if (!(s instanceof Spannable)) {
                s = new SpannableString(s);
            }
            ((Spannable) s).setSpan(TypefaceUtils.getSpan(typeface), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    /**
     * Applies a Typeface to a TextView.
     * Defaults to false for deferring, if you are having issues with the textview keeping
     * the custom Typeface, use
     * {@link #applyFontToTextView(android.widget.TextView, android.graphics.Typeface, boolean)}
     *
     * @param textView Not null, TextView or child of.
     * @param typeface Not null, Typeface to apply to the TextView.
     * @return true if applied otherwise false.
     * @see #applyFontToTextView(android.widget.TextView, android.graphics.Typeface, boolean)
     */
    public static boolean applyFontToTextView(final TextView textView, final Typeface typeface) {
        return applyFontToTextView(textView, typeface, false);
    }

    /**
     * Applies a Typeface to a TextView, if deferred,its recommend you don't call this multiple
     * times, as this adds a TextWatcher.
     *
     * Deferring should really only be used on tricky views which get Typeface set by the system at
     * weird times.
     *
     * @param textView Not null, TextView or child of.
     * @param typeface Not null, Typeface to apply to the TextView.
     * @param deferred If true we use Typefaces and TextChange listener to make sure font is always
     *                 applied, but this sometimes conflicts with other
     *                 {@link android.text.Spannable}'s.
     * @return true if applied otherwise false.
     * @see #applyFontToTextView(android.widget.TextView, android.graphics.Typeface)
     */
    public static boolean applyFontToTextView(final TextView textView, final Typeface typeface, boolean deferred) {
        if (textView == null || typeface == null) return false;
        textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        textView.setTypeface(typeface);
        if (deferred) {
            textView.setText(applyTypefaceSpan(textView.getText(), typeface), TextView.BufferType.SPANNABLE);
            textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    applyTypefaceSpan(s, typeface);
                }
            });
        }
        return true;
    }

    /**
     * Useful for manually fonts to a TextView. Will not default back to font
     * set in {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig}
     *
     * @param context  Context
     * @param textView Not null, TextView to apply to.
     * @param filePath if null/empty will do nothing.
     * @return true if fonts been applied
     */
    public static boolean applyFontToTextView(final Context context, final TextView textView, final String filePath) {
        return applyFontToTextView(context, textView, filePath, false);
    }

    static boolean applyFontToTextView(final Context context, final TextView textView, final String filePath, boolean deferred) {
        if (textView == null || context == null) return false;
        final AssetManager assetManager = context.getAssets();
        final Typeface typeface = TypefaceUtils.load(assetManager, filePath);
        return applyFontToTextView(textView, typeface, deferred);
    }

    static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig config) {
        applyFontToTextView(context, textView, config, false);
    }

    static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig config, boolean deferred) {
        if (context == null || textView == null || config == null) return;
        if (!config.isFontSet()) return;
        applyFontToTextView(context, textView, config.getFontPath(), deferred);
    }

    /**
     * Applies font to TextView. Will fall back to the default one if not set.
     *
     * @param context      context
     * @param textView     textView to apply to.
     * @param config       Default Config
     * @param textViewFont nullable, will use Default Config if null or fails to find the
     *                     defined font.
     */
    public static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig config, final String textViewFont) {
        applyFontToTextView(context, textView, config, textViewFont, false);
    }

    static void applyFontToTextView(final Context context, final TextView textView, final CalligraphyConfig config, final String textViewFont, boolean deferred) {
        if (context == null || textView == null || config == null) return;
        if (!TextUtils.isEmpty(textViewFont) && applyFontToTextView(context, textView, textViewFont, deferred)) {
            return;
        }
        applyFontToTextView(context, textView, config, deferred);
    }

    /**
     * Tries to pull the Custom Attribute directly from the TextView.
     *
     * @param context     Activity Context
     * @param attrs       View Attributes
     * @param attributeId if -1 returns null.
     * @return null if attribute is not defined or added to View
     */
    static String pullFontPathFromView(Context context, AttributeSet attrs, int attributeId) {
        if (attributeId == -1 || attrs == null)
            return null;

        final String attributeName;
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

    /**
     * Tries to pull the Font Path from the View Style as this is the next decendent after being
     * defined in the View's xml.
     *
     * @param context     Activity Activity Context
     * @param attrs       View Attributes
     * @param attributeId if -1 returns null.
     * @return null if attribute is not defined or found in the Style
     */
    static String pullFontPathFromStyle(Context context, AttributeSet attrs, int attributeId) {
        if (attributeId == -1 || attrs == null)
            return null;
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{attributeId});
        if (typedArray != null) {
            try {
                // First defined attribute
                String fontFromAttribute = typedArray.getString(0);
                if (!TextUtils.isEmpty(fontFromAttribute)) {
                    return fontFromAttribute;
                }
            } catch (Exception ignore) {
                // Failed for some reason.
            } finally {
                typedArray.recycle();
            }
        }
        return null;
    }

    /**
     * Tries to pull the Font Path from the Text Appearance.
     *
     * @param context     Activity Context
     * @param attrs       View Attributes
     * @param attributeId if -1 returns null.
     * @return returns null if attribute is not defined or if no TextAppearance is found.
     */
    static String pullFontPathFromTextAppearance(final Context context, AttributeSet attrs, int attributeId) {
        if (attributeId == -1 || attrs == null) {
            return null;
        }

        int textAppearanceId = -1;
        final TypedArray typedArrayAttr = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.textAppearance});
        if (typedArrayAttr != null) {
            try {
                textAppearanceId = typedArrayAttr.getResourceId(0, -1);
            } catch (Exception ignored) {
                // Failed for some reason
                return null;
            } finally {
                typedArrayAttr.recycle();
            }
        }

        final TypedArray textAppearanceAttrs = context.obtainStyledAttributes(textAppearanceId, new int[]{attributeId});
        if (textAppearanceAttrs != null) {
            try {
                return textAppearanceAttrs.getString(0);
            } catch (Exception ignore) {
                // Failed for some reason.
                return null;
            } finally {
                textAppearanceAttrs.recycle();
            }
        }
        return null;
    }

    /**
     * Last but not least, try to pull the Font Path from the Theme, which is defined.
     *
     * @param context     Activity Context
     * @param styleAttrId Theme style id
     * @param attributeId if -1 returns null.
     * @return null if no theme or attribute defined.
     */
    static String pullFontPathFromTheme(Context context, int styleAttrId, int attributeId) {
        if (styleAttrId == -1 || attributeId == -1)
            return null;

        final Resources.Theme theme = context.getTheme();
        final TypedValue value = new TypedValue();

        theme.resolveAttribute(styleAttrId, value, true);
        final TypedArray typedArray = theme.obtainStyledAttributes(value.resourceId, new int[]{attributeId});
        try {
            String font = typedArray.getString(0);
            return font;
        } catch (Exception ignore) {
            // Failed for some reason.
            return null;
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Last but not least, try to pull the Font Path from the Theme, which is defined.
     *
     * @param context        Activity Context
     * @param styleAttrId    Theme style id
     * @param subStyleAttrId the sub style from the theme to look up after the first style
     * @param attributeId    if -1 returns null.
     * @return null if no theme or attribute defined.
     */
    static String pullFontPathFromTheme(Context context, int styleAttrId, int subStyleAttrId, int attributeId) {
        if (styleAttrId == -1 || attributeId == -1)
            return null;

        final Resources.Theme theme = context.getTheme();
        final TypedValue value = new TypedValue();

        theme.resolveAttribute(styleAttrId, value, true);
        int subStyleResId = -1;
        final TypedArray parentTypedArray = theme.obtainStyledAttributes(value.resourceId, new int[]{subStyleAttrId});
        try {
            subStyleResId = parentTypedArray.getResourceId(0, -1);
        } catch (Exception ignore) {
            // Failed for some reason.
            return null;
        } finally {
            parentTypedArray.recycle();
        }

        if (subStyleResId == -1) return null;
        final TypedArray subTypedArray = context.obtainStyledAttributes(subStyleResId, new int[]{attributeId});
        if (subTypedArray != null) {
            try {
                return subTypedArray.getString(0);
            } catch (Exception ignore) {
                // Failed for some reason.
                return null;
            } finally {
                subTypedArray.recycle();
            }
        }
        return null;
    }

    private static Boolean sToolbarCheck = null;

    /**
     * See if the user has added appcompat-v7, this is done at runtime, so we only check once.
     *
     * @return true if the v7.Toolbar is on the classpath
     */
    static boolean canCheckForV7Toolbar() {
        if (sToolbarCheck == null) {
            try {
                Class.forName("android.support.v7.widget.Toolbar");
                sToolbarCheck = Boolean.TRUE;
            } catch (ClassNotFoundException e) {
                sToolbarCheck = Boolean.FALSE;
            }
        }
        return sToolbarCheck;
    }

    private CalligraphyUtils() {
    }

}
