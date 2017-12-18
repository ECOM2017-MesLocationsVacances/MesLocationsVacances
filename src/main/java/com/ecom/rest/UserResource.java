package com.ecom.rest;

import com.ecom.domain.ReservationEntity;
import com.ecom.domain.security.UserEntity;
import com.ecom.domain.security.UserRole;
import com.ecom.domain.security.UserStatus;
import com.ecom.service.ReservationService;
import com.ecom.service.RoomService;
import com.ecom.service.security.RegistrationMailSender;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.service.security.UserService;
import com.ecom.web.util.ApplicationBaseURLBuider;
import com.ecom.web.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

@Path("/user")
@Named
public class UserResource implements Serializable {
    final double URL_VALID_PERIOD_IN_HOURS = 24;

    @Inject
    private UserService userService;

    @Path("{user}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@PathParam("user") String login, @QueryParam("password") String password) {
        if (SecurityWrapper.login(login, password, false))
            return Response.ok(userService.findUserByUsername(login), MediaType.APPLICATION_JSON).build();
        else
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong login or password").build();
    }

    @Path("register")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserEntity newUser) {
        if (newUser.getEmail() == null || newUser.getUsername() == null || newUser.getPassword() == null)
            // Check if a user with same username already exists
            if (userService.findUserByUsername(newUser.getUsername()) != null)
                return Response.status(Response.Status.CONFLICT).entity("Username already in use").build();
        // Check if a user with same email already exists
        if (userService.findUserByEmail(newUser.getEmail()) != null)
            return Response.status(Response.Status.CONFLICT).entity("Mail address already in use").build();
        newUser.setRoles(Arrays.asList(new UserRole[]{UserRole.Registered}));
        newUser.setStatus(UserStatus.Active);
        String emailConfirmationKey = UUID.randomUUID().toString();
        newUser.setEmailConfirmationKey(emailConfirmationKey);
        //TODO need final URL
        String confirmationURL = /*ApplicationBaseURLBuider.getURL() + */"/pages/user/activation.xhtml?key=" + emailConfirmationKey;
        RegistrationMailSender.sendRegistrationActivation(newUser.getEmail(), confirmationURL);
        userService.save(newUser);
        return Response.ok("Registration successful").build();
    }

    @Path("activation")
    @GET
    public Response find(@QueryParam("key") String key) {
        UserEntity user = userService.findUserByEmailConfirmationKey(key);
        Response response;
        if (user != null) {
            Date creationDate = user.getCreatedAt();
            boolean valid = new Date().getTime() - creationDate.getTime() <= URL_VALID_PERIOD_IN_HOURS * 1000 * 3600;
            if (user.getStatus().equals(UserStatus.NotConfirmed)) {
                if (valid) {
                    user.setStatus(UserStatus.Active);
                    userService.update(user);
                    response = Response.ok(SecurityWrapper.getUsername()).build();
                } else {
                    user.setStatus(UserStatus.RegistrationError);
                    userService.update(user);
                    response = Response.status(Response.Status.CONFLICT).build();
                }
            } else
                response = Response.status(Response.Status.NOT_FOUND).build();
        } else
            response = Response.status(Response.Status.NOT_FOUND).build();
        return response;
    }
}
