package uk.co.chrisjenx.calligraphy;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
public class CalligraphyLayoutInflater extends LayoutInflater {

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit."
    };

    private final int mAttributeId;
    // Wrapper Factories
    private WrapperFactory mWrapperFactory;
    private WrapperFactory2 mWrapperFactory2;
    // Wrapped Factories
    private Factory mFactory = null;
    private Factory2 mFactory2 = null;
    // Private Factory Hax
    private PrivateFactoryWrapper mPrivateFactoryWrapper = null;
    private Method mSetPrivateFactoryMethod = null;
    private boolean mPrivateFactorySet = false;

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

    @Override
    public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        setHiddenPrivateFactory();
        return super.inflate(parser, root, attachToRoot);
    }

    // ===
    // Wrapping goodies
    // ===
    private void setUpLayoutFactories() {

        // Create Factory1 Wrapper
        mWrapperFactory = new WrapperFactory();
        // Get current factory if we have one.
        if (getFactory() != null) mFactory = getFactory();

        // If we are HC+ we get and set Factory2 otherwise we just wrap Factory1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWrapperFactory2 = new WrapperFactory2();
            if (getFactory2() != null) mFactory2 = getFactory2();
            setFactory2(mWrapperFactory2); // Sets both Factory/Factory2
        } else {
            setFactory(mWrapperFactory);
        }
    }

    private void setHiddenPrivateFactory() {
        if (!(getContext() instanceof Factory2)) return;
        if (mSetPrivateFactoryMethod != null && !mPrivateFactorySet) {
            applyWrapperToPrivateFactory(mSetPrivateFactoryMethod);
        }
        final Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("setPrivateFactory")) {
                applyWrapperToPrivateFactory(method);
                mSetPrivateFactoryMethod = method;
            }
        }
    }

    private void applyWrapperToPrivateFactory(Method method) {
        try {
            mPrivateFactoryWrapper = new PrivateFactoryWrapper((Factory2) getContext());
            method.setAccessible(true);
            method.invoke(this, mPrivateFactoryWrapper);
            mPrivateFactorySet = true;
        } catch (IllegalAccessException ignored) {
        } catch (InvocationTargetException ignored) {
        }
    }

    @Override
    public void setFactory(Factory factory) {
        // Only set our factory and wrap calls to the Factory trying to be set!
        if (factory == mWrapperFactory) {
            super.setFactory(factory);
            return;
        }
        mFactory = factory;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setFactory2(Factory2 factory2) {
        // Only set our factory and wrap calls to the Factory2 trying to be set!
        if (factory2 == mWrapperFactory2) {
            super.setFactory2(factory2);
            return;
        }
        mFactory2 = factory2;
    }

    // LayoutInflater ViewCreators
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

        Log.d("Calli", "Inflater1 onCreateView: " + view);

        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        final View view = super.onCreateView(parent, name, attrs);
        Log.d("Calli", "Inflater2 onCreateView: " + view);
        return view;
    }
    // Layout inflater Creators

    // ===
    // Wrapper Factories for Pre/Post HC
    // ===
    class WrapperFactory implements Factory {
        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            if (mFactory != null) {
                final View view = mFactory.onCreateView(name, context, attrs);
                Log.d("Calli", "Factory1 onCreateView: " + view);
                return view;
            }
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class WrapperFactory2 implements Factory2 {
        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return mWrapperFactory.onCreateView(name, context, attrs);
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            if (mFactory2 != null) {
                final View view = mFactory2.onCreateView(parent, name, context, attrs);
                Log.d("Calli", "Factory2 onCreateView: " + view);
                return view;
            }
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class PrivateFactoryWrapper implements Factory2 {

        private final Factory2 factory2;

        public PrivateFactoryWrapper(Factory2 factory2) {
            this.factory2 = factory2;
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            // Ask the Activity to try and create the view!
            final View view = factory2.onCreateView(parent, name, context, attrs);
            Log.d("Calli", "PrivateFactory2 onCreateView: " + view);
            return view;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            // Ask the Activity to try and create the view!
            final View view = factory2.onCreateView(name, context, attrs);
            Log.d("Calli", "PrivateFactory1 onCreateView: " + view);
            return view;
        }
    }

}
