package uk.co.chrisjenx.calligraphy;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
public class CalligraphyContextWrapper extends ContextWrapper {

    static final String CALLIGRAPHY_CONFIG_SERVICE = CalligraphyConfig.class.getName();

    private CalligraphyLayoutInflater mInflater;

    private final int mAttributeId;
    private final CalligraphyConfig mCalligraphyConfig;

    /**
     * Convenience for calling {@link #wrap(Context, CalligraphyConfig)} with the
     * {@linkplain CalligraphyConfig#get default configuration}.
     *
     * Remember if you are defining default in the
     * {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig} make sure this is initialised before
     * the activity is created.
     */
    public static ContextWrapper wrap(Context base) {
        return wrap(base, CalligraphyConfig.get());
    }

    /**
     * Configures Calligraphy to be used in the {@code context} with the provided {@code config}.
     *
     * <p>You would rarely need to use this method directly, so unless you need a different
     * configuration in multiple contexts in you app, prefer defining a
     * {@linkplain CalligraphyConfig#initDefault default configuration} and use
     * {@link #wrap(Context)} instead.
     *
     * @return Context to use as the {@linkplain Activity#attachBaseContext base context} of your
     * Activity or for initializing a view hierarchy.
     */
    public static ContextWrapper wrap(Context base, CalligraphyConfig config) {
        return new CalligraphyContextWrapper(base, config);
    }

    /**
     * You only need to call this <b>IF</b> you call
     * {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig.Builder#disablePrivateFactoryInjection()}
     * This will need to be called from the
     * {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * method to enable view font injection if the view is created inside the activity onCreateView.
     *
     * You would implement this method like so in you base activity.
     * <pre>
     * {@code
     * public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
     *   return CalligraphyContextWrapper.onActivityCreateView(this, parent, super.onCreateView(parent, name, context, attrs), name, context, attrs);
     * }
     * }
     * </pre>
     *
     * @param activity The activity the original that the ContextWrapper was attached too.
     * @param parent   Parent view from onCreateView
     * @param view     The View Created inside onCreateView or from super.onCreateView
     * @param name     The View name from onCreateView
     * @param context  The context from onCreateView
     * @param attr     The AttributeSet from onCreateView
     * @return The same view passed in, or null if null passed in.
     */
    public static View onActivityCreateView(Activity activity, View parent, View view, String name, Context context, AttributeSet attr) {
        return get(activity).onActivityCreateView(parent, view, name, context, attr);
    }

    /**
     * Get the Calligraphy Activity Fragment Instance to allow callbacks for when views are created.
     *
     * @param activity The activity the original that the ContextWrapper was attached too.
     * @return Interface allowing you to call onActivityViewCreated
     */
    static CalligraphyActivityFactory get(Activity activity) {
        if (!(activity.getLayoutInflater() instanceof CalligraphyLayoutInflater)) {
            throw new RuntimeException("This activity does not wrap the Base Context! See CalligraphyContextWrapper.wrap(Context)");
        }
        return (CalligraphyActivityFactory) activity.getLayoutInflater();
    }

    CalligraphyContextWrapper(Context base, CalligraphyConfig config) {
        super(base);
        mAttributeId = config.getAttrId();
        mCalligraphyConfig = config;
    }

    /**
     * Override the default AttributeId, this will always take the custom attribute defined here
     * and ignore the one set in {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig}.
     *
     * Remember if you are defining default in the
     * {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base        ContextBase to Wrap
     * @param attributeId Attribute to lookup.
     * @deprecated use {@link #wrap(android.content.Context)}
     */
    @Deprecated
    public CalligraphyContextWrapper(Context base, int attributeId) {
        super(base);
        mAttributeId = attributeId;
        mCalligraphyConfig = null;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = new CalligraphyLayoutInflater(LayoutInflater.from(getBaseContext()), this, mAttributeId, false);
            }
            return mInflater;
        }
        if (CALLIGRAPHY_CONFIG_SERVICE.equals(name)) {
            return mCalligraphyConfig != null ? mCalligraphyConfig : CalligraphyConfig.get();
        }
        return super.getSystemService(name);
    }

}
