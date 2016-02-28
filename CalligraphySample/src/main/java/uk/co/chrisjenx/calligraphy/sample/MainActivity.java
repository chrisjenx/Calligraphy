package uk.co.chrisjenx.calligraphy.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static butterknife.ButterKnife.findById;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inject pragmatically
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, PlaceholderFragment.getInstance())
                .commit();


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                toolbar.setTitle("Calligraphy Added");
                toolbar.setSubtitle("Added subtitle");
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override public void run() {
                toolbar.setTitle(null);
                toolbar.setSubtitle("Added subtitle");
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override public void run() {
                toolbar.setTitle("Calligraphy added back");
                toolbar.setSubtitle("Added subtitle");
            }
        }, 3000);
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
