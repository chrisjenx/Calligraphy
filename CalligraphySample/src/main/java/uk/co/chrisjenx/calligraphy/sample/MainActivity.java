package uk.co.chrisjenx.calligraphy.sample;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.chrisjenx.calligraphy.Calligraphy;
import uk.co.chrisjenx.calligraphy.ViewFactory;

public class MainActivity extends Activity {
    private final Calligraphy calligraphy = new Calligraphy("fonts/Roboto-ThinItalic.ttf");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewFactory.registerOnViewCreatedListener(calligraphy);
        ViewFactory.onActivityCreated(this, R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        ViewFactory.unregisterOnViewCreatedListener(calligraphy);
        super.onDestroy();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return ViewFactory.onCreateView(super.onCreateView(parent, name, context, attrs), parent, name, context, attrs);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return ViewFactory.onCreateView(super.onCreateView(name, context, attrs), name, context, attrs);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }
    }
}
