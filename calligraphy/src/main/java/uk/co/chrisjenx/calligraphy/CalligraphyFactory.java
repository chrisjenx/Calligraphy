package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.Map;

class CalligraphyFactory implements LayoutInflater.Factory {
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit."
    };
    private static final Map<Class<? extends TextView>, Integer> sStyles
            = new HashMap<Class<? extends TextView>, Integer>() {
        {
            put(TextView.class, android.R.attr.textViewStyle);
            put(Button.class, android.R.attr.buttonStyle);
            put(EditText.class, android.R.attr.editTextStyle);
            put(AutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            put(MultiAutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            put(CheckBox.class, android.R.attr.checkboxStyle);
            put(RadioButton.class, android.R.attr.radioButtonStyle);
            put(ToggleButton.class, android.R.attr.buttonStyleToggle);
        }
    };


    private final LayoutInflater.Factory factory;
    private final int mAttributeId;

    public CalligraphyFactory(LayoutInflater.Factory factory, int attributeId) {
        this.factory = factory;
        this.mAttributeId = attributeId == 0 ? android.R.attr.fontFamily : attributeId;
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
            // Try to get typeface attribute value
            // Since we're not using namespace it's a little bit tricky

            // Try view xml attributes
            String textViewFont = CalligraphyUtils.pullFontPath(context, attrs, mAttributeId);

            // Try view style attributes
            if (TextUtils.isEmpty(textViewFont)) {
                textViewFont = CalligraphyUtils.pullFontPathFromStyle(context, attrs, mAttributeId);
            }

            // Try theme attributes
            if (TextUtils.isEmpty(textViewFont)) {
                // Use TextAppearance as default style
                final int styleId = sStyles.containsKey(view.getClass())
                        ? sStyles.get(view.getClass())
                        : android.R.attr.textAppearance;
                textViewFont = CalligraphyUtils.pullFontPathFromTheme(context, styleId, mAttributeId);
            }
            CalligraphyUtils.applyFontToTextView(context, (TextView) view, CalligraphyConfig.get(), textViewFont);
        }
    }
}
