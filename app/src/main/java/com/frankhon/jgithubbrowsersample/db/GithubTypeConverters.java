package com.frankhon.jgithubbrowsersample.db;

import androidx.room.TypeConverter;

import com.hon.mylogger.MyLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank_Hon on 7/26/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class GithubTypeConverters {

    private GithubTypeConverters() {
    }

    @TypeConverter
    public static List<Integer> stringToInt(String data) {
        if (data == null) {
            return null;
        }

        String[] strArray = data.split(",");
        List<Integer> target = new ArrayList<>();

        try {
            for (String str : strArray) {
                target.add(Integer.valueOf(str));
            }
            return target;
        } catch (NumberFormatException e) {
            MyLogger.e(e, "Cannot convert %s to number", data);
            return null;
        }
    }

    @TypeConverter
    public static String intListToString(List<Integer> ints) {

        if (ints == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ints.size(); i++) {
            if (i == ints.size() - 1) {
                sb.append(ints.get(i));
            } else {
                sb.append(ints.get(i)).append(",");
            }
        }
        return sb.toString();
    }
}
