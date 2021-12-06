package com.alex.multithreading.parser.impl;

import com.alex.multithreading.entity.BusStop;
import com.alex.multithreading.parser.CustomParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CustomParserImpl implements CustomParser {

    @Override
    public void parse(List<String> values, List<List<BusStop>> routes, List<String> busNumbers) {
        if (values != null) {
            for (String value : values) {
                String[] scheduleData = value.split(VALUE_SPLITTER);
                List<BusStop> route = new ArrayList<>();
                switch (scheduleData[0]) {
                    case "bus": {
                        busNumbers.add(scheduleData[1]);
                        break;
                    }
                    case "route": {
                        for (int i = 1; i < scheduleData.length; i++) {
                            String stationName = scheduleData[i];
                            BusStop stop = null;
                            for (var existingRoute : routes) {
                                for (var existingStation : existingRoute) {
                                    if (existingStation.getName().equals(stationName)) {
                                        stop = existingStation;
                                    }
                                }
                            }
                            if (stop == null) {
                                route.add(new BusStop(i, stationName, 3));
                            } else {
                                Semaphore semaphore = stop.getSemaphore();
                                route.add(new BusStop(i, stationName, semaphore));
                            }
                        }
                        routes.add(route);
                        break;
                    }
                }
            }
        }
    }
}
