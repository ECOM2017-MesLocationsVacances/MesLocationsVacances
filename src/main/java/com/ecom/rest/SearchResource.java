package com.ecom.rest;

import com.ecom.domain.RoomEntity;
import com.ecom.service.EstablishmentService;
import com.ecom.service.RoomService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
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

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<EstablishmentEntity> getAllEstablishments() {
//        return establishmentService.findAllEstablishmentEntities();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoomEntity> getFreeRoomsInCity(@QueryParam("from") Long from, @QueryParam("to") Long to, @QueryParam("city") String place) {
        if (place == null) place = "";
        if (from == null) from = Long.MAX_VALUE;
        if (to == null) to = Long.MAX_VALUE;
        return roomService.findFreeRoomsInCity(new Date(from), new Date(to), place);
    }
}
