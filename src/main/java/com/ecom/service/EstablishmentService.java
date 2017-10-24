package com.ecom.service;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.security.UserEntity;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.PersistenceUnitUtil;
import javax.transaction.Transactional;

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
    public EstablishmentEntity lazilyLoadImageToEstablishment(EstablishmentEntity establishment) {
        PersistenceUnitUtil u = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        if (!u.isLoaded(establishment, "image") && establishment.getId() != null) {
            establishment = find(establishment.getId());
            establishment.getImage().getId();
        }
        return establishment;
    }
    
}