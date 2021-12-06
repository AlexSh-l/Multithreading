package com.alex.multithreading;

import com.alex.multithreading.entity.BusDepot;
import com.alex.multithreading.entity.BusStop;
import com.alex.multithreading.exception.FileReaderException;
import com.alex.multithreading.parser.CustomParser;
import com.alex.multithreading.parser.impl.CustomParserImpl;
import com.alex.multithreading.reader.CustomFileReader;
import com.alex.multithreading.reader.impl.CustomFileReaderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        CustomFileReader fileReader = new CustomFileReaderImpl();
        try {
            if (fileReader.readFile().isPresent()) {
                List<String> values = fileReader.readFile().get();
                List<List<BusStop>> routes = new ArrayList<>();
                List<String> busNumbers = new ArrayList<>();
                CustomParser parser = new CustomParserImpl();
                parser.parse(values, routes, busNumbers);
                BusDepot depot = BusDepot.getInstance();
                depot.sendBuses(busNumbers, routes, 5);
            }
        } catch (FileReaderException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
