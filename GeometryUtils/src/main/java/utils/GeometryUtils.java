package utils;

public class GeometryUtils {
    // Преобразование из сантиметров в метры
    public static double cmToMeters(double cm) {
        return cm / 100;
    }

    // Сравнение двух кругов по радиусам
    public static boolean areCirclesEqual(double radius1, double radius2) {
        return radius1 == radius2;
    }
}

