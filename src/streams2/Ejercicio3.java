package streams2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ejercicio 3: ¿qué ocurre al ejecutar este código? ¿Por qué?
 * Se compara collect(Collectors.toList()) (el del enunciado) con .toList() (la línea comentada).
 */
public class Ejercicio3 {

    public static void main(String[] args) {
        Producto p1 = new Producto("Leche", 1.20);
        Producto p2 = new Producto("Pan", 0.90);
        Producto p3 = new Producto("Huevos", 2.50);
        Producto p4 = new Producto("Arroz", 1.80);
        Producto p5 = new Producto("Leche", 1.20);
        Producto p6 = new Producto("Café", 3.50);

        Cliente c1 = new Cliente("Ana", Arrays.asList(p1, p2));
        Cliente c2 = new Cliente("Luis", Arrays.asList(p3, p4, p5));
        Cliente c3 = new Cliente("Marta", Arrays.asList(p6));
        List<Cliente> clientes = Arrays.asList(c1, c2, c3);

        System.out.println("--- Código del enunciado: collect(Collectors.toList()) ---");
        List<Producto> todosLosProductos = clientes.stream()
                .flatMap(c -> c.getProductos().stream())
                // .toList();
                .collect(Collectors.toList());
        for (int contador = 0; contador < todosLosProductos.size(); contador++)
            System.out.println(todosLosProductos.get(contador).getNombre());
        todosLosProductos.add(p6);
        System.out.println("add(p6) correcto. Tamaño: " + todosLosProductos.size()
                + " | clase de la lista: " + todosLosProductos.getClass().getName());

        System.out.println();
        System.out.println("--- Si se usa la línea comentada: .toList() ---");
        List<Producto> inmodificable = clientes.stream()
                .flatMap(c -> c.getProductos().stream())
                .toList();
        System.out.println("Clase de la lista: " + inmodificable.getClass().getName());
        try {
            inmodificable.add(p6);
        } catch (UnsupportedOperationException e) {
            System.out.println("add(p6) lanza " + e.getClass().getSimpleName()
                    + ": la lista devuelta por toList() es inmodificable");
        }
    }
}
