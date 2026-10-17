# Ejercicios de Streams y Method Reference en Java

Soluciones de los ejercicios de la API **Stream** y los **method references** de Java (2º DAM, Optativa de Programación). Cada ejercicio práctico es un programa independiente con su propio `main`. Las respuestas teóricas y las salidas de cada programa están en **[RESPUESTAS.md](RESPUESTAS.md)**.

Los enunciados originales están en el directorio [docs/](docs/):

| Documento | Contenido |
| --- | --- |
| [Ejercicios de Method Reference.docx](docs/Ejercicios%20de%20Method%20Reference.docx) | 7 conversiones entre lambda y method reference |
| [Ejercicios de streams.docx](docs/Ejercicios%20de%20streams.docx) | 5 ejercicios: `filter`, `map`, `peek`, evaluación perezosa |
| [Ejercicios de streams II.docx](docs/Ejercicios%20de%20streams%20II.docx) | 5 ejercicios: `mapToInt`, colectores, `flatMap` y análisis de un fichero con `Files.lines` |

## Requisitos

- **JDK 17 o superior.** El código usa `record` y `Stream.toList()`, que requieren Java 16+. Se ha probado con Temurin 25.
- No se necesita Maven, Gradle ni ninguna librería externa.

## Inicio rápido

Todos los comandos se ejecutan desde la raíz del proyecto (`ejercicios-streams`). El ejercicio del hotel lee `data/reservas.txt` y escribe `data/reservas_caras.txt` con rutas relativas, así que debe ejecutarse desde la raíz.

**1. Compilar** (las clases se generan en `bin/`):

```powershell
# PowerShell
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
```

```bash
# Git Bash / Linux / macOS
javac -encoding UTF-8 -d bin $(find src -name "*.java")
```

**2. Ejecutar un ejercicio:**

```bash
java -Dstdout.encoding=UTF-8 -cp bin streams2.Ejercicio5Hotel
```

`-Dstdout.encoding=UTF-8` sirve para que `€` y las tildes se vean bien en la consola de Windows. En PowerShell hay que ponerlo entre comillas: `"-Dstdout.encoding=UTF-8"`.

Desde **VS Code** (con el Extension Pack for Java) o **IntelliJ** también se puede abrir cualquier clase y pulsar *Run* sobre su `main`, siempre que el directorio de trabajo sea la raíz del proyecto.

## Ejercicios y clases

| Ejercicio | Clase a ejecutar | Qué trabaja |
| --- | --- | --- |
| Method Reference 1–7 | `methodreference.MethodReferenceEjercicios` | Lambda ↔ method reference (estático, de instancia y de tipo) |
| Streams 1 | `streams1.Ejercicio1` | Operaciones intermedias sin operación terminal (evaluación perezosa) |
| Streams 2 | `streams1.Ejercicio2` | `filter` simple y encadenado |
| Streams 3 | `streams1.Ejercicio3` | `map(String::toUpperCase)` |
| Streams 4 | `streams1.Ejercicio4` | Filtrar por longitud de cadena |
| Streams 5 | `streams1.Ejercicio5` | El orden de `filter` y `map` cambia el resultado |
| Streams II 1 | `streams2.Ejercicio1` | `mapToInt(...).sum()` |
| Streams II 2 | `streams2.Ejercicio2` | Nuevo `ArrayList` con `Collectors.toCollection` |
| Streams II 3 | `streams2.Ejercicio3` | `Collectors.toList()` (modificable) frente a `toList()` (inmodificable) |
| Streams II 4 | `streams2.Ejercicio4` | Supermercado: `flatMap`, `filter`, `mapToDouble().sum()` |
| Streams II 5 | `streams2.Ejercicio5Hotel` | Hotel: `Files.lines`, `groupingBy`, `partitioningBy`, `peek`, escritura en fichero |

## Estructura del proyecto

```text
ejercicios-streams/
├── README.md
├── RESPUESTAS.md            Respuestas teóricas, código clave y salidas de cada ejercicio
├── docs/                    Enunciados originales (.docx)
├── data/
│   ├── reservas.txt         Datos de entrada del ejercicio del hotel
│   └── reservas_caras.txt   Salida del ejercicio del hotel (se regenera al ejecutarlo)
├── src/
│   ├── methodreference/     MethodReferenceEjercicios + Calculadora, Operacion, Utilidades
│   ├── streams1/            Ejercicio1 … Ejercicio5
│   └── streams2/            Ejercicio1 … Ejercicio4, Ejercicio5Hotel
│                            + Producto, Cliente (del enunciado) y Reserva (record)
└── bin/                     Clases compiladas (se puede borrar; se regenera con javac)
```

## Notas sobre las soluciones

- **Streams II, ejercicio 2:** el enunciado del documento está cortado ("…en el que hayas"), así que se resuelven tres variantes: multiplicar por 2, quedarse con los pares y eliminar los repetidos.
- **Method Reference, ejercicio 4:** el enunciado usa la interfaz `Operacion` sin definirla. Se ha creado en [Operacion.java](src/methodreference/Operacion.java) como interfaz funcional con `int aplicar(int a, int b)`.
- **Ejercicio del hotel:** las reservas con servicios `ninguno` se guardan con la lista de servicios vacía (ver [Reserva.java](src/streams2/Reserva.java)), por eso "ninguno" no aparece en los apartados de servicios.
