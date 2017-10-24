package com.ecom.rest;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.ReservationEntity;
import com.ecom.service.EstablishmentService;
import com.ecom.service.ReservationService;

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

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<EstablishmentEntity> getAllEstablishments() {
//        return establishmentService.findAllEstablishmentEntities();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EstablishmentEntity> getAllEstablishmentsInCity(@QueryParam("from") Long from, @QueryParam("to") Long to, @QueryParam("city") String place) {
        if (place == null) place = "";
        if (from == null) from = Long.MAX_VALUE;
        if (to == null) to = 0L;
        return establishmentService.findFreeEstablishmentsInCity(new Date(from), new Date(to), place);
    }
}
