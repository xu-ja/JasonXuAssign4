package jason.xu.s991545529.ui.webservice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jason.xu.s991545529.R;

public class WebServiceFrag extends Fragment {

    EditText zipCode;
    JSONObject data;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_webservice, container, false);

        data = null;
        zipCode = (EditText) root.findViewById(R.id.jasonEditTextTextPostalAddress);
        String zipCodeStr = String.valueOf(zipCode.getText());
        if (zipCodeStr.length() < 5) {
            // Alert dialog to tell user that zip code does not exist
            AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
            builder.setCancelable(false);
            builder.setMessage(getResources().getString(R.string.zip_invalid));

            // Closes dialog if ok is pressed
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "ok", close dialog
                    dialog.cancel();
                }
            });

            builder.setTitle(getResources().getString(R.string.webservice_text));
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alert = builder.create();
            alert.show();
            zipCode.setError(getResources().getString(R.string.zip_invalid));
        } else {
            int zipCodeInt = Integer.parseInt(zipCodeStr);
            getJSON(zipCodeInt);
        }

        return root;
    }

    public void getJSON(final int zip) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("https://samples.openweathermap.org/data/2.5/weather?zip="
                            + zip + ",us&appid=b6907d289e10d714a6e88b30761fae22");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuffer json = new StringBuffer(1024);
                    String temp = "";
                    while((temp = reader.readLine()) != null) {
                        json.append(temp).append("\n");
                    }
                    reader.close();

                    data = new JSONObject(json.toString());

                    // If cod is not 200, something went wrong...
                    if (data.getInt("cod") != 200) {
                        Log.d("WEATHER NOT RECEIVED", data.toString());
                        // Alert dialog to tell user that zip code does not exist
                        AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                        builder.setCancelable(false);
                        builder.setMessage(getResources().getString(R.string.zip_not_exist));

                        // Closes dialog if ok is pressed
                        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if user pressed "ok", close dialog
                                dialog.cancel();
                            }
                        });

                        builder.setTitle(getResources().getString(R.string.webservice_text));
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        AlertDialog alert = builder.create();
                        alert.show();
                        zipCode.setError(getResources().getString(R.string.zip_not_exist));
                        return null;
                    }
                } catch (Exception e) {
                    Log.e("OPEN WEATHER", "Exception caught...", e);
                    return null;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void Void) {
                if (data != null) {
                    Log.d("WEATHER RECEIVED", data.toString());
                }
            }
        }.execute();
    }
}