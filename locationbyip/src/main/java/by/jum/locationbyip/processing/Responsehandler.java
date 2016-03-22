package by.jum.locationbyip.processing;

import android.os.AsyncTask;
import by.jum.locationbyip.models.LocationInformation;

public class ResponseHandler extends AsyncTask<String, Void, LocationInformation> {

    @Override
    protected LocationInformation doInBackground(String... params) {
        String response = new APIConnector().getResponse(params[0]);
        ResponseParser responseParser = new ResponseParserImlp();
        return responseParser.getLocationInformation(response);
    }
}