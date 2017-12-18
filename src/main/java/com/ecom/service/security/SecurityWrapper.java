package com.ecom.service.security;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.SimpleByteSource;

/**
 * Central wrapping of the Apache Shiro security library
 */
@Named("security")
public class SecurityWrapper implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(SecurityWrapper.class.getName());
    
    // Because Shiro does not support public permissions, we need to workaround them:
    private static final Set<String> publicPermissions = 
            new HashSet<>(Arrays.asList(new String[]{"room:read" ,"room:read" ,"room:read" ,"room:read" ,"establishment:create" ,"establishment:read" ,"establishment:update" ,"establishment:delete" ,"establishment:create" ,"establishment:read" ,"establishment:update" ,"establishment:delete" ,"establishment:create" ,"establishment:read" ,"establishment:update" ,"establishment:delete" ,"establishment:create" ,"establishment:read" ,"establishment:update" ,"establishment:delete" ,"reservation:create" ,"reservation:read" ,"reservation:update" ,"reservation:delete" ,"reservation:create" ,"reservation:read" ,"reservation:update" ,"reservation:delete" ,"reservation:create" ,"reservation:read" ,"reservation:update" ,"reservation:delete" ,"reservation:create" ,"reservation:read" ,"reservation:update" ,"reservation:delete"}));
    
    public static boolean login(String username, String password, boolean rememberMe) {
        try {
            getSubject().login(new UsernamePasswordToken(username, password, rememberMe));
        } catch (AuthenticationException e) {
            logger.log(Level.WARNING, "AuthenticationException", e);
            return false;
        }
        return true;
    }
    
    public static void logout() {
        getSubject().logout();
    }
    
    public static String getUsername() {
        
        if (getSubject().getPrincipal() == null) {
            return null;
        }
        
        return (String) getSubject().getPrincipal();
    }
    
    public static boolean isPermitted(String permission) {
        return publicPermissions.contains(permission)
                || getSubject().isPermitted(permission);
    }

    public static boolean hasReadPermissionOnlyOwner(String entity) {
        return !getSubject().isPermitted(entity + ":read")
                && getSubject().isPermitted(entity + ":read:owner");
    }
    
    public static String generateSalt() {
        return new BigInteger(250, new SecureRandom()).toString(32);
    }
    
    public static String hashPassword(String password, String salt) {
        Sha512Hash hash = new Sha512Hash(password, (new SimpleByteSource(salt)).getBytes());
        return hash.toHex();
    }

    public static Subject getSubject() {
        Subject subject;
        try {
            subject = SecurityUtils.getSubject();
        } catch (UnavailableSecurityManagerException e) {
            Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
            SecurityManager securityManager = factory.getInstance();
            SecurityUtils.setSecurityManager(securityManager);
            subject = SecurityUtils.getSubject();
        }
        return subject;
    }
}
