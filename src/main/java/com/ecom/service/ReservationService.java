package com.ecom.service;

import com.ecom.domain.ReservationEntity;
import com.ecom.domain.RoomEntity;
import com.ecom.domain.security.UserEntity;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class ReservationService extends BaseService<ReservationEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public ReservationService(){
        super(ReservationEntity.class);
    }
    
    @Transactional
    public List<ReservationEntity> findAllReservationEntities() {
        
        return entityManager.createQuery("SELECT o FROM Reservation o ", ReservationEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Reservation o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(ReservationEntity reservation) {

        /* This is called before a Reservation is deleted. Place here all the
           steps to cut dependencies to other entities */
        
    }

    @Transactional
    public List<ReservationEntity> findAvailableReservations(RoomEntity room) {
        return entityManager.createQuery("SELECT o FROM Reservation o WHERE o.room IS NULL", ReservationEntity.class).getResultList();
    }

    @Transactional
    public List<ReservationEntity> findReservationsByRoom(RoomEntity room) {
        return entityManager.createQuery("SELECT o FROM Reservation o WHERE o.room = :room", ReservationEntity.class).setParameter("room", room).getResultList();
    }

    @Transactional
    public List<ReservationEntity> findAvailableReservations(UserEntity user) {
        return entityManager.createQuery("SELECT o FROM Reservation o WHERE o.user IS NULL", ReservationEntity.class).getResultList();
    }

    @Transactional
    public List<ReservationEntity> findReservationsByUser(UserEntity user) {
        return entityManager.createQuery("SELECT o FROM Reservation o WHERE o.user = :user", ReservationEntity.class).setParameter("user", user).getResultList();
    }

}
