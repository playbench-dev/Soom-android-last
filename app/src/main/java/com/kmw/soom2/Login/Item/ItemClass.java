package com.kmw.soom2.Login.Item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemClass {

    public static boolean checkEmail(String email) {
        final String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPass(String password) {
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z\\d~!@#$%^&*()+/_?:;<>'\"-]{6,16}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean checkNick(String nickname) {
        Pattern p = Pattern.compile("[A-Za-z\\d[ㄱ-ㅎ가-힣]\\d$@$!%*#?&\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]{2,8}$");
        Matcher m = p.matcher(nickname);
        return m.matches();
    }
}
