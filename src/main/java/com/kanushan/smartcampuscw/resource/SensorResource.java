/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kanushan.smartcampuscw.resource;

import com.kanushan.smartcampuscw.dao.RoomDAO;
import com.kanushan.smartcampuscw.dao.SensorDAO;
import com.kanushan.smartcampuscw.model.Room;
import com.kanushan.smartcampuscw.model.Sensor;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;

@Path("/sensors")
public class SensorResource {

    private SensorDAO sensorDAO = new SensorDAO();
    private RoomDAO roomDAO = new RoomDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        if (type != null && !type.isEmpty()) {
            return sensorDAO.getSensorsByType(type);
        }

        return sensorDAO.getAllSensors();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor sensor) {

        Room room = roomDAO.getRoomById(sensor.getRoomId());

        if (room == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room does not exist")
                    .build();
        }

        sensorDAO.addSensor(sensor);
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }
    
    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") String id) {
        return new SensorReadingResource(id);
    }
}

