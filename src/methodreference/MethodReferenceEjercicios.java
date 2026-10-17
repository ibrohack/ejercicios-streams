package methodreference;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Ejercicios de Method Reference: conversiones entre lambdas y method references.
 * En cada ejercicio se declaran las dos versiones y se comprueba que dan el mismo resultado.
 */
public class MethodReferenceEjercicios {

    public static void main(String[] args) {

        // 1. De method reference a lambda
        Function<String, Integer> convertirRef = Integer::parseInt;
        Function<String, Integer> convertir = texto -> Integer.parseInt(texto);
        System.out.println("1. convertir(\"42\")      -> ref: " + convertirRef.apply("42")
                + " | lambda: " + convertir.apply("42"));

        // 2. De lambda a method reference
        Function<String, String> mayusculasLambda = texto -> texto.toUpperCase();
        Function<String, String> mayusculas = String::toUpperCase;
        System.out.println("2. mayusculas(\"hola\")   -> lambda: " + mayusculasLambda.apply("hola")
                + " | ref: " + mayusculas.apply("hola"));

        // 3. De method reference a lambda
        Consumer<String> imprimirRef = System.out::println;
        Consumer<String> imprimir = texto -> System.out.println(texto);
        System.out.print("3. imprimir (ref)       -> ");
        imprimirRef.accept("Hola mundo");
        System.out.print("   imprimir (lambda)    -> ");
        imprimir.accept("Hola mundo");

        // 4. De lambda a method reference (método de instancia de un objeto concreto)
        Calculadora calculadora = new Calculadora();
        Operacion sumaLambda = (a, b) -> calculadora.sumar(a, b);
        Operacion suma = calculadora::sumar;
        System.out.println("4. suma(3, 4)           -> lambda: " + sumaLambda.aplicar(3, 4)
                + " | ref: " + suma.aplicar(3, 4));

        // 5. De method reference a lambda
        Function<String, Integer> longitudRef = String::length;
        Function<String, Integer> longitud = texto -> texto.length();
        System.out.println("5. longitud(\"streams\")  -> ref: " + longitudRef.apply("streams")
                + " | lambda: " + longitud.apply("streams"));

        // 6. De lambda a method reference
        Predicate<String> vacioLambda = texto -> texto.isEmpty();
        Predicate<String> vacio = String::isEmpty;
        System.out.println("6. vacio(\"\") / vacio(\"a\") -> lambda: " + vacioLambda.test("") + "/" + vacioLambda.test("a")
                + " | ref: " + vacio.test("") + "/" + vacio.test("a"));

        // 7. De lambda a method reference (método estático)
        Predicate<Integer> comprobarLambda = numero -> Utilidades.esPar(numero);
        Predicate<Integer> comprobar = Utilidades::esPar;
        System.out.println("7. comprobar(8) / comprobar(7) -> lambda: " + comprobarLambda.test(8) + "/" + comprobarLambda.test(7)
                + " | ref: " + comprobar.test(8) + "/" + comprobar.test(7));
    }
}
