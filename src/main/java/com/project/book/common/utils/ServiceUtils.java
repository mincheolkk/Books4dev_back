package com.project.book.common.utils;

import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.Objects;

public class ServiceUtils {
    public static String listToString(List<String> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Strings.EMPTY;
        }
        return String.join(",", list);
    }
}
