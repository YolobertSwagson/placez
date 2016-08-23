package wusadevelopment.albert.com.placez;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Albert on 23.08.2016.
 */
public class WarningAlert extends android.support.v4.app.DialogFragment {
    int arg;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        arg = getArguments().getInt("id");
        builder.setMessage("Wirklich löschen?")
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //schließt das Fenster
                        dialog.dismiss();
                    }
                }).setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    getActivity().onBackPressed();
                    Controller.getInstance(getActivity()).removePlace(arg);
                    Home.pAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}