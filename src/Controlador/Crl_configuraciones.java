package Controlador;

import java.util.Properties;
import javax.swing.JOptionPane;

public class Crl_configuraciones {

}

//
//
//        Properties propidad = new Properties();
//        //propidad.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
//        
//        propidad.setProperty("mail.smtp.host", "smtp.gmail.com");
//        propidad.setProperty("mail.smtp.starttls.enable", "true");
//        propidad.setProperty("mail.smtp.port", "587");
//        propidad.setProperty("mail.smtp.auth", "true");
//
//
//        Session sesion = Session.getDefaultInstance(propidad);
//        
//        String correoEnvia = "fd0287931@gmail.com";
//        String contraseña = "Aa1593575";
//        String destinatario = txtCorreo.getText();
//        String asunto = txtConsultaOpinion.getText();
//        String mensaje = txtDescripcion.getText();
//        
//        MimeMessage mail = new MimeMessage(sesion);
//        
//        try {
//            
//            mail.setFrom(new InternetAddress(correoEnvia));
//            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
//            mail.setSubject(asunto);
//            mail.setText(mensaje);
//              
//            Transport transporte = sesion.getTransport("smtp");
//            transporte.connect(correoEnvia,contraseña);
//            transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
//            transporte.close();
//            
//            JOptionPane.showMessageDialog(null, "Se envio con exito a " + destinatario);
//            
//        } catch (MessagingException e) {
//            
//            System.out.println(e);
//            JOptionPane.showMessageDialog(null, "El Gmail no es el correcto o algo paso en el envio.");
//        
//        }
