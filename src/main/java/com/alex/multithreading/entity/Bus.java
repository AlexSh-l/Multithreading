package com.alex.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Bus implements Runnable {

    private static final Logger logger = LogManager.getLogger();
    private String number;
    private List<Passenger> passengers = new ArrayList<>();
    private List<BusStop> route;
    private int stopIndex;
    private final Random random = new Random();
    private boolean isLeaving = true;

    public Bus(String number, List<BusStop> route) {
        this.stopIndex = 0;
        this.number = number;
        this.route = route;
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
    }

    public void removeAllPassengers(List<Passenger> passengers) {
        this.passengers.removeAll(passengers);
    }

    public void run() {
        while (stopIndex < route.size()) {
            BusStop currentStop = route.get(stopIndex);
            Semaphore semaphore = currentStop.getSemaphore();
            try {
                semaphore.acquire();
                logger.info("Bus number: " + number + " arrived to " + currentStop.getName() + " with passengers:" + passengers.size());
                Passenger leavingPassenger;
                if (passengers.size() > 0) {
                    int leavingPassengers = random.nextInt(passengers.size());
                    int passengerIndex;
                    for (int i = 0; i < leavingPassengers; i++) {
                        if (passengers.size() == 1) {
                            leavingPassenger = passengers.get(0);
                        } else {
                            passengerIndex = random.nextInt(passengers.size());
                            if (passengerIndex >= passengers.size()) {
                                passengerIndex = passengers.size() - 1;
                            }
                            leavingPassenger = passengers.get(passengerIndex);
                        }
                        currentStop.arrivingPassenger(leavingPassenger);
                        passengers.remove(leavingPassenger);
                    }
                }
                Optional<Passenger> boardingPassenger;
                if (currentStop.passengerCount() > 0) {
                    int boardingPassengers = random.nextInt(currentStop.passengerCount());
                    for (int i = 0; i < boardingPassengers; i++) {
                        boardingPassenger = currentStop.boardingPassenger();
                        if (boardingPassenger.isPresent()) {
                            passengers.add(boardingPassenger.get());
                        }
                    }
                }
                TimeUnit.SECONDS.sleep(2);
                logger.info("Bus number: " + number + " leaving the " + currentStop.getName() + " with passengers:" + passengers.size());
                stopIndex++;
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            semaphore.release();
            if (isLeaving && (stopIndex == route.size())) {
                invertRoute();
                stopIndex = 0;
            }
        }
    }

    private void invertRoute() {
        List<BusStop> existingRoute = List.copyOf(route);
        route.clear();
        int j = 1;
        for (int i = existingRoute.size() - 1; i >= 0; i--) {
            BusStop stop = existingRoute.get(i);
            stop.setNumber(j);
            route.add(stop);
            j++;
        }
        isLeaving = false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Bus{");
        sb.append("number='").append(number).append('\'');
        sb.append(", passengers=").append(passengers);
        sb.append(", route=").append(route);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bus bus = (Bus) o;
        if (stopIndex != bus.stopIndex) return false;
        if (number != null ? !number.equals(bus.number) : bus.number != null) return false;
        if (passengers != null ? !passengers.equals(bus.passengers) : bus.passengers != null) return false;
        return route != null ? route.equals(bus.route) : bus.route == null;
    }

    @Override
    public int hashCode() {
        int result = number != null ? number.hashCode() : 0;
        result = 31 * result + (passengers != null ? passengers.hashCode() : 0);
        result = 31 * result + (route != null ? route.hashCode() : 0);
        result = 31 * result + stopIndex;
        return result;
    }
}
