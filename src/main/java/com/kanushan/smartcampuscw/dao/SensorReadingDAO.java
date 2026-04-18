/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kanushan.smartcampuscw.dao;

import com.kanushan.smartcampuscw.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorReadingDAO {

    private static final Map<String, List<SensorReading>> readingsBySensor = new HashMap<>();

    public List<SensorReading> getReadingsForSensor(String sensorId) {
        return readingsBySensor.getOrDefault(sensorId, new ArrayList<>());
    }

    public void addReading(String sensorId, SensorReading reading) {
        readingsBySensor
                .computeIfAbsent(sensorId, key -> new ArrayList<>())
                .add(reading);
    }
}