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

    private CalligraphyLayoutInflater mInflater;

    private final int mAttributeId;

    /**
     * Uses the default configuration from {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig}
     *
     * Remember if you are defining default in the
     * {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base ContextBase to Wrap.
     * @return ContextWrapper to pass back to the activity.
     */
    public static ContextWrapper wrap(Context base) {
        return new CalligraphyContextWrapper(base);
    }

    /**
     * Get the Calligraphy Activity Fragment Instance to allow callbacks for when views are created.
     *
     * @param activity The activity the original that the ContextWrapper was attached too.
     * @return Interface allowing you to call onActivityViewCreated
     */
    public static CalligraphyActivityFactory2 get(Activity activity) {
        if (!(activity.getLayoutInflater() instanceof CalligraphyLayoutInflater)) {
            throw new RuntimeException("This activity does not wrap the Base Context! See CalligraphyContextWrapper.wrap(Context)");
        }
        return (CalligraphyActivityFactory2) activity.getLayoutInflater();
    }

    public static View onActivityCreateView(Activity activity, View parent, View view, String name, Context context, AttributeSet attr) {
        return get(activity).onActivityCreateView(parent, view, name, context, attr);
    }

    /**
     * Uses the default configuration from {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig}
     *
     * Remember if you are defining default in the
     * {@link uk.co.chrisjenx.calligraphy.CalligraphyConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base ContextBase to Wrap
     */
    CalligraphyContextWrapper(Context base) {
        super(base);
        mAttributeId = CalligraphyConfig.get().getAttrId();
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
     * @deprecated TODO
     */
    @Deprecated
    public CalligraphyContextWrapper(Context base, int attributeId) {
        super(base);
        mAttributeId = attributeId;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = new CalligraphyLayoutInflater(LayoutInflater.from(getBaseContext()), this, mAttributeId);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }

}
