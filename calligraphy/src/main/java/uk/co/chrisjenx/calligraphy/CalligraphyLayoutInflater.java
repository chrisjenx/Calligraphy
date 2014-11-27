package uk.co.chrisjenx.calligraphy;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
class CalligraphyLayoutInflater extends LayoutInflater implements ActivityFactory2 {

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit."
    };

    private final int mAttributeId;
    private final CalligraphyFactory mCalligraphyFactory;

    protected CalligraphyLayoutInflater(Context context, int attributeId) {
        super(context);
        mAttributeId = attributeId;
        mCalligraphyFactory = new CalligraphyFactory(attributeId);
        setUpLayoutFactories();
    }

    protected CalligraphyLayoutInflater(LayoutInflater original, Context newContext, int attributeId) {
        super(original, newContext);
        mAttributeId = attributeId;
        mCalligraphyFactory = new CalligraphyFactory(attributeId);
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
            if (getFactory2() != null && !(getFactory2() instanceof WrapperFactory2)) {
                // Sets both Factory/Factory2
                setFactory2(getFactory2());
            }
        }
        // We can do this as setFactory2 is used for both methods.
        if (getFactory() != null && !(getFactory() instanceof WrapperFactory)) {
            setFactory(getFactory());
        }
    }

    @Override
    public void setFactory(Factory factory) {
        // Only set our factory and wrap calls to the Factory trying to be set!
        if (!(factory instanceof WrapperFactory)) {
            super.setFactory(new WrapperFactory(factory, this, mCalligraphyFactory));
        } else {
            super.setFactory(factory);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setFactory2(Factory2 factory2) {
        // Only set our factory and wrap calls to the Factory2 trying to be set!
        if (!(factory2 instanceof WrapperFactory2)) {
            super.setFactory2(new WrapperFactory2(factory2, mCalligraphyFactory));
        } else {
            super.setFactory2(factory2);
        }
    }

    // ===
    // LayoutInflater ViewCreators
    // Works in order of inflation
    // ===

    /**
     * The Activity onCreateView (PrivateFactory) is the third port of call for LayoutInflation.
     * We opted to manual injection over aggressive reflection, this should be less fragile.
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onActivityCreateView(View parent, View view, String name, Context context, AttributeSet attrs) {
        return mCalligraphyFactory.onViewCreated(createCustomViewInternal(parent, view, name, context, attrs), context, attrs);
    }

    /**
     * The LayoutInflater onCreateView is the fourth port of call for LayoutInflation.
     * BUT only for none CustomViews.
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected View onCreateView(View parent, String name, @NonNull AttributeSet attrs) throws ClassNotFoundException {
        return mCalligraphyFactory.onViewCreated(super.onCreateView(parent, name, attrs),
                getContext(), attrs);
    }

    /**
     * The LayoutInflater onCreateView is the fourth port of call for LayoutInflation.
     * BUT only for none CustomViews.
     * Basically if this method doesn't inflate the View nothing probably will.
     */
    @Override
    protected View onCreateView(String name, @NonNull AttributeSet attrs) throws ClassNotFoundException {
        // This mimics the {@code PhoneLayoutInflater} in the way it tries to inflate the base
        // classes, if this fails its pretty certain the app will fail at this point.
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

        return mCalligraphyFactory.onViewCreated(view, view.getContext(), attrs);
    }

    private Field mConstructorArgs = null;

    /**
     * Nasty method to inflate custom layouts that haven't been handled else where. If this fails it
     * will fall back through to the PhoneLayoutInflater method of inflating custom views where
     * Calligraphy will NOT have a hook into.
     *
     * @param parent  parent view
     * @param view    view if it has been inflated by this point, if this is not null this method
     *                just returns this value.
     * @param name    name of the thing to inflate.
     * @param context Context to inflate by if parent is null
     * @param attrs   Attr for this view which we can steal fontPath from too.
     * @return view or the View we inflate in here.
     */
    private View createCustomViewInternal(View parent, View view, String name, Context context, AttributeSet attrs) {
        // I by no means advise anyone to do this normally, but Google have locked down access to
        // the createView() method, so we never get a callback with attributes at the end of the
        // createViewFromTag chain (which would solve all this unnecessary rubbish).
        // We at the very least try to optimise this as much as possible.
        // We only call for customViews (As they are the ones that never go through onCreateView(...)).
        // We also maintain the Field reference and make it accessible which will make a pretty
        // significant difference to performance on Android 4.0+.
        if (view == null && name.indexOf('.') > -1) {
            if (mConstructorArgs == null)
                mConstructorArgs = CalligraphyUtils.getField(LayoutInflater.class, this, "mConstructorArgs");

            final Object[] mConstructorArgsArr = (Object[]) CalligraphyUtils.getValue(mConstructorArgs, this);
            final Object lastContext = mConstructorArgsArr[0];
            mConstructorArgsArr[0] = parent != null ? parent.getContext() : context;
            CalligraphyUtils.setValue(mConstructorArgs, this, mConstructorArgsArr);
            try {
                view = createView(name, null, attrs);
            } catch (ClassNotFoundException ignored) {
            } finally {
                mConstructorArgsArr[0] = lastContext;
                CalligraphyUtils.setValue(mConstructorArgs, this, mConstructorArgsArr);
            }
        }
        return view;
    }

    // ===
    // Wrapper Factories for Pre/Post HC
    // ===

    /**
     * Factory 1 is the first port of call for LayoutInflation
     */
    private static class WrapperFactory implements Factory {

        private final Factory mFactory;
        private final CalligraphyLayoutInflater mInflater;
        private final CalligraphyFactory mCalligraphyFactory;

        public WrapperFactory(Factory factory, CalligraphyLayoutInflater inflater, CalligraphyFactory calligraphyFactory) {
            mFactory = factory;
            mInflater = inflater;
            mCalligraphyFactory = calligraphyFactory;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                return mCalligraphyFactory.onViewCreated(
                        mInflater.createCustomViewInternal(
                                null, mFactory.onCreateView(name, context, attrs), name, context, attrs
                        ),
                        context, attrs
                );
            }
            return mCalligraphyFactory.onViewCreated(
                    mFactory.onCreateView(name, context, attrs),
                    context, attrs
            );
        }
    }

    /**
     * Factory 2 is the second port of call for LayoutInflation
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static class WrapperFactory2 implements Factory2 {
        private final Factory2 mFactory2;
        private final CalligraphyFactory mCalligraphyFactory;

        public WrapperFactory2(Factory2 factory2, CalligraphyFactory calligraphyFactory) {
            mFactory2 = factory2;
            mCalligraphyFactory = calligraphyFactory;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return mCalligraphyFactory.onViewCreated(
                    mFactory2.onCreateView(name, context, attrs),
                    context, attrs);
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            return mCalligraphyFactory.onViewCreated(
                    mFactory2.onCreateView(parent, name, context, attrs),
                    context, attrs);
        }
    }

}
