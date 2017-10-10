package com.ecom.service;

import com.ecom.domain.HotelEntity;
import com.ecom.domain.RoomEntity;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class RoomService extends BaseService<RoomEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public RoomService(){
        super(RoomEntity.class);
    }
    
    @Transactional
    public List<RoomEntity> findAllRoomEntities() {
        
        return entityManager.createQuery("SELECT o FROM Room o ", RoomEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Room o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(RoomEntity room) {

        /* This is called before a Room is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<RoomEntity> findAvailableRooms(HotelEntity hotel) {
        return entityManager.createQuery("SELECT o FROM Room o WHERE o.hotel IS NULL", RoomEntity.class).getResultList();
    }

    @Transactional
    public List<RoomEntity> findRoomsByHotel(HotelEntity hotel) {
        return entityManager.createQuery("SELECT o FROM Room o WHERE o.hotel = :hotel", RoomEntity.class).setParameter("hotel", hotel).getResultList();
    }

}
