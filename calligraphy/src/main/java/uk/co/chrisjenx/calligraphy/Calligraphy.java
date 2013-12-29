package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class Calligraphy implements ViewFactory.OnViewCreatedListener {
    private final String fallback;

    public Calligraphy(final String defaultFont) {
        this.fallback = defaultFont;
    }

    @Override
    public void onViewCreated(View view, String tag, Context context, AttributeSet attrs) {
        if (view instanceof TextView) {
            final String font = CalligraphyUtils.pullFontFamily(context, attrs);
            final TextView text = (TextView) view;

            CalligraphyUtils.applyFontToTextView(context, text, font, fallback);
        }
    }
}
