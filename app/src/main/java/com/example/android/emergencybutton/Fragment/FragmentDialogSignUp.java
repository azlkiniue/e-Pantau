package com.example.android.emergencybutton.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.android.emergencybutton.Activity.LoginActivity;
import com.example.android.emergencybutton.Activity.SignupActivity;

public class FragmentDialogSignUp extends DialogFragment {
    public InterfaceCommunicator interfaceCommunicator;

    public interface InterfaceCommunicator {
        void sendRequestCode(int code);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.interfaceCommunicator = (InterfaceCommunicator) context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement InterfaceCommunicator");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Perhatian !! Cek nama anda karena nama tidak dapat diubah, apakah anda yakin?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        FragmentDialogSignUp.this.interfaceCommunicator.sendRequestCode(1);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        FragmentDialogSignUp.this.interfaceCommunicator.sendRequestCode(2);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
