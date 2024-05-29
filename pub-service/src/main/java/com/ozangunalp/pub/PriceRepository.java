package com.ozangunalp.pub;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.reactive.messaging.TableView;

@ApplicationScoped
public class PriceRepository {

    @ConfigProperty(name = "location")
    String location;

    private double price = 5.00;

    @Channel("prices")
    TableView<String, Double> prices;

    public double getCurrentPrice() {
        Double value = prices.get(location);
        if (value == null) {
            return price;
        }
        return value;
    }

    public Multi<Double> getPriceStream() {
        return prices.filterKey(location)
                .map(Tuple2::getItem2);
    }
}
