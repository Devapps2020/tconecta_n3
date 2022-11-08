package mx.devapps.utils.components;

import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Arrays;

import mx.devapps.utils.interfaces.IHFragment;

public abstract class HFragment extends Fragment implements IHFragment {

    private View view = null;
    private HActivity context = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (HActivity) context;
    }

    @Override
    public View onCreated(View view) {

        if( this.view == null) {
            this.view = view;
            this.initFragment();
        }

        return this.view;
    }

    @Override
    public View onCreated(View view, Integer drawable) {

        if( this.view == null) {
            getContext().getWindow().getDecorView().setBackground(getResources().getDrawable(drawable));
            this.view = view;
            this.initFragment();
        }

        return this.view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreated(View view, Integer drawable, Boolean oscuro) {

        if( this.view == null) {
            getContext().getWindow().getDecorView().setBackground(getContext().getResources().getDrawable(drawable));

            if(oscuro)
                getContext().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            this.view = view;
            this.initFragment();
        }

        return this.view;
    }

    public HActivity getContext() {
        return this.context;
    }

    public View getView() {
        return this.view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public boolean onBackPressed() {
        return false;
    }

}
