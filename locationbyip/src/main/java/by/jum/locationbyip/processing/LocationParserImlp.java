package by.jum.locationbyip.processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import by.jum.locationbyip.LocationInformation;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.KeyJSONAttrConstants;
import by.jum.locationbyip.constants.UrlConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LocationParserImlp implements LocationParser {

    private final String TAG = LocationParserImlp.class.toString();

    @Override
    public List<LocationInformation> getLocationInformation(String information) {
        List<LocationInformation> locationInformations = new ArrayList<>();
        Object json;
        try {
            json = new JSONTokener(information).nextValue();
            JSONObject jsonObject;
            if (json instanceof JSONObject) {
                jsonObject = new JSONObject(information);
                locationInformations.add(getGeoInformation(jsonObject));
            } else if (json instanceof JSONArray) {
                JSONArray jsonArray = new JSONArray(information);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    locationInformations.add(getGeoInformation(jsonObject));
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, ErrorConstants.JSON_PARSE_EXCEPTION);
            e.printStackTrace();
        }
        return locationInformations;
    }

    private LocationInformation getGeoInformation(JSONObject jsonObject) throws JSONException {
        LocationInformation locationInformation = new LocationInformation();
        JSONObject cityJsonObject = jsonObject.getJSONObject(KeyJSONAttrConstants.CITY);
        JSONObject countryJsonObject = jsonObject.getJSONObject(KeyJSONAttrConstants.COUNTRY);
        String domain = countryJsonObject.getString(KeyJSONAttrConstants.DOMAIN).toLowerCase();

        locationInformation.setCity(cityJsonObject.getString(KeyJSONAttrConstants.NAME));
        locationInformation.setCountry(countryJsonObject.getString(KeyJSONAttrConstants.NAME));
        locationInformation.setFlag(getFlag(domain));
        locationInformation.setIp(jsonObject.getString(KeyJSONAttrConstants.IP));
        return locationInformation;
    }


    private Bitmap getFlag(String domain) {
        InputStream stream = null;
        Bitmap flag = null;
        try {
            URL url = new URL(UrlConstants.FLAG_URL + domain + ".png");
            stream = url.openStream();
            flag = BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            Log.e(TAG, ErrorConstants.FLAG_URL_CONNECT_EXCEPTION);
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, ErrorConstants.CONNECTION_CLOSE_ERROR);
                e.printStackTrace();
            }
        }
        return flag;
    }


}
