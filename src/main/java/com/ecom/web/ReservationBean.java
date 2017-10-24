package com.ecom.web;

import com.ecom.domain.ReservationEntity;
import com.ecom.domain.RoomEntity;
import com.ecom.domain.security.UserEntity;
import com.ecom.service.ReservationService;
import com.ecom.service.RoomService;
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

@Named("reservationBean")
@ViewScoped
public class ReservationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ReservationBean.class.getName());
    
    private List<ReservationEntity> reservationList;

    private ReservationEntity reservation;
    
    @Inject
    private ReservationService reservationService;
    
    @Inject
    private RoomService roomService;
    
    @Inject
    private UserService userService;
    
    private List<RoomEntity> allRoomsList;
    
    private List<UserEntity> allUsersList;
    
    public void prepareNewReservation() {
        reset();
        this.reservation = new ReservationEntity();
        // set any default values now, if you need
        // Example: this.reservation.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (reservation.getId() != null) {
                reservation = reservationService.update(reservation);
                message = "message_successfully_updated";
            } else {
                reservation = reservationService.save(reservation);
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
        
        reservationList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            reservationService.delete(reservation);
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
    
    public void onDialogOpen(ReservationEntity reservation) {
        reset();
        this.reservation = reservation;
    }
    
    public void reset() {
        reservation = null;
        reservationList = null;
        
        allRoomsList = null;
        
        allUsersList = null;
        
    }

    // Get a List of all room
    public List<RoomEntity> getRooms() {
        if (this.allRoomsList == null) {
            this.allRoomsList = roomService.findAllRoomEntities();
        }
        return this.allRoomsList;
    }
    
    // Update room of the current reservation
    public void updateRoom(RoomEntity room) {
        this.reservation.setRoom(room);
        // Maybe we just created and assigned a new room. So reset the allRoomList.
        allRoomsList = null;
    }
    
    // Get a List of all user
    public List<UserEntity> getUsers() {
        if (this.allUsersList == null) {
            this.allUsersList = userService.findAllUserEntities();
        }
        return this.allUsersList;
    }
    
    // Update user of the current reservation
    public void updateUser(UserEntity user) {
        this.reservation.setUser(user);
        // Maybe we just created and assigned a new user. So reset the allUserList.
        allUsersList = null;
    }
    
    public ReservationEntity getReservation() {
        if (this.reservation == null) {
            prepareNewReservation();
        }
        return this.reservation;
    }
    
    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }
    
    public List<ReservationEntity> getReservationList() {
        if (reservationList == null) {
            reservationList = reservationService.findAllReservationEntities();
        }
        return reservationList;
    }

    public void setReservationList(List<ReservationEntity> reservationList) {
        this.reservationList = reservationList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(ReservationEntity reservation, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
