package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.view.LayoutInflater;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
class CalligraphyLayoutInflater extends LayoutInflater {
    protected CalligraphyLayoutInflater(Context context) {
        super(context);
        setUpLayoutFactory();
    }

    protected CalligraphyLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
        setUpLayoutFactory();
    }

    private void setUpLayoutFactory() {
        if (!(getFactory() instanceof CalligraphyFactory)) {
            setFactory(new CalligraphyFactory(getFactory()));
        }
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new CalligraphyLayoutInflater(this, newContext);
    }
}
