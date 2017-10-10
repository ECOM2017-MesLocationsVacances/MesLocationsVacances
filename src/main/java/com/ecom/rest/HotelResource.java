package com.ecom.rest;

import com.ecom.domain.HotelEntity;
import com.ecom.service.HotelService;

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

@Path("/hotels")
@Named
public class HotelResource implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private HotelService hotelService;
    
    /**
     * Get the complete list of Hotel Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /hotels
     * @return List of HotelEntity (JSON)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HotelEntity> getAllHotels() {
        return hotelService.findAllHotelEntities();
    }
    
    /**
     * Get the number of Hotel Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /hotels/count
     * @return Number of HotelEntity
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public long getCount() {
        return hotelService.countAllEntries();
    }
    
    /**
     * Get a Hotel Entity <br/>
     * HTTP Method: GET <br/>
     * Example URL: /hotels/3
     * @param id
     * @return A Hotel Entity (JSON)
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HotelEntity getHotelById(@PathParam("id") Long id) {
        return hotelService.find(id);
    }
    
    /**
     * Create a Hotel Entity <br/>
     * HTTP Method: POST <br/>
     * POST Request Body: New HotelEntity (JSON) <br/>
     * Example URL: /hotels
     * @param hotel
     * @return A HotelEntity (JSON)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HotelEntity addHotel(HotelEntity hotel) {
        return hotelService.save(hotel);
    }
    
    /**
     * Update an existing Hotel Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated HotelEntity (JSON) <br/>
     * Example URL: /hotels
     * @param hotel
     * @return A HotelEntity (JSON)
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HotelEntity updateHotel(HotelEntity hotel) {
        return hotelService.update(hotel);
    }
    
    /**
     * Delete an existing Hotel Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /hotels/3
     * @param id
     */
    @Path("{id}")
    @DELETE
    public void deleteHotel(@PathParam("id") Long id) {
        HotelEntity hotel = hotelService.find(id);
        hotelService.delete(hotel);
    }
    
}
