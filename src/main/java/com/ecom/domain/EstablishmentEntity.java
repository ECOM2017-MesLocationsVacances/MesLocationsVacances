package com.ecom.domain;

import com.ecom.domain.security.UserEntity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity(name="Establishment")
@Table(name="\"ESTABLISHMENT\"")
@XmlRootElement
public class EstablishmentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private EstablishmentImage image;
    
    @Size(max = 50)
    @Column(length = 50, name="\"name\"")
    @NotNull
    private String name;

    @Size(max = 255)
    @Column(length = 255, name="\"place\"")
    @NotNull
    private String place;

    @ManyToOne(optional=true)
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "ID")
    private UserEntity manager;

    @Size(max = 255)
    @Column(name="\"description\"")
    private String description;

    @XmlTransient
    public EstablishmentImage getImage() {
        return image;
    }

    public void setImage(EstablishmentImage image) {
        this.image = image;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public UserEntity getManager() {
        return this.manager;
    }

    public void setManager(UserEntity user) {
        this.manager = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
