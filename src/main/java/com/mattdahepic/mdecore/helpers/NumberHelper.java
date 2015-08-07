package com.mattdahepic.mdecore.helpers;

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
}
