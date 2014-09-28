package logicaPrograma;


import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

public class EnviarEmail {
	//DEFINICIÓN DE ATRIBUTOS
	private String to, from, asunto,cuerpo;
	private Properties properties= System.getProperties();
	private Session sesion;
	private MimeMessage mensaje;
	
	
	//DEFINICION DE MÉTODOS
	
	public class SmtpAuthenticator extends Authenticator {
		public SmtpAuthenticator() {

		    super();
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
		 String username = "gesbiblio2014.2";
		 String password = "prograpoo1";
		    if ((username != null) && (username.length() > 0) && (password != null) 
		      && (password.length   () > 0)) {

		        return new PasswordAuthentication(username, password);
		    }

		    return null;
		}
	}
		
	public void enviarMensaje(){
		try{
			properties.put("mail.smtp.starttls.enable", "true"); 
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.user", "gesbiblio2014.2"); // User name
			properties.put("mail.smtp.password", "prograpoo1"); // password
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
			SmtpAuthenticator authentification= new SmtpAuthenticator();
			sesion= Session.getDefaultInstance(properties,authentification);
			mensaje = new MimeMessage(sesion);
			mensaje.setFrom(new InternetAddress(from));
			mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mensaje.setSubject(asunto);
			mensaje.setText(cuerpo);
			Transport.send(mensaje);
		}
		catch(MessagingException mex){
			mex.printStackTrace();
		}
	}
	
	public EnviarEmail(String de, String para, String pAsunto, String pCuerpo){
		from= de;
		to= para;
		asunto= pAsunto;
		cuerpo= pCuerpo;
	}
}
