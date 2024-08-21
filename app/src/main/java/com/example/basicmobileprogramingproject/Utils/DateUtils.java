package com.example.basicmobileprogramingproject.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Chuyển đổi từ đối tượng Date sang định dạng chuỗi dd/MM/yyyy
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    // Chuyển đổi từ định dạng chuỗi dd/MM/yyyy sang đối tượng Date
    public static Date parseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy định dạng ngày mặc định dd/MM/yyyy
    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static String getToday() {
        return formatDate(new Date());
    }
}
