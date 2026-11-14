package restaurante.correo;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import restaurante.config.Configuracion;
import restaurante.config.Configuracion.Claves;

/**
 * Envío de correos (por ejemplo, reportes en PDF) a través del servidor SMTP
 * configurado en config/app.properties.
 */
public final class ServicioCorreo {

    private static final String TIEMPO_ESPERA_MS = "20000";

    private ServicioCorreo() {
    }

    /** Indica si hay un servidor SMTP con usuario y contraseña configurados. */
    public static boolean configurado() {
        Configuracion cfg = Configuracion.get();
        return cfg.definido(Claves.SMTP_HOST) && cfg.definido(Claves.SMTP_USUARIO)
                && cfg.definido(Claves.SMTP_CONTRASENA);
    }

    /**
     * Envía un correo de texto plano con los adjuntos indicados.
     * Es una operación de red: no debe llamarse desde el hilo de la interfaz.
     */
    public static void enviar(String destinatario, String asunto, String cuerpo, List<File> adjuntos)
            throws MessagingException {
        if (!configurado()) {
            throw new MessagingException("El envío de correos no está configurado. Complete las claves smtp.* "
                    + "en config/app.properties.");
        }
        Configuracion cfg = Configuracion.get();
        String usuario = cfg.texto(Claves.SMTP_USUARIO, "");
        String contrasena = cfg.texto(Claves.SMTP_CONTRASENA, "");
        String remitente = cfg.texto(Claves.SMTP_REMITENTE, "");

        Properties props = new Properties();
        props.put("mail.smtp.host", cfg.texto(Claves.SMTP_HOST, ""));
        props.put("mail.smtp.port", cfg.texto(Claves.SMTP_PUERTO, "587"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", TIEMPO_ESPERA_MS);
        props.put("mail.smtp.timeout", TIEMPO_ESPERA_MS);
        props.put("mail.smtp.writetimeout", TIEMPO_ESPERA_MS);

        Session sesion = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contrasena);
            }
        });

        MimeMessage mensaje = new MimeMessage(sesion);
        mensaje.setFrom(new InternetAddress(remitente.isEmpty() ? usuario : remitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, true));
        mensaje.setSubject(asunto, StandardCharsets.UTF_8.name());

        MimeMultipart partes = new MimeMultipart();
        MimeBodyPart texto = new MimeBodyPart();
        texto.setText(cuerpo == null ? "" : cuerpo, StandardCharsets.UTF_8.name());
        partes.addBodyPart(texto);
        for (File archivo : adjuntos) {
            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo)));
            adjunto.setFileName(archivo.getName());
            partes.addBodyPart(adjunto);
        }
        mensaje.setContent(partes);
        Transport.send(mensaje);
    }
}
