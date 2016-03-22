package by.jum.locationbyip.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import by.jum.locationbyip.R;
import by.jum.locationbyip.constants.Messages;
import by.jum.locationbyip.constants.Patterns;
import by.jum.locationbyip.database.DatabaseHandler;
import by.jum.locationbyip.models.ItemInformation;
import by.jum.locationbyip.models.LocationInformation;
import by.jum.locationbyip.processing.APIConnector;
import by.jum.locationbyip.processing.RequestParserImpl;
import by.jum.locationbyip.processing.ResponseParser;
import by.jum.locationbyip.processing.ResponseParserImlp;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class InformationActivity extends Activity {

    private static final String TAG = InformationActivity.class.toString();
    private LayoutInflater ltInflater;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        String info = intent.getStringExtra(Messages.IP_TEXT);

        ltInflater = getLayoutInflater();
        Map<String, Integer> countIpMap = new RequestParserImpl().parse(info);
        linearLayout = (LinearLayout) findViewById(R.id.for_items_linear);

        new SomeClass(this).execute(countIpMap);
    }

    private class SomeClass extends AsyncTask<Map<String, Integer>, View, Void> {

        private Context context;
        private Pattern pattern;
        private ResponseParser responseParser;
        private ProgressDialog dialog;

        public SomeClass(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Map<String, Integer>... params) {
            Map<String, Integer> countIpMap = params[0];
            Set<String> ipSet = countIpMap.keySet();
            ItemInformation itemInformation = new ItemInformation();
            LocationInformation locationInformation;

            DatabaseHandler handler = new DatabaseHandler(context);
            SQLiteDatabase database = handler.getWritableDatabase();

            pattern = Pattern.compile(Patterns.CORRECT_IP_PATTERN);
            APIConnector apiConnector = new APIConnector();
            responseParser = new ResponseParserImlp();

            for (String ip : ipSet) {
                if (!isValidIp(ip)) {
                    itemInformation.setInfForTextView(ip + " (" + countIpMap.get(ip) + ")\n" + Messages.INCORRECT_IP);
                    itemInformation.setFlag(BitmapFactory.decodeResource(getResources(), R.drawable.sad_smile));
                    fillItem(itemInformation);
                    Log.i(TAG, ip + " is" + Messages.INCORRECT_IP);
                } else {
                    locationInformation = handler.getInformationByIP(ip, database);
                    if (locationInformation == null) {
                        locationInformation = getInformationByAPI(ip, apiConnector);
                        if (locationInformation == null) {
                            itemInformation.setInfForTextView(ip + " (" + countIpMap.get(ip) + ")\n" +
                                    Messages.NOT_INF + ", " + Messages.NOT_INF);
                            itemInformation.setFlag(BitmapFactory.decodeResource(getResources(), R.drawable.not_inf));
                            fillItem(itemInformation);
                            continue;
                        } else {
                            handler.addInformation(locationInformation, database);
                        }
                    }
                    itemInformation.setInfForTextView(ip + " (" + countIpMap.get(ip) + ")\n" +
                            locationInformation.getCountry() + ", " + locationInformation.getCity());
                    itemInformation.setFlag(locationInformation.getFlag());
                    fillItem(itemInformation);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(View... values) {
            super.onProgressUpdate(values);
            linearLayout.addView(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Processing");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
        }

        private void fillItem(ItemInformation itemInformation) {
            View item = ltInflater.inflate(R.layout.item, linearLayout, false);
            TextView textView = (TextView) item.findViewById(R.id.info_text);
            ImageView imageView = (ImageView) item.findViewById(R.id.flag_image_view);
            textView.setText(itemInformation.getInfForTextView());
            imageView.setImageBitmap(itemInformation.getFlag());
            publishProgress(item);
        }

        private LocationInformation getInformationByAPI(String info, APIConnector apiConnector) {
            String response = apiConnector.getResponse(info);
            return responseParser.getLocationInformation(response);
        }

        private boolean isValidIp(final String ip) {
            return pattern.matcher(ip).matches();
        }
    }
}
