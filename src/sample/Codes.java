package sample;

import java.util.*;

public class Codes {


    private static Map<String,String> statusList = new HashMap<>();

    static {
        statusList.put("2ffca3b083298826","STATUS_OK");
        statusList.put("76498372f59b8bdb","STATUS_REGISTRATION");
        statusList.put("85ff5d938376480d","STATUS_ERROR1");
        statusList.put("837685ff5d48093d","STATUS_ERROR2");
        statusList.put("8ff488375693d05d","STATUS_ERROR3");
    }

    protected static String getStatus(String code) {
        return statusList.get(code);
    }
}
