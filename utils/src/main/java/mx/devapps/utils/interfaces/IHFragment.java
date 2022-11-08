package mx.devapps.utils.interfaces;

import android.view.View;

public interface IHFragment {

    View onCreated(View view);

    View onCreated(View view, Integer drawable);

    View onCreated(View view, Integer drawable, Boolean oscuro);

    void initFragment();

}
