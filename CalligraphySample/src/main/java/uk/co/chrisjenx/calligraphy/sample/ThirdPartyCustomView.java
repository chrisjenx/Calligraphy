package uk.co.chrisjenx.calligraphy.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Sample of a third party custom view NOT extending TextView class.
 * In the sample application, the customization of this third party view is done using a
 * registered CalligraphyFactoryPlugin.
 */
public class ThirdPartyCustomView extends LinearLayout {

    TextView innerTextView;

    public ThirdPartyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createInnerTextView(context, attrs);
    }

    private void createInnerTextView(Context context, AttributeSet attrs) {
        innerTextView = new TextView(context);

        int[] set = {
                android.R.attr.text
        };
        TypedArray a = context.obtainStyledAttributes(attrs, set);

        innerTextView.setText("Third party internal TextView created programmatically: " + a.getText(0));

        a.recycle();

        addView(this.innerTextView);
    }

    public void setTypeface(Typeface tf) {
        innerTextView.setTypeface(tf);
    }

}
