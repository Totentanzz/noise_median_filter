package app;


import javax.swing.*;
import java.io.File;

public class FileExplorer {
    private static JFileChooser fileChooser;

    static {
        setUIStyle();
        fileChooser = new JFileChooser();

    }
    public static File openFile() {
        File chosenImage = null;
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            chosenImage = fileChooser.getSelectedFile();
        }
        return chosenImage;
    }

    private static void setUIStyle() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException exc) {
            exc.printStackTrace();
        } catch (InstantiationException exc) {
            exc.printStackTrace();
        } catch (IllegalAccessException exc) {
            exc.printStackTrace();
        } catch (UnsupportedLookAndFeelException exc) {
            exc.printStackTrace();
        }
    }
}
