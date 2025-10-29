package funciones;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

public class FuncionApp {

    public static long generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(999999 - 111111 + 1) + 111111;
    }

    public static LocalDate generateRandomDate() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 24);
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);
        return startDate.plusDays(randomDays);
    }


    public static String generateRandomCUIT() {
        // Generar el prefijo (tipo de persona: 20, 27, 30)
        int[] possiblePrefixes = {20, 27, 30};
        int prefix = possiblePrefixes[ThreadLocalRandom.current().nextInt(0, possiblePrefixes.length)];

        // Generar el número base de 8 dígitos (que simula el DNI o identificación)
        long dni = ThreadLocalRandom.current().nextLong(10000000L, 99999999L);

        // Concatenar el prefijo y el número base
        String baseCuit = String.format("%02d%d", prefix, dni);

        // Calcular el dígito verificador
        int verificador = calculateCheckDigit(baseCuit);

        // Retornar el CUIT completo con el dígito verificador
        return baseCuit + verificador;
    }

    private static int calculateCheckDigit(String baseCuit) {
        int[] coefficients = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2}; // Coeficientes para el cálculo del dígito verificador
        int sum = 0;

        // Aplicar el algoritmo a los primeros 10 dígitos
        for (int i = 0; i < baseCuit.length(); i++) {
            sum += Character.getNumericValue(baseCuit.charAt(i)) * coefficients[i];
        }

        // Calcular el dígito verificador
        int remainder = sum % 11;
        int checkDigit = 11 - remainder;

        if (checkDigit == 11) {
            return 0;
        } else if (checkDigit == 10) {
            return 9;
        } else {
            return checkDigit;
        }
    }

    public static String formatLocalDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static String getFormatMilDecimal(double valor, int decimal) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);

        formatter.setMaximumFractionDigits(decimal);
        formatter.setMinimumFractionDigits(decimal);

        return formatter.format(valor);
    }

    public static LocalDate restarSeisMeses(LocalDate fecha) {
        return fecha.minusMonths(6);
    }

    public static String formatCuitConGuiones(String cuitSinGuiones) {
        if (cuitSinGuiones != null && cuitSinGuiones.length() == 11)
            return cuitSinGuiones.substring(0, 2) + "-" + cuitSinGuiones.substring(2, 10) + "-"
                    + cuitSinGuiones.substring(10, 11);

        return cuitSinGuiones;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }

    // Nuevo método: genera un código único corto para usar como 'codigo' en artículos
    public static String generateUniqueCode() {
        // Usar UUID y tomar los primeros 8 caracteres (sin guiones) para un identificador compacto
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

}
