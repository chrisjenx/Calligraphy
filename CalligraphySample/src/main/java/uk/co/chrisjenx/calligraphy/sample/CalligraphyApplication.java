package uk.co.chrisjenx.calligraphy.sample;

import android.app.Application;

import com.malinskiy.materialicons.widget.IconTextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by chris on 06/05/2014.
 * For Calligraphy.
 */
public class CalligraphyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-ThinItalic.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                        .ignoreClass(IconTextView.class)
                        .build()
        );
    }
}
