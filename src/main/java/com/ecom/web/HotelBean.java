package com.ecom.web;

import com.ecom.domain.HotelEntity;
import com.ecom.domain.security.UserEntity;
import com.ecom.service.HotelService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.service.security.UserService;
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

@Named("hotelBean")
@ViewScoped
public class HotelBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(HotelBean.class.getName());
    
    private List<HotelEntity> hotelList;

    private HotelEntity hotel;
    
    @Inject
    private HotelService hotelService;
    
    @Inject
    private UserService userService;
    
    private List<UserEntity> allNewField3sList;
    
    public void prepareNewHotel() {
        reset();
        this.hotel = new HotelEntity();
        // set any default values now, if you need
        // Example: this.hotel.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (hotel.getId() != null) {
                hotel = hotelService.update(hotel);
                message = "message_successfully_updated";
            } else {
                hotel = hotelService.save(hotel);
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
        
        hotelList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            hotelService.delete(hotel);
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
    
    public void onDialogOpen(HotelEntity hotel) {
        reset();
        this.hotel = hotel;
    }
    
    public void reset() {
        hotel = null;
        hotelList = null;
        
        allNewField3sList = null;
        
    }

    // Get a List of all newField3
    public List<UserEntity> getNewField3s() {
        if (this.allNewField3sList == null) {
            this.allNewField3sList = userService.findAllUserEntities();
        }
        return this.allNewField3sList;
    }
    
    // Update newField3 of the current hotel
    public void updateNewField3(UserEntity user) {
        this.hotel.setNewField3(user);
        // Maybe we just created and assigned a new user. So reset the allNewField3List.
        allNewField3sList = null;
    }
    
    public HotelEntity getHotel() {
        if (this.hotel == null) {
            prepareNewHotel();
        }
        return this.hotel;
    }
    
    public void setHotel(HotelEntity hotel) {
        this.hotel = hotel;
    }
    
    public List<HotelEntity> getHotelList() {
        if (hotelList == null) {
            hotelList = hotelService.findAllHotelEntities();
        }
        return hotelList;
    }

    public void setHotelList(List<HotelEntity> hotelList) {
        this.hotelList = hotelList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(HotelEntity hotel, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
