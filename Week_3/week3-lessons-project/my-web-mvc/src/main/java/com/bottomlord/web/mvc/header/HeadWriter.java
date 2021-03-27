package com.bottomlord.web.mvc.header;

import java.util.List;
import java.util.Map;

/**
 * @author ChenYue
 * @date 2021/3/2 18:14
 */
public interface HeadWriter {
    void write(Map<String, List<String>> headers, String... headerValues);
}
