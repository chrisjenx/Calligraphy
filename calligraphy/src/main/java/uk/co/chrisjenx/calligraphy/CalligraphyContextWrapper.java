package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
public class CalligraphyContextWrapper extends ContextWrapper {

    private LayoutInflater mInflater;

    public CalligraphyContextWrapper(Context base) {
        super(base);
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = new CalligraphyLayoutInflater(LayoutInflater.from(getBaseContext()), this);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }

}
