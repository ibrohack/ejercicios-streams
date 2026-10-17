package streams1;

import java.util.Arrays;
import java.util.List;

/**
 * Ejercicio 2: mostrar solo los impares y añadir un segundo filtro.
 */
public class Ejercicio2 {

    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);

        System.out.println("Impares:");
        numeros.stream()
                .filter(n -> n % 2 != 0)
                .forEach(System.out::println);

        System.out.println("Impares y mayores que 4 (dos filter encadenados):");
        numeros.stream()
                .filter(n -> n % 2 != 0)
                .filter(n -> n > 4)
                .forEach(System.out::println);

        System.out.println("Impares y mayores que 4 (un solo filter con &&):");
        numeros.stream()
                .filter(n -> n % 2 != 0 && n > 4)
                .forEach(System.out::println);
    }
}
