package com.ecom.rest;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.ReservationEntity;
import com.ecom.domain.ReservationStatus;
import com.ecom.domain.RoomEntity;
import com.ecom.domain.security.UserEntity;
import com.ecom.service.ReservationService;
import com.ecom.service.RoomService;
import com.ecom.service.security.RegistrationMailSender;
import com.ecom.service.security.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/paypal")
@Named
public class PayPalResource implements Serializable {

    @Inject
    private ReservationService reservationService;

    @Inject
    private RoomService roomService;

    @Inject
    private UserService userService;

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ReservationEntity reservation) {
        String paymentID;
        float price;
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("https://api.sandbox.paypal.com/");
        PayPalClient paypal =  target.proxy(PayPalClient.class);

        long diff = Math.abs(reservation.getEnddate().getTime() - reservation.getStartdate().getTime());
        long days = diff / (24 * 60 * 60 * 1000);

        RoomEntity room = roomService.find(reservation.getRoom().getId());
        reservation.setRoom(room);
        UserEntity user = userService.find(reservation.getUser().getId());
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.Unconfirmed);

        reservation = reservationService.save(reservation);

        price = room.getPrice().floatValue() * days;

        String paymentInfo = "{\n" +
                "  \"intent\": \"sale\",\n" +
                "  \"redirect_urls\": {\n" +
                "    \"return_url\": \"https://google.com\",\n" +
                "    \"cancel_url\": \"https://google.com\"\n" +
                "  },\n" +
                "  \"payer\": {\n" +
                "    \"payment_method\": \"paypal\"\n" +
                "  },\n" +
                "  \"transactions\": [{\n" +
                "    \"amount\": {\n" +
                "      \"total\": \""+String.format("%.2f", price)+"\",\n" +
                "      \"currency\": \"EUR\"\n" +
                "    }\n" +
                "  }]\n" +
                "}";

        String paymentResult = paypal.create("application/json","Bearer A21AAEUPjKmi1bB_LqblEQLHuBRtQVcSjptnWfGFw5XAcEBXEfNa-7tawylYuUTKEbPn1qQvU5VZJfiqyNwwr8wFtY9pxi9rg", paymentInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> map;
        try {
            map = objectMapper.readValue(paymentResult, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
        paymentID = map.get("id");

        return Response.ok("{\n" +
                "    \"paymentID\": \""+paymentID+"\"\n" +
                "    \"reservationID\": \""+reservation.getId()+"\"\n" +
                "}").build();
    }


    @POST
    @Path("/{reservation}/execute")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response execute(@PathParam("reservation") Long id, String ids) {
        ReservationEntity reservation = reservationService.find(id);
        if (reservation == null)
            return Response.status(500).build();

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("https://api.sandbox.paypal.com/");
        PayPalClient paypal =  target.proxy(PayPalClient.class);
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> map;
        try {
            map = objectMapper.readValue(ids, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
        String paymentID = map.get("paymentID");
        String payerID = "{\n" +
                "  \"payer_id\": \""+map.get("payerID")+"\"\n" +
                "}";

        String result = paypal.execute("application/json","Bearer A21AAEUPjKmi1bB_LqblEQLHuBRtQVcSjptnWfGFw5XAcEBXEfNa-7tawylYuUTKEbPn1qQvU5VZJfiqyNwwr8wFtY9pxi9rg", paymentID,  payerID);
        try {
            map = objectMapper.readValue(result, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
        String state = map.get("state");
        if (!state.equals("approved")) {
            reservationService.delete(reservation);
            return Response.status(500).build();
        }
        reservation.setStatus(ReservationStatus.Confirmed);
        reservationService.update(reservation);
        RegistrationMailSender.sendConfirmation(reservation.getRoom().getEstablishment().getManager().getEmail(), reservation, false, false);
        return Response.ok().build();
    }

    @Path("/v1/payments")
    public interface PayPalClient {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        List<EstablishmentEntity> getEstablishments();

        @Path("payment")
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        String create(@HeaderParam("Content-Type") String type, @HeaderParam("Authorization") String token,
                      String paymentInfo);

        @Path("payment/{paymentID}/execute")
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        String execute(@HeaderParam("Content-Type") String type, @HeaderParam("Authorization") String token,
                       @PathParam("paymentID") String paymentID, String payerID);

    }
}
