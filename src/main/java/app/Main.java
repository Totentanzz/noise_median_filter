package app;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import moe.leer.codeflowcore.CodeFlow;
import moe.leer.codeflowcore.exception.CodeFlowException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File chosenImage = new File("src/main/resources/RGB_tucan.jpeg");
        if (chosenImage.exists() && chosenImage.getName().split("\\.")[1].matches("jpg|jpeg|png|bmp")) {
            System.out.println("\nChosen file: " + chosenImage);
            BufferedImage loadedImage = ImageHandler.loadImage(chosenImage.getAbsolutePath());
            BufferedImage grayImage = ImageHandler.convertToGrayScale(loadedImage);
            BufferedImage noisedImage = ImageHandler.applyImpulseNoise(grayImage,0.5);
            BufferedImage filteredImage = ImageHandler.applyMedianFilter(noisedImage,5,1,3);
            ImageHandler.saveImage(filteredImage,"src/main/resources/FILTERED_05_5x1x3_tucan.jpg");
        }
    }
}