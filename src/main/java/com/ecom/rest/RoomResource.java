package com.ecom.rest;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.RoomEntity;
import com.ecom.service.EstablishmentService;
import com.ecom.service.RoomService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.service.security.UserService;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rooms")
@Named
public class RoomResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RoomService roomService;

    @Inject
    private EstablishmentService establishmentService;

    @Inject
    private UserService userService;
    
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
    @Path("{user}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(@PathParam("user") String login, @HeaderParam("password") String password, RoomEntity room) {
        EstablishmentEntity establishment = establishmentService.find(room.getEstablishment().getId());
        room.setEstablishment(establishment);
        if (SecurityWrapper.login(login, password, false)
                && SecurityWrapper.isPermitted("establishment:create")
                && establishment.getManager().equals(userService.findUserByUsername(login))) {
            return Response.ok(roomService.save(room), MediaType.APPLICATION_JSON).build();
        } else
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password").build();
    }
    
    /**
     * Update an existing Room Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated RoomEntity (JSON) <br/>
     * Example URL: /rooms
     * @param room
     * @return A RoomEntity (JSON)
     */
    @Path("{user}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoom(@PathParam("user") String login, @HeaderParam("password") String password, RoomEntity room) {
        EstablishmentEntity establishment = establishmentService.find(room.getEstablishment().getId());
        if (SecurityWrapper.login(login, password, false)
                && SecurityWrapper.isPermitted("establishment:create")
                && establishment.getManager().equals(userService.findUserByUsername(login))) {
            return Response.ok(roomService.update(room), MediaType.APPLICATION_JSON).build();
        } else
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password").build();
    }
    
    /**
     * Delete an existing Room Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /rooms/3
     * @param id
     */
    @Path("{user}/{id}")
    @DELETE
    public Response deleteRoom(@PathParam("user") String login, @HeaderParam("password") String password, @PathParam("id") Long id) {
        RoomEntity room = roomService.find(id);
        EstablishmentEntity establishment = establishmentService.find(room.getEstablishment().getId());
        if (SecurityWrapper.login(login, password, false)
                && SecurityWrapper.isPermitted("establishment:create")
                && establishment.getManager().equals(userService.findUserByUsername(login))) {
            roomService.delete(room);
            return Response.ok().build();
        } else
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password").build();
    }
    
}
