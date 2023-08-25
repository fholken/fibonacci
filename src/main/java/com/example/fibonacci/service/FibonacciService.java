package com.example.fibonacci.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FibonacciService {
    public List<Long> generateFibonacciSeries() {
        LocalTime currentTime = LocalTime.now();
        //LocalTime currentTime = LocalTime.of(14, 23, 4);  // Ejemplo: 14:23:04
        int minutesSeed = currentTime.getMinute();
        int secondsNumNumbers = currentTime.getSecond();

        int X = minutesSeed / 10;
        int Y = minutesSeed % 10;
        secondsNumNumbers = (minutesSeed < 10) ? secondsNumNumbers + 1 : secondsNumNumbers + 2;

        List<Long> series = new ArrayList<>();
        series.add((long) X);
        series.add((long) Y);

        for (int i = 2; i < secondsNumNumbers; i++) {
            long nextNumber = series.get(i - 1) + series.get(i - 2);
            series.add(nextNumber);
        }

        Collections.reverse(series);
        return series;
    }
}
