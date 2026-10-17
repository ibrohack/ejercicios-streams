package streams1;

import java.util.Arrays;
import java.util.List;

/**
 * Ejercicio 5: no es lo mismo hacer filter y luego map que al revés.
 */
public class Ejercicio5 {

    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);

        // Primero filtra los originales mayores que 3 (4, 5, 6) y luego los duplica
        List<Integer> filtrarYMapear = numeros.stream()
                .filter(n -> n > 3)
                .map(n -> n * 2)
                .toList();

        // Primero duplica todos (2, 4, 6, 8, 10, 12) y luego filtra los mayores que 3
        List<Integer> mapearYFiltrar = numeros.stream()
                .map(n -> n * 2)
                .filter(n -> n > 3)
                .toList();

        System.out.println("filter(n > 3).map(n * 2): " + filtrarYMapear);
        System.out.println("map(n * 2).filter(n > 3): " + mapearYFiltrar);
        System.out.println("¿Son iguales? " + filtrarYMapear.equals(mapearYFiltrar));
    }
}
