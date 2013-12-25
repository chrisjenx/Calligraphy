package uk.co.chrisjenx.calligraphy;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
public class CalligraphyLayoutInflater extends LayoutInflater {
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit."
    };
    private static final Map<Class<? extends TextView>, Integer> sStyles=
        new HashMap<Class<? extends TextView>, Integer>() {
            {
                put(Button.class, android.R.attr.buttonStyle);
                put(TextView.class, android.R.attr.textViewStyle);
                put(CheckBox.class, android.R.attr.checkboxStyle);
                put(EditText.class, android.R.attr.editTextStyle);
                put(RadioButton.class, android.R.attr.radioButtonStyle);
                put(ToggleButton.class, android.R.attr.buttonStyleToggle);
                put(AutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            }
        };

    private final int mAttributeId;

    protected CalligraphyLayoutInflater(Context context, int attributeId) {
        super(context);
        this.mAttributeId = attributeId;
    }

    public CalligraphyLayoutInflater(LayoutInflater original, Context newContext, int attributeId) {
        super(original, newContext);
        this.mAttributeId = attributeId;
    }

    /**
     * Override onCreateView to instantiate names that correspond to the
     * widgets known to the Widget factory. If we don't find a match,
     * call through to our super class.
     */
    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        for (String prefix : sClassPrefixList) {
            try {
                View view = createView(name, prefix, attrs);
                if (view != null) {
                    textViewFilter(view, attrs);
                    return view;
                }
            } catch (ClassNotFoundException e) {
                // In this case we want to let the base class take a crack
                // at it.
            }
        }

        return super.onCreateView(name, attrs);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new CalligraphyLayoutInflater(this, newContext, mAttributeId);
    }

    private void textViewFilter(final View view, final AttributeSet attrs) {
        if (view == null || !(view instanceof TextView)) return;
        final TextView textView = (TextView)view;

        final Context context = getContext();
        // Try to get typeface attribute value
        // Since we're not using namespace it's a little bit tricky

        // Try view xml attributes
        String textViewFont = CalligraphyUtils.pullFontFamily(context, mAttributeId, attrs);

        // Try view style attributes
        if (TextUtils.isEmpty(textViewFont)) {
            textViewFont = CalligraphyUtils.pullFontFamilyFromStyle(context, mAttributeId, attrs);
        }

        // Try theme attributes
        if (TextUtils.isEmpty(textViewFont)) {
            // Use TextAppearance as default style
            final int style = sStyles.containsKey(textView.getClass())
                ? sStyles.get(textView.getClass())
                : android.R.attr.textAppearance;
            textViewFont = CalligraphyUtils.pullFontFamilyFromTheme(context, mAttributeId, style);
        }

        CalligraphyUtils.applyFontToTextView(getContext(), (TextView) view, CalligraphyConfig.get(), textViewFont);
    }
}
