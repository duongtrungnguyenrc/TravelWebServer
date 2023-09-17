package com.web.travel.core;

import org.springframework.stereotype.Component;

import java.util.Date;

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
}
