package by.jum.locationbyip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import by.jum.locationbyip.LocationInformation;
import by.jum.locationbyip.R;
import by.jum.locationbyip.adapter.LocationInformationAdapter;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.Messages;
import by.jum.locationbyip.processing.ResponseHeader;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class InformationActivity extends AppCompatActivity {

    private final String TAG = InformationActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        String info = intent.getStringExtra(Messages.IP_TEXT);

        List<LocationInformation> locationInformations = null;
        ResponseHeader header = new ResponseHeader();
        try {
            locationInformations = header.execute(info).get();
        } catch (InterruptedException e) {
            Log.e(TAG, ErrorConstants.PROCESSING_STOPPED_ERROR);
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, ErrorConstants.RESPONSE_PROCESSING_ERROR);
        }

        LocationInformationAdapter informationAdapter = new LocationInformationAdapter(this, locationInformations);
        ListView listView = (ListView) findViewById(R.id.infoView);
        listView.setAdapter(informationAdapter);
    }

}
