package uk.co.chrisjenx.calligraphy;

import android.os.Build;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public class CalligraphyConfig {

    /**
     * The default styles for the factory to lookup. The builder builds an extended immutable
     * map of this with any additional custom styles.
     */
    private static final Map<Class<? extends TextView>, Integer> DEFAULT_STYLES = new HashMap<>();

    static {
        {
            DEFAULT_STYLES.put(TextView.class, android.R.attr.textViewStyle);
            DEFAULT_STYLES.put(Button.class, android.R.attr.buttonStyle);
            DEFAULT_STYLES.put(EditText.class, android.R.attr.editTextStyle);
            DEFAULT_STYLES.put(AutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            DEFAULT_STYLES.put(MultiAutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            DEFAULT_STYLES.put(CheckBox.class, android.R.attr.checkboxStyle);
            DEFAULT_STYLES.put(RadioButton.class, android.R.attr.radioButtonStyle);
            DEFAULT_STYLES.put(ToggleButton.class, android.R.attr.buttonStyleToggle);
        }
    }

    private static CalligraphyConfig sInstance;

    /**
     * Set the default Calligraphy Config
     *
     * @param calligraphyConfig the config build using the builder.
     * @see uk.co.chrisjenx.calligraphy.CalligraphyConfig.Builder
     */
    public static void initDefault(CalligraphyConfig calligraphyConfig) {
        sInstance = calligraphyConfig;
    }

    /**
     * The current Calligraphy Config.
     * If not set it will create a default config.
     */
    public static CalligraphyConfig get() {
        if (sInstance == null)
            sInstance = new CalligraphyConfig(new Builder());
        return sInstance;
    }

    /**
     * Is a default font set?
     */
    private final boolean mIsFontSet;
    /**
     * The default Font Path if nothing else is setup.
     */
    private final String mFontPath;
    /**
     * Default Font Path Attr Id to lookup
     */
    private final int mAttrId;
    /**
     * Use Reflection to inject the private factory.
     */
    private final boolean mReflection;
    /**
     * Use Reflection to intercept CustomView inflation with the correct Context.
     */
    private final boolean mCustomViewCreation;
    /**
     * Class Styles. Build from DEFAULT_STYLES and the builder.
     */
    private final Map<Class<? extends TextView>, Integer> mClassStyleAttributeMap;

    protected CalligraphyConfig(Builder builder) {
        mIsFontSet = builder.isFontSet;
        mFontPath = builder.fontAssetPath;
        mAttrId = builder.attrId;
        mReflection = builder.reflection;
        mCustomViewCreation = builder.customViewCreation;
        final Map<Class<? extends TextView>, Integer> tempMap = new HashMap<>(DEFAULT_STYLES);
        tempMap.putAll(builder.mStyleClassMap);
        mClassStyleAttributeMap = Collections.unmodifiableMap(tempMap);
    }

    /**
     * @return mFontPath for text views might be null
     */
    public String getFontPath() {
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

    /* default */ Map<Class<? extends TextView>, Integer> getClassStyles() {
        return mClassStyleAttributeMap;
    }

    /**
     * @return the custom attrId to look for, -1 if not set.
     */
    public int getAttrId() {
        return mAttrId;
    }

    public static class Builder {
        /**
         * Default AttrID if not set.
         */
        public static final int INVALID_ATTR_ID = -1;
        /**
         * Use Reflection to inject the private factory. Doesn't exist pre HC. so defaults to false.
         */
        private boolean reflection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        /**
         * Use Reflection to intercept CustomView inflation with the correct Context.
         */
        private boolean customViewCreation = true;
        /**
         * The fontAttrId to look up the font path from.
         */
        private int attrId = R.attr.fontPath;
        /**
         * Has the user set the default font path.
         */
        private boolean isFontSet = false;
        /**
         * The default fontPath
         */
        private String fontAssetPath = null;
        /**
         * Additional Class Styles. Can be empty.
         */
        private Map<Class<? extends TextView>, Integer> mStyleClassMap = new HashMap<>();

        /**
         * This defaults to R.attr.fontPath. So only override if you want to use your own attrId.
         *
         * @param fontAssetAttrId the custom attribute to look for fonts in assets.
         * @return this builder.
         */
        public Builder setFontAttrId(int fontAssetAttrId) {
            this.attrId = fontAssetAttrId != INVALID_ATTR_ID ? fontAssetAttrId : INVALID_ATTR_ID;
            return this;
        }

        /**
         * Set the default font if you don't define one else where in your styles.
         *
         * @param defaultFontAssetPath a path to a font file in the assets folder, e.g. "fonts/Roboto-light.ttf",
         *                             passing null will default to the device font-family.
         * @return this builder.
         */
        public Builder setDefaultFontPath(String defaultFontAssetPath) {
            this.isFontSet = !TextUtils.isEmpty(defaultFontAssetPath);
            this.fontAssetPath = defaultFontAssetPath;
            return this;
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
        public Builder disablePrivateFactoryInjection() {
            this.reflection = false;
            return this;
        }

        /**
         * Due to the poor inflation order where custom views are created and never returned inside an
         * {@code onCreateView(...)} method. We have to create CustomView's at the latest point in the
         * overrideable injection flow.
         *
         * On HoneyComb+ this is inside the {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
         * Pre HoneyComb this is in the {@link android.view.LayoutInflater.Factory#onCreateView(String, android.util.AttributeSet)}
         *
         * We wrap base implementations, so if you LayoutInflater/Factory/Activity creates the
         * custom view before we get to this point, your view is used. (Such is the case with the
         * TintEditText etc)
         *
         * The problem is, the native methods pass there parents context to the constructor in a really
         * specific place. We have to mimic this in {@link uk.co.chrisjenx.calligraphy.CalligraphyLayoutInflater#createCustomViewInternal(android.view.View, android.view.View, String, android.content.Context, android.util.AttributeSet)}
         * To mimic this we have to use reflection as the Class constructor args are hidden to us.
         *
         * We have discussed other means of doing this but this is the only semi-clean way of doing it.
         * (Without having to do proxy classes etc).
         *
         * Calling this will of course speed up inflation by turning off reflection, but not by much,
         * But if you want Calligraphy to inject the correct typeface then you will need to make sure your CustomView's
         * are created before reaching the LayoutInflater onViewCreated.
         */
        public Builder disableCustomViewInflation() {
            this.customViewCreation = false;
            return this;
        }

        /**
         * Add a custom style to get looked up. If you use a custom class that has a parent style
         * which is not part of the default android styles you will need to add it here.
         *
         * The Calligraphy inflater is unaware of custom styles in your custom classes. We use
         * the class type to look up the style attribute in the theme resources.
         *
         * So if you had a {@code MyTextField.class} which looked up it's default style as
         * {@code R.attr.textFieldStyle} you would add those here.
         *
         * {@code builder.addCustomStyle(MyTextField.class,R.attr.textFieldStyle}
         *
         * @param styleClass             the class that related to the parent styleResource. null is ignored.
         * @param styleResourceAttribute e.g. {@code R.attr.textFieldStyle}, 0 is ignored.
         * @return this builder.
         */
        public Builder addCustomStyle(final Class<? extends TextView> styleClass, final int styleResourceAttribute) {
            if (styleClass == null || styleResourceAttribute == 0) return this;
            mStyleClassMap.put(styleClass, styleResourceAttribute);
            return this;
        }

        public CalligraphyConfig build() {
            this.isFontSet = !TextUtils.isEmpty(fontAssetPath);
            return new CalligraphyConfig(this);
        }
    }
}
