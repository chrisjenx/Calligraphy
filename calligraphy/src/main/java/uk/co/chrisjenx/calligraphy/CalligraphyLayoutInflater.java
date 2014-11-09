package uk.co.chrisjenx.calligraphy;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
public class CalligraphyLayoutInflater extends LayoutInflater implements ActivityFactory2 {

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit."
    };

    private final int mAttributeId;

    protected CalligraphyLayoutInflater(Context context, int attributeId) {
        super(context);
        mAttributeId = attributeId;
        setUpLayoutFactories();
    }

    protected CalligraphyLayoutInflater(LayoutInflater original, Context newContext, int attributeId) {
        super(original, newContext);
        mAttributeId = attributeId;
        setUpLayoutFactories();
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new CalligraphyLayoutInflater(this, newContext, mAttributeId);
    }

    // ===
    // Wrapping goodies
    // ===

    /**
     * We don't want to unnecessary create/set our factories if there are none there. We try to be
     * as lazy as possible.
     */
    private void setUpLayoutFactories() {
        // If we are HC+ we get and set Factory2 otherwise we just wrap Factory1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getFactory2() != null) {
                // Sets both Factory/Factory2
                setFactory2(getFactory2());
            }
        } else if (getFactory() != null) {
            setFactory(getFactory());
        }
    }

    @Override
    public void setFactory(Factory factory) {
        // Only set our factory and wrap calls to the Factory trying to be set!
        if (!(factory instanceof WrapperFactory)) {
            super.setFactory(new WrapperFactory(factory));
        } else {
            super.setFactory(factory);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setFactory2(Factory2 factory2) {
        // Only set our factory and wrap calls to the Factory2 trying to be set!
        if (!(factory2 instanceof WrapperFactory2)) {
            super.setFactory2(new WrapperFactory2(factory2));
        } else {
            super.setFactory2(factory2);
        }
    }

    // ===
    // LayoutInflater ViewCreators
    // ===

    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        View view = null;
        for (String prefix : sClassPrefixList) {
            try {
                view = createView(name, prefix, attrs);
            } catch (ClassNotFoundException e) {
            }
        }
        // In this case we want to let the base class take a crack
        // at it.
        if (view == null) view = super.onCreateView(name, attrs);

        //TODO intercept
        Log.d("Calli", "Inflater1 onCreateView: " + view);

        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        final View view = super.onCreateView(parent, name, attrs);
        //TODO intercept
        Log.d("Calli", "Inflater2 onCreateView: " + view);
        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onActivityCreateView(View view, AttributeSet attrs) {
        // Ask the Activity to try and create the view!
        if (view != null) {
            //TODO intercept
        }
        Log.d("Calli", "ActivityFactory2 onCreateView: " + view);
        return view;
    }

    // ===
    // Wrapper Factories for Pre/Post HC
    // ===
    static class WrapperFactory implements Factory {

        private final Factory mFactory;

        public WrapperFactory(Factory factory) {
            mFactory = factory;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            final View view = mFactory.onCreateView(name, context, attrs);
            //TODO intercept
            Log.d("Calli", "Factory1 onCreateView: " + view);
            return view;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static class WrapperFactory2 implements Factory2 {
        private final Factory2 mFactory2;

        public WrapperFactory2(Factory2 factory2) {

            mFactory2 = factory2;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            final View view = mFactory2.onCreateView(name, context, attrs);
            //TODO intercept
            Log.d("Calli", "Factory1 onCreateView: " + view);
            return view;
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            if (mFactory2 != null) {
                final View view = mFactory2.onCreateView(parent, name, context, attrs);
                //TODO intercept
                Log.d("Calli", "Factory2 onCreateView: " + view);
                return view;
            }
            return null;
        }
    }

}
