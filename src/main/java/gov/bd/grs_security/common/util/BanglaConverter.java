package gov.bd.grs_security.common.util;

import gov.bd.grs_security.common.enums.ServiceType;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class BanglaConverter {

    private static final char[] banglaDigits = {'০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'};
    private static final String[] banglaDigitsString = {"০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯"};

    private static WeakHashMap<String, String> amPm, days, months;
    public static Map<Integer, String> MONTH_INT;

    static {
        amPm = new WeakHashMap<String, String>() {{
            put("AM", "পুর্বাহ্ন");
            put("PM", "অপরাহ্ন");
        }};
        days = new WeakHashMap<String, String>() {{
            put("Sun", "রবিবার");
            put("Mon", "সোমবার");
            put("Tue", "মঙ্গলবার");
            put("Wed", "বুধবার");
            put("Thu", "বৃহস্পতি");
            put("Fri", "শুক্রবার");
            put("Sat", "শনিবার");
        }};
        months = new WeakHashMap<String, String>() {{
            put("Jan", "জানুয়ারী");
            put("Feb", "ফেব্রুয়ারি");
            put("Mar", "মার্চ");
            put("Apr", "এপ্রিল");
            put("May", "মে");
            put("Jun", "জুন");
            put("Jul", "জুলাই");
            put("Aug", "অগাস্ট");
            put("Sep", "সেপ্টেম্বর");
            put("Oct", "অক্টোবর");
            put("Nov", "নভেম্বর");
            put("Dec", "ডিসেম্বর");
        }};

        MONTH_INT = new HashMap<Integer, String>() {{
            put(1, "জানুয়ারী");
            put(2, "ফেব্রুয়ারি");
            put(3, "মার্চ");
            put(4, "এপ্রিল");
            put(5, "মে");
            put(6, "জুন");
            put(7, "জুলাই");
            put(8, "অগাস্ট");
            put(9, "সেপ্টেম্বর");
            put(10, "অক্টোবর");
            put(11, "নভেম্বর");
            put(12, "ডিসেম্বর");
        }};
    }

    public static final StringBuilder convertToBanglaDigitBuilder(String number) {
        if (number == null)
            return new StringBuilder("");
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < number.length(); i++) {
                if (Character.isDigit(number.charAt(i))) {
                    if (((int) (number.charAt(i)) - 48) <= 9) {
                        builder.append(banglaDigits[(int) (number.charAt(i)) - 48]);
                    } else {
                        builder.append(number.charAt(i));
                    }
                } else {
                    builder.append(number.charAt(i));
                }
            }
        } catch (Exception e) {
            return new StringBuilder("");
        }
        return builder;
    }

    public static final String getDateBanglaFromEnglish(String number) {
        StringBuilder builder = convertToBanglaDigitBuilder(number);
        String dateBangla = builder.toString();
        if (dateBangla.contains("AM")) {
            dateBangla = dateBangla.replaceAll("AM", amPm.get("AM"));
        } else if (dateBangla.contains("PM")) {
            dateBangla = dateBangla.replaceAll("PM", amPm.get("PM"));
        }
        return dateBangla;
    }

    public static final StringBuilder getDateBanglaFromEnglishBuilder(String number) {
        if (number == null)
            return new StringBuilder("");
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < number.length(); i++) {
                if (Character.isDigit(number.charAt(i))) {
                    if (((int) (number.charAt(i)) - 48) <= 9) {
                        builder.append(banglaDigits[(int) (number.charAt(i)) - 48]);
                    } else {
                        builder.append(number.charAt(i));
                    }
                } else {
                    builder.append(number.charAt(i));
                }
            }
        } catch (Exception e) {
            return new StringBuilder("");
        }
        if (builder.indexOf("AM") != -1) {
            StringUtil.replaceAll(builder, "AM", amPm.get("AM"));
        } else if (builder.indexOf("PM") != -1) {
            StringUtil.replaceAll(builder, "PM", amPm.get("PM"));
        }
        return builder;
    }

    public static final StringBuilder getDateBanglaFromEnglishFullBuilder(String number) {
        StringBuilder banglaDateBuilder = getDateBanglaFromEnglishBuilder(number);
        for (String day : days.keySet()) {
            if (banglaDateBuilder.indexOf(day) != -1) {
                StringUtil.replaceAll(banglaDateBuilder, day, days.get(day));
                break;
            }
        }
        for (String month : months.keySet()) {
            if (banglaDateBuilder.indexOf(month) != -1) {
                StringUtil.replaceAll(banglaDateBuilder, month, months.get(month));
                break;
            }
        }

        return banglaDateBuilder;
    }

    public static final String getDateBanglaFromEnglishFull(String number) {

        return getDateBanglaFromEnglishFullBuilder(number).toString();
    }

    public static final String getDateBanglaFromEnglishFull24HourFormat(String number) {
        String banglaDate = getDateBanglaFromEnglishFull(number);

        String[] dates = banglaDate.split(" ");
        String date = "";
        String[] time = dates[3].split(":");
        date += dates[1] + "-" + dates[2] + "-" + dates[5] + " সময়: " + time[0] + "-" + time[1];

        return date;
    }

    public static String convertToBanglaDigit(long id) {
        if (id == -1){
            return null;
        }
        String idInString = String.valueOf(id);
        return convertToBanglaDigit(idInString);
    }

    public static String convertToBanglaDigit(String idInString) {

        StringBuilder builder = convertToBanglaDigitBuilder(idInString);
        return builder.toString();
    }

    public static String convertToEnglish(String id) {
        StringBuilder idInStringBuilder = new StringBuilder(String.valueOf(id));

        String englishDigit = "";
        String banglaDigit = "";

        for (int i = 0; i < banglaDigits.length; i++) {
            englishDigit = String.valueOf(i);
            banglaDigit = banglaDigitsString[i];
            StringUtil.replaceAll(idInStringBuilder, banglaDigit, englishDigit);
        }
        return idInStringBuilder.toString();
    }

    public static String convertAllToEnglish(String id) {

        return convertToEnglish(id);
    }

    public static Boolean isABanglaDigit(String id) {

        String englishDigit = "";
        String banglaDigit = "";

        StringBuilder builder = new StringBuilder(id);

        for (int i = 0; i < banglaDigits.length; i++) {
            englishDigit = String.valueOf(i);
            banglaDigit = banglaDigitsString[i];
            StringUtil.replaceAll(builder, englishDigit, banglaDigit);
        }
        try {
            Long.parseLong(builder.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

