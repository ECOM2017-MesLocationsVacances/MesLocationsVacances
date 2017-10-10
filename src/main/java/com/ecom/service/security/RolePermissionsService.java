package com.ecom.service.security;

import com.ecom.domain.security.RolePermission;
import com.ecom.service.BaseService;

import java.io.Serializable;

import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class RolePermissionsService extends BaseService<RolePermission> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public RolePermissionsService(){
        super(RolePermission.class);
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM RolePermission o", Long.class).getSingleResult();
    }

}
