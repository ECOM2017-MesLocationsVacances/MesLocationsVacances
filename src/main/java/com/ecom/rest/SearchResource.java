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
    public List<EstablishmentEntity> getFreeEstablishments(@QueryParam("place") String place,
                                                           @QueryParam("sizeA") Integer sizeA, @QueryParam("sizeC") Integer sizeC,
                                                           @QueryParam("from") Long from, @QueryParam("to") Long to,
                                                           @QueryParam("duration") Long duration) {
        if (place == null) place = "";
        if (sizeA == null) sizeA = 0;
        if (sizeC == null) sizeC = 0;
        if (from == null) from = Long.MAX_VALUE;
        if (to == null) to = Long.MAX_VALUE;
        if (duration == null) duration = to - from;
        return establishmentService.findFreeEstablishmentsForDuration(place, sizeA, sizeC, new Date(from), new Date(to), duration);
    }

    @GET
    @Path("/{establishment}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFreeRoomsInEstablishments(@PathParam("establishment") Long establishment,
                                                 @QueryParam("sizeA") Integer sizeA, @QueryParam("sizeC") Integer sizeC,
                                                 @QueryParam("from") Long from, @QueryParam("to") Long to,
                                                 @QueryParam("duration") Long duration) {
        if (sizeA == null) sizeA = 0;
        if (sizeC == null) sizeC = 0;
        if (from == null) from = Long.MAX_VALUE;
        if (to == null) to = Long.MAX_VALUE;
        if (duration == null) duration = to - from;
        return Response.ok(
                roomService.findFreeRoomsInEstablishmentForDuration(
                        establishmentService.find(establishment),
                        sizeA, sizeC,
                        new Date(from), new Date(to),
                        duration
                )
        ).build();
    }
}
