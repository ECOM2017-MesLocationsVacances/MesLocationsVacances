package com.ecom.rest;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.service.EstablishmentService;
import com.ecom.service.RoomService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Path("/search")
@Named
public class SearchResource implements Serializable {

    @Inject
    private EstablishmentService establishmentService;

    @Inject
    private RoomService roomService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EstablishmentEntity> getFreeEstablishments(@QueryParam("from") Long from, @QueryParam("to") Long to, @QueryParam("place") String place, @QueryParam("duration") Long duration) {
        if (place == null) place = "";
        if (from == null) from = Long.MAX_VALUE;
        if (to == null) to = Long.MAX_VALUE;
        if (duration == null) duration = to - from;
        return establishmentService.findFreeEstablishmentsForDuration(new Date(from), new Date(to), duration, place);
    }

    @GET
    @Path("/{establishment}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFreeRoomsInEstablishments(@PathParam("establishment") Long establishment, @QueryParam("from") Long from, @QueryParam("to") Long to, @QueryParam("duration") Long duration) {
        if (from == null) from = Long.MAX_VALUE;
        if (to == null) to = Long.MAX_VALUE;
        if (duration == null) duration = to - from;
        return Response.ok(roomService.findFreeRoomsInEstablishmentForDuration(new Date(from), new Date(to), duration, establishmentService.find(establishment))).build();
    }
}
