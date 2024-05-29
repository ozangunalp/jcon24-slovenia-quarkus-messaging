package com.ozangunalp.stream;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.reactive.messaging.keyed.KeyedMulti;
import io.smallrye.reactive.messaging.pulsar.OutgoingMessage;

/**
 * Compute the number of order / location / 10s.
 */
@ApplicationScoped
public class OrderAggregator {

    @Incoming("orders")
    @Outgoing("order-aggregate")
    public Multi<Tuple2<String, Integer>> aggregate(KeyedMulti<String, Double> ordersPerLocation) {
        return ordersPerLocation
                .group().intoLists().every(Duration.ofSeconds(10))
                .log("list")
                .map(list -> Tuple2.of(ordersPerLocation.key(), list.size()))
                .log("order-aggregate");
    }
}
