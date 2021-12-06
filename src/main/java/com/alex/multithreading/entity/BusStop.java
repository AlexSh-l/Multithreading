package com.alex.multithreading.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class BusStop {
    private int number;
    private String name;
    private final List<Passenger> passengers = new ArrayList<>();
    private Random random = new Random();
    private Semaphore semaphore;

    public BusStop(int number, String name, int numberOfBuses) {
        this.number = number;
        this.name = name;
        this.semaphore = new Semaphore(numberOfBuses);
        int amountOfPassengers = random.nextInt(50);
        for (int i = 0; i <= amountOfPassengers; i++) {
            passengers.add(new Passenger());
        }
    }

    public BusStop(int number, String name, Semaphore semaphore) {
        this.number = number;
        this.name = name;
        this.semaphore = semaphore;
        int amountOfPassengers = random.nextInt(50);
        for (int i = 0; i <= amountOfPassengers; i++) {
            passengers.add(new Passenger());
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void arrivingPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public Optional<Passenger> boardingPassenger() {
        int passengerIndex;
        Passenger boardingPassenger = null;
        if (passengers.size() > 0) {
            if (passengers.size() == 1) {
                passengerIndex = 0;
            } else {
                passengerIndex = random.nextInt(passengers.size());
                if (passengerIndex >= passengers.size()) {
                    passengerIndex = passengers.size() - 1;
                }
            }
            boardingPassenger = passengers.get(passengerIndex);
            passengers.remove(boardingPassenger);
        }
        return Optional.ofNullable(boardingPassenger);
    }

    public int passengerCount() {
        return passengers.size();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BusStop{");
        sb.append("number=").append(number);
        sb.append(", name='").append(name).append('\'');
        sb.append(", passengers=").append(passengers);
        sb.append(", semaphore=").append(semaphore);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStop busStop = (BusStop) o;
        if (number != busStop.number) return false;
        if (name != null ? !name.equals(busStop.name) : busStop.name != null) return false;
        if (passengers != null ? !passengers.equals(busStop.passengers) : busStop.passengers != null) return false;
        return semaphore != null ? semaphore.equals(busStop.semaphore) : busStop.semaphore == null;
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (passengers != null ? passengers.hashCode() : 0);
        result = 31 * result + (semaphore != null ? semaphore.hashCode() : 0);
        return result;
    }
}
