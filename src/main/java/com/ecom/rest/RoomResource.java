package com.ecom.rest;

import com.ecom.domain.RoomEntity;
import com.ecom.service.RoomService;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rooms")
@Named
public class RoomResource implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private RoomService roomService;
    
    /**
     * Get the complete list of Room Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /rooms
     * @return List of RoomEntity (JSON)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoomEntity> getAllRooms() {
        return roomService.findAllRoomEntities();
    }
    
    /**
     * Get the number of Room Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /rooms/count
     * @return Number of RoomEntity
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public long getCount() {
        return roomService.countAllEntries();
    }
    
    /**
     * Get a Room Entity <br/>
     * HTTP Method: GET <br/>
     * Example URL: /rooms/3
     * @param id
     * @return A Room Entity (JSON)
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RoomEntity getRoomById(@PathParam("id") Long id) {
        return roomService.find(id);
    }
    
    /**
     * Create a Room Entity <br/>
     * HTTP Method: POST <br/>
     * POST Request Body: New RoomEntity (JSON) <br/>
     * Example URL: /rooms
     * @param room
     * @return A RoomEntity (JSON)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RoomEntity addRoom(RoomEntity room) {
        return roomService.save(room);
    }
    
    /**
     * Update an existing Room Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated RoomEntity (JSON) <br/>
     * Example URL: /rooms
     * @param room
     * @return A RoomEntity (JSON)
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RoomEntity updateRoom(RoomEntity room) {
        return roomService.update(room);
    }
    
    /**
     * Delete an existing Room Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /rooms/3
     * @param id
     */
    @Path("{id}")
    @DELETE
    public void deleteRoom(@PathParam("id") Long id) {
        RoomEntity room = roomService.find(id);
        roomService.delete(room);
    }
    
}
