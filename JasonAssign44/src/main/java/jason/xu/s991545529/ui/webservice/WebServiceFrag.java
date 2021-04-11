package jason.xu.s991545529.ui.webservice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jason.xu.s991545529.R;

public class WebServiceFrag extends Fragment {

    EditText zipCode;
    JSONObject data;
    View root;
    TextView jsonText;
    List<Message> msgs;
    HttpURLConnection con;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_webservice, container, false);

        data = null;
        msgs = null;
        con = null;
        zipCode = (EditText) root.findViewById(R.id.jasonEditTextTextPostalAddress);
        jsonText = (TextView) root.findViewById(R.id.jasonTextViewJSON);

        Button webservice = (Button) root.findViewById(R.id.jasonButtonWebService);
        webservice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String zipCodeStr = String.valueOf(zipCode.getText());
                if (zipCodeStr.length() < 5) {
                    // Alert dialog to tell user that zip code is invalid
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
            }
        });

        return root;
    }

    public void getJSON(final int zip) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("https://samples.openweathermap.org/data/2.5/weather?zip="
                            + zip + ",us&appid=b6907d289e10d714a6e88b30761fae22");
                    con = (HttpURLConnection) url.openConnection();
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
/*                    try {
                        msgs = readJsonStream(con.getInputStream());
                        jsonText.setText("City: " + msgs.get(0).getCity() +
                                "\nLongitude: " + msgs.get(0).getCoord().getLon() +
                                "\nLatitude: " + msgs.get(0).getCoord().getLat() +
                                "\nHumidity: " + msgs.get(0).getMain().getHumidity() +
                                "\nTemperature: " + msgs.get(0).getMain().getTemperature());
                    } catch (Exception e) {
                        Log.e("OPEN WEATHER", "msgs exception... ", e);
                    }*/
                    jsonText.setText(data.toString());
                    Log.d("WEATHER RECEIVED", data.toString());
                }
            }
        }.execute();
    }

    public List<Message> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Message> readMessagesArray(JsonReader reader) throws IOException {
        List<Message> messages = new ArrayList<Message>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public Message readMessage(JsonReader reader) throws IOException {
        String city = null;
        Main main = null;
        Coord coord = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("coord")) {
                coord = readCoord(reader);
            } else if (name.equals("main")) {
                main = readMain(reader);
            } else if (name.equals("name")) {
                city = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Message(coord, main, city);
    }

    public Coord readCoord(JsonReader reader) throws IOException {
        String lon = null;
        String lat = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("lon")) {
                lon = reader.nextString();
            } else if (name.equals("lat")) {
                lat = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Coord(lon, lat);
    }

    public Main readMain(JsonReader reader) throws IOException {
        String humidity = null;
        String temperature = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("humidity")) {
                humidity = reader.nextString();
            } else if (name.equals("temperature")) {
                temperature = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Main(humidity, temperature);
    }

    private class Message {
        Coord coord;
        Main main;
        String city;

        public Message(Coord coordinates, Main m, String c) {
            coord = coordinates;
            main = m;
            city = c;
        }

        public Coord getCoord() {
            return coord;
        }

        public Main getMain() {
            return main;
        }

        public String getCity() {
            return city;
        }
    }

    private class Coord {
        String longitude;
        String latitude;

        public Coord(String lon, String lat) {
            longitude = lon;
            latitude = lat;
        }

        public String getLon() {
            return longitude;
        }

        public String getLat() {
            return latitude;
        }
    }

    private class Main {
        String humidity;
        String temperature;

        public Main(String hum, String temp) {
            humidity = hum;
            temperature = temp;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getTemperature() {
            return temperature;
        }
    }
}