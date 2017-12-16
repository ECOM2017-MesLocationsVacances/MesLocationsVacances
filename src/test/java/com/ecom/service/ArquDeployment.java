package com.ecom.service;

import com.ecom.domain.BaseEntity;
import com.ecom.rest.EstablishmentResource;
import com.ecom.rest.RESTConfig;
import com.ecom.service.security.SecurityWrapper;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.primefaces.model.SortOrder;

public class ArquDeployment {
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(RESTConfig.class, SortOrder.class)
                .addPackages(true, BaseEntity.class.getPackage(), EstablishmentService.class.getPackage(), SecurityWrapper.class.getPackage())
                .addAsLibraries(Maven.resolver()
                        .resolve("org.apache.shiro:shiro-core:1.3.2", "org.apache.shiro:shiro-web:1.3.2", "org.mockito:mockito-all:1.9.5")
                        .withTransitivity().asFile())
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("test-shiro.ini", "shiro.ini")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
