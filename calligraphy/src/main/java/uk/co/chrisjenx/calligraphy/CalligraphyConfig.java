package uk.co.chrisjenx.calligraphy;

import android.text.TextUtils;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public class CalligraphyConfig {

    private static CalligraphyConfig mInstance;

    /**
     * Init the Calligraphy Config file. Each time you call this you set a new default. Of course setting this multiple
     * times during runtime could have undesired effects.
     *
     * @param defaultFontAssetPath a path to a font file in the assets folder, e.g. "fonts/roboto-light.ttf",
     *                             passing null will default to the device font-family.
     */
    public static void initDefault(String defaultFontAssetPath) {
        mInstance = new CalligraphyConfig(defaultFontAssetPath);
    }

    /**
     * Init only the custom attribute to lookup.
     *
     * @param defaultAttributeId the custom attribute to look for instead of fontFamily attribute.
     * @see #initDefault(String, int)
     */
    public static void initDefault(int defaultAttributeId) {
        mInstance = new CalligraphyConfig(defaultAttributeId);
    }

    /**
     * Define the default font and the custom attribute to lookup globally.
     *
     * @param defaultFontAssetPath path to a font file in the assets folder, e.g. "fonts/roboto-light.ttf",
     * @param defaultAttributeId   the custom attribute to look for instead of fontFamily attribute.
     * @see #initDefault(String)
     * @see #initDefault(int)
     */
    public static void initDefault(String defaultFontAssetPath, int defaultAttributeId) {
        mInstance = new CalligraphyConfig(defaultFontAssetPath, defaultAttributeId);
    }

    static CalligraphyConfig get() {
        if (mInstance == null)
            mInstance = new CalligraphyConfig();
        return mInstance;
    }


    private final String mFontPath;
    private final boolean mIsFontSet;
    private final int mAttrId;

    private CalligraphyConfig() {
        this(null, 0);
    }

    private CalligraphyConfig(int attrId) {
        this(null, attrId);
    }

    private CalligraphyConfig(String defaultFontAssetPath) {
        this(defaultFontAssetPath, 0);
    }

    private CalligraphyConfig(String defaultFontAssetPath, int attrId) {
        this.mFontPath = defaultFontAssetPath;
        mIsFontSet = !TextUtils.isEmpty(defaultFontAssetPath);
        mAttrId = attrId;
    }

    /**
     * @return mFontPath for text views might be null
     */
    String getFontPath() {
        return mFontPath;
    }

    /**
     * @return true if set, false if null|empty
     */
    boolean isFontSet() {
        return mIsFontSet;
    }

    /**
     * @return the custom attrId to look for, default to fontFamily if not set.
     */
    public int getAttrId() {
        if (mAttrId == 0)
            return android.R.attr.fontFamily;
        return mAttrId;
    }
}
