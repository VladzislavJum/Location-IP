package by.jum.locationbyip.processing;

import by.jum.locationbyip.models.LocationInformation;

public interface ResponseParser {
    LocationInformation getLocationInformation(String information);
}
