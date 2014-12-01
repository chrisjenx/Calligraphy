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
     * @param defaultFontAssetPath a path to a font file in the assets folder, e.g. "fonts/Roboto-light.ttf",
     *                             passing null will default to the device font-family.
     * @return The just created CalligraphyConfig.
     */
    public static CalligraphyConfig initDefault(String defaultFontAssetPath) {
        return mInstance = new CalligraphyConfig(defaultFontAssetPath);
    }

    /**
     * Init only the custom attribute to lookup.
     *
     * @param defaultAttributeId the custom attribute to look for.
     * @return The just created CalligraphyConfig.
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
     * @return The just created CalligraphyConfig.
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
    private boolean mReflection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    /**
     * Use Reflection to intercept CustomView inflation with the correct Context.
     */
    private boolean mCustomViewCreation = true;

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
     * <p>Turn of the use of Reflection to inject the private factory.
     * This has operational consequences! Please read and understand before disabling.
     * <b>This is already disabled on pre Honeycomb devices. (API 11)</b></p>
     *
     * <p> If you disable this you will need to override your {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * as this is set as the {@link android.view.LayoutInflater} private factory.</p>
     * <br>
     * <b> Use the following code in the Activity if you disable FactoryInjection:</b>
     * <pre><code>
     * {@literal @}Override
     * {@literal @}TargetApi(Build.VERSION_CODES.HONEYCOMB)
     * public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
     *   return CalligraphyContextWrapper.onActivityCreateView(this, parent, super.onCreateView(parent, name, context, attrs), name, context, attrs);
     * }
     * </code></pre>
     */
    public void disablePrivateFactoryInjection() {
        mReflection = false;
    }

    /**
     * Due to the poor inflation order where custom views are created and never returned inside an
     * {@code onCreateView(...)} method. We have to create CustomView's at the latest point in the
     * overrideable injection flow.
     *
     * On HoneyComb+ this is inside the {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * Pre HoneyComb this is in the {@link android.view.LayoutInflater.Factory#onCreateView(String, android.util.AttributeSet)}
     *
     * We wrap base implimentations, so if you LayoutInflater/Factory/Activity creates the
     * custom view before we get to this point, your view is used. (Such is the case with the
     * TintEditText etc)
     *
     * The problem is, the native methods pass there parents context to the constructor in a really
     * specific place. We have to mimic this in {@link uk.co.chrisjenx.calligraphy.CalligraphyLayoutInflater#createCustomViewInternal(android.view.View, android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * To mimic this we have to use reflection as the Class construtor args are hidden to us.
     *
     * We have discussed other means of doing this but this is the only semi-clean way of doing it.
     * (Without having to do proxy classes etc).
     *
     * Calling this will of course speed up inflation by turning off reflection, but not by much,
     * But if you want Calligraphy to inject the correct typeface then you will need to make sure your CustomView's
     * are created before reaching the LayoutInflater onViewCreated.
     */
    public void disableCustomViewInflation() {
        mCustomViewCreation = false;
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

    public boolean isCustomViewCreation() {
        return mCustomViewCreation;
    }

    /**
     * @return the custom attrId to look for, -1 if not set.
     */
    public int getAttrId() {
        return mAttrId;
    }
}
