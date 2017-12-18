package com.ecom.rest;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.service.EstablishmentService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.service.security.UserService;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/establishments")
@Named
public class EstablishmentResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EstablishmentService establishmentService;

    @Inject
    private UserService userService;

    /**
     * Get the complete list of Establishment Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /establishments
     *
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
     *
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
     *
     * @param establishment
     * @return A EstablishmentEntity (JSON)
     */
    @Path("{user}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEstablishment(@PathParam("user") String login, @HeaderParam("password") String password, EstablishmentEntity establishment) {
        if (SecurityWrapper.login(login, password, false)
                && SecurityWrapper.isPermitted("establishment:create")) {
            establishment.setManager(userService.findUserByUsername(login));
            return Response.ok(establishmentService.save(establishment), MediaType.APPLICATION_JSON).build();
        } else
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password").build();
    }

    /**
     * Update an existing Establishment Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated EstablishmentEntity (JSON) <br/>
     * Example URL: /establishments
     *
     * @param establishment
     * @return A EstablishmentEntity (JSON)
     */
    @Path("{user}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEstablishment(@PathParam("user") String login, @HeaderParam("password") String password, EstablishmentEntity establishment) {
        if (SecurityWrapper.login(login, password, false)
                && SecurityWrapper.isPermitted("establishment:create")
                && establishmentService.find(establishment.getId()).getManager().equals(userService.findUserByUsername(login))) {
            return Response.ok(establishmentService.update(establishment), MediaType.APPLICATION_JSON).build();
        } else
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password").build();
    }

    /**
     * Delete an existing Establishment Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /establishments/3
     *
     * @param id
     */
    @Path("{user}/{id}")
    @DELETE
    public Response deleteEstablishment(@PathParam("user") String login, @HeaderParam("password") String password, @PathParam("id") Long id) {
        if (SecurityWrapper.login(login, password, false)
                && SecurityWrapper.isPermitted("establishment:create")
                && establishmentService.find(id).getManager().equals(userService.findUserByUsername(login))) {
            establishmentService.delete(establishmentService.find(id));
            return Response.ok().build();
        } else
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password").build();
    }

}
