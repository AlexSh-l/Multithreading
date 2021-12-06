package com.alex.multithreading.entity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusDepot {
    private static BusDepot instance;

    public static BusDepot getInstance() {
        if (instance == null) {
            instance = new BusDepot();
        }
        return instance;
    }

    public void sendBuses(List<String> busNumbers, List<List<BusStop>> routes, int amountOfBusesAtATime) {
        ExecutorService executorService = Executors.newFixedThreadPool(amountOfBusesAtATime);
        for (int i = 0; i < busNumbers.size(); i++) {
            executorService.execute(new Bus(busNumbers.get(i), routes.get(i)));
        }
        executorService.shutdown();
    }
}
