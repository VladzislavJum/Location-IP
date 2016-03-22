package by.jum.locationbyip.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import by.jum.locationbyip.R;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.Messages;
import by.jum.locationbyip.models.LocationInformation;
import by.jum.locationbyip.processing.ResponseHandler;

import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity implements View.OnClickListener {

    private final String TAG = MainActivity.class.toString();

    private ImageButton refreshButton;
    private Button infoButton;
    private EditText ipEditText;
    private TextView locationTextView;
    private ImageView flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoButton = (Button) findViewById(R.id.info_button);
        ipEditText = (EditText) findViewById(R.id.ip_edit_text);
        locationTextView = (TextView) findViewById(R.id.location_text_view);
        flag = (ImageView) findViewById(R.id.flag_image_view);

        refreshButton = (ImageButton) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(MainActivity.this)) {
                    refreshButton.setVisibility(View.INVISIBLE);
                    computeCurrentLocation();
                } else {
                    Toast.makeText(MainActivity.this, Messages.NOT_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });

        infoButton.setOnClickListener(this);

        if (isNetworkAvailable(this)) {
            computeCurrentLocation();
        } else {
            refreshButton.setVisibility(View.VISIBLE);
            locationTextView.setText(Messages.NOT_INTERNET);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isNetworkAvailable(this)) {
            refreshButton.setVisibility(View.INVISIBLE);
            computeCurrentLocation();
        } else {
            refreshButton.setVisibility(View.VISIBLE);
            locationTextView.setText(Messages.NOT_INTERNET);
        }
    }

    @Override
    public void onClick(View v) {
        if (!isNetworkAvailable(this)) {
            refreshButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, Messages.NOT_INTERNET, Toast.LENGTH_SHORT).show();
            locationTextView.setText(Messages.NOT_INTERNET);
            return;
        }
        String message = ipEditText.getText().toString().replace(" ", "");
        if (message.equals("")) {
            Toast.makeText(this, Messages.INPUT_IP, Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, InformationActivity.class);
        intent.putExtra(Messages.IP_TEXT, message);
        startActivity(intent);

    }

    private void computeCurrentLocation() {
        try {
            LocationInformation information = new ResponseHandler().execute("").get();
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
}
