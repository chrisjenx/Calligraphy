package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
class CalligraphyLayoutInflater extends LayoutInflater {
    private static final String sTextViewClassName = TextView.class.getSimpleName();
    private static final String sButtonClassName = Button.class.getSimpleName();

    protected CalligraphyLayoutInflater(Context context) {
        super(context);
    }

    protected CalligraphyLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
    }

    /**
     * Override onCreateView to instantiate names that correspond to the
     * widgets known to the Widget factory. If we don't find a match,
     * call through to our super class.
     */
    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        return textViewFilter(super.onCreateView(name, attrs), name, attrs);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new CalligraphyLayoutInflater(this, newContext);
    }

    private View textViewFilter(final View view, final String name, final AttributeSet attrs) {
        if (view == null) return null;
        if (sTextViewClassName.equals(name) || sButtonClassName.equals(name)) {
            String textViewFont = CalligraphyUtils.pullFontFamily(getContext(), attrs);
            CalligraphyUtils.applyFontToTextView(getContext(), (TextView) view, CalligraphyConfig.get(), textViewFont);
        }
        return view;
    }
}
