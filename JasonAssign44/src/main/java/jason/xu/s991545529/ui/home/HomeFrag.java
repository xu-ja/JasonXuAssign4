package jason.xu.s991545529.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;

import jason.xu.s991545529.R;

/*
    Jason Xu 991545529
    Section: Mondays 6:00pm-9:00pm
 */
public class HomeFrag extends Fragment {

    TextView dateTimeTextView;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        dateTimeTextView = root.findViewById(R.id.jasonTextDateTime);
        // runs content() which constantly updates the Date and Time
        content();

        return root;
    }

    public void content() {
        long date = System.currentTimeMillis();
        // Formats date and time
        SimpleDateFormat dateTime = new SimpleDateFormat("MMM dd yyyy - hh:mm:ss a");
        String dateTimeString = dateTime.format(date);
        dateTimeTextView.setText(dateTimeString);
        // Refreshes date and time every 1000 milliseconds
        refresh(1000);
    }

    // Refreshes content() method on intervals of milliseconds parameter
    private void refresh(int milliseconds) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }
}