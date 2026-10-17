package streams2;

import java.util.List;

/**
 * Una línea del fichero reservas.txt:
 * id;cliente;ciudadOrigen;tipoHabitacion;noches;precioNoche;servicios
 * Si el campo servicios vale "ninguno", la lista de servicios queda vacía.
 */
public record Reserva(int id, String cliente, String ciudadOrigen, String tipoHabitacion,
                      int noches, double precioNoche, List<String> servicios) {

    public static Reserva desdeLinea(String linea) {
        String[] campos = linea.trim().split(";");
        List<String> servicios = campos[6].equals("ninguno")
                ? List.of()
                : List.of(campos[6].split(","));
        return new Reserva(
                Integer.parseInt(campos[0]),
                campos[1],
                campos[2],
                campos[3],
                Integer.parseInt(campos[4]),
                Double.parseDouble(campos[5]),
                servicios);
    }

    public double importe() {
        return noches * precioNoche;
    }

    @Override
    public String toString() {
        return String.format("Reserva %2d | %-5s | %-8s | %-10s | %d noches x %.2f € = %.2f € | servicios: %s",
                id, cliente, ciudadOrigen, tipoHabitacion, noches, precioNoche, importe(),
                servicios.isEmpty() ? "ninguno" : String.join(", ", servicios));
    }
}
