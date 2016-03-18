package by.jum.locationbyip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import by.jum.locationbyip.models.LocationInformation;
import by.jum.locationbyip.R;

import java.util.List;

public class LocationInformationAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private LocationInformation information;
    private List<LocationInformation> informationList;

    public LocationInformationAdapter(Context context, List<LocationInformation> informationList) {
        this.informationList = informationList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return informationList.size();
    }

    @Override
    public Object getItem(int position) {
        return informationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item, parent, false);
        }
        information = (LocationInformation) getItem(position);
        ((TextView) view.findViewById(R.id.infoText)).setText(information.getIp() + "\n" +
                information.getCountry() + ", " + information.getCity());
        ((ImageView) view.findViewById(R.id.flag_image_view)).setImageBitmap(information.getFlag());
        return view;
    }
}
