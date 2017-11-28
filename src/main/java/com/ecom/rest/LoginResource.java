package com.ecom.rest;

import com.ecom.domain.ReservationEntity;
import com.ecom.domain.security.UserEntity;
import com.ecom.service.ReservationService;
import com.ecom.service.RoomService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.service.security.UserService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;

@Path("/login")
@Named
public class LoginResource implements Serializable {
    @Path("{user}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(@PathParam("user") String login, @QueryParam("password") String password) {
        if (SecurityWrapper.login(login, password, false))
            return Response.ok(SecurityWrapper.getUsername()).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password").build();
    }
}
