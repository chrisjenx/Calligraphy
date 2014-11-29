package uk.co.chrisjenx.calligraphy;

import android.os.Build;
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
    public static CalligraphyConfig initDefault(String defaultFontAssetPath) {
        return mInstance = new CalligraphyConfig(defaultFontAssetPath);
    }

    /**
     * Init only the custom attribute to lookup.
     *
     * @param defaultAttributeId the custom attribute to look for.
     * @see #initDefault(String, int)
     */
    public static CalligraphyConfig initDefault(int defaultAttributeId) {
        return mInstance = new CalligraphyConfig(defaultAttributeId);
    }

    /**
     * Define the default font and the custom attribute to lookup globally.
     *
     * @param defaultFontAssetPath path to a font file in the assets folder, e.g. "fonts/Roboto-light.ttf",
     * @param defaultAttributeId   the custom attribute to look for.
     * @see #initDefault(String)
     * @see #initDefault(int)
     */
    public static CalligraphyConfig initDefault(String defaultFontAssetPath, int defaultAttributeId) {
        return mInstance = new CalligraphyConfig(defaultFontAssetPath, defaultAttributeId);
    }

    static CalligraphyConfig get() {
        if (mInstance == null)
            mInstance = new CalligraphyConfig();
        return mInstance;
    }


    private final String mFontPath;
    private final boolean mIsFontSet;
    private final int mAttrId;

    /**
     * Use Reflection to inject the private factory. Doesn't exist pre HC. so defaults to false.
     */
    private boolean mReflection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? true : false;

    private CalligraphyConfig() {
        this(null, -1);
    }

    private CalligraphyConfig(int attrId) {
        this(null, attrId);
    }

    private CalligraphyConfig(String defaultFontAssetPath) {
        this(defaultFontAssetPath, -1);
    }

    private CalligraphyConfig(String defaultFontAssetPath, int attrId) {
        this.mFontPath = defaultFontAssetPath;
        mIsFontSet = !TextUtils.isEmpty(defaultFontAssetPath);
        mAttrId = attrId != -1 ? attrId : -1;
    }

    /**
     * Turn of the use of Reflection to inject the private factory.
     * This has operational concenquences! Please read and understand before disabling.
     * <b>This is already disabled on pre Honeycomb devices. (API 11)</b>
     *
     * If you disable this you will need to override your {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * as this is set as the {@link android.view.LayoutInflater} private factory.
     *
     * <pre>
     * {@code
     *
     * @Override
     * @TargetApi(Build.VERSION_CODES.HONEYCOMB) public View onCreateView(View parent, String name, @NonNull Context context, @NonNull AttributeSet attrs) {
     * return CalligraphyContextWrapper.onActivityCreateView(this, parent, super.onCreateView(parent, name, context, attrs), name, context, attrs);
     * }
     * }
     * </pre>
     */
    public void disableReflection() {
        mReflection = false;
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

    public boolean isReflection() {
        return mReflection;
    }

    /**
     * @return the custom attrId to look for, -1 if not set.
     */
    public int getAttrId() {
        return mAttrId;
    }
}
