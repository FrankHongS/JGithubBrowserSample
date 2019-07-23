package com.frankhon.jgithubbrowsersample.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class StringUtil {

    public static final Pattern LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");
    public static final Pattern PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)");

    public static final String NEXT_LINK = "next";

    private StringUtil() {
    }

    public static Map<String, String> extractLinks(String str) {
        Map<String, String> links = new LinkedHashMap<>();
        Matcher matcher = LINK_PATTERN.matcher(str);

        while (matcher.find()) {
            int count = matcher.groupCount();
            if (count == 2) {
                links.put(matcher.group(2), matcher.group(1));
            }
        }

        return links;
    }
}
