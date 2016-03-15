package by.jum.locationbyip.processing;

import by.jum.locationbyip.LocationInformation;

import java.util.List;

public interface LocationParser {
    List<LocationInformation> getLocationInformation(String information);
}
