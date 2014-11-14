package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.view.LayoutInflater;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
class CalligraphyLayoutInflater extends LayoutInflater {

    private final int mAttributeId;

    protected CalligraphyLayoutInflater(Context context, int attributeId) {
        super(context);
        mAttributeId = attributeId;
        setUpLayoutFactory();
    }

    public CalligraphyLayoutInflater(LayoutInflater original, Context newContext, int attributeId) {
        super(original, newContext);
        mAttributeId = attributeId;
        setUpLayoutFactory();
    }

    private void setUpLayoutFactory() {
        // Don't try to set factory over the top of the current child one.
        if (!(getFactory() instanceof CalligraphyFactory)) {
            setFactory(new CalligraphyFactory(this, getFactory(), mAttributeId));
        }
        // Some cases when the inflater is cloned and not returned as the LayoutInflater from the
        // system service context.getSystemService(LAYOUT_INFLATER_SERVICE);
        // This makes sure CalligraphyLayoutInflater.this is set on the factory to make sure
        // it calls back up the Inflater stack correctly.
        if (getFactory() instanceof CalligraphyFactory) {
            ((CalligraphyFactory) getFactory()).setLayoutInflater(this);
        }
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new CalligraphyLayoutInflater(this, newContext, mAttributeId);
    }
}
