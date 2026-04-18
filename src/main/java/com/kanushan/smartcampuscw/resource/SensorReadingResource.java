/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kanushan.smartcampuscw.resource;

import com.kanushan.smartcampuscw.dao.SensorDAO;
import com.kanushan.smartcampuscw.dao.SensorReadingDAO;
import com.kanushan.smartcampuscw.model.Sensor;
import com.kanushan.smartcampuscw.model.SensorReading;
import com.kanushan.smartcampuscw.exception.SensorUnavailableException;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SensorReadingResource {

    private SensorReadingDAO readingDAO = new SensorReadingDAO();
    private SensorDAO sensorDAO = new SensorDAO();

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getReadings() {
        return readingDAO.getReadingsForSensor(sensorId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {

        Sensor sensor = null;

        for (Sensor s : sensorDAO.getAllSensors()) {
            if (s.getId().equals(sensorId)) {
                sensor = s;
                break;
            }
        }

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is in maintenance mode");
        }

        readingDAO.addReading(sensorId, reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}