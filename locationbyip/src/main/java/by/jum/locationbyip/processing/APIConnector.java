package by.jum.locationbyip.processing;

import android.util.Log;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.UrlConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class APIConnector {

    private final String TAG = APIConnector.class.toString();

    public String getResponse(String ip) {
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

}
