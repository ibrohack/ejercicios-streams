package streams2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Ejercicio 5: análisis de reservas de un hotel a partir de reservas.txt.
 * Se ejecuta desde la raíz del proyecto: los ficheros se leen y escriben en el directorio data/.
 */
public class Ejercicio5Hotel {

    private static final Path DIRECTORIO_DATOS = Path.of("data");
    private static final Path FICHERO_RESERVAS = DIRECTORIO_DATOS.resolve("reservas.txt");
    private static final Path FICHERO_CARAS = DIRECTORIO_DATOS.resolve("reservas_caras.txt");
    private static final double LIMITE = 300;

    public static void main(String[] args) throws IOException {

        // ===================== Bloque 1. Lectura y filtrado =====================

        // 1. Leer con Files.lines(); try-with-resources cierra el flujo al terminar
        List<Reserva> reservas;
        try (Stream<String> lineas = Files.lines(FICHERO_RESERVAS)) {
            reservas = lineas
                    .filter(linea -> !linea.isBlank())
                    .map(Reserva::desdeLinea)
                    .toList();
        }
        titulo("1. Reservas leídas del fichero (" + reservas.size() + ")");
        reservas.forEach(System.out::println);

        // 2. Reservas con importe superior a 300 €
        titulo("2. Reservas con importe > 300 €");
        reservas.stream()
                .filter(r -> r.importe() > LIMITE)
                .forEach(System.out::println);

        // 3. Nombres de los clientes que proceden de Bilbao
        List<String> clientesBilbao = reservas.stream()
                .filter(r -> r.ciudadOrigen().equals("Bilbao"))
                .map(Reserva::cliente)
                .toList();
        titulo("3. Clientes de Bilbao");
        System.out.println(clientesBilbao);

        // 4. Clientes con al menos tres noches, ordenados alfabéticamente
        List<String> clientesTresNoches = reservas.stream()
                .filter(r -> r.noches() >= 3)
                .map(Reserva::cliente)
                .sorted()
                .toList();
        titulo("4. Clientes con 3 o más noches (orden alfabético)");
        System.out.println(clientesTresNoches);

        // ===================== Bloque 2. Cálculos =====================

        // 5. Facturación total
        double facturacionTotal = reservas.stream()
                .mapToDouble(Reserva::importe)
                .sum();
        titulo("5. Facturación total");
        System.out.printf("%.2f €%n", facturacionTotal);

        // 6. Importe medio
        double importeMedio = reservas.stream()
                .mapToDouble(Reserva::importe)
                .average()
                .orElse(0);
        titulo("6. Importe medio por reserva");
        System.out.printf("%.2f €%n", importeMedio);

        // 7. Número de reservas por tipo de habitación
        Map<String, Long> reservasPorTipo = reservas.stream()
                .collect(Collectors.groupingBy(Reserva::tipoHabitacion, TreeMap::new, Collectors.counting()));
        titulo("7. Reservas por tipo de habitación");
        reservasPorTipo.forEach((tipo, total) -> System.out.println(tipo + ": " + total));

        // 8. Facturación por ciudad de origen
        Map<String, Double> facturacionPorCiudad = reservas.stream()
                .collect(Collectors.groupingBy(Reserva::ciudadOrigen, TreeMap::new,
                        Collectors.summingDouble(Reserva::importe)));
        titulo("8. Facturación por ciudad de origen");
        facturacionPorCiudad.forEach((ciudad, total) -> System.out.printf("%s: %.2f €%n", ciudad, total));

        // 9. Reserva de mayor importe
        Optional<Reserva> reservaMayor = reservas.stream()
                .max(Comparator.comparingDouble(Reserva::importe));
        titulo("9. Reserva de mayor importe");
        reservaMayor.ifPresent(System.out::println);

        // 10. Porcentaje de reservas que superan los 300 €
        long numeroCaras = reservas.stream()
                .filter(r -> r.importe() > LIMITE)
                .count();
        double porcentaje = numeroCaras * 100.0 / reservas.size();
        titulo("10. Porcentaje de reservas > 300 €");
        System.out.printf("%d de %d reservas = %.2f %%%n", numeroCaras, reservas.size(), porcentaje);

        // ===================== Bloque 3. Operaciones avanzadas =====================

        // 11. Servicios sin duplicados y ordenados. Las reservas con "ninguno" tienen
        //     la lista de servicios vacía (ver Reserva.desdeLinea), así que no aparecen.
        List<String> serviciosContratados = reservas.stream()
                .flatMap(r -> r.servicios().stream())
                .distinct()
                .sorted()
                .toList();
        titulo("11. Servicios contratados (sin duplicados, ordenados)");
        System.out.println(serviciosContratados);

        // 12. Número de reservas que incluyen cada servicio
        Map<String, Long> reservasPorServicio = reservas.stream()
                .flatMap(r -> r.servicios().stream())
                .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
        titulo("12. Reservas que incluyen cada servicio");
        reservasPorServicio.forEach((servicio, total) -> System.out.println(servicio + ": " + total));

        // 13. Separar en dos grupos: > 300 € y <= 300 €
        Map<Boolean, List<Reserva>> particion = reservas.stream()
                .collect(Collectors.partitioningBy(r -> r.importe() > LIMITE));
        titulo("13. Reservas separadas en dos grupos");
        System.out.println("Superan los 300 € (" + particion.get(true).size() + "):");
        particion.get(true).forEach(r -> System.out.println("   " + r));
        System.out.println("No superan los 300 € (" + particion.get(false).size() + "):");
        particion.get(false).forEach(r -> System.out.println("   " + r));

        // 14. Las tres reservas de mayor importe, de mayor a menor
        titulo("14. Las 3 reservas de mayor importe");
        reservas.stream()
                .sorted(Comparator.comparingDouble(Reserva::importe).reversed())
                .limit(3)
                .forEach(System.out::println);

        // 15. peek() antes y después del filtro
        titulo("15. peek() antes y después de filter(importe > 300)");
        List<Reserva> pasanFiltro = reservas.stream()
                .peek(r -> System.out.printf("   antes del filtro:   reserva %2d (%.2f €)%n", r.id(), r.importe()))
                .filter(r -> r.importe() > LIMITE)
                .peek(r -> System.out.printf("   después del filtro: reserva %2d (%.2f €)%n", r.id(), r.importe()))
                .toList();
        System.out.println("Han pasado el filtro " + pasanFiltro.size() + " reservas");

        // ===================== Bloque 4. Escritura en fichero =====================

        // 16. Escribir las reservas de más de 300 € en reservas_caras.txt, una por línea,
        //     directamente desde el stream (sin lista intermedia)
        try (Stream<String> lineas = Files.lines(FICHERO_RESERVAS);
             BufferedWriter escritor = Files.newBufferedWriter(FICHERO_CARAS)) {
            lineas.filter(linea -> !linea.isBlank())
                    .filter(linea -> Reserva.desdeLinea(linea).importe() > LIMITE)
                    .forEach(linea -> escribirLinea(escritor, linea));
        }
        titulo("16. Fichero " + FICHERO_CARAS + " generado");
        try (Stream<String> lineas = Files.lines(FICHERO_CARAS)) {
            lineas.forEach(linea -> System.out.println("   " + linea));
        }
    }

    // write() lanza IOException (excepción comprobada), que no se puede propagar desde la lambda de forEach
    private static void escribirLinea(BufferedWriter escritor, String linea) {
        try {
            escritor.write(linea);
            escritor.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}
