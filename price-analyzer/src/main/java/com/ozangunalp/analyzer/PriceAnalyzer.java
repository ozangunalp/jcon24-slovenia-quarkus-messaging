package com.ozangunalp.analyzer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.pulsar.OutgoingMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class PriceAnalyzer {

    private static final double BASE_PRICE = 5.00;
    private final Map<String, Double> prices = new HashMap<>();

    record Report(String location, double temperature, int numberOfOrder) {

    }

    @Inject
    LLMPriceFinder priceFinder;

    @Incoming("reports")
    @Outgoing("prices")
    @Blocking
    @ActivateRequestContext
    public OutgoingMessage<Double> analyze(Report report) {
        System.out.println("analyzing... " + report);
        double newPrice = priceFinder.updatePrice(prices.getOrDefault(report.location(), BASE_PRICE), report.temperature(), report.numberOfOrder());
        prices.put(report.location(), newPrice);
        System.out.println("new price: " + newPrice);
        return OutgoingMessage.of(report.location, newPrice);
    }


}
