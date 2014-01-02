package uk.co.chrisjenx.calligraphy;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * A helper loading {@link android.graphics.Typeface} avoiding the leak of the font when loaded
 * by multiple calls to {@link android.graphics.Typeface#createFromAsset(android.content.res.AssetManager, String)}
 * on pre-ICS versions.
 * <p/>
 * More details can be found here https://code.google.com/p/android/issues/detail?id=9904
 * <p/>
 * Created by Chris Jenkins on 04/09/13.
 */
public final class TypefaceUtils {

    private static final HashMap<String, Typeface> sCachedFonts = new HashMap<String, Typeface>();

    /**
     * A helper loading a custom font.
     *
     * @param assetManager App's asset manager.
     * @param filePath     The path of the file.
     * @return Return {@link android.graphics.Typeface} or null if the path is invalid.
     */
    public static Typeface load(final AssetManager assetManager, final String filePath) {
        synchronized (sCachedFonts) {
            try {
                if (!sCachedFonts.containsKey(filePath)) {
                    final Typeface typeface = Typeface.createFromAsset(assetManager, filePath);
                    sCachedFonts.put(filePath, typeface);
                    return typeface;
                }
            } catch (Exception e) {
                Log.w("Calligraphy", "Can't create asset from " + filePath + ". Make sure you have passed in the correct path and file name.", e);
                sCachedFonts.put(filePath, null);
                return null;
            }
            return sCachedFonts.get(filePath);
        }
    }

    private TypefaceUtils() {
    }
}
