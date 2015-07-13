package uk.co.chrisjenx.calligraphy.sample;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyFactoryPlugin;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Sample CalligraphyFactoryPlugin: apply font for ThirdPartyCustomView objects.
 */
public class ThirdPartyCustomViewCalligraphyFactoryPlugin implements CalligraphyFactoryPlugin {
    private final int attributeId;

    public ThirdPartyCustomViewCalligraphyFactoryPlugin(int attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public void onViewCreated(View view, Context context, AttributeSet attrs) {
        if (view instanceof ThirdPartyCustomView) {
            ThirdPartyCustomView thirdPartyCustomView = (ThirdPartyCustomView) view;

            String fontPath = CalligraphyUtils.pullFontPathFromAttributesHierarchyWithDefault(context, attrs, attributeId, CalligraphyConfig.get());

            if (!TextUtils.isEmpty(fontPath)) {
                Typeface typeface = TypefaceUtils.load(context.getAssets(), fontPath);
                thirdPartyCustomView.setTypeface(typeface);
            }
        }
    }
}
