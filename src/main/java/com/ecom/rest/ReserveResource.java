package com.ecom.rest;

import com.ecom.domain.ReservationEntity;
import com.ecom.domain.RoomEntity;
import com.ecom.domain.security.UserEntity;
import com.ecom.service.ReservationService;
import com.ecom.service.RoomService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.service.security.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;

@Path("/reserve")
@Named
public class ReserveResource implements Serializable {
    @Inject
    private ReservationService reservationService;
    @Inject
    private UserService userService;
    @Inject
    private RoomService roomService;

    @Path("{user}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reserve(@PathParam("user") String login, @HeaderParam("password") String password, ReservationEntity reservation) {
        Response response;
        if (SecurityWrapper.login(login, password, false)) {
            String username = SecurityWrapper.getUsername();
            if (username != null) {
                RoomEntity room = roomService.find(reservation.getRoom().getId());
                UserEntity user = userService.findUserByUsername(username);
                reservation.setRoom(room);
                reservation.setUser(user);
                reservation.setCreateddate(new Date());
                reservationService.save(reservation);
                response = Response.ok("Bon voyage!").build();
            } else
                response = Response.status(Response.Status.BAD_REQUEST).build();
        } else
            response = Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password " + password).build();
        return response;
    }
}
