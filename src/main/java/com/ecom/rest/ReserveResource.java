package com.ecom.rest;

import com.ecom.domain.ReservationEntity;
import com.ecom.domain.security.UserEntity;
import com.ecom.service.ReservationService;
import com.ecom.service.RoomService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.service.security.UserService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.io.Serializable;
import java.util.Date;

@Path("/reserve")
@Named
public class ReserveResource implements Serializable {
    @Inject
    private ReservationService reservationService;
    @Inject
    private RoomService roomService;
    @Inject
    private UserService userService;

    @Path("{user}/{room}")
    @POST
    public void reserve(@PathParam("user") String login, @QueryParam("password") String password,
                        @PathParam("room") Long room, @QueryParam("from") Long from, @QueryParam("to") Long to) {
        if (SecurityWrapper.login(login, password, false)) {
            ReservationEntity reservation = new ReservationEntity();
            reservation.setCreateddate(new Date());
            reservation.setRoom(roomService.find(room));
            reservation.setStartdate(new Date(from));
            reservation.setEnddate(new Date(to));
            String username = SecurityWrapper.getUsername();
            if (username != null) {
                UserEntity user = userService.findUserByUsername(username);
                reservation.setUser(user);
                reservationService.save(reservation);
            }
        }
    }
}
