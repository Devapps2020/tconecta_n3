package com.blm.qiubopay.tools;

import java.util.regex.Pattern;

public class Validator {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern NAME_REGEX = Pattern.compile("^[\\\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,15}$");//("/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])([A-Za-z\\d$@$!%*?&]|[^ ]){8,15}$/");

    private Validator(){}

    public static boolean isEmailValid(String email){
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
    }

    public static boolean isPasswordValid(String data){
        return VALID_PASSWORD_REGEX.matcher(data).find();
    }

    public static boolean isChassisNameValid(String chassisName) {
        return chassisName.length() > 4;
    }

    public static boolean isChassisValid(String chassis) {
        return chassis.length() == 17;
    }

    public static boolean isNameValid(String name){
        return name.trim().length() > 2;
    }

    public static boolean isNotEmpty(String name){
        return name.trim().length() > 0;
    }

    public static boolean isValidLength(String param, int min, int max){
        return param.trim().length() >= min && param.trim().length() <= max;
    }

    public static boolean isPhoneNumberValid(String phoneNumber){
        for (char ch : phoneNumber.toCharArray()) {
            if(!Character.isDigit(ch))
                return false;
        }
        return phoneNumber.length() == 10;
    }
}
