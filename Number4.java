import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Number4 {
    private final int width, height; // ширина и высота изображения
    private final BufferedImage bufferedImage; //изображение, с которым работаем

    public static void main(String[] args) throws IOException {
        Number4 number4 = new Number4();
        number4.working(0.5, 0.5);
        number4.working(0.5, 1.5);
        number4.working(1.5, 0.5);
        number4.working(1.5, 1.5);
    }

    public Number4() throws IOException {
        File file = new File("image.bmp");
        bufferedImage = ImageIO.read(file);
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
    }

    // реализация, завязанная на использовании интерполяционного ряда Котельникова
    public void working(double n, double m) throws IOException {
        double[][] tmpRed = new double[width][];
        double[][] tmpGreen = new double[width][];
        double[][] tmpBlue = new double[width][];

        int resWidth = (int) (width * n);
        int resHeight = (int) (height * m);
        BufferedImage result = new BufferedImage(resWidth, resHeight, bufferedImage.getType());

        for (int x = 0; x < width; ++x) {
            tmpRed[x] = new double[resHeight];
            tmpGreen[x] = new double[resHeight];
            tmpBlue[x] = new double[resHeight];
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 1; y <= resHeight; ++y) {
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int k = 1; k <= height; ++k) {
                    Color color = new Color(bufferedImage.getRGB(x, k - 1));
                    double sinc = calculateSinc(Math.PI * (y / m - k));
                    red += color.getRed() * sinc;
                    green += color.getGreen() * sinc;
                    blue += color.getBlue() * sinc;
                }
                tmpRed[x][y - 1] = red;
                tmpGreen[x][y - 1] = green;
                tmpBlue[x][y - 1] = blue;
            }
        }

        for (int x = 1; x <= resWidth; ++x) {
            for (int y = 0; y < resHeight; ++y) {
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int k = 1; k <= width; ++k) {
                    double sinc = calculateSinc(Math.PI * (x / n - k));
                    red += tmpRed[k - 1][y] * sinc;
                    green += tmpGreen[k - 1][y] * sinc;
                    blue += tmpBlue[k - 1][y] * sinc;
                }

                Color color = new Color(correctColor(red), correctColor(green), correctColor(blue));
                result.setRGB(x - 1, y, color.getRGB());
            }
        }

        File output = new File("newImageN" + n + "M" + m + ".bmp");
        ImageIO.write(result, "bmp", output);
    }

    public int correctColor(double color) {
        int maxColor = 256; // RGB принимает значения от 0 до 255
        if (color >= maxColor) {
            return (int) (color - maxColor);
        }
        if (color < 0) {
            return (int) (-color);
        }
        return (int) color;
    }

    public double calculateSinc(double x) {
        if (x != 0) {
            return (Math.sin(x) / x);
        } else {
            return 1;
        }
    }
}