package com.ecom.service;

import com.ecom.domain.EstablishmentEntity;
import com.ecom.domain.RoomEntity;
import com.ecom.domain.security.UserEntity;
import com.ecom.domain.security.UserRole;
import com.ecom.domain.security.UserStatus;
import com.ecom.service.security.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates some test users in fresh database.
 * 
 * TODO This class is temporary for test, only. Just delete this class
 * if you do not need the test users to be created automatically.
 *
 */
@Singleton
@Startup
public class DatabaseCreator {

    private static final Logger logger = Logger.getLogger(DatabaseCreator.class.getName());

    @Inject
    private EstablishmentService establishmentService;

    @Inject
    private RoomService roomService;
    
    @PostConstruct
    public void postConstruct() {
        logger.log(Level.WARNING, "Creating establishments");
        EstablishmentEntity establishment1 = new EstablishmentEntity();
        establishment1.setName("Gîte du Brillant");
        establishment1.setDescription("LE meilleur gîte de la région");
        establishment1.setPlace("Nice");
        establishmentService.save(establishment1);

        EstablishmentEntity establishment2 = new EstablishmentEntity();
        establishment2.setName("Gîte du Soyeux");
        establishment2.setDescription("Le second meilleur gîte");
        establishment2.setPlace("Nice");
        establishmentService.save(establishment2);

        EstablishmentEntity establishment3 = new EstablishmentEntity();
        establishment3.setName("Poubell El Divina");
        establishment3.setDescription("Un excellent logement dans la ville de l'amour");
        establishment3.setPlace("Paris");
        establishmentService.save(establishment3);

        EstablishmentEntity establishment4 = new EstablishmentEntity();
        establishment4.setName("Hôtel de Californie");
        establishment4.setDescription("Le fameux hôtel de la chanson");
        establishment4.setPlace("Grenoble");
        establishmentService.save(establishment4);

        EstablishmentEntity establishment5 = new EstablishmentEntity();
        establishment5.setName("Chez Roger le Tavernier");
        establishment5.setDescription("Bienvenue dans mon humble taverne");
        establishment5.setPlace("Meylan");
        establishmentService.save(establishment5);


        logger.log(Level.WARNING, "Creating rooms");
        RoomEntity room1 = new RoomEntity();
        room1.setName("Chambre principale");
        room1.setPrice(BigDecimal.valueOf(250));
        room1.setDescription("Deux lits");
        room1.setEstablishment(establishment1);
        roomService.save(room1);

        RoomEntity room2 = new RoomEntity();
        room2.setName("Chambre principale");
        room2.setPrice(BigDecimal.valueOf(200));
        room2.setDescription("Deux lits");
        room2.setEstablishment(establishment2);
        roomService.save(room2);

        RoomEntity room3 = new RoomEntity();
        room3.setName("Entre les cartons et les sacs poubelles");
        room3.setPrice(BigDecimal.valueOf(2.67));
        room3.setDescription("Une place");
        room3.setEstablishment(establishment3);
        roomService.save(room3);

        RoomEntity room4 = new RoomEntity();
        room4.setName("Numéro 1");
        room4.setPrice(BigDecimal.valueOf(80));
        room4.setDescription("Multiples places");
        room4.setEstablishment(establishment4);
        roomService.save(room4);

        RoomEntity room5 = new RoomEntity();
        room5.setName("Numéro 2");
        room5.setPrice(BigDecimal.valueOf(80));
        room5.setDescription("Multiples places");
        room5.setEstablishment(establishment4);
        roomService.save(room5);

        RoomEntity room6 = new RoomEntity();
        room6.setName("Numéro 3");
        room6.setPrice(BigDecimal.valueOf(80));
        room6.setDescription("Multiples places");
        room6.setEstablishment(establishment4);
        roomService.save(room6);

        RoomEntity room7 = new RoomEntity();
        room7.setName("Numéro 4");
        room7.setPrice(BigDecimal.valueOf(80));
        room7.setDescription("Multiples places");
        room7.setEstablishment(establishment4);
        roomService.save(room7);

        RoomEntity room8 = new RoomEntity();
        room8.setName("Numéro 5");
        room8.setPrice(BigDecimal.valueOf(80));
        room8.setDescription("Multiples places");
        room8.setEstablishment(establishment4);
        roomService.save(room8);

        RoomEntity room9 = new RoomEntity();
        room9.setName("Numéro 6");
        room9.setPrice(BigDecimal.valueOf(80));
        room9.setDescription("Multiples places");
        room9.setEstablishment(establishment4);
        roomService.save(room9);

        RoomEntity room10 = new RoomEntity();
        room10.setName("Chambre à l'étage");
        room10.setPrice(BigDecimal.valueOf(42));
        room10.setDescription("Chambres modestes");
        room10.setEstablishment(establishment5);
        roomService.save(room10);

        RoomEntity room11 = new RoomEntity();
        room11.setName("Chambre dans la grange");
        room11.setPrice(BigDecimal.valueOf(14));
        room11.setDescription("Chambres modestes");
        room11.setEstablishment(establishment5);
        roomService.save(room11);
    }
}
