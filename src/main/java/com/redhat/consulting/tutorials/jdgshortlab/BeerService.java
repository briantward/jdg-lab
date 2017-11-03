package com.redhat.consulting.tutorials.jdgshortlab;

import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;

import java.util.List;

public interface BeerService {
    List<Beer> getBeerByIbuBetween(double low, double high);

    List<Beer> getBeerByIbuBetweenIQ(double low, double high);

    List<Beer> getAllBeers(boolean desc);

    List<Beer> getBeerByFuzzyMatchDescription(String description);

    List<Beer> getBeerByWildcardDescription(String description);

    List<Beer> getBeerByName();

    List<Beer> getBeerByWildcardName();

    List<Beer> getBeerByFuzzyMatchBrewery();
}
