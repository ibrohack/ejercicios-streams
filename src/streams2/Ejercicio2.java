package streams2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ejercicio 2: obtener un nuevo ArrayList de Integer a partir de otro.
 * El enunciado está incompleto ("en el que hayas ..."), así que se resuelven tres variantes.
 */
public class Ejercicio2 {

    public static void main(String[] args) {
        ArrayList<Integer> original = new ArrayList<>(List.of(4, 7, 2, 7, 9, 4, 10, 3));
        System.out.println("Original:                 " + original);

        // Variante A: cada elemento multiplicado por 2
        ArrayList<Integer> duplicados = original.stream()
                .map(n -> n * 2)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("A) Multiplicados por 2:   " + duplicados);

        // Variante B: solo los números pares
        ArrayList<Integer> pares = original.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("B) Solo pares:            " + pares);

        // Variante C: sin elementos repetidos
        ArrayList<Integer> sinRepetidos = original.stream()
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("C) Sin repetidos:         " + sinRepetidos);

        // Las listas nuevas son ArrayList modificables y la original no cambia
        duplicados.add(100);
        System.out.println("A) tras add(100):         " + duplicados);
        System.out.println("Original (sin cambios):   " + original);
    }
}
