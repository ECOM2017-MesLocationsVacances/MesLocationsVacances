package com.ecom.web;

import com.ecom.domain.HotelEntity;
import com.ecom.domain.RoomEntity;
import com.ecom.service.HotelService;
import com.ecom.service.RoomService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.web.util.MessageFactory;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

@Named("roomBean")
@ViewScoped
public class RoomBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(RoomBean.class.getName());
    
    private List<RoomEntity> roomList;

    private RoomEntity room;
    
    @Inject
    private RoomService roomService;
    
    @Inject
    private HotelService hotelService;
    
    private List<HotelEntity> allHotelsList;
    
    public void prepareNewRoom() {
        reset();
        this.room = new RoomEntity();
        // set any default values now, if you need
        // Example: this.room.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (room.getId() != null) {
                room = roomService.update(room);
                message = "message_successfully_updated";
            } else {
                room = roomService.save(room);
                message = "message_successfully_created";
            }
        } catch (OptimisticLockException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "message_optimistic_locking_exception";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "message_save_exception";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
        
        roomList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            roomService.delete(room);
            message = "message_successfully_deleted";
            reset();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "message_delete_exception";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
        FacesContext.getCurrentInstance().addMessage(null, MessageFactory.getMessage(message));
        
        return null;
    }
    
    public void onDialogOpen(RoomEntity room) {
        reset();
        this.room = room;
    }
    
    public void reset() {
        room = null;
        roomList = null;
        
        allHotelsList = null;
        
    }

    // Get a List of all hotel
    public List<HotelEntity> getHotels() {
        if (this.allHotelsList == null) {
            this.allHotelsList = hotelService.findAllHotelEntities();
        }
        return this.allHotelsList;
    }
    
    // Update hotel of the current room
    public void updateHotel(HotelEntity hotel) {
        this.room.setHotel(hotel);
        // Maybe we just created and assigned a new hotel. So reset the allHotelList.
        allHotelsList = null;
    }
    
    public RoomEntity getRoom() {
        if (this.room == null) {
            prepareNewRoom();
        }
        return this.room;
    }
    
    public void setRoom(RoomEntity room) {
        this.room = room;
    }
    
    public List<RoomEntity> getRoomList() {
        if (roomList == null) {
            roomList = roomService.findAllRoomEntities();
        }
        return roomList;
    }

    public void setRoomList(List<RoomEntity> roomList) {
        this.roomList = roomList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(RoomEntity room, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
