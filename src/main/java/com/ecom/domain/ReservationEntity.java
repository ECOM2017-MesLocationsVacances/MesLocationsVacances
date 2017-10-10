package com.ecom.domain;

import com.ecom.domain.security.UserEntity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="Reservation")
@Table(name="\"RESERVATION\"")
@XmlRootElement
public class ReservationEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="\"createddate\"")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date createddate;

    @Column(name="\"startdate\"")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startdate;

    @Column(name="\"duration\"")
    @Digits(integer = 4, fraction = 0)
    @NotNull
    private Integer duration;

    @ManyToOne(optional=true)
    @JoinColumn(name = "HOTEL_ID", referencedColumnName = "ID")
    private HotelEntity hotel;

    @ManyToOne(optional=true)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity user;

    public Date getCreateddate() {
        return this.createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public Date getStartdate() {
        return this.startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public HotelEntity getHotel() {
        return this.hotel;
    }

    public void setHotel(HotelEntity hotel) {
        this.hotel = hotel;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
