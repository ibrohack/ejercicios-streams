package streams2;

import java.util.Arrays;
import java.util.List;

/**
 * Ejercicio 1: explicar qué hace mapToInt(Integer::intValue).sum().
 */
public class Ejercicio1 {

    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);

        // Stream<Integer> -> IntStream (desempaqueta cada Integer a int) -> suma de todos
        int suma = numeros.stream()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("Lista: " + numeros + " -> suma = " + suma);

        // Equivalente con un bucle tradicional
        int sumaBucle = 0;
        for (Integer numero : numeros) {
            sumaBucle += numero;
        }
        System.out.println("Misma suma con un bucle for: " + sumaBucle);

        // Con una lista vacía, sum() devuelve 0
        List<Integer> vacia = List.of();
        System.out.println("Lista vacía -> suma = " + vacia.stream().mapToInt(Integer::intValue).sum());
    }
}
