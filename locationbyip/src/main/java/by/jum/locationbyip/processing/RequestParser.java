package by.jum.locationbyip.processing;

import java.util.Map;

public interface RequestParser {
    Map<String, Integer> parse(String response);
}
