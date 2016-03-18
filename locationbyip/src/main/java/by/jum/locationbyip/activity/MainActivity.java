package by.jum.locationbyip.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.jum.locationbyip.R;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.Messages;
import by.jum.locationbyip.models.LocationInformation;
import by.jum.locationbyip.processing.ResponseHeader;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.toString();

    private LinearLayout currentLocationLayout;

    private Button refreshButton;
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
        locationTextView = (TextView) findViewById(R.id.location_text_view);
        flag = (ImageView) findViewById(R.id.flag_image_view);
        currentLocationLayout = (LinearLayout) findViewById(R.id.currentLocationLayout);

        infoButton.setOnClickListener(this);
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        if (isNetworkAvailable(this)) {
            computeCurrentLocation();
        } else {
            addRefreshButton();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeRefreshButton();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isNetworkAvailable(this)) {
            computeCurrentLocation();
        } else {
            addRefreshButton();
        }
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
        if (!isNetworkAvailable(this)) {
            addRefreshButton();
            return;
        }
        Intent intent = new Intent(this, InformationActivity.class);
        intent.putExtra(Messages.IP_TEXT, ipEditText.getText().toString().replace(" ", ""));
        startActivity(intent);

    }

    private void computeCurrentLocation() {
        try {
            LocationInformation information = new ResponseHeader().execute("").get();
            locationTextView.setText(information.getIp() + "\n" + information.getCountry() + ", " + information.getCity());
            flag.setImageBitmap(information.getFlag());
        } catch (InterruptedException e) {
            Log.e(TAG, ErrorConstants.PROCESSING_STOPPED_ERROR);
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, ErrorConstants.RESPONSE_PROCESSING_ERROR);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void addRefreshButton() {
        if (refreshButton == null) {
            flag.setImageBitmap(null);
            locationTextView.setText(Messages.NOT_INTERNET);

            refreshButton = new Button(this);
            refreshButton.setText(Messages.REFRESH);
            currentLocationLayout.addView(refreshButton);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetworkAvailable(MainActivity.this)) {
                        removeRefreshButton();
                        computeCurrentLocation();
                    }
                }
            });
        }
    }

    private void removeRefreshButton() {
        currentLocationLayout.removeView(refreshButton);
        refreshButton = null;
    }
}
