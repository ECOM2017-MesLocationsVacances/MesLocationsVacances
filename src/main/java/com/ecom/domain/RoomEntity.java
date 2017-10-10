package com.ecom.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="Room")
@Table(name="\"ROOM\"")
@XmlRootElement
public class RoomEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Digits(integer = 4,  fraction = 2)
    @Column(precision = 6, scale = 2, name="\"price\"")
    @NotNull
    private BigDecimal price;

    @ManyToOne(optional=true)
    @JoinColumn(name = "HOTEL_ID", referencedColumnName = "ID")
    private HotelEntity hotel;

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public HotelEntity getHotel() {
        return this.hotel;
    }

    public void setHotel(HotelEntity hotel) {
        this.hotel = hotel;
    }

}
