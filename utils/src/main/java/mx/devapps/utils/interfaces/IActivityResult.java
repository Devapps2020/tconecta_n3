package mx.devapps.utils.interfaces;

import android.content.Intent;

public interface IActivityResult{

    void onActivityResult(int requestCode, int resultCode, Intent data);

}
