package restaurante.ui.util;

import java.awt.Image;
import java.awt.image.BaseMultiResolutionImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Iconos de la aplicación (src/main/resources/iconos), generados con
 * herramientas/GeneradorIconos.java.
 *
 * <p>Cada icono tiene una versión normal y otra @2x: Java elige la adecuada
 * según el escalado de la pantalla, por lo que se ven nítidos al 125 % o 150 %.</p>
 */
public final class Iconos {

    private static final Map<String, Icon> CACHE = new ConcurrentHashMap<>();

    private Iconos() {
    }

    public static Icon icono(String nombre) {
        return CACHE.computeIfAbsent(nombre, Iconos::cargar);
    }

    private static Icon cargar(String nombre) {
        List<Image> variantes = new ArrayList<>();
        for (String sufijo : new String[] {"", "@2x"}) {
            URL url = Iconos.class.getResource("/iconos/" + nombre + sufijo + ".png");
            if (url != null) {
                variantes.add(new ImageIcon(url).getImage());
            }
        }
        if (variantes.isEmpty()) {
            throw new IllegalArgumentException("Icono inexistente: " + nombre);
        }
        return new ImageIcon(new BaseMultiResolutionImage(variantes.toArray(new Image[0])));
    }
}
