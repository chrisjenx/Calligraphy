package uk.co.chrisjenx.calligraphy.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setTitle("Calligraphy changed");
                getSupportActionBar().setSubtitle("Added subtitle");
            }
        }, 1000);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ButterKnife.inject(this, view);
        }

        @OnClick({R.id.button_bold})
        public void onClickBoldButton() {
            Toast.makeText(getActivity(), "Custom Typeface toast text", Toast.LENGTH_SHORT).show();
        }

        @OnClick({R.id.button_default})
        public void onClickDefaultButton() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Custom Typeface Dialog");
            builder.setTitle("Sample Dialog");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }
}
