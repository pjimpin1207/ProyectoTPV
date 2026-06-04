package controlador;

import java.util.HashMap;
import java.util.Map;
import modelo.Mesa;

public class GestorMesas {
    private static GestorMesas instancia;
    private Map<Integer, Mesa> mapaMesas;

    private GestorMesas() {
        mapaMesas = new HashMap<>();
        for (int i = 1; i <= 11; i++) {
            mapaMesas.put(i, new Mesa(i));
        }
    }

    public static GestorMesas getInstancia() {
        if (instancia == null) {
            instancia = new GestorMesas();
        }
        return instancia;
    }

    public Mesa getMesa(int numero) {
        return mapaMesas.get(numero);
    }
}