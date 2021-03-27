package com.bottomlord.web.mvc.header;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ChenYue
 * @date 2021/3/2 18:18
 */
public class CacheControlHeadWriter implements HeadWriter {
    @Override
    public void write(Map<String, List<String>> headers, String... headerValues) {
        headers.put("cache-control", Arrays.asList(headerValues));
    }
}
