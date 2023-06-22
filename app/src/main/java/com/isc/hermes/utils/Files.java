package com.isc.hermes.utils;

import android.os.Environment;

import java.io.File;

public class Files {

    private static final String DIRECTORY_PATH = "/data/user/0/com.isc.hermes/files/";
    private String urlFile;
    private String nameDirectory;
    private File urlDirectory;
    private File directory;

    public Files(String nameDirectory) {
        this.nameDirectory = nameDirectory;
        this.urlFile = DIRECTORY_PATH + this.nameDirectory;
        this.urlDirectory = new File(this.urlFile);
        this.directory = new File(Environment.getExternalStorageDirectory(), nameDirectory);
    }

    private boolean fileExists(File file) {
        return file.exists();
    }

    public void saveFile() {
        createDirectory();
    }

    private void createDirectory() {
        if (!fileExists(directory)) {
            directory.mkdirs();
        }
    }

}
