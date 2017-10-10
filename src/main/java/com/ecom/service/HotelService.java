package com.ecom.service;

import com.ecom.domain.HotelEntity;
import com.ecom.domain.security.UserEntity;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class HotelService extends BaseService<HotelEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public HotelService(){
        super(HotelEntity.class);
    }
    
    @Transactional
    public List<HotelEntity> findAllHotelEntities() {
        
        return entityManager.createQuery("SELECT o FROM Hotel o ", HotelEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Hotel o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(HotelEntity hotel) {

        /* This is called before a Hotel is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllHotelRoomsAssignments(hotel);
        
        this.cutAllHotelReservationsAssignments(hotel);
        
    }

    // Remove all assignments from all room a hotel. Called before delete a hotel.
    @Transactional
    private void cutAllHotelRoomsAssignments(HotelEntity hotel) {
        entityManager
                .createQuery("UPDATE Room c SET c.hotel = NULL WHERE c.hotel = :p")
                .setParameter("p", hotel).executeUpdate();
    }
    
    // Remove all assignments from all reservation a hotel. Called before delete a hotel.
    @Transactional
    private void cutAllHotelReservationsAssignments(HotelEntity hotel) {
        entityManager
                .createQuery("UPDATE Reservation c SET c.hotel = NULL WHERE c.hotel = :p")
                .setParameter("p", hotel).executeUpdate();
    }
    
    @Transactional
    public List<HotelEntity> findAvailableHotels(UserEntity user) {
        return entityManager.createQuery("SELECT o FROM Hotel o WHERE o.newField3 IS NULL", HotelEntity.class).getResultList();
    }

    @Transactional
    public List<HotelEntity> findHotelsByNewField3(UserEntity user) {
        return entityManager.createQuery("SELECT o FROM Hotel o WHERE o.newField3 = :user", HotelEntity.class).setParameter("user", user).getResultList();
    }

}
