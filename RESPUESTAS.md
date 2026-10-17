# Respuestas: Ejercicios de Streams y Method Reference

Cada ejercicio práctico es una clase Java con su propio `main`, dentro de [src/](src/). Las salidas que aparecen aquí se han obtenido ejecutando los programas con JDK 25.

## Cómo compilar y ejecutar

Desde la carpeta `ejercicios-streams` (Git Bash):

```bash
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -Dstdout.encoding=UTF-8 -cp bin methodreference.MethodReferenceEjercicios
java -Dstdout.encoding=UTF-8 -cp bin streams1.Ejercicio1      # Ejercicio1 … Ejercicio5
java -Dstdout.encoding=UTF-8 -cp bin streams2.Ejercicio1      # Ejercicio1 … Ejercicio4
java -Dstdout.encoding=UTF-8 -cp bin streams2.Ejercicio5Hotel # hay que ejecutarlo desde esta carpeta (lee data/reservas.txt)
```

---

## 1. Ejercicios de Method Reference

Programa: [MethodReferenceEjercicios.java](src/methodreference/MethodReferenceEjercicios.java), que declara las dos versiones de cada ejercicio y comprueba que dan el mismo resultado. Clases auxiliares: [Calculadora.java](src/methodreference/Calculadora.java), [Utilidades.java](src/methodreference/Utilidades.java) y [Operacion.java](src/methodreference/Operacion.java). El enunciado usa la interfaz `Operacion` pero no la define, así que se ha creado como `@FunctionalInterface` con el método `int aplicar(int a, int b)`.

| # | Enunciado | Respuesta |
| --- | --- | --- |
| 1 | `Function<String, Integer> convertir = Integer::parseInt;` | `Function<String, Integer> convertir = texto -> Integer.parseInt(texto);` |
| 2 | `Function<String, String> mayusculas = texto -> texto.toUpperCase();` | `Function<String, String> mayusculas = String::toUpperCase;` |
| 3 | `Consumer<String> imprimir = System.out::println;` | `Consumer<String> imprimir = texto -> System.out.println(texto);` |
| 4 | `Operacion suma = (a, b) -> calculadora.sumar(a, b);` | `Operacion suma = calculadora::sumar;` |
| 5 | `Function<String, Integer> longitud = String::length;` | `Function<String, Integer> longitud = texto -> texto.length();` |
| 6 | `Predicate<String> vacio = texto -> texto.isEmpty();` | `Predicate<String> vacio = String::isEmpty;` |
| 7 | `Predicate<Integer> comprobar = numero -> Utilidades.esPar(numero);` | `Predicate<Integer> comprobar = Utilidades::esPar;` |

Tipos de method reference que aparecen:

- **Método estático** (`Clase::metodoEstatico`): `Integer::parseInt` y `Utilidades::esPar`.
- **Método de instancia de un objeto concreto** (`objeto::metodo`): `System.out::println` y `calculadora::sumar`.
- **Método de instancia de un objeto cualquiera de un tipo** (`Clase::metodo`; el parámetro de la lambda es el objeto sobre el que se llama al método): `String::toUpperCase`, `String::length` y `String::isEmpty`.

Salida:

```text
1. convertir("42")      -> ref: 42 | lambda: 42
2. mayusculas("hola")   -> lambda: HOLA | ref: HOLA
3. imprimir (ref)       -> Hola mundo
   imprimir (lambda)    -> Hola mundo
4. suma(3, 4)           -> lambda: 7 | ref: 7
5. longitud("streams")  -> ref: 7 | lambda: 7
6. vacio("") / vacio("a") -> lambda: true/false | ref: true/false
7. comprobar(8) / comprobar(7) -> lambda: true/false | ref: true/false
```

---

## 2. Ejercicios de streams

### Ejercicio 1: ¿Qué hace este código? (teórico)

```java
numeros.stream()
       .filter(n -> n > 2)
       .map(n -> n * 2)
       .peek(n -> System.out.println("tras map: " + n))
       .sorted();
```

**Respuesta:** describe un pipeline que se quedaría con los números mayores que 2 (3, 4, 5, 6), los multiplicaría por 2 (6, 8, 10, 12), imprimiría cada uno con `peek` ("tras map: …") y los ordenaría con `sorted()`.

**Pero al ejecutarlo no hace nada y no imprime nada.** `filter`, `map`, `peek` y `sorted` son **operaciones intermedias**, y los streams son **perezosos**: las operaciones intermedias solo se ejecutan cuando hay una **operación terminal** (`forEach`, `toList`, `collect`, `count`…). Como el código no tiene ninguna, el stream se crea, se descarta y ningún elemento llega a recorrerlo.

Si se añade una operación terminal (por ejemplo `.toList()`), se imprime "tras map: 6/8/10/12" y el resultado es `[6, 8, 10, 12]`. Todos los `peek` salen antes de que haya resultado porque `sorted()` es una operación con estado: necesita recibir todos los elementos antes de ordenarlos y devolverlos.

Demostración: [Ejercicio1.java](src/streams1/Ejercicio1.java)

```text
--- Código del enunciado (sin operación terminal) ---
(no se ha impreso nada: los streams son perezosos)

--- Mismo código añadiendo una operación terminal (.toList()) ---
tras map: 6
tras map: 8
tras map: 10
tras map: 12
Resultado: [6, 8, 10, 12]
```

### Ejercicio 2: Mostrar solo los impares y añadir otro filtro

```java
numeros.stream()
       .filter(n -> n % 2 != 0)
       .forEach(System.out::println);
```

**Para añadir otro filtro** basta con encadenar otro `.filter(...)` después del primero: cada filtro recibe solo los elementos que han pasado el anterior. También se pueden juntar las dos condiciones en un único filtro con `&&`.

```java
numeros.stream()
       .filter(n -> n % 2 != 0)   // impares
       .filter(n -> n > 4)        // y mayores que 4
       .forEach(System.out::println);

// equivalente:
numeros.stream().filter(n -> n % 2 != 0 && n > 4).forEach(System.out::println);
```

Programa: [Ejercicio2.java](src/streams1/Ejercicio2.java)

```text
Impares:
1
3
5
Impares y mayores que 4 (dos filter encadenados):
5
Impares y mayores que 4 (un solo filter con &&):
5
```

### Ejercicio 3: Mostrar la lista en mayúsculas

```java
nombres.stream()
       .map(String::toUpperCase)
       .forEach(System.out::println);
```

Programa: [Ejercicio3.java](src/streams1/Ejercicio3.java)

```text
ANA
LUIS
PEDRO
```

### Ejercicio 4: Nombres con más de 4 letras de un ArrayList inventado

```java
List<String> nombres = new ArrayList<>();
nombres.add("Ana"); nombres.add("Iker"); nombres.add("Marta"); nombres.add("Jon");
nombres.add("Nerea"); nombres.add("Aitor"); nombres.add("Unai"); nombres.add("Leire");

nombres.stream()
       .filter(nombre -> nombre.length() > 4)
       .forEach(System.out::println);
```

Programa: [Ejercicio4.java](src/streams1/Ejercicio4.java)

```text
Marta
Nerea
Aitor
Leire
```

### Ejercicio 5: `filter` y luego `map` no es lo mismo que al revés

Con la lista `[1, 2, 3, 4, 5, 6]` y la misma condición (`n > 3`) y la misma transformación (`n * 2`):

- `filter(n > 3).map(n * 2)`: la condición se aplica a los **valores originales** (4, 5, 6 pasan) y después se duplican → `[8, 10, 12]`.
- `map(n * 2).filter(n > 3)`: primero se duplican todos (2, 4, 6, 8, 10, 12) y la condición se aplica a los **valores ya transformados**, así que solo se descarta el 2 → `[4, 6, 8, 10, 12]`.

Cada operación trabaja sobre lo que le entrega la anterior, así que el orden cambia el resultado. (Cuando los dos órdenes dan lo mismo, conviene filtrar primero para no transformar elementos que luego se descartan.)

Programa: [Ejercicio5.java](src/streams1/Ejercicio5.java)

```text
filter(n > 3).map(n * 2): [8, 10, 12]
map(n * 2).filter(n > 3): [4, 6, 8, 10, 12]
¿Son iguales? false
```

---

## 3. Ejercicios de streams II

### Ejercicio 1: Explica qué hace este código (teórico)

```java
int suma = numeros.stream()
                  .mapToInt(Integer::intValue)
                  .sum();
```

**Respuesta:** calcula la suma de todos los números de la lista `numeros` (una `List<Integer>`) y la guarda en `suma`.

1. `numeros.stream()` crea un `Stream<Integer>` (objetos `Integer`).
2. `.mapToInt(Integer::intValue)` convierte cada `Integer` en su `int` primitivo (desempaquetado; equivale a `n -> n.intValue()`) y devuelve un `IntStream`, un stream especializado en `int`.
3. `.sum()` es la operación terminal: suma todos los valores del `IntStream` y devuelve un `int`. `Stream<Integer>` no tiene `sum()`; `IntStream` sí (y también `average()`, `max()`, etc.).

Si la lista está vacía, el resultado es `0`. Si la lista contiene algún `null`, `intValue()` lanza `NullPointerException`.

Demostración: [Ejercicio1.java](src/streams2/Ejercicio1.java)

```text
Lista: [1, 2, 3, 4, 5, 6] -> suma = 21
Misma suma con un bucle for: 21
Lista vacía -> suma = 0
```

### Ejercicio 2: Nuevo ArrayList de Integer a partir de otro

El enunciado del documento está cortado ("…en el que hayas"), así que se resuelven tres variantes. En todas, `Collectors.toCollection(ArrayList::new)` garantiza que el resultado es un `ArrayList` nuevo y modificable, y la lista original no cambia.

```java
ArrayList<Integer> original = new ArrayList<>(List.of(4, 7, 2, 7, 9, 4, 10, 3));

ArrayList<Integer> duplicados = original.stream().map(n -> n * 2)
        .collect(Collectors.toCollection(ArrayList::new));       // A) multiplicados por 2
ArrayList<Integer> pares = original.stream().filter(n -> n % 2 == 0)
        .collect(Collectors.toCollection(ArrayList::new));       // B) solo pares
ArrayList<Integer> sinRepetidos = original.stream().distinct()
        .collect(Collectors.toCollection(ArrayList::new));       // C) sin repetidos
```

Programa: [Ejercicio2.java](src/streams2/Ejercicio2.java)

```text
Original:                 [4, 7, 2, 7, 9, 4, 10, 3]
A) Multiplicados por 2:   [8, 14, 4, 14, 18, 8, 20, 6]
B) Solo pares:            [4, 2, 4, 10]
C) Sin repetidos:         [4, 7, 2, 9, 10, 3]
A) tras add(100):         [8, 14, 4, 14, 18, 8, 20, 6, 100]
Original (sin cambios):   [4, 7, 2, 7, 9, 4, 10, 3]
```

### Ejercicio 3: ¿Qué ocurre al ejecutar este código? ¿Por qué? (teórico)

**Qué ocurre:** el código funciona sin errores. `flatMap` junta en una sola lista los productos de los tres clientes, el bucle imprime los 6 nombres (Leche, Pan, Huevos, Arroz, Leche, Café) y `todosLosProductos.add(p6)` añade otra vez el Café, de modo que la lista pasa a tener 7 elementos.

**Por qué:** `collect(Collectors.toList())` crea una lista **nueva**, y en la implementación actual del JDK es un `java.util.ArrayList`, que es **modificable**, así que `add` funciona. Ojo: la documentación de `Collectors.toList()` **no garantiza** el tipo ni la mutabilidad de la lista. Si se necesita una lista modificable con seguridad, lo correcto es `Collectors.toCollection(ArrayList::new)`.

**Si se usa la línea comentada `.toList()`** (Java 16+), el resultado es una lista **inmodificable** (`ImmutableCollections$ListN`). El bucle imprimiría lo mismo, pero `add(p6)` lanzaría **`UnsupportedOperationException`** y el programa terminaría con error si no se captura.

Demostración de los dos casos: [Ejercicio3.java](src/streams2/Ejercicio3.java)

```text
--- Código del enunciado: collect(Collectors.toList()) ---
Leche
Pan
Huevos
Arroz
Leche
Café
add(p6) correcto. Tamaño: 7 | clase de la lista: java.util.ArrayList

--- Si se usa la línea comentada: .toList() ---
Clase de la lista: java.util.ImmutableCollections$ListN
add(p6) lanza UnsupportedOperationException: la lista devuelta por toList() es inmodificable
```

### Ejercicio 4: Gestión de compras en un supermercado

Clases del enunciado: [Producto.java](src/streams2/Producto.java) y [Cliente.java](src/streams2/Cliente.java). Programa: [Ejercicio4.java](src/streams2/Ejercicio4.java). Se resuelve con `flatMap` y sin bucles `for`.

```java
// 1. Todos los productos en una sola lista
List<Producto> todosLosProductos = clientes.stream()
        .flatMap(c -> c.getProductos().stream())
        .toList();
// 2. Nombre de cada producto
todosLosProductos.stream().map(Producto::getNombre).forEach(System.out::println);
// 3. Productos de más de 2 €
todosLosProductos.stream().filter(p -> p.getPrecio() > 2).forEach(System.out::println);
// 4. Importe total
double total = todosLosProductos.stream().mapToDouble(Producto::getPrecio).sum();
```

Salida:

```text
1. Todos los productos: [Leche - 1.2 €, Pan - 0.9 €, Huevos - 2.5 €, Arroz - 1.8 €, Leche - 1.2 €, Café - 3.5 €]
2. Nombres:
   Leche
   Pan
   Huevos
   Arroz
   Leche
   Café
3. Productos de más de 2 €:
   Huevos - 2.5 €
   Café - 3.5 €
4. Importe total: 11,10 €
```

### Ejercicio 5: Análisis de reservas de un hotel

- Datos: [data/reservas.txt](data/reservas.txt), copiado tal cual del enunciado (12 reservas).
- Modelo: [Reserva.java](src/streams2/Reserva.java), un `record` con `desdeLinea(String)` para convertir cada línea e `importe() = noches * precioNoche`. Si el campo servicios vale `ninguno`, se guarda una lista vacía.
- Programa: [Ejercicio5Hotel.java](src/streams2/Ejercicio5Hotel.java)

| # | Técnica utilizada | Resultado |
| --- | --- | --- |
| 1 | `try (Stream<String> lineas = Files.lines(...))`: el try-with-resources cierra el fichero | 12 reservas leídas |
| 2 | `filter(r -> r.importe() > 300)` | reservas 1, 4, 5, 7, 10 y 11 |
| 3 | `filter(ciudad == Bilbao).map(Reserva::cliente).toList()` | [Ana, Ane, Marta, Aitor, Iker] |
| 4 | `filter(noches >= 3).map(cliente).sorted().toList()` | [Ana, Ane, Laura, Maite, Marta, Nerea] |
| 5 | `mapToDouble(Reserva::importe).sum()` | 3870,00 € |
| 6 | `mapToDouble(Reserva::importe).average()` | 322,50 € |
| 7 | `groupingBy(tipoHabitacion, counting())` | Doble 4, Individual 4, Suite 4 |
| 8 | `groupingBy(ciudadOrigen, summingDouble(importe))` | Bilbao 1730, Donostia 785, Madrid 605, Vitoria 750 |
| 9 | `max(Comparator.comparingDouble(Reserva::importe))` | reserva 11 (Maite, 720 €) |
| 10 | `count()` de las que superan 300 / total × 100 | 6 de 12 = 50 % |
| 11 | `flatMap(servicios).distinct().sorted()` | [desayuno, parking, spa] |
| 12 | `flatMap(servicios)` + `groupingBy(identity(), counting())` | desayuno 6, parking 4, spa 4 |
| 13 | `partitioningBy(r -> r.importe() > 300)` | 6 superan / 6 no superan |
| 14 | `sorted(comparingDouble(importe).reversed()).limit(3)` | 720 € (11), 540 € (1), 475 € (5) |
| 15 | `peek` → `filter` → `peek` | se ve qué reservas entran al filtro y cuáles lo superan |
| 16 | `Files.lines` → `filter` → `forEach(escribir)` con un `BufferedWriter`, sin lista intermedia | [data/reservas_caras.txt](data/reservas_caras.txt) con 6 líneas |

Notas:

- En los apartados 7, 8 y 12 se usa `TreeMap::new` para que los resultados salgan ordenados alfabéticamente.
- En el 11, "ninguno" no aparece porque esas reservas tienen la lista de servicios vacía.
- En el 15, los mensajes salen intercalados: cada reserva recorre el pipeline completo antes de que entre la siguiente. Las que no superan el filtro solo muestran el mensaje "antes".
- En el 16, `write()` lanza `IOException` (excepción comprobada), que no se puede propagar desde la lambda de `forEach`. Por eso se envuelve en `UncheckedIOException` dentro del método `escribirLinea`.

Salida completa:

```text
=== 1. Reservas leídas del fichero (12) ===
Reserva  1 | Ana   | Bilbao   | Suite      | 3 noches x 180,00 € = 540,00 € | servicios: desayuno, spa
Reserva  2 | Pedro | Madrid   | Doble      | 2 noches x 95,00 € = 190,00 € | servicios: desayuno
Reserva  3 | Ane   | Bilbao   | Individual | 4 noches x 65,00 € = 260,00 € | servicios: ninguno
Reserva  4 | Luis  | Vitoria  | Suite      | 2 noches x 180,00 € = 360,00 € | servicios: spa, parking
Reserva  5 | Marta | Bilbao   | Doble      | 5 noches x 95,00 € = 475,00 € | servicios: desayuno, parking
Reserva  6 | Jon   | Donostia | Individual | 1 noches x 65,00 € = 65,00 € | servicios: ninguno
Reserva  7 | Aitor | Bilbao   | Suite      | 2 noches x 180,00 € = 360,00 € | servicios: desayuno, spa, parking
Reserva  8 | Laura | Madrid   | Doble      | 3 noches x 95,00 € = 285,00 € | servicios: parking
Reserva  9 | Iker  | Bilbao   | Doble      | 1 noches x 95,00 € = 95,00 € | servicios: desayuno
Reserva 10 | Nerea | Vitoria  | Individual | 6 noches x 65,00 € = 390,00 € | servicios: spa
Reserva 11 | Maite | Donostia | Suite      | 4 noches x 180,00 € = 720,00 € | servicios: desayuno
Reserva 12 | Unai  | Madrid   | Individual | 2 noches x 65,00 € = 130,00 € | servicios: ninguno

=== 2. Reservas con importe > 300 € ===
Reserva  1 | Ana   | Bilbao   | Suite      | 3 noches x 180,00 € = 540,00 € | servicios: desayuno, spa
Reserva  4 | Luis  | Vitoria  | Suite      | 2 noches x 180,00 € = 360,00 € | servicios: spa, parking
Reserva  5 | Marta | Bilbao   | Doble      | 5 noches x 95,00 € = 475,00 € | servicios: desayuno, parking
Reserva  7 | Aitor | Bilbao   | Suite      | 2 noches x 180,00 € = 360,00 € | servicios: desayuno, spa, parking
Reserva 10 | Nerea | Vitoria  | Individual | 6 noches x 65,00 € = 390,00 € | servicios: spa
Reserva 11 | Maite | Donostia | Suite      | 4 noches x 180,00 € = 720,00 € | servicios: desayuno

=== 3. Clientes de Bilbao ===
[Ana, Ane, Marta, Aitor, Iker]

=== 4. Clientes con 3 o más noches (orden alfabético) ===
[Ana, Ane, Laura, Maite, Marta, Nerea]

=== 5. Facturación total ===
3870,00 €

=== 6. Importe medio por reserva ===
322,50 €

=== 7. Reservas por tipo de habitación ===
Doble: 4
Individual: 4
Suite: 4

=== 8. Facturación por ciudad de origen ===
Bilbao: 1730,00 €
Donostia: 785,00 €
Madrid: 605,00 €
Vitoria: 750,00 €

=== 9. Reserva de mayor importe ===
Reserva 11 | Maite | Donostia | Suite      | 4 noches x 180,00 € = 720,00 € | servicios: desayuno

=== 10. Porcentaje de reservas > 300 € ===
6 de 12 reservas = 50,00 %

=== 11. Servicios contratados (sin duplicados, ordenados) ===
[desayuno, parking, spa]

=== 12. Reservas que incluyen cada servicio ===
desayuno: 6
parking: 4
spa: 4

=== 13. Reservas separadas en dos grupos ===
Superan los 300 € (6):
   Reserva  1 | Ana   | Bilbao   | Suite      | 3 noches x 180,00 € = 540,00 € | servicios: desayuno, spa
   Reserva  4 | Luis  | Vitoria  | Suite      | 2 noches x 180,00 € = 360,00 € | servicios: spa, parking
   Reserva  5 | Marta | Bilbao   | Doble      | 5 noches x 95,00 € = 475,00 € | servicios: desayuno, parking
   Reserva  7 | Aitor | Bilbao   | Suite      | 2 noches x 180,00 € = 360,00 € | servicios: desayuno, spa, parking
   Reserva 10 | Nerea | Vitoria  | Individual | 6 noches x 65,00 € = 390,00 € | servicios: spa
   Reserva 11 | Maite | Donostia | Suite      | 4 noches x 180,00 € = 720,00 € | servicios: desayuno
No superan los 300 € (6):
   Reserva  2 | Pedro | Madrid   | Doble      | 2 noches x 95,00 € = 190,00 € | servicios: desayuno
   Reserva  3 | Ane   | Bilbao   | Individual | 4 noches x 65,00 € = 260,00 € | servicios: ninguno
   Reserva  6 | Jon   | Donostia | Individual | 1 noches x 65,00 € = 65,00 € | servicios: ninguno
   Reserva  8 | Laura | Madrid   | Doble      | 3 noches x 95,00 € = 285,00 € | servicios: parking
   Reserva  9 | Iker  | Bilbao   | Doble      | 1 noches x 95,00 € = 95,00 € | servicios: desayuno
   Reserva 12 | Unai  | Madrid   | Individual | 2 noches x 65,00 € = 130,00 € | servicios: ninguno

=== 14. Las 3 reservas de mayor importe ===
Reserva 11 | Maite | Donostia | Suite      | 4 noches x 180,00 € = 720,00 € | servicios: desayuno
Reserva  1 | Ana   | Bilbao   | Suite      | 3 noches x 180,00 € = 540,00 € | servicios: desayuno, spa
Reserva  5 | Marta | Bilbao   | Doble      | 5 noches x 95,00 € = 475,00 € | servicios: desayuno, parking

=== 15. peek() antes y después de filter(importe > 300) ===
   antes del filtro:   reserva  1 (540,00 €)
   después del filtro: reserva  1 (540,00 €)
   antes del filtro:   reserva  2 (190,00 €)
   antes del filtro:   reserva  3 (260,00 €)
   antes del filtro:   reserva  4 (360,00 €)
   después del filtro: reserva  4 (360,00 €)
   antes del filtro:   reserva  5 (475,00 €)
   después del filtro: reserva  5 (475,00 €)
   antes del filtro:   reserva  6 (65,00 €)
   antes del filtro:   reserva  7 (360,00 €)
   después del filtro: reserva  7 (360,00 €)
   antes del filtro:   reserva  8 (285,00 €)
   antes del filtro:   reserva  9 (95,00 €)
   antes del filtro:   reserva 10 (390,00 €)
   después del filtro: reserva 10 (390,00 €)
   antes del filtro:   reserva 11 (720,00 €)
   después del filtro: reserva 11 (720,00 €)
   antes del filtro:   reserva 12 (130,00 €)
Han pasado el filtro 6 reservas

=== 16. Fichero data\reservas_caras.txt generado ===
   1;Ana;Bilbao;Suite;3;180.00;desayuno,spa
   4;Luis;Vitoria;Suite;2;180.00;spa,parking
   5;Marta;Bilbao;Doble;5;95.00;desayuno,parking
   7;Aitor;Bilbao;Suite;2;180.00;desayuno,spa,parking
   10;Nerea;Vitoria;Individual;6;65.00;spa
   11;Maite;Donostia;Suite;4;180.00;desayuno
```
