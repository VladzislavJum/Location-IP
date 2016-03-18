package by.jum.locationbyip.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.jum.locationbyip.R;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.Messages;
import by.jum.locationbyip.database.DatabaseHandler;
import by.jum.locationbyip.models.LocationInformation;
import by.jum.locationbyip.processing.ResponseHeader;
import by.jum.locationbyip.processing.ResponseParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InformationActivity extends AppCompatActivity {

    private final String TAG = InformationActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        String info = intent.getStringExtra(Messages.IP_TEXT);
        LocationInformation locationInformation;
        LayoutInflater ltInflater = getLayoutInflater();
        Map<String, Integer> countIpMap = new ResponseParser().parse(info);
        List<String> ipList = new ArrayList<>(countIpMap.keySet());

        DatabaseHandler handler = new DatabaseHandler(this);
        SQLiteDatabase database = handler.getWritableDatabase();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.for_items_linear);


        for (String ip : ipList) {
            View item = ltInflater.inflate(R.layout.item, linearLayout, false);
            TextView textView = (TextView) item.findViewById(R.id.infoText);
            ImageView imageView = (ImageView) item.findViewById(R.id.flag_image_view);
            if (handler.getInformationByIP(ip, database) == null) {
                locationInformation = getInfByAPI(ip);
                handler.addInformation(locationInformation, database);
            } else {
                locationInformation = handler.getInformationByIP(ip, database);
            }

            textView.setText(locationInformation.getIp() + " (" + countIpMap.get(ip) + ")\n" +
                    locationInformation.getCountry() + ", " + locationInformation.getCity());
            imageView.setImageBitmap(locationInformation.getFlag());
            linearLayout.addView(item);
        }
    }

    private LocationInformation getInfByAPI(String info){
        LocationInformation locationInformation = null;
        try {
            locationInformation = new ResponseHeader().execute(info).get();
        } catch (InterruptedException e) {
            Log.e(TAG, ErrorConstants.PROCESSING_STOPPED_ERROR);
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, ErrorConstants.RESPONSE_PROCESSING_ERROR);
        }
        return locationInformation;
    }

    private void addProgresDialog(int count){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Title");
        dialog.setMessage("Message");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(count);
        dialog.setIndeterminate(true);
        dialog.show();

        Handler h = new Handler() {
            public void handleMessage(Message msg) {
                // выключаем анимацию ожидания
                dialog.setIndeterminate(false);
                if (dialog.getProgress() < dialog.getMax()) {
                    // увеличиваем значения индикаторов
                    dialog.incrementProgressBy(1);
//                    dialog.sesendEmptyMessageDelayed(0, 100);
                } else {
                    dialog.dismiss();
                }
            }
        };

    }
}
