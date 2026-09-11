package avancecurricular.ui.console;

import java.util.Scanner;

public class LectorConsola {
    private static final Scanner scanner = new Scanner(System.in);

    public static String leerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Debe ingresar un número entero válido.");
            }
        }
    }

    public static double leerDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Debe ingresar un valor numérico válido (use punto para decimales).");
            }
        }
    }
}