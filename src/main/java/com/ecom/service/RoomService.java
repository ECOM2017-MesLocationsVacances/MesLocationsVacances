package com.ecom.service;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.ReservationEntity;
import com.ecom.domain.RoomEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Named;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

@Named
public class RoomService extends BaseService<RoomEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    public RoomService() {
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
    public List<RoomEntity> findFreeRoomsInCity(Date from, Date to, String place) {
        place = "%" + place + "%";
        return entityManager.createQuery(
                "SELECT o " +
                        "FROM Room o " +
                        "WHERE o.establishment.place LIKE :place " +
                        "AND o.id NOT IN (" +
                        "SELECT r.room.id " +
                        "FROM Reservation r " +
                        "WHERE r.room.establishment.place LIKE :place "+
                        "AND ((r.startdate > :startDate AND r.startdate < :endDate) " +
                        "OR (r.enddate < :endDate AND r.enddate > :startDate)" +
                        "OR (r.startdate < :startDate AND r.enddate > :endDate))" +
                        ")", RoomEntity.class)
                .setParameter("place", place)
                .setParameter("startDate", from, TemporalType.DATE)
                .setParameter("endDate", to, TemporalType.DATE)
                .getResultList();
    }

    @Transactional
    public List<RoomEntity> findFreeRoomsInCityForDuration(Date from, Date to, Long time, String place) {
        place = "%" + place + "%";
        List<RoomEntity> freeRooms =
                entityManager.createQuery(
                        "SELECT o " +
                                "FROM Room o " +
                                "WHERE o.establishment.place LIKE :place " +
                                "AND o.id NOT IN (" +
                                "SELECT r.room.id " +
                                "FROM Reservation r " +
                                "WHERE r.room.establishment.place LIKE :place "+
                                "AND ((r.startdate > :startDate AND r.startdate < :endDate) " +
                                "OR (r.enddate < :endDate AND r.enddate > :startDate)" +
                                "OR (r.startdate < :startDate AND r.enddate > :endDate))" +
                                ")", RoomEntity.class)
                        .setParameter("place", place)
                        .setParameter("startDate", from, TemporalType.DATE)
                        .setParameter("endDate", to, TemporalType.DATE)
                        .getResultList();
        List<ReservationEntity> reservationsUnsorted =
                entityManager.createQuery(
                        "SELECT r.room.id " +
                                "FROM Reservation r " +
                                "WHERE r.room.establishment.place LIKE :place "+
                                "AND ((r.startdate > :startDate AND r.startdate < :endDate) " +
                                "OR (r.enddate < :endDate AND r.enddate > :startDate)" +
                                "OR (r.startdate < :startDate AND r.enddate > :endDate))"
                        , ReservationEntity.class)
                        .setParameter("place", place)
                        .setParameter("startDate", from, TemporalType.DATE)
                        .setParameter("endDate", to, TemporalType.DATE)
                        .getResultList();
        HashMap<RoomEntity, ArrayList<ReservationEntity>> reservationsByRoom = new HashMap<>();
        for (ReservationEntity reservation : reservationsUnsorted) {
            RoomEntity room = reservation.getRoom();
            if (reservationsByRoom.containsKey(room))
                reservationsByRoom.get(room).add(reservation);
            else {
                ArrayList<ReservationEntity> reservations = new ArrayList<>();
                reservations.add(reservation);
                reservationsByRoom.put(room, reservations);
            }
        }
        Object[] freeRoomsPartial = reservationsByRoom.keySet().stream().parallel().filter(e -> {
            ArrayList<ReservationEntity> reservations = reservationsByRoom.get(e);
            for (int i = 1; i < reservations.size(); i++)
                if (reservations.get(i).getStartdate().getTime() - reservations.get(i - 1).getEnddate().getTime() >= time)
                    return true;
            return false;
        }).toArray();
        for (Object room : freeRoomsPartial)
            freeRooms.add((RoomEntity) room);
        return freeRooms;
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
