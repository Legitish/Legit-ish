package legitish.utils;

import legitish.module.modulesettings.ModuleDoubleSliderSetting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtils {
    private static final Random rand = new Random();

    public static double round(double value, int places) {
        if (places < 0) {
            return 0;
        }
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static void b(ModuleDoubleSliderSetting a) {
        if (a.getInputMin() > a.getInputMax()) {
            double p = a.getInputMin();
            a.setValueMin(a.getInputMax());
            a.setValueMax(p);
        }
    }

    public static double mmVal(ModuleDoubleSliderSetting a, Random r) {
        return a.getInputMin() == a.getInputMax() ? a.getInputMin() : a.getInputMin() + r.nextDouble() * (a.getInputMax() - a.getInputMin());
    }

    public static Random rand() {
        return rand;
    }

    public static int randomInt(double inputMin, double v) {
        return (int) ((Math.random() * (v - inputMin)) + inputMin);
    }
}
