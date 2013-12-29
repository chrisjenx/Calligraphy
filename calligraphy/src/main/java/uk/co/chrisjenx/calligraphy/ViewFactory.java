package uk.co.chrisjenx.calligraphy;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;

public class ViewFactory {
    private static final Collection<OnViewCreatedListener> listeners = new ArrayList<OnViewCreatedListener>();

    public static interface OnViewCreatedListener {
        public void onViewCreated(View view, String tag, Context context, AttributeSet attrs);
    }

    public static void registerOnViewCreatedListener(OnViewCreatedListener listener) {
        listeners.add(listener);
    }

    public static void unregisterOnViewCreatedListener(OnViewCreatedListener listener) {
        listeners.remove(listener);
    }

    public static void onActivityCreated(Activity activity) {
        onActivityCreated(activity, 0);
    }

    public static void onActivityCreated(Activity activity, int layout) {
        if (activity.getLayoutInflater().getFactory() == null) {
            activity.getLayoutInflater().setFactory(activity);
        }

        if (layout != 0) {
            activity.setContentView(layout);
        }
    }

    public static View onCreateView(View view, View parent, String name, Context context, AttributeSet attrs) {
        return onCreateView(view, name, context, attrs);
    }

    public static View onCreateView(View view, String name, Context context, AttributeSet attrs) {
        if (view == null) {
            if (!name.contains(".")) {
                view = createViewOrFailQuietly(name, "android.widget.", context, attrs);
            } else {
                view = createViewOrFailQuietly(name, null, context, attrs);
            }
        }

        if (view != null) {
            for (final OnViewCreatedListener listener : listeners) {
                listener.onViewCreated(view, name, context, attrs);
            }
        }

        return view;
    }

    private static View createViewOrFailQuietly(String name, String prefix, Context context, AttributeSet attrs) {
        try {
            return LayoutInflater.from(context).createView(name, prefix, attrs);
        } catch (Exception ignore) {
            return null;
        }
    }
}