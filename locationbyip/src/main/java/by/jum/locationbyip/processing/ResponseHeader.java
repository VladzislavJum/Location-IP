package by.jum.locationbyip.processing;

import android.os.AsyncTask;
import android.util.Log;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.UrlConstants;
import by.jum.locationbyip.models.LocationInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ResponseHeader extends AsyncTask<String, Void, LocationInformation> {

    private final String TAG = ResponseHeader.class.toString();

    @Override
    protected LocationInformation doInBackground(String... params) {
        String response = getResponse(params[0]);
        LocationParser locationParser = new LocationParserImlp();
        return locationParser.getLocationInformation(response);
    }

    private String getResponse(String ip) {
        BufferedReader reader = null;
        StringBuilder allResponse = new StringBuilder();
        try {
            URL geoAPIUrl = new URL(UrlConstants.GEO_API_URL + ip);
            URLConnection urlConnection = geoAPIUrl.openConnection();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String response;
            while ((response = reader.readLine()) != null) {
                allResponse.append(response);
            }
        } catch (IOException e) {
            Log.e(TAG, ErrorConstants.RESPONSE_READ_ERROR);
                    e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, ErrorConstants.CONNECTION_CLOSE_ERROR);
                e.printStackTrace();
            }
        }
        return allResponse.toString();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}