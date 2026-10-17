package streams1;

import java.util.Arrays;
import java.util.List;

/**
 * Ejercicio 1: ¿qué hace este código?
 * El pipeline del enunciado no tiene operación terminal, así que no se ejecuta nada.
 */
public class Ejercicio1 {

    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);

        System.out.println("--- Código del enunciado (sin operación terminal) ---");
        numeros.stream()
                .filter(n -> n > 2)
                .map(n -> n * 2)
                .peek(n -> System.out.println("tras map: " + n))
                .sorted();
        System.out.println("(no se ha impreso nada: los streams son perezosos)");

        System.out.println();
        System.out.println("--- Mismo código añadiendo una operación terminal (.toList()) ---");
        List<Integer> resultado = numeros.stream()
                .filter(n -> n > 2)
                .map(n -> n * 2)
                .peek(n -> System.out.println("tras map: " + n))
                .sorted()
                .toList();
        System.out.println("Resultado: " + resultado);
    }
}
