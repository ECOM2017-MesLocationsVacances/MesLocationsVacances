package com.ecom.rest;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.service.EstablishmentService;

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

@Path("/establishments")
@Named
public class EstablishmentResource implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private EstablishmentService establishmentService;
    
    /**
     * Get the complete list of Establishment Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /establishments
     * @return List of EstablishmentEntity (JSON)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EstablishmentEntity> getAllEstablishments() {
        return establishmentService.findAllEstablishmentEntities();
    }
    
    /**
     * Get the number of Establishment Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /establishments/count
     * @return Number of EstablishmentEntity
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public long getCount() {
        return establishmentService.countAllEntries();
    }
    
    /**
     * Get a Establishment Entity <br/>
     * HTTP Method: GET <br/>
     * Example URL: /establishments/3
     * @param id
     * @return A Establishment Entity (JSON)
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public EstablishmentEntity getEstablishmentById(@PathParam("id") Long id) {
        return establishmentService.find(id);
    }
    
    /**
     * Create a Establishment Entity <br/>
     * HTTP Method: POST <br/>
     * POST Request Body: New EstablishmentEntity (JSON) <br/>
     * Example URL: /establishments
     * @param establishment
     * @return A EstablishmentEntity (JSON)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EstablishmentEntity addEstablishment(EstablishmentEntity establishment) {
        return establishmentService.save(establishment);
    }
    
    /**
     * Update an existing Establishment Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated EstablishmentEntity (JSON) <br/>
     * Example URL: /establishments
     * @param establishment
     * @return A EstablishmentEntity (JSON)
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EstablishmentEntity updateEstablishment(EstablishmentEntity establishment) {
        return establishmentService.update(establishment);
    }
    
    /**
     * Delete an existing Establishment Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /establishments/3
     * @param id
     */
    @Path("{id}")
    @DELETE
    public void deleteEstablishment(@PathParam("id") Long id) {
        EstablishmentEntity establishment = establishmentService.find(id);
        establishmentService.delete(establishment);
    }
    
}
