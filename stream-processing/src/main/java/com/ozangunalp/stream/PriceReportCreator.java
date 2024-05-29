package com.ozangunalp.stream;

import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.reactive.messaging.TableView;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * Consume the temperature / location / min and the beer order / location / min to create a report.
 * That report will be consumed by the Price analyzer
 */
@ApplicationScoped
public class PriceReportCreator {

    @Channel("temperature-aggregate")
    TableView<String, Double> temperatures;

    record Report(String location, double temperature, int numberOfOrder) {

    }

    @Incoming("order-aggregate")
    @Outgoing("reports")
    public Report emitReport(Tuple2<String, Integer> orderCountPerLocationPerTimePeriod) {
        // LIVE CODE THIS
        System.out.println("Computing report from " + orderCountPerLocationPerTimePeriod);
        var location = orderCountPerLocationPerTimePeriod.getItem1();
        var count = orderCountPerLocationPerTimePeriod.getItem2();
        var temperature = temperatures.get(location);
        if (temperature != null) {
            return new Report(location, temperature, count);
        } else {
            System.out.println("no report - no temperature for " + location + " in " + temperatures.keys());
            return null;
        }
    }

}
