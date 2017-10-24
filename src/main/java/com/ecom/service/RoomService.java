package com.ecom.service;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.RoomEntity;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.PersistenceUnitUtil;
import javax.transaction.Transactional;

@Named
public class RoomService extends BaseService<RoomEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public RoomService(){
        super(RoomEntity.class);
    }
    
    @Transactional
    public List<RoomEntity> findAllRoomEntities() {
        
        return entityManager.createQuery("SELECT o FROM Room o LEFT JOIN FETCH o.image", RoomEntity.class).getResultList();
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
        
        this.cutAllRoomReservationsAssignments(room);
        
    }

    // Remove all assignments from all reservation a room. Called before delete a room.
    @Transactional
    private void cutAllRoomReservationsAssignments(RoomEntity room) {
        entityManager
                .createQuery("UPDATE Reservation c SET c.room = NULL WHERE c.room = :p")
                .setParameter("p", room).executeUpdate();
    }
    
    @Transactional
    public List<RoomEntity> findAvailableRooms(EstablishmentEntity establishment) {
        return entityManager.createQuery("SELECT o FROM Room o WHERE o.establishment IS NULL", RoomEntity.class).getResultList();
    }

    @Transactional
    public List<RoomEntity> findRoomsByEstablishment(EstablishmentEntity establishment) {
        return entityManager.createQuery("SELECT o FROM Room o WHERE o.establishment = :establishment", RoomEntity.class).setParameter("establishment", establishment).getResultList();
    }

    @Transactional
    public RoomEntity lazilyLoadImageToRoom(RoomEntity room) {
        PersistenceUnitUtil u = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        if (!u.isLoaded(room, "image") && room.getId() != null) {
            room = find(room.getId());
            room.getImage().getId();
        }
        return room;
    }
    
}
