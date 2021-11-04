import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Number2 {
    private final ArrayList<Double> u1 = new ArrayList<>(); // вектор первого сигнала
    private final ArrayList<Double> u2 = new ArrayList<>(); // вектор второго сигнала
    private int f1 = 1, f2 = 2; // частоты первого и второго сигнала
    private final double dt = 0.001d; // шаг по времени

    public static void main(String[] args) throws IOException {
        Number2 number2 = new Number2();
        Scanner scanner = new Scanner(System.in);
        double a = scanner.nextDouble(); // a и b - границы, на которых сигналы заданы
        double b = scanner.nextDouble();

        number2.buildFunctionGraphs(a, b);
        number2.loadToFile();
        System.out.println("\n2.1 Graphics are built.");
        System.out.println("\n2.2 Scalar: " + number2.calculateScalar(a, b));
        System.out.println("\n2.3 Norm of u1(t): " + number2.calculateNormFunc(a, b, number2.getF1()));
        System.out.println("Norm of u2(t): " + number2.calculateNormFunc(a, b, number2.getF2()));

        if ((int)(number2.calculateScalar(a, b)) == 0 &&
                (int)number2.calculateNormFunc(a, b, number2.getF1()) == 1 &&
                (int)number2.calculateNormFunc(a, b, number2.getF2()) == 1) {
            System.out.println("\n2.4 These functions are orthogonal");
        } else {
            System.out.println("\n2.4 These functions are not orthogonal");
        }

        double normU1 = number2.calculateNormFunc(a, b, number2.getF1());
        double normU2 = number2.calculateNormFunc(a, b, number2.getF2());
        ArrayList<Double> newU1 = new ArrayList<>();
        ArrayList<Double> newU2 = new ArrayList<>();
        number2.doOrthonormBasis(newU1, newU2, normU1, normU2);
        System.out.println("\n2.5 Norm of new u1(t): " + number2.calculateNormFunc(a, b, newU1));
        System.out.println("Norm of new u2(t): " + number2.calculateNormFunc(a, b, newU2));
        System.out.println("Scalar: " + number2.calculateScalar(newU1, newU2, a, b));

        System.out.println("\n2.6");
        number2.setF1(number2.getF1() * 2);
        number2.setF2(number2.getF2() * 2);
        if ((int)(number2.calculateScalar(a, b)) == 0) {
            System.out.println("\n2.6.1 These functions are orthogonal");
        } else {
            System.out.println("\n2.6.1 These functions are not orthogonal");
        }
        System.out.println("\nNorm of new u1(t): " + number2.calculateNormFunc(a, b, newU1));
        System.out.println("Norm of new u2(t): " + number2.calculateNormFunc(a, b, newU2));
        number2.setF1(number2.getF1() / 2);
        number2.setF2(number2.getF2() / 2);
        //number2.setT(2 * number2.getT());
        a *= 2;
        b *= 2;
        if ((int)(number2.calculateScalar(a, b)) == 0) {
            System.out.println("\n2.6.2 These functions are orthogonal");
        } else {
            System.out.println("\n2.6.2 These functions are not orthogonal");
        }
        newU1.clear();
        newU2.clear();
        number2.buildFunctionGraphs(newU1, newU2, a, b);
        for (int i = 0; i < newU1.size(); ++i) {
            newU1.set(i, newU1.get(i) / normU1);
            newU2.set(i, newU2.get(i) / normU2);
        }
        System.out.println("\nNorm of new u1(t): " + number2.calculateNormFunc(a , b , newU1));
        System.out.println("Norm of new u2(t): " + number2.calculateNormFunc(a , b , newU2));

        newU1.clear();
        newU2.clear();
        a /= 4;
        b /= 4;
        number2.buildFunctionGraphs(newU1, newU2, a, b);
        if ((int)(number2.calculateScalar(newU1, newU2, a, b)) == 0) {
            System.out.println("\n2.6.3 These functions are orthogonal");
        } else {
            System.out.println("\n2.6.3 These functions are not orthogonal");
        }
        newU1.clear();
        newU2.clear();
        number2.buildFunctionGraphs(newU1, newU2, a, b);
        for (int i = 0; i < newU1.size(); ++i) {
            newU1.set(i, newU1.get(i) / normU1);
            newU2.set(i, newU2.get(i) / normU2);
        }
        System.out.println("\nNorm of new u1(t): " + number2.calculateNormFunc(a , b, newU1));
        System.out.println("Norm of new u2(t): " + number2.calculateNormFunc(a, b, newU2));
    }

    public void loadToFile() throws IOException {
        File file = new File("graphicU1.txt");
        File file1 = new File("graphicU2.txt");
        FileWriter fileWriter = new FileWriter(file);
        FileWriter fileWriter1 = new FileWriter(file1);
        double t = 0;
        for (int i = 0; i < u1.size(); ++i) {
            String tmp = u1.get(i).toString();
            String tmp1 = u2.get(i).toString();
            fileWriter.write(t + "     " + tmp + "\n");
            fileWriter1.write(t + "    " + tmp1 + "\n");
            fileWriter.flush();
            fileWriter1.flush();
            t += dt;
        }
    }

    public int getF1() {
        return f1;
    }

    public int getF2() {
        return f2;
    }

    public void setF1(int f1) {
        this.f1 = f1;
    }

    public void setF2(int f2) {
        this.f2 = f2;
    }

    public void doOrthonormBasis(ArrayList<Double> newU1, ArrayList<Double> newU2, double normU1, double normU2) {
        for (int i = 0; i < u1.size(); ++i) {
            newU1.add(u1.get(i) / normU1);
            newU2.add(u2.get(i) / normU2);
        }
    }

    public void buildFunctionGraphs(double a, double b) {
        for (double t = a; t < b; t += dt ) {
            u1.add(calculateFunction(f1, t));
            u2.add(calculateFunction(f2, t));
        }
    }

    public void buildFunctionGraphs(ArrayList<Double> newU1, ArrayList<Double> newU2, double a, double b) {
        for (double t = a; t < b; t += dt ) {
            newU1.add(calculateFunction(f1, t));
            newU2.add(calculateFunction(f2, t));
        }
    }

    public double calculateFunction(double f, double t) {
        return Math.sin(2 * Math.PI * f * t);
    }
    
    public double calculateScalar(double a, double b) {
        return (1 / (b - a)) * integral( a, b);
    }

    public double calculateScalar(ArrayList<Double> function1, ArrayList<Double> function2, double a, double b) {
        return (1 / (b - a)) * integral(a, b, function1, function2);
    }

    public double calculateNormFunc(double a, double b, double f) {
        double area = 0;
        for (double t = 0; t < ((b - a) / dt); ++t) {
            area += calculateFunction(f, a + t * dt) * calculateFunction(f, a + t * dt);
        }
        return Math.sqrt( area * dt / ( b - a) );
    }

    public double calculateNormFunc(double a, double b, ArrayList<Double> function) {
        double area = 0;
        for (int i = 0; i < function.size(); ++i) {
            area += function.get(i) * function.get(i);
        }
        return Math.sqrt( area * dt / (b - a) );
    }

    public double integral(double a, double b) { // считаем интеграл методом прямоугольника
        double area = 0;
        for (double t = 0; t < ((b - a) / dt); ++t) {
            area += dt * (calculateFunction(f1, a + t * dt) * calculateFunction(f2, a + t * dt));
        }
        return area;
    }
    public double integral(double a, double b, ArrayList<Double> function1, ArrayList<Double> function2) {
        double area = 0;
        for (double t = 0; t < ((b - a) / dt); ++t) {
            area += dt * (function1.get((int)t) * function2.get((int)t));
        }
        return area;
    }
}
