package com.lambdaprofessional.myvillage.utils;

public class BoolUtils {

    public static Boolean parseBoolean(String boolStr) {
        if (boolStr == null) return null;
        if (boolStr.equalsIgnoreCase("true")) return true;
        if (boolStr.equalsIgnoreCase("yes")) return true;
        if (boolStr.equalsIgnoreCase("y")) return true;
        if (boolStr.equalsIgnoreCase("1")) return true;
        if (boolStr.equalsIgnoreCase("false")) return false;
        if (boolStr.equalsIgnoreCase("no")) return false;
        if (boolStr.equalsIgnoreCase("n")) return false;
        if (boolStr.equalsIgnoreCase("0")) return false;
        return null;
    }

}
