package uk.co.chrisjenx.calligraphy.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public static Fragment getInstance() {
        return new PlaceholderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.button_bold)
    public void onClickBoldButton() {
        Toast.makeText(getActivity(), "Custom Typeface toast text", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_default)
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

    @OnClick(R.id.button_non_default_config)
    public void onClickNonDefaultConfig() {
        final Context calligraphyContext =
            CalligraphyContextWrapper.wrap(getActivity(), new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/Oswald-Stencbab.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport.class)
                    .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                    .build());
        new android.support.v7.app.AlertDialog.Builder(calligraphyContext)
                .setTitle(R.string.dialog_non_default_config_title)
                .setView(R.layout.dialog_non_default_config)
                .show();
    }
}
