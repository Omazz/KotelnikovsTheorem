import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Number3 {
    private final double F = 10; // частота
    private final double T = 10 / F; // период
    private final double[] f = { 1.5f, 1.75f, 2, 3, 1000}; // массив коэфф-в с частотами
    private final double Fd = 1000; // частота дискретизации
    private final double dt = T / Fd; // шаг между дискретными отсчётами во временной области

    public static void main(String[] args) throws IOException {
        Number3 number3 = new Number3();
        number3.working();
    }

    public void working() throws IOException {
        for (int i = 0; i < f.length; ++i) {
            double Td = T / (f[i] * F);
            ArrayList<Double> function = calculateFunction(F, T, Td);
            ArrayList<Double> restoredSignal = calculateOriginalSignal(function, Td);
            loadToFile("graphic" + i, restoredSignal);
        }
        ArrayList<Double> originalSignal = calculateFunction(F, T, dt);
        loadToFile("originalSignal", originalSignal);
    }

    public ArrayList<Double> calculateFunction(double F, double T, double dt) {
        ArrayList<Double> function = new ArrayList<>();
        double size = T / dt;
        double t = 0;
        for (int i = 0; i < size; ++i, t += dt) {
            function.add(Math.sin(2 * Math.PI * F * t));
        }
        return function;
    }

    public void loadToFile(String name, ArrayList<Double> function) throws IOException {
        File file = new File(name + ".txt");
        FileWriter fileWriter = new FileWriter(file);
        double t = 0;
        for (int i = 0; i < function.size(); ++i, t += dt) {
            String tmp = function.get(i).toString();
            String buff = t + "     " + tmp + "\n";
            fileWriter.write(buff);
            fileWriter.flush();
        }
    }

// применение интерполяционного ряда Котельникова для восстановления исходного сигнала
    public ArrayList<Double> calculateOriginalSignal(ArrayList<Double> function, double Td) {
        ArrayList<Double> restoredSignal = new ArrayList<>();
        double t = 0;
        for (int i = 0; i < T / dt; ++i) {
            double tmp = 0;
            for (int n = 0; n < function.size(); ++n) {
                tmp += function.get(n) * calculateSinc(Math.PI * ((t / Td) - n));
            }
            t += dt;
            restoredSignal.add(tmp);
        }
        return restoredSignal;
    }

    public double calculateSinc(double x) {
        if (x != 0) {
            return (Math.sin(x) / x);
        } else {
            return 1;
        }
    }
}
