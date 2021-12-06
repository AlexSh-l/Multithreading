package com.alex.multithreading.parser;

import com.alex.multithreading.entity.BusStop;

import java.util.List;

public interface CustomParser {

    String VALUE_SPLITTER = " ";

    void parse(List<String> values, List<List<BusStop>> routes, List<String> busNumbers);
}
