package by.jum.locationbyip.processing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ResponseParser {
    private static final String IP_PATTERN = ",|;|\n";

    public Map<String, Integer> parse(String response) {
        Map<String, Integer> countIpMap = new HashMap<>();
        Integer count;
        String elem;

        Pattern pattern = Pattern.compile(IP_PATTERN);
        String[] ipStrings = pattern.split(response);
        List<String> ipList = Arrays.asList(ipStrings);

        Iterator<String> iterator = ipList.iterator();

        while (iterator.hasNext()){
            elem = iterator.next();
            count = countIpMap.get(elem);
            countIpMap.put(elem, count == null ? 1 : count + 1);
        }

        return countIpMap;
    }
}
