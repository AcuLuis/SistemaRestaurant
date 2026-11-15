import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.imageio.ImageIO;

/**
 * Genera los iconos y el logotipo de la aplicación (src/main/resources/iconos).
 *
 * <p>Todos los gráficos se dibujan con Java2D, sin recursos externos.
 * Uso (desde la raíz del proyecto):</p>
 * <pre>java herramientas/GeneradorIconos.java src/main/resources/iconos</pre>
 */
public class GeneradorIconos {

    private static final Color AZUL = new Color(0x2F6FB2);
    private static final Color VERDE = new Color(0x2E8B57);
    private static final Color NARANJA = new Color(0xE07B24);
    private static final Color ROJO = new Color(0xC0392B);
    private static final Color MORADO = new Color(0x7D4CA8);
    private static final Color GRIS = new Color(0x56636E);
    private static final Color TURQUESA = new Color(0x1F8A8A);
    private static final Color DORADO = new Color(0xC9A227);
    private static final Color MARINO = new Color(0x1F3B57);
    private static final Color BLANCO = Color.WHITE;

    private static final int TAM = 24;

    public static void main(String[] args) throws IOException {
        File carpeta = new File(args.length > 0 ? args[0] : "src/main/resources/iconos");
        if (!carpeta.isDirectory() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear " + carpeta);
        }
        Map<String, Consumer<Graphics2D>> iconos = new LinkedHashMap<>();
        iconos.put("administracion", GeneradorIconos::administracion);
        iconos.put("usuarios", GeneradorIconos::usuarios);
        iconos.put("datos", GeneradorIconos::datos);
        iconos.put("mantenimiento", GeneradorIconos::mantenimiento);
        iconos.put("contrato", GeneradorIconos::contrato);
        iconos.put("rol", GeneradorIconos::rol);
        iconos.put("area", GeneradorIconos::area);
        iconos.put("producto", GeneradorIconos::producto);
        iconos.put("insumo", GeneradorIconos::insumo);
        iconos.put("reportes", GeneradorIconos::reportes);
        iconos.put("parametros", GeneradorIconos::parametros);
        iconos.put("calendario", GeneradorIconos::calendario);
        iconos.put("tabla", GeneradorIconos::tabla);
        iconos.put("plato", GeneradorIconos::plato);
        iconos.put("ubicacion", GeneradorIconos::ubicacion);
        iconos.put("correo", GeneradorIconos::correo);
        iconos.put("enviar", GeneradorIconos::enviar);
        iconos.put("redes", GeneradorIconos::redes);
        iconos.put("enlace", GeneradorIconos::enlace);
        iconos.put("transacciones", GeneradorIconos::transacciones);
        iconos.put("venta", GeneradorIconos::venta);
        iconos.put("inventario", GeneradorIconos::inventario);
        iconos.put("empleado", GeneradorIconos::empleado);
        iconos.put("cliente", GeneradorIconos::cliente);
        iconos.put("mesa", GeneradorIconos::mesa);
        iconos.put("receta", GeneradorIconos::receta);
        iconos.put("produccion", GeneradorIconos::produccion);
        iconos.put("reserva", GeneradorIconos::reserva);
        iconos.put("inicio", GeneradorIconos::inicio);
        iconos.put("sesion", GeneradorIconos::sesion);
        for (Map.Entry<String, Consumer<Graphics2D>> icono : iconos.entrySet()) {
            for (int escala : new int[] {1, 2}) {
                BufferedImage img = new BufferedImage(TAM * escala, TAM * escala, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = preparar(img);
                g.scale(escala, escala);
                icono.getValue().accept(g);
                g.dispose();
                String sufijo = escala == 1 ? "" : "@2x";
                ImageIO.write(img, "png", new File(carpeta, icono.getKey() + sufijo + ".png"));
            }
        }
        ImageIO.write(logotipo(520), "png", new File(carpeta, "logotipo.png"));
        ImageIO.write(logotipo(200), "png", new File(carpeta, "logotipo-pequeno.png"));
        ImageIO.write(logotipo(256), "png", new File(carpeta, "logotipo-256.png"));
        escribirIco(logotipo(256), new File(carpeta, "logotipo.ico"));
        System.out.println("Iconos generados en " + carpeta.getAbsolutePath());
    }

    private static Graphics2D preparar(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g;
    }

    private static void trazo(Graphics2D g, Color c, float ancho) {
        g.setColor(c);
        g.setStroke(new BasicStroke(ancho, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    private static void linea(Graphics2D g, double x1, double y1, double x2, double y2) {
        g.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    private static void persona(Graphics2D g, Color c, double cx, double cy, double escala) {
        g.setColor(c);
        g.fill(new Ellipse2D.Double(cx - 3.2 * escala, cy - 7.5 * escala, 6.4 * escala, 6.4 * escala));
        g.fill(new RoundRectangle2D.Double(cx - 6 * escala, cy, 12 * escala, 7.5 * escala, 6 * escala, 6 * escala));
    }

    // ------------------------------------------------------------ Iconos

    private static void administracion(Graphics2D g) {
        GeneralPath escudo = new GeneralPath();
        escudo.moveTo(12, 2);
        escudo.lineTo(20.5, 5.5);
        escudo.curveTo(20.5, 13, 17, 19, 12, 22);
        escudo.curveTo(7, 19, 3.5, 13, 3.5, 5.5);
        escudo.closePath();
        g.setColor(AZUL);
        g.fill(escudo);
        trazo(g, BLANCO, 2f);
        linea(g, 8, 12, 11, 15);
        linea(g, 11, 15, 16.5, 8.5);
    }

    private static void usuarios(Graphics2D g) {
        persona(g, AZUL, 9, 10.5, 1.0);
        g.setColor(VERDE);
        g.fill(new Ellipse2D.Double(13.5, 12.5, 9.5, 9.5));
        trazo(g, BLANCO, 1.8f);
        linea(g, 18.25, 14.8, 18.25, 19.7);
        linea(g, 15.8, 17.25, 20.7, 17.25);
    }

    private static void datos(Graphics2D g) {
        g.setColor(TURQUESA);
        g.fill(new RoundRectangle2D.Double(4, 4, 16, 16, 4, 4));
        g.fill(new Ellipse2D.Double(4, 1.5, 16, 5.5));
        g.fill(new Ellipse2D.Double(4, 17, 16, 5.5));
        trazo(g, BLANCO, 1.4f);
        g.draw(new Arc2D.Double(4, 6.5, 16, 5, 180, 180, Arc2D.OPEN));
        g.draw(new Arc2D.Double(4, 12, 16, 5, 180, 180, Arc2D.OPEN));
    }

    private static void mantenimiento(Graphics2D g) {
        trazo(g, GRIS, 3.2f);
        linea(g, 6, 18, 14, 10);
        g.setColor(GRIS);
        g.fill(new Ellipse2D.Double(11.5, 3, 10, 10));
        g.setColor(new Color(0, 0, 0, 0));
        g.setComposite(java.awt.AlphaComposite.Clear);
        g.fill(new RoundRectangle2D.Double(15.5, 2, 3.2, 6.5, 2, 2));
        g.setComposite(java.awt.AlphaComposite.SrcOver);
        g.setColor(NARANJA);
        g.fill(new Ellipse2D.Double(3, 16, 5, 5));
    }

    private static void documento(Graphics2D g, Color c) {
        g.setColor(c);
        g.fill(new RoundRectangle2D.Double(4.5, 2, 15, 20, 3, 3));
        trazo(g, BLANCO, 1.4f);
        linea(g, 8, 7, 16, 7);
        linea(g, 8, 10.5, 16, 10.5);
        linea(g, 8, 14, 13, 14);
    }

    private static void contrato(Graphics2D g) {
        documento(g, AZUL);
        trazo(g, DORADO, 1.6f);
        GeneralPath firma = new GeneralPath();
        firma.moveTo(8, 18.5);
        firma.curveTo(10, 15.5, 11, 20.5, 13, 17.5);
        firma.curveTo(14, 16.5, 15, 19, 16.5, 18);
        g.draw(firma);
    }

    private static void rol(Graphics2D g) {
        g.setColor(MORADO);
        g.fill(new RoundRectangle2D.Double(2.5, 5, 19, 14, 3.5, 3.5));
        g.setColor(BLANCO);
        g.fill(new Ellipse2D.Double(5.5, 8, 5, 5));
        g.fill(new RoundRectangle2D.Double(4.5, 13, 7, 3.5, 3, 3));
        trazo(g, BLANCO, 1.4f);
        linea(g, 13.5, 9.5, 18.5, 9.5);
        linea(g, 13.5, 13, 17, 13);
    }

    private static void area(Graphics2D g) {
        g.setColor(TURQUESA);
        g.fill(new RoundRectangle2D.Double(3, 3, 8, 8, 2, 2));
        g.fill(new RoundRectangle2D.Double(13, 3, 8, 8, 2, 2));
        g.fill(new RoundRectangle2D.Double(3, 13, 8, 8, 2, 2));
        g.setColor(NARANJA);
        g.fill(new RoundRectangle2D.Double(13, 13, 8, 8, 2, 2));
    }

    private static void producto(Graphics2D g) {
        GeneralPath tapa = new GeneralPath();
        tapa.moveTo(3, 8);
        tapa.lineTo(12, 3.5);
        tapa.lineTo(21, 8);
        tapa.lineTo(12, 12.5);
        tapa.closePath();
        g.setColor(new Color(0xD99A4E));
        g.fill(tapa);
        GeneralPath izq = new GeneralPath();
        izq.moveTo(3, 8);
        izq.lineTo(12, 12.5);
        izq.lineTo(12, 21.5);
        izq.lineTo(3, 17);
        izq.closePath();
        g.setColor(new Color(0xB8752E));
        g.fill(izq);
        GeneralPath der = new GeneralPath();
        der.moveTo(21, 8);
        der.lineTo(12, 12.5);
        der.lineTo(12, 21.5);
        der.lineTo(21, 17);
        der.closePath();
        g.setColor(new Color(0x9A5F22));
        g.fill(der);
    }

    private static void insumo(Graphics2D g) {
        g.setColor(VERDE);
        GeneralPath hoja = new GeneralPath();
        hoja.moveTo(12, 2.5);
        hoja.curveTo(17, 4, 18, 8, 12.5, 10);
        hoja.curveTo(11, 7, 11, 5, 12, 2.5);
        g.fill(hoja);
        g.setColor(NARANJA);
        g.fill(new RoundRectangle2D.Double(5, 9, 14, 13, 5, 5));
        trazo(g, BLANCO, 1.4f);
        linea(g, 8.5, 14, 15.5, 14);
        linea(g, 8.5, 17.5, 15.5, 17.5);
    }

    private static void reportes(Graphics2D g) {
        g.setColor(AZUL);
        g.fill(new RoundRectangle2D.Double(3, 12, 4.5, 9, 1.5, 1.5));
        g.setColor(VERDE);
        g.fill(new RoundRectangle2D.Double(9.75, 6, 4.5, 15, 1.5, 1.5));
        g.setColor(NARANJA);
        g.fill(new RoundRectangle2D.Double(16.5, 3, 4.5, 18, 1.5, 1.5));
    }

    private static void parametros(Graphics2D g) {
        trazo(g, GRIS, 1.8f);
        linea(g, 3, 6, 21, 6);
        linea(g, 3, 12, 21, 12);
        linea(g, 3, 18, 21, 18);
        g.setColor(AZUL);
        g.fill(new Ellipse2D.Double(14, 3, 6, 6));
        g.setColor(NARANJA);
        g.fill(new Ellipse2D.Double(5, 9, 6, 6));
        g.setColor(VERDE);
        g.fill(new Ellipse2D.Double(11, 15, 6, 6));
    }

    private static void calendario(Graphics2D g) {
        g.setColor(ROJO);
        g.fill(new RoundRectangle2D.Double(3, 4, 18, 6, 3, 3));
        g.setColor(new Color(0xE9EEF3));
        g.fill(new RoundRectangle2D.Double(3, 8, 18, 13, 3, 3));
        trazo(g, GRIS, 1.6f);
        linea(g, 8, 2.5, 8, 6);
        linea(g, 16, 2.5, 16, 6);
        g.setColor(GRIS);
        for (int fila = 0; fila < 2; fila++) {
            for (int col = 0; col < 3; col++) {
                g.fill(new RoundRectangle2D.Double(6 + col * 4.5, 11 + fila * 4.5, 3, 3, 1, 1));
            }
        }
    }

    private static void tabla(Graphics2D g) {
        g.setColor(AZUL);
        g.fill(new RoundRectangle2D.Double(3, 3, 18, 18, 3, 3));
        g.setColor(BLANCO);
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {
                if (fila == 0 || col == 0) {
                    continue;
                }
                g.fill(new RoundRectangle2D.Double(5 + col * 5.3, 5 + fila * 5.3, 4, 4, 1, 1));
            }
        }
        g.setColor(new Color(0xBFD6EE));
        g.fill(new RoundRectangle2D.Double(5, 5, 15, 3.6, 1, 1));
        g.fill(new RoundRectangle2D.Double(5, 5, 3.6, 15, 1, 1));
    }

    private static void plato(Graphics2D g) {
        g.setColor(new Color(0xD5DCE3));
        g.fill(new Ellipse2D.Double(2, 6, 20, 15));
        g.setColor(BLANCO);
        g.fill(new Ellipse2D.Double(5, 8.5, 14, 10));
        g.setColor(NARANJA);
        g.fill(new Arc2D.Double(6.5, 3, 11, 13, 0, 180, Arc2D.CHORD));
        g.fill(new Ellipse2D.Double(10.5, 1.5, 3, 3));
    }

    private static void ubicacion(Graphics2D g) {
        GeneralPath pin = new GeneralPath();
        pin.moveTo(12, 22);
        pin.curveTo(6, 15, 4.5, 12, 4.5, 9.5);
        pin.curveTo(4.5, 5, 8, 2, 12, 2);
        pin.curveTo(16, 2, 19.5, 5, 19.5, 9.5);
        pin.curveTo(19.5, 12, 18, 15, 12, 22);
        pin.closePath();
        g.setColor(ROJO);
        g.fill(pin);
        g.setColor(BLANCO);
        g.fill(new Ellipse2D.Double(9, 6.5, 6, 6));
    }

    private static void sobre(Graphics2D g, Color c) {
        g.setColor(c);
        g.fill(new RoundRectangle2D.Double(2.5, 5, 19, 14, 3, 3));
        trazo(g, BLANCO, 1.6f);
        GeneralPath v = new GeneralPath();
        v.moveTo(4.5, 7.5);
        v.lineTo(12, 13);
        v.lineTo(19.5, 7.5);
        g.draw(v);
    }

    private static void correo(Graphics2D g) {
        sobre(g, AZUL);
    }

    private static void enviar(Graphics2D g) {
        GeneralPath avion = new GeneralPath();
        avion.moveTo(2.5, 11);
        avion.lineTo(21.5, 3);
        avion.lineTo(15, 21);
        avion.lineTo(11, 13.5);
        avion.closePath();
        g.setColor(AZUL);
        g.fill(avion);
        trazo(g, BLANCO, 1.3f);
        linea(g, 11, 13.5, 21.5, 3);
    }

    private static void redes(Graphics2D g) {
        trazo(g, GRIS, 1.8f);
        linea(g, 6, 12, 17.5, 5.5);
        linea(g, 6, 12, 17.5, 18.5);
        g.setColor(AZUL);
        g.fill(new Ellipse2D.Double(2, 8, 8, 8));
        g.setColor(VERDE);
        g.fill(new Ellipse2D.Double(14, 2, 7, 7));
        g.setColor(NARANJA);
        g.fill(new Ellipse2D.Double(14, 15, 7, 7));
    }

    private static void enlace(Graphics2D g) {
        g.setColor(TURQUESA);
        g.fill(new Ellipse2D.Double(2.5, 2.5, 19, 19));
        trazo(g, BLANCO, 1.3f);
        g.draw(new Ellipse2D.Double(8, 2.5, 8, 19));
        linea(g, 2.5, 12, 21.5, 12);
        linea(g, 4.5, 7, 19.5, 7);
        linea(g, 4.5, 17, 19.5, 17);
    }

    private static void transacciones(Graphics2D g) {
        g.setColor(DORADO);
        g.fill(new Ellipse2D.Double(2.5, 9, 13, 13));
        g.setColor(new Color(0xE4C35A));
        g.fill(new Ellipse2D.Double(8.5, 2, 13, 13));
        g.setColor(new Color(0x8A6D10));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        g.drawString("$", 12.2f, 12.3f);
    }

    private static void venta(Graphics2D g) {
        GeneralPath ticket = new GeneralPath();
        ticket.moveTo(5, 2);
        ticket.lineTo(19, 2);
        ticket.lineTo(19, 22);
        for (double x = 19; x > 5; x -= 3.5) {
            ticket.lineTo(x - 1.75, 20);
            ticket.lineTo(x - 3.5, 22);
        }
        ticket.closePath();
        g.setColor(VERDE);
        g.fill(ticket);
        trazo(g, BLANCO, 1.4f);
        linea(g, 8, 6.5, 16, 6.5);
        linea(g, 8, 10, 16, 10);
        linea(g, 8, 13.5, 13, 13.5);
    }

    private static void inventario(Graphics2D g) {
        g.setColor(GRIS);
        g.fill(new RoundRectangle2D.Double(4, 3.5, 16, 18.5, 3, 3));
        g.setColor(new Color(0xE9EEF3));
        g.fill(new RoundRectangle2D.Double(6, 6, 12, 14, 2, 2));
        g.setColor(GRIS);
        g.fill(new RoundRectangle2D.Double(8.5, 2, 7, 3.5, 2, 2));
        trazo(g, VERDE, 1.8f);
        linea(g, 8, 13, 10.5, 15.5);
        linea(g, 10.5, 15.5, 15.5, 10);
    }

    private static void empleado(Graphics2D g) {
        persona(g, AZUL, 12, 11, 1.25);
        g.setColor(DORADO);
        g.fill(new RoundRectangle2D.Double(10.5, 12.5, 3, 5, 1, 1));
    }

    private static void cliente(Graphics2D g) {
        persona(g, VERDE, 12, 11, 1.25);
    }

    private static void mesa(Graphics2D g) {
        g.setColor(new Color(0x9A5F22));
        g.fill(new RoundRectangle2D.Double(2.5, 7, 19, 4, 2, 2));
        trazo(g, new Color(0x9A5F22), 2.2f);
        linea(g, 6, 11, 5, 20);
        linea(g, 18, 11, 19, 20);
        g.setColor(TURQUESA);
        g.fill(new RoundRectangle2D.Double(8.5, 3, 7, 4, 2, 2));
    }

    private static void receta(Graphics2D g) {
        g.setColor(ROJO);
        g.fill(new RoundRectangle2D.Double(4, 2, 16, 20, 3, 3));
        g.setColor(BLANCO);
        g.fill(new Ellipse2D.Double(8, 5.5, 8, 5));
        g.fill(new RoundRectangle2D.Double(9, 9, 6, 4, 1, 1));
        trazo(g, BLANCO, 1.3f);
        linea(g, 7.5, 16, 16.5, 16);
        linea(g, 7.5, 19, 14, 19);
    }

    private static void produccion(Graphics2D g) {
        g.setColor(GRIS);
        g.fill(new RoundRectangle2D.Double(3, 10, 18, 11, 3, 3));
        g.fill(new RoundRectangle2D.Double(1.5, 8.5, 21, 3, 2, 2));
        trazo(g, NARANJA, 1.6f);
        GeneralPath vapor = new GeneralPath();
        vapor.moveTo(8, 7);
        vapor.curveTo(6.5, 5, 9.5, 4, 8, 1.5);
        g.draw(vapor);
        GeneralPath vapor2 = new GeneralPath();
        vapor2.moveTo(14, 7);
        vapor2.curveTo(12.5, 5, 15.5, 4, 14, 1.5);
        g.draw(vapor2);
    }

    private static void reserva(Graphics2D g) {
        calendario(g);
        g.setColor(VERDE);
        g.fill(new Ellipse2D.Double(12, 12, 10.5, 10.5));
        trazo(g, BLANCO, 1.7f);
        linea(g, 14.6, 17.3, 16.6, 19.3);
        linea(g, 16.6, 19.3, 20, 15.2);
    }

    private static void inicio(Graphics2D g) {
        GeneralPath casa = new GeneralPath();
        casa.moveTo(12, 2.5);
        casa.lineTo(22, 11.5);
        casa.lineTo(19, 11.5);
        casa.lineTo(19, 21);
        casa.lineTo(5, 21);
        casa.lineTo(5, 11.5);
        casa.lineTo(2, 11.5);
        casa.closePath();
        g.setColor(AZUL);
        g.fill(casa);
        g.setColor(BLANCO);
        g.fill(new RoundRectangle2D.Double(10, 14, 4, 7, 1, 1));
    }

    private static void sesion(Graphics2D g) {
        persona(g, GRIS, 10, 11, 1.1);
        trazo(g, AZUL, 2f);
        linea(g, 15, 16, 22, 16);
        linea(g, 19, 13, 22, 16);
        linea(g, 19, 19, 22, 16);
    }

    // ------------------------------------------------------------ Logotipo

    private static BufferedImage logotipo(int tam) {
        BufferedImage img = new BufferedImage(tam, tam, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = preparar(img);
        double s = tam / 100.0;
        g.scale(s, s);
        g.setColor(MARINO);
        g.fill(new Ellipse2D.Double(2, 2, 96, 96));
        trazo(g, DORADO, 1.6f);
        g.draw(new Ellipse2D.Double(6, 6, 88, 88));
        // Plato
        g.setColor(new Color(0xE9EEF3));
        g.fill(new Ellipse2D.Double(30, 22, 40, 40));
        g.setColor(BLANCO);
        g.fill(new Ellipse2D.Double(36, 28, 28, 28));
        // Tenedor
        trazo(g, BLANCO, 2.6f);
        linea(g, 21, 24, 21, 62);
        trazo(g, BLANCO, 1.6f);
        linea(g, 17.5, 22, 17.5, 33);
        linea(g, 21, 22, 21, 33);
        linea(g, 24.5, 22, 24.5, 33);
        g.draw(new Arc2D.Double(17.5, 28, 7, 8, 180, 180, Arc2D.OPEN));
        // Cuchillo
        GeneralPath hoja = new GeneralPath();
        hoja.moveTo(79, 22);
        hoja.curveTo(85, 30, 84, 40, 79, 42);
        hoja.closePath();
        g.setColor(BLANCO);
        g.fill(hoja);
        trazo(g, BLANCO, 2.6f);
        linea(g, 79, 40, 79, 62);
        // Texto
        g.setColor(BLANCO);
        texto(g, "SISTEMA", 74, 7.5f);
        g.setColor(DORADO);
        texto(g, "RESTAURANT", 84, 9f);
        g.dispose();
        return img;
    }

    /** Escribe un icono .ico de Windows con una imagen PNG de 256 x 256 (formato admitido desde Windows Vista). */
    private static void escribirIco(BufferedImage imagen, File destino) throws IOException {
        java.io.ByteArrayOutputStream png = new java.io.ByteArrayOutputStream();
        ImageIO.write(imagen, "png", png);
        byte[] datos = png.toByteArray();
        java.nio.ByteBuffer cabecera = java.nio.ByteBuffer.allocate(22).order(java.nio.ByteOrder.LITTLE_ENDIAN);
        cabecera.putShort((short) 0).putShort((short) 1).putShort((short) 1);
        cabecera.put((byte) 0).put((byte) 0).put((byte) 0).put((byte) 0);
        cabecera.putShort((short) 1).putShort((short) 32).putInt(datos.length).putInt(22);
        try (java.io.FileOutputStream salida = new java.io.FileOutputStream(destino)) {
            salida.write(cabecera.array());
            salida.write(datos);
        }
    }

    private static void texto(Graphics2D g, String texto, float y, float tam) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 1).deriveFont(tam));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(texto, (float) (50 - fm.getStringBounds(texto, g).getWidth() / 2), y);
    }
}
