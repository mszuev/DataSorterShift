package ru.mzuev.datasorter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppSettings {
    private String outputPath = ".";
    private String filePrefix = "";
    private boolean appendMode = false;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath != null ? outputPath : ".";
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix != null ? filePrefix : "";
    }

    public boolean isAppendMode() {
        return appendMode;
    }

    public void setAppendMode(boolean appendMode) {
        this.appendMode = appendMode;
    }

    public Path getFullPath(String filename) {
        return Paths.get(outputPath, filePrefix + filename);
    }
}