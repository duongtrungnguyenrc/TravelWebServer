package com.web.travel.core;

import com.web.travel.model.Rate;

import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.List;

public class RateCalculator {
    public static double getAverageRates(Collection<Rate> rates){
        List<Integer> points = rates.stream().map(
                Rate::getPoint
        ).toList();

        IntSummaryStatistics intSummaryStatistics = points
                .stream()
                .mapToInt(point -> point)
                .summaryStatistics();
        return Math.round(intSummaryStatistics.getAverage()*100.0)/100.0;
    }
}
