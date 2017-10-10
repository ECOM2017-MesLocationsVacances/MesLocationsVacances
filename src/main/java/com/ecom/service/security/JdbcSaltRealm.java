package com.ecom.service.security;

import org.apache.shiro.realm.jdbc.JdbcRealm;

public class JdbcSaltRealm extends JdbcRealm {
    public JdbcSaltRealm() {
        setSaltStyle(SaltStyle.COLUMN);
    }

}
