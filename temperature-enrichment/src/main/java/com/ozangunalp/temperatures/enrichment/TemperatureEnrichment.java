package com.ozangunalp.temperatures.enrichment;


import io.smallrye.reactive.messaging.pulsar.OutgoingMessage;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class TemperatureEnrichment {

    @Incoming("temperatures")
    @Outgoing("temps")
    @Transactional
    public OutgoingMessage<TemperatureMeasurement> fromMqttToKafka(JsonObject temperature) {
        // LIVE CODE THIS
        var location = DeviceEntity.findLocationForDevice(temperature.getString("device"));
        TemperatureMeasurement outcome = new TemperatureMeasurement(location, temperature.getDouble("value"));
        System.out.println("Writing " + outcome);
        return OutgoingMessage.of(location, outcome);
    }

    record TemperatureMeasurement(String location, double temperature) {
    }


}
