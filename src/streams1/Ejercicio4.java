package streams1;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejercicio 4: mostrar los nombres con más de 4 letras de un ArrayList inventado.
 */
public class Ejercicio4 {

    public static void main(String[] args) {
        List<String> nombres = new ArrayList<>();
        nombres.add("Ana");
        nombres.add("Iker");
        nombres.add("Marta");
        nombres.add("Jon");
        nombres.add("Nerea");
        nombres.add("Aitor");
        nombres.add("Unai");
        nombres.add("Leire");

        nombres.stream()
                .filter(nombre -> nombre.length() > 4)
                .forEach(System.out::println);
    }
}
