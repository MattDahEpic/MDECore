package com.mattdahepic.mdecore.common.helpers;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NumberHelper {
    public static String IntegerToRomanNumerals(int number) {
        String ret = "";
        while (number >= 1000) {
            ret += "M";
            number -= 1000;
        }
        while (number >= 900) {
            ret += "CM";
            number -= 900;
        }
        while (number >= 500) {
            ret += "D";
            number -= 500;
        }
        while (number >= 400) {
            ret += "CD";
            number -= 400;
        }
        while (number >= 100) {
            ret += "C";
            number -= 100;
        }
        while (number >= 90) {
            ret += "XC";
            number -= 90;
        }
        while (number >= 50) {
            ret += "L";
            number -= 50;
        }
        while (number >= 40) {
            ret += "XL";
            number -= 40;
        }
        while (number >= 10) {
            ret += "X";
            number -= 10;
        }
        while (number >= 9) {
            ret += "IX";
            number -= 9;
        }
        while (number >= 5) {
            ret += "V";
            number -= 5;
        }
        while (number >= 4) {
            ret += "IV";
            number -= 4;
        }
        while (number >= 1) {
            ret += "I";
            number -= 1;
        }
        return ret;
    }
    private static final NavigableMap<Long, String> suffixes = new TreeMap<Long, String>();
    static {
        suffixes.put(1000L, "k");
        suffixes.put(1000000L, "M");
        suffixes.put(1000000000L, "G");
        suffixes.put(1000000000000L, "T");
        suffixes.put(1000000000000000L, "P");
        suffixes.put(1000000000000000000L, "E");
    }
    public static String niceifyNumber (long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return niceifyNumber(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + niceifyNumber(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
}
