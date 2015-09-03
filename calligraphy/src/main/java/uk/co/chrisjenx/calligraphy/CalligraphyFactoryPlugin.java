package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Define a plugin for CalligraphyFactory class. Plugins are called after a view has been inflated.
 * The plugin can process custom attributes and apply them on the newly created view.
 *
 * Plugins are registered using {@code CalligraphyConfig.Builder.addFactoryPlugin()}
 */
public interface CalligraphyFactoryPlugin {
    void onViewCreated(View view, Context context, AttributeSet attrs);
}
