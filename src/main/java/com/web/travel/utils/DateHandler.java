package com.web.travel.utils;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.TimeZone;

@Component
public class DateHandler {
    public int getDiffDay(Date date1, Date date2){
        long date1InMs = date1.getTime();
        long date2InMs = date2.getTime();
        long timeDiff = 0;

        if(date1InMs > date2InMs)
            timeDiff = date1InMs - date2InMs;
        else
            timeDiff = date2InMs - date1InMs;

        return (int) (timeDiff / (1000 * 60 * 60 * 24));
    }

    public static Date getCurrentDateTime(){
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Date date = new Date();
        date.setTime(date.getTime() + timeZone.getRawOffset());
        return date;
    }
}
