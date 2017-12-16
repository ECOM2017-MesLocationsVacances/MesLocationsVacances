package com.ecom.service;

import com.ecom.domain.BaseEntity;
import com.ecom.domain.EstablishmentEntity;
import com.ecom.rest.EstablishmentResource;
import com.ecom.rest.RESTConfig;
import com.ecom.service.security.SecurityWrapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.SortOrder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URL;


@RunWith(Arquillian.class)
public class EstablishmentResourceTest {
    @Deployment
    public static WebArchive createDeployment() {
        return ArquDeployment.create().addClass(EstablishmentResource.class);
    }

    @BeforeClass
    public static void initResteasyClient() {
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
        System.out.println("######TESTS######");
    }

    @Test
    @RunAsClient
    @InSequence(1)
    public void getEstablishmentsEmpty(@ArquillianResource URL url) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url.toString() + "api");
        EstablishmentClient service = target.proxy(EstablishmentClient.class);
        String jsonResult = service.getEstablishments();
        assert jsonResult.equals("[]");
    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void addEstablishment(@ArquillianResource URL url) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url.toString() + "api");
        EstablishmentClient service = target.proxy(EstablishmentClient.class);
        EstablishmentEntity establishment = new EstablishmentEntity();
        establishment.setName("Gite du Brillant");
        establishment.setDescription("LE meilleur gite de la region");
        establishment.setPlace("Nice");
        String jsonResult = service.addEstablishment("admin", "admin", establishment);
        System.out.println(jsonResult);
    }

    @Test
    @RunAsClient
    @InSequence(1)
    public void getEstablishmentsPopulated(@ArquillianResource URL url) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url.toString() + "api");
        EstablishmentClient service = target.proxy(EstablishmentClient.class);
        String jsonResult = service.getEstablishments();
        assert !jsonResult.equals("[]");
    }

    @Path("/establishments")
    public interface EstablishmentClient {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        String getEstablishments();

        @Path("{user}")
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        String addEstablishment(@PathParam("user") String login, @HeaderParam("password") String password, EstablishmentEntity establishment);
    }
}
