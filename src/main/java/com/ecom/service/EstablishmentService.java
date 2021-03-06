package com.ecom.service;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.ReservationEntity;
import com.ecom.domain.RoomEntity;
import com.ecom.domain.security.UserEntity;

import javax.inject.Named;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Named
public class EstablishmentService extends BaseService<EstablishmentEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public EstablishmentService(){
        super(EstablishmentEntity.class);
    }
    
    @Transactional
    public List<EstablishmentEntity> findAllEstablishmentEntities() {
        return entityManager.createQuery("SELECT o FROM Establishment o LEFT JOIN FETCH o.image", EstablishmentEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Establishment o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(EstablishmentEntity establishment) {

        /* This is called before a Establishment is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllEstablishmentRoomsAssignments(establishment);
        
    }

    // Remove all assignments from all room a establishment. Called before delete a establishment.
    @Transactional
    private void cutAllEstablishmentRoomsAssignments(EstablishmentEntity establishment) {
        entityManager
                .createQuery("UPDATE Room c SET c.establishment = NULL WHERE c.establishment = :p")
                .setParameter("p", establishment).executeUpdate();
    }
    
    @Transactional
    public List<EstablishmentEntity> findAvailableEstablishments(UserEntity user) {
        return entityManager.createQuery("SELECT o FROM Establishment o WHERE o.manager IS NULL", EstablishmentEntity.class).getResultList();
    }

    @Transactional
    public List<EstablishmentEntity> findEstablishmentsByManager(UserEntity user) {
        return entityManager.createQuery("SELECT o FROM Establishment o WHERE o.manager = :user", EstablishmentEntity.class).setParameter("user", user).getResultList();
    }

    @Transactional
    public List<EstablishmentEntity> findFreeEstablishments(Date from, Date to, String place) {
        place = "%" + place + "%";
        return entityManager.createQuery(
                "SELECT DISTINCT o.establishment " +
                        "FROM Room o " +
                        "WHERE o.establishment.place LIKE :place " +
                        "AND o.id NOT IN (" +
                        "SELECT r.room.id " +
                        "FROM Reservation r " +
                        "WHERE r.room.establishment.place LIKE :place "+
                        "AND ((r.startdate > :startDate AND r.startdate < :endDate) " +
                        "OR (r.enddate < :endDate AND r.enddate > :startDate)" +
                        "OR (r.startdate < :startDate AND r.enddate > :endDate))" +
                        ")", EstablishmentEntity.class)
                .setParameter("place", place)
                .setParameter("startDate", from, TemporalType.DATE)
                .setParameter("endDate", to, TemporalType.DATE)
                .getResultList();
    }

    @Transactional
    public List<EstablishmentEntity> findFreeEstablishmentsForDuration(String place,
                                                                       int sizeA, int sizeC,
                                                                       Date from, Date to,
                                                                       long duration) {
        place = "%" + place + "%";
        List<EstablishmentEntity> fullyFree =
                entityManager.createQuery(
                        "SELECT DISTINCT o.establishment " +
                                "FROM Room o " +
                                "WHERE o.establishment.place LIKE :place " +
                                "AND o.sizeA >= :sizeA " +
                                "AND o.sizeC >= :sizeC " +
                                "AND o.id NOT IN (" +
                                "SELECT r.room.id " +
                                "FROM Reservation r " +
                                "WHERE r.room.establishment.place LIKE :place "+
                                "AND r.room.sizeA >= :sizeA " +
                                "AND r.room.sizeC >= :sizeC " +
                                "AND ((r.startdate > :startDate AND r.startdate < :endDate) " +
                                "OR (r.enddate < :endDate AND r.enddate > :startDate)" +
                                "OR (r.startdate < :startDate AND r.enddate > :endDate))" +
                                ")", EstablishmentEntity.class)
                        .setParameter("place", place)
                        .setParameter("startDate", from, TemporalType.DATE)
                        .setParameter("endDate", to, TemporalType.DATE)
                        .setParameter("sizeA", sizeA)
                        .setParameter("sizeC", sizeC)
                        .getResultList();
        List<ReservationEntity> reservationsUnsorted =
                entityManager.createQuery(
                        "SELECT r " +
                                "FROM Reservation r " +
                                "WHERE r.room.establishment.place LIKE :place "+
                                "AND r.room.sizeA >= :sizeA " +
                                "AND r.room.sizeC >= :sizeC " +
                                "AND ((r.startdate > :startDate AND r.startdate < :endDate) " +
                                "OR (r.enddate < :endDate AND r.enddate > :startDate)" +
                                "OR (r.startdate < :startDate AND r.enddate > :endDate))"
                        , ReservationEntity.class)
                        .setParameter("place", place)
                        .setParameter("startDate", from, TemporalType.DATE)
                        .setParameter("endDate", to, TemporalType.DATE)
                        .setParameter("sizeA", sizeA)
                        .setParameter("sizeC", sizeC)
                        .getResultList();
        HashMap<RoomEntity, ArrayList<ReservationEntity>> reservationsByRoom = new HashMap<>();
        for (ReservationEntity reservation : reservationsUnsorted) {
            RoomEntity room = reservation.getRoom();
            if (reservationsByRoom.containsKey(room))
                reservationsByRoom.get(room).add(reservation);
            else {
                if (!fullyFree.contains(room.getEstablishment())) {
                    ArrayList<ReservationEntity> reservations = new ArrayList<>();
                    reservations.add(reservation);
                    reservationsByRoom.put(room, reservations);
                }
            }
        }
        Object[] partiallyFree = reservationsByRoom.keySet().stream().parallel()
                .filter(e -> {
                    ArrayList<ReservationEntity> reservations = reservationsByRoom.get(e);
                    for (int i = 1; i < reservations.size(); i++)
                        if (reservations.get(i).getStartdate().getTime() - reservations.get(i - 1).getEnddate().getTime() >= duration)
                            return true;
                    return false;
                })
                .map(RoomEntity::getEstablishment)
                .distinct()
                .toArray();
        for (Object establishment : partiallyFree)
            fullyFree.add((EstablishmentEntity) establishment);
        return fullyFree;
    }


    @Transactional
    public EstablishmentEntity lazilyLoadImageToEstablishment(EstablishmentEntity establishment) {
        PersistenceUnitUtil u = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        if (!u.isLoaded(establishment, "image") && establishment.getId() != null) {
            establishment = find(establishment.getId());
            establishment.getImage().getId();
        }
        return establishment;
    }
    
}
