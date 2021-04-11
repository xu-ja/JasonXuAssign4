package jason.xu.s991545529.ui.download;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import jason.xu.s991545529.R;

public class DownloadFrag extends Fragment {

    View root;
    Spinner spinner;
    String spinnerSelection;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_download, container, false);

        String[] spinnerTitles;
        int[] spinnerImages;

        spinnerTitles = getResources().getStringArray(R.array.spinner_items);
        spinnerImages = new int[]{R.drawable.flower, R.drawable.nature, R.drawable.sky};

        CustomAdapter customAdapter = new CustomAdapter(root.getContext(), spinnerTitles, spinnerImages);
        spinner.setAdapter(customAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView adapterView){}
        });

        return root;
    }
}