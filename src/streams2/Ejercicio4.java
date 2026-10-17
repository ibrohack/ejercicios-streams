package streams2;

import java.util.Arrays;
import java.util.List;

/**
 * Ejercicio 4: gestión de compras en un supermercado (flatMap, sin bucles for).
 */
public class Ejercicio4 {

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

        // 1. Todos los productos en una única lista (aplanando las listas de cada cliente)
        List<Producto> todosLosProductos = clientes.stream()
                .flatMap(c -> c.getProductos().stream())
                .toList();
        System.out.println("1. Todos los productos: " + todosLosProductos);

        // 2. Nombre de cada producto
        System.out.println("2. Nombres:");
        todosLosProductos.stream()
                .map(Producto::getNombre)
                .forEach(nombre -> System.out.println("   " + nombre));

        // 3. Productos con precio superior a 2 €
        System.out.println("3. Productos de más de 2 €:");
        todosLosProductos.stream()
                .filter(p -> p.getPrecio() > 2)
                .forEach(p -> System.out.println("   " + p));

        // 4. Importe total de todas las compras
        double total = todosLosProductos.stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
        System.out.printf("4. Importe total: %.2f €%n", total);
    }
}
