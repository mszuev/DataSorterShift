package ru.mzuev.datasorter;

import ru.mzuev.datasorter.io.DataWriter;
import ru.mzuev.datasorter.processor.TypeDetector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AppSettings settings = new AppSettings();
        List<String> inputFiles = new ArrayList<>();

        parseArguments(args, settings, inputFiles);

        if (inputFiles.isEmpty()) {
            printUsage();
            return;
        }

        try (
                DataWriter intWriter = new DataWriter(
                        settings.getFullPath("integers.txt").toString(),
                        settings.isAppendMode()
                );
                DataWriter floatWriter = new DataWriter(
                        settings.getFullPath("floats.txt").toString(),
                        settings.isAppendMode()
                );
                DataWriter stringWriter = new DataWriter(
                        settings.getFullPath("strings.txt").toString(),
                        settings.isAppendMode()
                )
        ) {
            for (String filename : inputFiles) {
                processFile(filename, intWriter, floatWriter, stringWriter);
            }
            System.out.println("Processing completed successfully");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void parseArguments(String[] args, AppSettings settings, List<String> inputFiles) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 < args.length) {
                        settings.setOutputPath(args[++i]);
                    }
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        settings.setFilePrefix(args[++i]);
                    }
                    break;
                case "-a":
                    settings.setAppendMode(true);
                    break;
                default:
                    if (!args[i].startsWith("-")) {
                        inputFiles.add(args[i]);
                    } else {
                        System.err.println("Warning: Unknown option " + args[i]);
                    }
            }
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar DataSorterShift.jar [options] file1 [file2 ...]");
        System.out.println("Options:");
        System.out.println("  -o <outputPath>   Set output directory");
        System.out.println("  -p <prefix>       Set prefix for output files");
        System.out.println("  -a                Append to existing files");
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