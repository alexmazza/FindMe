package com.prismio.findme;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class BaseActivity extends FragmentActivity {

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public static class ErrorDialogFragment extends DialogFragment{

        private Dialog mDialog;

        public ErrorDialogFragment(){
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog){
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }


    }

    /**
     * Checks if Google Play Services is available, if not, attempts recovery
     */
    protected boolean serviceConnected(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(resultCode == ConnectionResult.SUCCESS){
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if(dialog != null){
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                errorDialogFragment.setDialog(dialog);
                errorDialogFragment.show(getSupportFragmentManager(), "FindMe");
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                Log.d("FindMe", "Dialog returned correctly");
                break;
            default:
                Log.d("FindMe", "Error");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FindMeApp) getApplication()).inject(this);
    }
}
