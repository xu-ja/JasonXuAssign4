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

/*
    Jason Xu 991545529
    Section: Mondays 6:00pm-9:00pm
 */
public class DownloadFrag extends Fragment {

    View root;
    Spinner spinner;
    String spinnerSelection;
    ImageView imageView;
    Button button;
    ProgressDialog progressDialog;
    URL url;
    String urlSelected;
    AsyncTask downloadImage;

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

        button = (Button) root.findViewById(R.id.jasonButtonDownload);
        imageView = (ImageView) root.findViewById(R.id.jasonImageView);
        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(getResources().getString(R.string.menu_download));
        progressDialog.setIcon(getResources().getDrawable(R.drawable.ic_menu_gallery));
        progressDialog.setMessage(getResources().getString(R.string.downloading));

        urlSelected = getResources().getString(R.string.flower_link);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                if (i == 0) {
                    urlSelected = getResources().getString(R.string.flower_link);
                } else if (i == 1) {
                    urlSelected = getResources().getString(R.string.nature_link);
                } else if (i == 2) {
                    urlSelected = getResources().getString(R.string.sky_link);
                }
            }
            @Override
            public void onNothingSelected(AdapterView adapterView){}
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                downloadImage = new DownloadTask().execute(stringToURL(urlSelected));
            }
        });
        return root;
    }

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