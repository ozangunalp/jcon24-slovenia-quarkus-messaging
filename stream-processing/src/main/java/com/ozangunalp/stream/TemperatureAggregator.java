package com.ozangunalp.stream;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.reactive.messaging.keyed.KeyedMulti;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;

/**
 * Compute the average temperature / location / 10s
 */
@ApplicationScoped
public class TemperatureAggregator {

    record TemperatureMeasurement(String location, double temperature) {
    }

    @Incoming("temps")
    @Outgoing("temperature-aggregate")
    public Multi<Tuple2<String, Double>> aggregate(KeyedMulti<String, TemperatureMeasurement> temperaturePerLocation) {
        // LIVE CODE THIS
        return temperaturePerLocation.group().intoLists().every(Duration.ofSeconds(10))
                .flatMap(l -> {
                    // Compute the average
                    return Multi.createFrom().item(l.stream().mapToDouble(r -> r.temperature).average().orElse(0.0))
                            .filter(t -> t != 0.0);
                }).map(l -> Tuple2.of(temperaturePerLocation.key(), l))
                .log("temps-agg");
    }
}
