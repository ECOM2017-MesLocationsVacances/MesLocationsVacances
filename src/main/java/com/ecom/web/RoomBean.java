package com.ecom.web;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.RoomEntity;
import com.ecom.domain.RoomImage;
import com.ecom.service.EstablishmentService;
import com.ecom.service.RoomService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.web.util.MessageFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("roomBean")
@ViewScoped
public class RoomBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(RoomBean.class.getName());
    
    private List<RoomEntity> roomList;

    private RoomEntity room;
    
    @Inject
    private RoomService roomService;
    
    UploadedFile uploadedImage;
    byte[] uploadedImageContents;
    
    @Inject
    private EstablishmentService establishmentService;
    
    private List<EstablishmentEntity> allEstablishmentsList;
    
    public void prepareNewRoom() {
        reset();
        this.room = new RoomEntity();
        // set any default values now, if you need
        // Example: this.room.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (this.uploadedImage != null) {
                try {

                    BufferedImage image;
                    try (InputStream in = new ByteArrayInputStream(uploadedImageContents)) {
                        image = ImageIO.read(in);
                    }
                    image = Scalr.resize(image, Method.BALANCED, 300);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageOutputStream imageOS = ImageIO.createImageOutputStream(baos);
                    Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(
                            uploadedImage.getContentType());
                    ImageWriter imageWriter = (ImageWriter) imageWriters.next();
                    imageWriter.setOutput(imageOS);
                    imageWriter.write(image);
                    
                    baos.close();
                    imageOS.close();
                    
                    logger.log(Level.INFO, "Resized uploaded image from {0} to {1}", new Object[]{uploadedImageContents.length, baos.toByteArray().length});
            
                    RoomImage roomImage = new RoomImage();
                    roomImage.setContent(baos.toByteArray());
                    room.setImage(roomImage);
                } catch (Exception e) {
                    FacesMessage facesMessage = MessageFactory.getMessage(
                            "message_upload_exception");
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    FacesContext.getCurrentInstance().validationFailed();
                    return null;
                }
            }
            
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
        
        allEstablishmentsList = null;
        
        uploadedImage = null;
        uploadedImageContents = null;
        
    }

    // Get a List of all establishment
    public List<EstablishmentEntity> getEstablishments() {
        if (this.allEstablishmentsList == null) {
            this.allEstablishmentsList = establishmentService.findAllEstablishmentEntities();
        }
        return this.allEstablishmentsList;
    }
    
    // Update establishment of the current room
    public void updateEstablishment(EstablishmentEntity establishment) {
        this.room.setEstablishment(establishment);
        // Maybe we just created and assigned a new establishment. So reset the allEstablishmentList.
        allEstablishmentsList = null;
    }
    
    public void handleImageUpload(FileUploadEvent event) {
        
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(
                event.getFile().getContentType());
        if (!imageWriters.hasNext()) {
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_image_type_not_supported");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
        
        this.uploadedImage = event.getFile();
        this.uploadedImageContents = event.getFile().getContents();
        
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public byte[] getUploadedImageContents() {
        if (uploadedImageContents != null) {
            return uploadedImageContents;
        } else if (room != null && room.getImage() != null) {
            room = roomService.lazilyLoadImageToRoom(room);
            return room.getImage().getContent();
        }
        return null;
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
