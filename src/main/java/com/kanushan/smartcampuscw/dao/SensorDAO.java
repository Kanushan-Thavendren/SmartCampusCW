/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kanushan.smartcampuscw.dao;

import com.kanushan.smartcampuscw.model.Sensor;
import java.util.ArrayList;
import java.util.List;

public class SensorDAO {

    private static final List<Sensor> sensors = new ArrayList<>();

    public List<Sensor> getAllSensors() {
        return sensors;
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public List<Sensor> getSensorsByType(String type) {
        List<Sensor> filteredSensors = new ArrayList<>();

        for (Sensor sensor : sensors) {
            if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }

        return filteredSensors;
    }
}