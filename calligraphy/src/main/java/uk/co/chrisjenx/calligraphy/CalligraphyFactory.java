package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

class CalligraphyFactory implements LayoutInflater.Factory {
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit."
    };

    private final LayoutInflater.Factory factory;

    public CalligraphyFactory(LayoutInflater.Factory factory) {
        this.factory = factory;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;

        if (context instanceof LayoutInflater.Factory) {
            view = ((LayoutInflater.Factory) context).onCreateView(name, context, attrs);
        }

        if (factory != null && view == null) {
            view = factory.onCreateView(name, context, attrs);
        }

        if (view == null) {
            view = createViewOrFailQuietly(name, context, attrs);
        }

        if (view != null) {
            onViewCreated(view, name, context, attrs);
        }

        return view;
    }

    protected View createViewOrFailQuietly(String name, Context context, AttributeSet attrs) {
        if (name.contains(".")) {
            return createViewOrFailQuietly(name, null, context, attrs);
        }

        for (final String prefix : sClassPrefixList) {
            final View view = createViewOrFailQuietly(name, prefix, context, attrs);

            if (view != null) {
                return view;
            }
        }

        return null;
    }

    protected View createViewOrFailQuietly(String name, String prefix, Context context, AttributeSet attrs) {
        try {
            return LayoutInflater.from(context).createView(name, prefix, attrs);
        } catch (Exception ignore) {
            return null;
        }
    }

    protected void onViewCreated(View view, String name, Context context, AttributeSet attrs) {
        if (view instanceof TextView) {
            String textViewFont = CalligraphyUtils.pullFontFamily(context, attrs);
            CalligraphyUtils.applyFontToTextView(context, (TextView) view, CalligraphyConfig.get(), textViewFont);
        }
    }
}
