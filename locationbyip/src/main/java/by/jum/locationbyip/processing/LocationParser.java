package by.jum.locationbyip.processing;

import by.jum.locationbyip.models.LocationInformation;

public interface LocationParser {
    LocationInformation getLocationInformation(String information);
}
