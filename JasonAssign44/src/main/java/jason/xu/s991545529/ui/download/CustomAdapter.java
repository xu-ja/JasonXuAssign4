package jason.xu.s991545529.ui.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import jason.xu.s991545529.R;

/*
    Jason Xu 991545529
    Section: Mondays 6:00pm-9:00pm
 */
public class CustomAdapter extends ArrayAdapter<String> {

    String[] spinnerTitles;
    int[] spinnerImages;
    Context context;

    public CustomAdapter(@NonNull Context con, String[] titles, int[] images) {
        super(con, R.layout.spinner_row);
        spinnerTitles = titles;
        spinnerImages = images;
        context = con;
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_row, parent, false);
            mViewHolder.spinnerImageView = (ImageView) convertView.findViewById(R.id.jasonSpinnerImageView);
            mViewHolder.spinnerTextView = (TextView) convertView.findViewById(R.id.jasonSpinnerTextView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.spinnerImageView.setImageResource(spinnerImages[position]);
        mViewHolder.spinnerTextView.setText(spinnerTitles[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView spinnerImageView;
        TextView spinnerTextView;
    }
}
