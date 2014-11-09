package uk.co.chrisjenx.calligraphy;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

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
     * @param base ContextBase to Wrap
     */
    public static ContextWrapper wrap(Context base) {
        return new CalligraphyContextWrapper(base);
    }

    public static ActivityFactory2 get(Activity activity) {
        if (!(activity.getLayoutInflater() instanceof CalligraphyLayoutInflater)) {
            throw new RuntimeException("This activity does not wrap the Base Context! See CalligraphyContextWrapper.wrap(Context)");
        }
        return (ActivityFactory2) activity.getLayoutInflater();
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
