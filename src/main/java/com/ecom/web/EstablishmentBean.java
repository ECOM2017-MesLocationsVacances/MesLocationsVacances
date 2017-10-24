package com.ecom.web;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.EstablishmentImage;
import com.ecom.domain.security.UserEntity;
import com.ecom.service.EstablishmentService;
import com.ecom.service.security.SecurityWrapper;
import com.ecom.service.security.UserService;
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

@Named("establishmentBean")
@ViewScoped
public class EstablishmentBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(EstablishmentBean.class.getName());
    
    private List<EstablishmentEntity> establishmentList;

    private EstablishmentEntity establishment;
    
    @Inject
    private EstablishmentService establishmentService;
    
    UploadedFile uploadedImage;
    byte[] uploadedImageContents;
    
    @Inject
    private UserService userService;
    
    private List<UserEntity> allManagersList;
    
    public void prepareNewEstablishment() {
        reset();
        this.establishment = new EstablishmentEntity();
        // set any default values now, if you need
        // Example: this.establishment.setAnything("test");
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
            
                    EstablishmentImage establishmentImage = new EstablishmentImage();
                    establishmentImage.setContent(baos.toByteArray());
                    establishment.setImage(establishmentImage);
                } catch (Exception e) {
                    FacesMessage facesMessage = MessageFactory.getMessage(
                            "message_upload_exception");
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    FacesContext.getCurrentInstance().validationFailed();
                    return null;
                }
            }
            
            if (establishment.getId() != null) {
                establishment = establishmentService.update(establishment);
                message = "message_successfully_updated";
            } else {
                establishment = establishmentService.save(establishment);
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
        
        establishmentList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            establishmentService.delete(establishment);
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
    
    public void onDialogOpen(EstablishmentEntity establishment) {
        reset();
        this.establishment = establishment;
    }
    
    public void reset() {
        establishment = null;
        establishmentList = null;
        
        allManagersList = null;
        
        uploadedImage = null;
        uploadedImageContents = null;
        
    }

    // Get a List of all manager
    public List<UserEntity> getManagers() {
        if (this.allManagersList == null) {
            this.allManagersList = userService.findAllUserEntities();
        }
        return this.allManagersList;
    }
    
    // Update manager of the current establishment
    public void updateManager(UserEntity user) {
        this.establishment.setManager(user);
        // Maybe we just created and assigned a new user. So reset the allManagerList.
        allManagersList = null;
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
        } else if (establishment != null && establishment.getImage() != null) {
            establishment = establishmentService.lazilyLoadImageToEstablishment(establishment);
            return establishment.getImage().getContent();
        }
        return null;
    }
    
    public EstablishmentEntity getEstablishment() {
        if (this.establishment == null) {
            prepareNewEstablishment();
        }
        return this.establishment;
    }
    
    public void setEstablishment(EstablishmentEntity establishment) {
        this.establishment = establishment;
    }
    
    public List<EstablishmentEntity> getEstablishmentList() {
        if (establishmentList == null) {
            establishmentList = establishmentService.findAllEstablishmentEntities();
        }
        return establishmentList;
    }

    public void setEstablishmentList(List<EstablishmentEntity> establishmentList) {
        this.establishmentList = establishmentList;
    }
    
    public boolean isPermitted(String permission) {
        return SecurityWrapper.isPermitted(permission);
    }

    public boolean isPermitted(EstablishmentEntity establishment, String permission) {
        
        return SecurityWrapper.isPermitted(permission);
        
    }
    
}
