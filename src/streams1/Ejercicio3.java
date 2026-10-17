package streams1;

import java.util.Arrays;
import java.util.List;

/**
 * Ejercicio 3: mostrar la lista en mayúsculas.
 */
public class Ejercicio3 {

    public static void main(String[] args) {
        List<String> nombres = Arrays.asList(
                "Ana",
                "Luis",
                "Pedro");

        nombres.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}
