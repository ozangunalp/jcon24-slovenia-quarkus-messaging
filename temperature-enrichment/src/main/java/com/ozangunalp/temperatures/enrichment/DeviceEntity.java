package com.ozangunalp.temperatures.enrichment;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "devices")
public class DeviceEntity extends PanacheEntity {

    public String deviceId;
    public String location;

    public static String findLocationForDevice(String deviceId) {
        DeviceEntity match = find("deviceId", deviceId).firstResult();
        if (match == null) {
            return "unknown";
        } else {
            return match.location;
        }
    }
}
