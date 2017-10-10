package com.ecom.domain;

import com.ecom.domain.security.UserEntity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="Hotel")
@Table(name="\"HOTEL\"")
@XmlRootElement
public class HotelEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @Column(length = 50, name="\"name\"")
    @NotNull
    private String name;

    @Size(max = 255)
    @Column(length = 255, name="\"place\"")
    @NotNull
    private String place;

    @ManyToOne(optional=true)
    @JoinColumn(name = "NEWFIELD3_ID", referencedColumnName = "ID")
    private UserEntity newField3;

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

    public UserEntity getNewField3() {
        return this.newField3;
    }

    public void setNewField3(UserEntity user) {
        this.newField3 = user;
    }

}
