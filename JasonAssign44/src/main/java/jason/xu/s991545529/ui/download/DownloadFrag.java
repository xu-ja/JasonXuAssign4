package jason.xu.s991545529.ui.download;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import jason.xu.s991545529.R;

public class DownloadFrag extends Fragment {

    View root;
    Spinner spinner;
    String spinnerSelection;
    ImageView imageView;
    Button button;
    ProgressDialog progressDialog;
    URL url;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_download, container, false);

        String[] spinnerTitles;
        int[] spinnerImages;

        spinnerTitles = getResources().getStringArray(R.array.spinner_items);
        spinnerImages = new int[]{R.drawable.flower, R.drawable.nature, R.drawable.sky};

        CustomAdapter customAdapter = new CustomAdapter(root.getContext(), spinnerTitles, spinnerImages);
        spinner = (Spinner) root.findViewById(R.id.jasonSpinner);
        spinner.setAdapter(customAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                if (i == 0) {

                } else if (i == 1) {

                } else if (i == 2) {

                }
            }
            @Override
            public void onNothingSelected(AdapterView adapterView){}
        });

        return root;
    }

/*    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/
    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{

        protected void onPreExecute(){
            progressDialog.show();
        }

        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;
            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                return BitmapFactory.decodeStream(bufferedInputStream);
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            progressDialog.dismiss();
            if(result != null){
                imageView.setImageBitmap(result);
            } else {
                // Notify user that an error occurred while downloading image
                Toast.makeText(root.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected URL stringToURL(String sUrl) {
        try {
            url = new URL(sUrl);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}