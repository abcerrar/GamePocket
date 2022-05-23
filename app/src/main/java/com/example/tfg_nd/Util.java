package com.example.tfg_nd;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class Util {

    //pendiente de unificar
    public Dialog getDialogAnim(Context context){
        Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_progressbar);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
