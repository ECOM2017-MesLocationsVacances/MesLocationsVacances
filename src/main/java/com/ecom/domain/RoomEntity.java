package com.ecom.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity(name="Room")
@Table(name="\"ROOM\"")
@XmlRootElement
public class RoomEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private RoomImage image;
    
    @Digits(integer = 4,  fraction = 2)
    @Column(precision = 6, scale = 2, name="\"price\"")
    @NotNull
    private BigDecimal price;

    @ManyToOne(optional=false)
    @JoinColumn(name = "ESTABLISHMENT_ID", referencedColumnName = "ID")
    private EstablishmentEntity establishment;

    @Size(max = 50)
    @Column(length = 50, name="\"name\"")
    @NotNull
    private String name;

    @XmlTransient
    public RoomImage getImage() {
        return image;
    }

    public void setImage(RoomImage image) {
        this.image = image;
    }
    
    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public EstablishmentEntity getEstablishment() {
        return this.establishment;
    }

    public void setEstablishment(EstablishmentEntity establishment) {
        this.establishment = establishment;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
