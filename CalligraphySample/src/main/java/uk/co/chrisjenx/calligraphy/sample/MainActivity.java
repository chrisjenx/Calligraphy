package uk.co.chrisjenx.calligraphy.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inject pragmatically
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, PlaceholderFragment.getInstance())
                .commit();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setTitle("Calligraphy changed");
                getSupportActionBar().setSubtitle("Added subtitle");
            }
        }, 1000);
    }

    /*
        Uncomment if you disable PrivateFactory injection. See CalligraphyConfig#disablePrivateFactoryInjection()
     */
//    @Override
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public View onCreateView(View parent, String name, @NonNull Context context, @NonNull AttributeSet attrs) {
//        return CalligraphyContextWrapper.onActivityCreateView(this, parent, super.onCreateView(parent, name, context, attrs), name, context, attrs);
//    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
