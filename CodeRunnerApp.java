import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class CodeRunnerApp {

    private static JFrame frame;
    private static JButton[] buttons = new JButton[5];

    public static void main(String[] args) {
        // Crearea ferestrei
        frame = new JFrame("Code Runner");
        frame.setLayout(new GridLayout(6, 1)); // 5 butoane + un loc pentru ieșire
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crearea butoanelor
        for (int i = 0; i < 5; i++) {
            buttons[i] = new JButton("Problema " + (i + 1));
            final int index = i;
            buttons[i].addActionListener(e -> runCode(index));
            frame.add(buttons[i]);
        }

        // Crearea unui TextArea pentru a afișa rezultatele
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.add(scrollPane);

        // Vizualizarea ferestrei
        frame.setVisible(true);
    }

    private static void runCode(int problemIndex) {
        String filePath = "problema" + (problemIndex + 1) + ".cpp"; // Căutăm fișierul C++ corespunzător
        String output = runCppCode(filePath);

        // Afișarea rezultatelor
        JTextArea outputArea = (JTextArea) ((JScrollPane) frame.getContentPane().getComponent(5)).getViewport().getView();
        outputArea.setText("Rezultatul pentru Problema " + (problemIndex + 1) + ":\n" + output);
    }

    private static String runCppCode(String filePath) {
        // Compilarea codului C++
        String output = "";
        try {
            // 1. Creăm un proces pentru a compila C++
            ProcessBuilder compileProcess = new ProcessBuilder("g++", filePath, "-o", "output");
            compileProcess.redirectErrorStream(true);
            Process compile = compileProcess.start();
            compile.waitFor();

            // 2. Dacă compilarea a fost reușită, rulăm programul C++
            ProcessBuilder runProcess = new ProcessBuilder("./output");
            runProcess.redirectErrorStream(true);
            Process run = runProcess.start();
            run.waitFor();

            // 3. Citim rezultatul din stdout
            BufferedReader reader = new BufferedReader(new InputStreamReader(run.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output += line + "\n";
            }
        } catch (IOException | InterruptedException e) {
            output = "Eroare la rularea codului: " + e.getMessage();
        }
        return output;
    }
}
