package ru.mzuev.datasorter;

import ru.mzuev.datasorter.io.DataWriter;
import ru.mzuev.datasorter.processor.TypeDetector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar DataSorterShift.jar file1 [file2 ...]");
            return;
        }

        try (
                DataWriter intWriter = new DataWriter("integers.txt");
                DataWriter floatWriter = new DataWriter("floats.txt");
                DataWriter stringWriter = new DataWriter("strings.txt")
        ) {
            for (String filename : args) {
                processFile(filename, intWriter, floatWriter, stringWriter);
            }
            System.out.println("Processing completed successfully");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void processFile(String filename,
                                    DataWriter intWriter,
                                    DataWriter floatWriter,
                                    DataWriter stringWriter)
            throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (line.trim().isEmpty()) continue;

                if (TypeDetector.isInteger(line)) {
                    intWriter.write(line);
                } else if (TypeDetector.isDecimal(line)) {
                    floatWriter.write(line);
                } else {
                    stringWriter.write(line);
                }
            }
            System.out.println("Processed " + lineCount + " lines from " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file " + filename + ": " + e.getMessage());
        }
    }
}