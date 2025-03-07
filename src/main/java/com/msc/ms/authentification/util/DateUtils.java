package com.msc.ms.authentification.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {


    public static Date addMonths(Date pDateToModify, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pDateToModify);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }
}
