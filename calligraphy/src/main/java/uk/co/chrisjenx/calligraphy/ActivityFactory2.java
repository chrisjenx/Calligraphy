package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by chris on 09/11/14.
 * For Calligraphy.
 */
public interface ActivityFactory2 {

    /**
     * Used to Wrap the Activity onCreateView method.
     *
     * TODO add example code in here.
     *
     * @param activity the calling context/activity which we are intercepting
     * @param parent   see {@link android.view.LayoutInflater.Factory2#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * @param name     see {@link android.view.LayoutInflater.Factory2#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * @param context  see {@link android.view.LayoutInflater.Factory2#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * @param attrs    see {@link android.view.LayoutInflater.Factory2#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * @return the result from the activities {@code onCreateView()}
     * @see android.view.LayoutInflater.Factory2
     */
    View onActivityCreateView(LayoutInflater.Factory2 activity, View parent, String name, Context context, AttributeSet attrs);
}
