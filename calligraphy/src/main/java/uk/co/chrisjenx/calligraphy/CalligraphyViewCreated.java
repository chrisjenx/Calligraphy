package uk.co.chrisjenx.calligraphy;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chris on 27/11/14.
 * For Calligraphy.
 */
public interface CalligraphyViewCreated {
    /**
     * Handle the created view
     *
     * @param view    nullable.
     * @param context shouldn't be null.
     * @param attrs   shouldn't be null.
     * @return null if null is passed in.
     */
    @Nullable
    View onViewCreated(@Nullable View view, Context context, AttributeSet attrs);
}
