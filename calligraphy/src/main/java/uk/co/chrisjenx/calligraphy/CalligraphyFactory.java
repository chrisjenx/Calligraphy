package uk.co.chrisjenx.calligraphy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
    private static final String ACTION_BAR_TITLE = "action_bar_title";
    private static final String ACTION_BAR_SUBTITLE = "action_bar_subtitle";
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

    /**
     * Some styles are in sub styles, such as actionBarTextStyle etc..
     *
     * @param view view to check.
     * @return 2 element array, default to -1 unless a style has been found.
     */
    protected static int[] getStyleForTextView(TextView view) {
        final int[] styleIds = new int[]{-1, -1};
        // Try to find the specific actionbar styles
        if (isActionBarTitle(view)) {
            styleIds[0] = android.R.attr.actionBarStyle;
            styleIds[1] = android.R.attr.titleTextStyle;
        } else if (isActionBarSubTitle(view)) {
            styleIds[0] = android.R.attr.actionBarStyle;
            styleIds[1] = android.R.attr.subtitleTextStyle;
        }
        if (styleIds[0] == -1) {
            // Use TextAppearance as default style
            styleIds[0] = sStyles.containsKey(view.getClass())
                    ? sStyles.get(view.getClass())
                    : android.R.attr.textAppearance;
        }
        return styleIds;
    }

    /**
     * An even dirtier way to see if the TextView is part of the ActionBar
     *
     * @param view TextView to check is Title
     * @return true if it is.
     */
    @SuppressLint("NewApi")
    protected static boolean isActionBarTitle(TextView view) {
        if (matchesResourceIdName(view, ACTION_BAR_TITLE)) return true;
        if (parentIsToolbarV7(view)) {
            final android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar) view.getParent();
            return TextUtils.equals(parent.getTitle(), view.getText());
        }
        return false;
    }

    /**
     * An even dirtier way to see if the TextView is part of the ActionBar
     *
     * @param view TextView to check is Title
     * @return true if it is.
     */
    @SuppressLint("NewApi")
    protected static boolean isActionBarSubTitle(TextView view) {
        if (matchesResourceIdName(view, ACTION_BAR_SUBTITLE)) return true;
        if (parentIsToolbarV7(view)) {
            final android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar) view.getParent();
            return TextUtils.equals(parent.getSubtitle(), view.getText());
        }
        return false;
    }

    protected static boolean parentIsToolbarV7(View view) {
        return CalligraphyUtils.canCheckForV7Toolbar() && view.getParent() != null && (view.getParent() instanceof android.support.v7.widget.Toolbar);
    }

    /**
     * Use to match a view against a potential view id. Such as ActionBar title etc.
     *
     * @param view    not null view you want to see has resource matching name.
     * @param matches not null resource name to match against. Its not case sensitive.
     * @return true if matches false otherwise.
     */
    protected static boolean matchesResourceIdName(View view, String matches) {
        if (view.getId() == View.NO_ID) return false;
        final String resourceEntryName = view.getResources().getResourceEntryName(view.getId());
        return resourceEntryName.equalsIgnoreCase(matches);
    }


    private final LayoutInflater.Factory factory;
    private final int mAttributeId;
    private LayoutInflater inflater;

    public CalligraphyFactory(LayoutInflater inflater, LayoutInflater.Factory factory, int attributeId) {
        this.inflater = inflater;
        this.factory = factory;
        this.mAttributeId = attributeId;
    }

    public void setLayoutInflater(LayoutInflater inflater) {
        this.inflater = inflater;
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
            // Try the system view inflater, this might not be correct if its been cloned and not
            // set as the default view inflater.
            return LayoutInflater.from(context).createView(name, prefix, attrs);
        } catch (Exception ignore) {
        }
        try {
            // If the above fails try the view inflater which we are attached too.
            return inflater.createView(name, prefix, attrs);
        } catch (Exception ignore) {
        }
        // Well this sucks... Guess nothing wants to create a view for us :(
        return null;
    }

    protected void onViewCreated(View view, String name, final Context context, AttributeSet attrs) {
        if (view instanceof TextView) {
            // Fast path the setting of TextView's font, means if we do some delayed setting of font,
            // which has already been set by use we skip this TextView (mainly for inflating custom,
            // TextView's inside the Toolbar/ActionBar).
            if (TypefaceUtils.isLoaded(((TextView) view).getTypeface())) {
                return;
            }
            // Try to get typeface attribute value
            // Since we're not using namespace it's a little bit tricky

            // Try view xml attributes
            String textViewFont = CalligraphyUtils.pullFontPathFromView(context, attrs, mAttributeId);

            // Try view style attributes
            if (TextUtils.isEmpty(textViewFont)) {
                textViewFont = CalligraphyUtils.pullFontPathFromStyle(context, attrs, mAttributeId);
            }

            // Try View TextAppearance
            if (TextUtils.isEmpty(textViewFont)) {
                textViewFont = CalligraphyUtils.pullFontPathFromTextAppearance(context, attrs, mAttributeId);
            }

            // Try theme attributes
            if (TextUtils.isEmpty(textViewFont)) {
                final int[] styleForTextView = getStyleForTextView((TextView) view);
                if (styleForTextView[1] != -1)
                    textViewFont = CalligraphyUtils.pullFontPathFromTheme(context, styleForTextView[0], styleForTextView[1], mAttributeId);
                else
                    textViewFont = CalligraphyUtils.pullFontPathFromTheme(context, styleForTextView[0], mAttributeId);
            }


            // Still need to defer the Native action bar, appcompat-v7:21+ uses the Toolbar underneath. But won't match these anyway.
            final boolean deferred = matchesResourceIdName(view, ACTION_BAR_TITLE) || matchesResourceIdName(view, ACTION_BAR_SUBTITLE);

            CalligraphyUtils.applyFontToTextView(context, (TextView) view, CalligraphyConfig.get(), textViewFont, deferred);
        }

        // AppCompat API21+ The ActionBar doesn't inflate default Title/SubTitle, we need to scan the
        // Toolbar(Which underlies the ActionBar) for its children.
        if (CalligraphyUtils.canCheckForV7Toolbar() && view instanceof android.support.v7.widget.Toolbar) {
            final ViewGroup parent = (ViewGroup) view;
            parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // No children, do nuffink!
                    if (parent.getChildCount() <= 0) return;
                    // Process children, defer draw as it has set the typeface.
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        onViewCreated(parent.getChildAt(i), null, context, null);
                    }
                }
            });
        }
    }


}
