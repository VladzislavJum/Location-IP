package by.jum.locationbyip.processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import by.jum.locationbyip.constants.ErrorConstants;
import by.jum.locationbyip.constants.KeyJSONAttrConstants;
import by.jum.locationbyip.constants.Messages;
import by.jum.locationbyip.constants.UrlConstants;
import by.jum.locationbyip.models.LocationInformation;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResponseParserImlp implements ResponseParser {

    private final static String TAG = ResponseParserImlp.class.toString();

    @Override
    public LocationInformation getLocationInformation(String information) {
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject(information);
            return getGeoInformation(jsonObject);
        } catch (JSONException e) {
            Log.e(TAG, ErrorConstants.JSON_PARSE_EXCEPTION);
        }
        return null;
    }

    private LocationInformation getGeoInformation(JSONObject jsonObject) throws JSONException {
        LocationInformation locationInformation = new LocationInformation();
        JSONObject cityJsonObject = jsonObject.getJSONObject(KeyJSONAttrConstants.CITY);
        JSONObject countryJsonObject = jsonObject.getJSONObject(KeyJSONAttrConstants.COUNTRY);
        String domain = countryJsonObject.getString(KeyJSONAttrConstants.DOMAIN).toLowerCase();
        String country = countryJsonObject.getString(KeyJSONAttrConstants.NAME);
        String city = cityJsonObject.getString(KeyJSONAttrConstants.NAME);
        if (country == null || country.equals("")) {
            locationInformation.setCountry(Messages.NOT_INF);
        } else {
            locationInformation.setCountry(country);
        }
        if (city == null || city.equals("")){
            locationInformation.setCity(Messages.NOT_INF);
        } else {
            locationInformation.setCity(city);
        }
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
