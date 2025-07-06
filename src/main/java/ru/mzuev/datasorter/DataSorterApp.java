package ru.mzuev.datasorter;

import ru.mzuev.datasorter.io.DataWriter;
import ru.mzuev.datasorter.processor.TypeDetector;
import ru.mzuev.datasorter.stats.StatisticsCollector;
import ru.mzuev.datasorter.stats.StatMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSorterApp {

    public static void main(String[] args) {
        AppSettings settings = new AppSettings();
        List<String> inputFiles = new ArrayList<>();
        StatisticsCollector statsCollector = new StatisticsCollector();

        parseArguments(args, settings, inputFiles, statsCollector);

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
                processFile(
                        filename,
                        intWriter,
                        floatWriter,
                        stringWriter,
                        statsCollector
                );
            }

            // Вывод статистики
            StatMode mode = statsCollector.getStatMode();
            if (mode == StatMode.SHORT) {
                statsCollector.printShortStatistics();
            } else if (mode == StatMode.FULL) {
                statsCollector.printFullStatistics();
            }

            System.out.println("Processing completed successfully");
        }
    }

    private static void parseArguments(String[] args,
                                       AppSettings settings,
                                       List<String> inputFiles,
                                       StatisticsCollector stats) {
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            switch (a) {
                case "-o" -> settings.setOutputPath(nextArg(args, ++i));
                case "-p" -> settings.setFilePrefix(nextArg(args, ++i));
                case "-a" -> settings.setAppendMode(true);
                case "-s" -> stats.setStatMode(StatMode.SHORT);
                case "-f" -> stats.setStatMode(StatMode.FULL);
                default -> {
                    if (a.startsWith("-")) {
                        System.err.println("Warning: Unknown option " + a);
                    } else {
                        inputFiles.add(a);
                    }
                }
            }
        }
    }

    private static String nextArg(String[] args, int i) {
        return (i < args.length) ? args[i] : "";
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar DataSorterShift.jar [options] file1 [file2 ...]");
        System.out.println("Options:");
        System.out.println("  -o <outputPath>   Set output directory");
        System.out.println("  -p <prefix>       Set prefix for output files");
        System.out.println("  -a                Append to existing files");
        System.out.println("  -s                Show short statistics");
        System.out.println("  -f                Show full statistics");
    }

    private static void processFile(String filename,
                                    DataWriter intWriter,
                                    DataWriter floatWriter,
                                    DataWriter stringWriter,
                                    StatisticsCollector statsCollector) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (TypeDetector.isInteger(line)) {
                    intWriter.write(line);
                    statsCollector.addInteger(line);
                } else if (TypeDetector.isDecimal(line)) {
                    floatWriter.write(line);
                    statsCollector.addFloat(line);
                } else {
                    stringWriter.write(line);
                    statsCollector.addString(line);
                }
            }
        } catch (IOException e) {
            System.err.println("I/O error in " + filename + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error in " + filename + ": " + e);
        }
    }
}