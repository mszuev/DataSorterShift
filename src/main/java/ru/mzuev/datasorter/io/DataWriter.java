package ru.mzuev.datasorter.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataWriter implements AutoCloseable {
    private BufferedWriter writer;
    private final String filePath;
    private boolean isActive = true;

    public DataWriter(String filePath) {
        this.filePath = filePath;
    }

    public void write(String data) throws IOException {
        if (!isActive || data == null || data.isEmpty()) return;

        try {
            if (writer == null) {
                createParentDirectories();
                writer = new BufferedWriter(new FileWriter(filePath));
            }
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            isActive = false;
            throw new IOException("Error writing to file '" + filePath + "': " + e.getMessage(), e);
        }
    }

    private void createParentDirectories() throws IOException {
        Path parentPath = Paths.get(filePath).getParent();
        if (parentPath != null && !Files.exists(parentPath)) {
            Files.createDirectories(parentPath);
        }
    }

    @Override
    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Error closing file writer: " + e.getMessage());
            }
        }
    }
}