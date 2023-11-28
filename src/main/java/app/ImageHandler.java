package app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ImageHandler {

    public static BufferedImage loadImage(String filePath) {
        BufferedImage loadedImage = null;
        try {
            loadedImage = ImageIO.read(new File(filePath));
            System.out.println("Chosen image was loaded into memory");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        return loadedImage;
    }

    public static void saveImage(BufferedImage image, String filePath) {
        try {
            File outputFile = new File(filePath);
            if (!outputFile.exists()) {
                System.out.println("File doesn't exists, creating new file");
                new FileOutputStream(outputFile, false).close();
            }
            boolean hasWritten = ImageIO.write(image, "jpg", outputFile);
            System.out.println("Image was saved into: " + filePath);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public static BufferedImage createGrayScaleImage(BufferedImage rgbImage) {
        int imageWidth = rgbImage.getWidth();
        int imageHeight = rgbImage.getHeight();
        BufferedImage grayImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics grayImageGraphics = grayImage.getGraphics();
        grayImageGraphics.drawImage(rgbImage, 0, 0, null);
        grayImageGraphics.dispose();
        return grayImage;
    }

    public static BufferedImage convertToGrayScale(BufferedImage rgbImage) {
        int imageWidth = rgbImage.getWidth();
        int imageHeight = rgbImage.getHeight();
        for (int x = 0; x < imageWidth; ++x) {
            for (int y = 0; y < imageHeight; ++y) {
                int pixelRGB = rgbImage.getRGB(x, y);
                double red = ((pixelRGB >> 16) & 0xFF) / 255.0;
                double green = ((pixelRGB >> 8) & 0xFF) / 255.0;
                double blue = (pixelRGB & 0xFF) / 255.0;
                double linearRed = Math.pow((red + 0.055) / 1.055, 2.4);
                double linearGreen = Math.pow((green + 0.055) / 1.055, 2.4);
                double linearBlue = Math.pow((blue + 0.055) / 1.055, 2.4);
                double luminance = 0.2126 * linearRed + 0.7152 * linearGreen + 0.0722 * linearBlue;
                int gray =  (int) (255 * (1.055 * Math.pow(luminance, 1.0 / 2.4) - 0.055));
                pixelRGB = (gray<<16) | (gray << 8) | gray;
                rgbImage.setRGB(x, y, pixelRGB);
            }
        }
        return rgbImage;
    }

    public static BufferedImage applyImpulseNoise(BufferedImage image, double noiseRatio) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        Random random = ThreadLocalRandom.current();
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (random.nextDouble() < noiseRatio) {
                    int noiseColor = random.nextBoolean() ? 0 : 255;
                    int noiseRGB = (noiseColor << 16) | (noiseColor << 8) | noiseColor;
                    image.setRGB(x, y, noiseRGB);
                }
            }
        }
        return image;
    }

    public static BufferedImage applyMedianFilter(BufferedImage noisedImage, int filterHeight, int filterWidth, int iterations) {
        int imageWidth = noisedImage.getWidth();
        int imageHeight = noisedImage.getHeight();
        int filterHalfWidth = filterWidth / 2;
        int filterHalfHeight = filterHeight / 2;
        BufferedImage filteredImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
        for (int iteration = 0; iteration < iterations; iteration++) {
            for (int y = filterHalfHeight; y < imageHeight - filterHalfHeight; y++) {
                for (int x = filterHalfWidth; x < imageWidth - filterHalfWidth; x++) {
                    int medianColor = calculateMedianColor(noisedImage, x, y, filterHeight, filterWidth);
                    filteredImage.setRGB(x, y, medianColor);
                }
            }
        }
        return filteredImage;
    }

    public static int calculateMedianColor(BufferedImage noisedImage, int x, int y, int filterHeight, int filterWidth) {
        int[] filterWindow = new int[filterHeight * filterWidth];
        int index = 0, xCoord = 0, yCoord = 0;
        for (int dy = 0; dy < filterHeight; dy++) {
            for (int dx = 0; dx < filterWidth; dx++) {
                xCoord = x + dx - filterWidth/2;
                yCoord = y + dy - filterHeight/2;
                filterWindow[index++] = noisedImage.getRGB(xCoord, yCoord);
            }
        }
        Arrays.sort(filterWindow);
        return filterWindow[filterWindow.length / 2];
    }

    public static void displayImage(BufferedImage image, String title, int width, int height) {
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(scaledImage);
        JFrame frame = new JFrame(title);
        JLabel label = new JLabel(icon);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(label);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
