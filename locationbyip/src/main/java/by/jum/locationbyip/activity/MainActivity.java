package by.jum.locationbyip.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import by.jum.locationbyip.LocationInformation;
import by.jum.locationbyip.R;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.processing.ResponseHeader;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.toString();

    private Button infoButton;
    private EditText ipEditText;
    private TextView locationTextView;
    private ImageView flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoButton = (Button) findViewById(R.id.infoButton);
        ipEditText = (EditText) findViewById(R.id.ipEditText);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        flag = (ImageView) findViewById(R.id.flagImageView);

        infoButton.setOnClickListener(this);
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        computeCurrentLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        ResponseHeader header = new ResponseHeader();
        StringBuilder builder = new StringBuilder();
        try {
            List<LocationInformation> locationInformations = header.execute(ipEditText.getText().toString().trim()).get();
            for (LocationInformation information : locationInformations) {
                builder.append(information.getCountry() + " " + information.getCity() + "\n");
            }
            Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            Log.e(TAG, ErrorConstants.PROCESSING_STOPPED_ERROR);
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, ErrorConstants.RESPONSE_PROCESSING_ERROR);
        }
    }

    private void computeCurrentLocation() {
        try {
            List<LocationInformation> informationList = new ResponseHeader().execute("").get();
            LocationInformation information = informationList.get(0);
            locationTextView.setText(information.getIp() + " " + information.getCity() + ", " + information.getCountry());
            flag.setImageBitmap(information.getFlag());
        } catch (InterruptedException e) {
            Log.e(TAG, ErrorConstants.PROCESSING_STOPPED_ERROR);
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, ErrorConstants.RESPONSE_PROCESSING_ERROR);
        }
    }
}
