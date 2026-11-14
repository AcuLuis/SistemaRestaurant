package restaurante.util;

import restaurante.datos.Jdbc;

public class Util {

    /**
     * Siguiente identificador de la tabla, solo para mostrarlo en pantalla.
     * El identificador definitivo se asigna al guardar, dentro de una transacción.
     */
    public int idNext(String nombTbl, String nombCamp) {
        return Jdbc.siguienteIdSugerido(nombTbl, nombCamp);
    }
}
