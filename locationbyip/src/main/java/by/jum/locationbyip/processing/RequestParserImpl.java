package by.jum.locationbyip.processing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestParserImpl implements RequestParser{

    private static final String IP_SEPARATOR_PATTERN = ",|;|\n";
    private Pattern pattern;

    public RequestParserImpl() {
        pattern = Pattern.compile(IP_SEPARATOR_PATTERN);
    }

    @Override
    public Map<String, Integer> parse(String response) {
        Map<String, Integer> countIpMap = new HashMap<>();
        String[] ipStrings = pattern.split(response);
        List<String> ipList = Arrays.asList(ipStrings);
        Integer count;
        String elem;

        Iterator<String> iterator = ipList.iterator();
        while (iterator.hasNext()){
            elem = iterator.next();
            count = countIpMap.get(elem);
            countIpMap.put(elem, count == null ? 1 : count + 1);
        }
        return countIpMap;
    }
}
