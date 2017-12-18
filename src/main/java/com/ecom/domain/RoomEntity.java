package com.ecom.domain;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;

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

    @Column(name="\"sizeA\"")
    @NotNull
    private int sizeA;

    @Column(name="\"sizeC\"")
    @NotNull
    private int sizeC;

    @ManyToOne(optional=false)
    @JoinColumn(name = "ESTABLISHMENT_ID", referencedColumnName = "ID")
    private EstablishmentEntity establishment;

    @Size(max = 50)
    @Column(length = 50, name="\"name\"")
    @NotNull
    private String name;

    @Size(max = 255)
    @Column(name="\"description\"")
    private String description;

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

    public int getSizeA() {
        return sizeA;
    }

    public void setSizeA(int sizeA) {
        this.sizeA = sizeA;
    }

    public int getSizeC() {
        return sizeC;
    }

    public void setSizeC(int sizeC) {
        this.sizeC = sizeC;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
