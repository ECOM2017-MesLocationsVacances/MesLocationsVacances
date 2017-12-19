package com.ecom.domain;

import com.ecom.domain.security.UserEntity;
import com.ecom.domain.security.UserStatus;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
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

    @Column(name="\"enddate\"")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date enddate;

    @ManyToOne(optional=true)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID")
    private RoomEntity room;

    @ManyToOne(optional=true)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ReservationStatus status;

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

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public RoomEntity getRoom() {
        return this.room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
