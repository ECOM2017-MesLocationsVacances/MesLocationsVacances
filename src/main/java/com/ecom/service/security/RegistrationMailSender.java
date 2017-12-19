package com.ecom.service.security;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import com.ecom.domain.ReservationEntity;
import org.omnifaces.util.Faces;

/**
 * Utility class for sending mails for registration activation
 * and password reset.
 *
 */
public class RegistrationMailSender implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(RegistrationMailSender.class.getName());

    private static final String REGISTRATION_SUBJECT = "registration subject";
    private static final String REGISTRATION_MESSAGE = "registration message\nLink:";
    private static final String PW_RESET_SUBJECT = "password reset subject";
    private static final String PW_RESET_MESSAGE = "password reset message\nLink:";
    private static final String CONFIRMATION_USER = "You have successfully reserved %s from %s to %s %s. \n ID = %d%n";
    private static final String CONFIRMATION_MANAGER = "La chambre %s a été réservée du %s au %s %s. \n ID = %d%n";
    private static final String PAID = "et a été payée";
    private static final String TO_PAY = "et est à payer sur place";


    public static void sendRegistrationActivation(String to, String activationLink) {

        try {
            Session session = (Session) new InitialContext().lookup("java:jboss/mail/mailSession");
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(REGISTRATION_SUBJECT);
            message.setText(REGISTRATION_MESSAGE + activationLink);

            logger.log(Level.INFO, "Sending registration activation to {0}", to);
            
            Transport.send(message);

            logger.log(Level.INFO, "Mail sent succesfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static void sendConfirmation(String to, ReservationEntity reservation, boolean user, boolean paid) {

        try {
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            String payment = paid ? PAID : TO_PAY;
            String userString = user ? CONFIRMATION_USER : CONFIRMATION_MANAGER;
            Session session = (Session) new InitialContext().lookup("java:jboss/mail/mailSession");
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(REGISTRATION_SUBJECT);
            message.setText(String.format(userString,
                    reservation.getRoom().getName(),
                    dt.format(reservation.getStartdate()),
                    dt.format(reservation.getEnddate()),
                    payment,
                    reservation.getId()
                    ));

            logger.log(Level.INFO, "Sending confirmation to {0}", to);

            Transport.send(message);

            logger.log(Level.INFO, "Mail sent succesfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static void sendPasswordReset(String to, String passwordResetLink) {

        ServletContext context = Faces.getServletContext();
        String from = context.getInitParameter("mail.from");

        try {

            Message message = new MimeMessage(getSession(context));
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(PW_RESET_SUBJECT);
            message.setText(PW_RESET_MESSAGE + passwordResetLink);

            logger.log(Level.INFO, "Sending password reset to {0}", to);
            
            Transport.send(message);

            logger.log(Level.INFO, "Mail sent succesfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static Session getSession(ServletContext context) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", context.getInitParameter("mail.smtp.host"));
        props.put("mail.smtp.port", context.getInitParameter("mail.smtp.port"));

        final String username = context.getInitParameter("mail.username");
        final String password = context.getInitParameter("mail.password");
        
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        
        return session;
    }

}
