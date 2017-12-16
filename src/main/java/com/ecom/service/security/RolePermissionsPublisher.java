package com.ecom.service.security;

import com.ecom.domain.security.RolePermission;
import com.ecom.domain.security.UserRole;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class RolePermissionsPublisher {

    private static final Logger logger = Logger.getLogger(RolePermissionsPublisher.class.getName());
    
    @Inject
    private RolePermissionsService rolePermissionService;
    
    @PostConstruct
    public void postConstruct() {

        if (rolePermissionService.countAllEntries() == 0) {
//TODO add permissions
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "room:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "room:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "room:delete"));

            rolePermissionService.save(new RolePermission(UserRole.Manager, "room:create"));
            
            rolePermissionService.save(new RolePermission(UserRole.Manager, "room:update"));
            
            rolePermissionService.save(new RolePermission(UserRole.Manager, "room:delete"));
            
            rolePermissionService.save(new RolePermission(UserRole.Administrator, "user:*"));
            
            logger.info("Successfully created permissions for user roles.");
        }
    }
}
