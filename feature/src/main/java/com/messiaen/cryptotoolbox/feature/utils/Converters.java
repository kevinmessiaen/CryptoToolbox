package com.messiaen.cryptotoolbox.feature.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.messiaen.cryptotoolbox.feature.persistence.entities.Quote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.room.TypeConverter;

public class Converters {

    public static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<String> csvToList(String csv) {
        if (csv == null)
            return null;

        List<String> values = new ArrayList<>();

        for (String value : csv.split(";"))
            values.add(value);

        return values;
    }

    @TypeConverter
    public static String listToCsv(List<String> values) {
        if (values == null)
            return null;

        String csv = "";

        for (int i = 0; i < values.size(); i++)
            csv += values.get(i) + ((values.size() < i + 1) ? "," : "");

        return csv;
    }

    @TypeConverter
    public static Map<String, Quote> jsonToQuoteMap(String value) {
        return gson.fromJson(value, new TypeToken<Map<String, Quote>>(){}.getType());
    }

    @TypeConverter
    public static String quoteMapToJson(Map<String, Quote> value) {
        return gson.toJson(value);
    }
}
