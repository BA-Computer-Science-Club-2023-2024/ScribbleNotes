package com.veryrandomcreator.scribblenotes;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;

public class Note {
    private String title;
    private String content;
    private String fileName;

    public static String DELIMITER = "\n";

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Note(String fileName, Context context) {
        this.fileName = fileName;
        File[] storageFiles = context.getFilesDir().listFiles();
        if (storageFiles == null) {
            System.err.println("Error while loading storage");
            return;
        }
        for (File file : storageFiles) {
            if (file.getName().equals(fileName)) {
                loadFromFile(file);
                return;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    private void loadFromFile(File noteFile) {
        try {
            String fileContent = new String(Files.readAllBytes(noteFile.toPath())); // Not very efficient and performance-safe (in case the file is very large, but fulfills the need)
            if (fileContent.isEmpty()) {
                System.err.println("File is empty");
                return;
            }
            if (!fileContent.contains(DELIMITER)) {
                this.title = fileContent;
                return;
            }
            String title = fileContent.split(DELIMITER)[0];
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < fileContent.split(DELIMITER).length; i++) {
                if (i != 0) {
                    content.append(fileContent.split(DELIMITER)[i]);
                }
            }
            this.title = title;
            this.content = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveToFile(Context context) {
        try {
            if (fileName == null) {
                fileName = generateUniqueFileName();
            }
            File saveFile = new File(context.getFilesDir(), fileName);
            saveFile.createNewFile();

            FileWriter noteFileWriter = new FileWriter(saveFile);
            noteFileWriter.append(title);
            noteFileWriter.append(DELIMITER);
            noteFileWriter.append(content);
            noteFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateUniqueFileName() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
    }

    public void deleteFile(Context context) {
        if (fileName != null) {
            context.deleteFile(fileName);
        }
    }
}
