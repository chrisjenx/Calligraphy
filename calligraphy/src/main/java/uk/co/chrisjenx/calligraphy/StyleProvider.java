package uk.co.chrisjenx.calligraphy;

import android.widget.TextView;

interface StyleProvider {
    int[] onProvideStyleForTextView(TextView textView);
}
