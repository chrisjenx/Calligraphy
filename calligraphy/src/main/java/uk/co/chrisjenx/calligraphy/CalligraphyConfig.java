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

    static CalligraphyConfig get() {
        if (mInstance == null)
            throw new IllegalStateException("You must initDefault for CalligraphyConfig, if you are going to use the CalligraphyContextWrapper");
        return mInstance;
    }


    private final String mFontPath;
    private final boolean mIsFontSet;

    private CalligraphyConfig(String defaultFontAssetPath) {
        this.mFontPath = defaultFontAssetPath;
        mIsFontSet = !TextUtils.isEmpty(defaultFontAssetPath);
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
}
