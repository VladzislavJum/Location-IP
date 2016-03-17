package by.jum.locationbyip.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import by.jum.locationbyip.LocationInformation;
import by.jum.locationbyip.R;
import by.jum.locationbyip.adapter.LocationInformationAdapter;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.Messages;
import by.jum.locationbyip.database.DatabaseHandler;
import by.jum.locationbyip.processing.ResponseHeader;
import by.jum.locationbyip.processing.ResponseParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = InformationActivity.class.toString();
    private List<LocationInformation> locationInformations;
    private LocationInformationAdapter informationAdapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        button = (Button) findViewById(R.id.but);
        button.setOnClickListener(this);


        Intent intent = getIntent();
        String info = intent.getStringExtra(Messages.IP_TEXT);


        Map<String, Integer> countIpMap = new ResponseParser().parse(info);
        List<String> ipList = new ArrayList<>(countIpMap.keySet());

        DatabaseHandler handler = new DatabaseHandler(this);
        SQLiteDatabase database = handler.getWritableDatabase();

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


        informationAdapter = new LocationInformationAdapter(this, locationInformations);
        ListView listView = (ListView) findViewById(R.id.infoView);
        listView.setAdapter(informationAdapter);
    }


    @Override
    public void onClick(View v) {
        locationInformations.add(locationInformations.get(0));
        informationAdapter.notifyDataSetChanged();
    }
}
