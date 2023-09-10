package com.veryrandomcreator.scribblenotes;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;

public class Note {
    /**
     * The title of the note
     */
    private String title;

    /**
     * The content of the note
     */
    private String content;

    /**
     * The name of the file being stored in the application storage. If unset by the constructor, a unique file name will be generated if {@link Note#saveToFile(Context)} is called
     */
    private String fileName;

    /**
     * A delimiter to split the title and contents of the file while in file storage
     */
    public static String DELIMITER = "\n";

    /**
     * Constructs a note unassociated with a file in storage, by only supplying information about the contents of the file.
     *
     * @param title The note title
     * @param content The note content
     */
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * Creates a note from storage, through the file name.
     *
     * @param fileName The name of the file in storage
     * @param context The context needed to access {@link Context#getFilesDir()}
     */
    public Note(String fileName, Context context) {
        this.fileName = fileName;
        File[] storageFiles = context.getFilesDir().listFiles(); // Retrieves the list of files stored in the app's directory
        if (storageFiles == null) { // Null check to remove Android Studio's warning about null-safety
            System.err.println("Error while loading storage");
            return;
        }
        for (File file : storageFiles) { // Iterates through the files
            if (file.getName().equals(fileName)) { // Checks if the file is the requested name
                loadFromFile(file); // Calls method to initialize fields from file
                return;
            }
        }
    }

    /**
     * Sets the title of the note
     *
     * @param title The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the content of the note
     *
     * @param content The content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the content of the title
     *
     * @return the content of the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the content of the note
     *
     * @return the content of the note
     */
    public String getContent() {
        return content;
    }

    /**
     * Initializes the fields from the content stored in the specified file
     *
     * @param noteFile the file to read
     */
    private void loadFromFile(File noteFile) {
        try {
            String fileContent = new String(Files.readAllBytes(noteFile.toPath())); // Reads the file contents to a string. Not a very efficient and performance-safe way of reading file (in case the file is very large, but fulfills the need)
            if (fileContent.isEmpty()) { // Checks if the file is empty
                System.err.println("File is empty");
                return;
            }
            if (!fileContent.contains(DELIMITER)) { // Checks if file contains a delimiter
                this.title = fileContent; // If it does not contain a delimiter, only the title is stored in the file, there is no content
                return;
            }
            String title = fileContent.split(DELIMITER)[0]; // Gets the title part of the file contents
            StringBuilder content = new StringBuilder(); // StringBuilder to append the rest of the contents, except title
            for (int i = 0; i < fileContent.split(DELIMITER).length; i++) {
                if (i != 0) {
                    content.append(fileContent.split(DELIMITER)[i]).append(i != fileContent.split(DELIMITER).length - 1 ? "\n" : ""); // Appends the rest of the contents, except title
                }
            }
            this.title = title; // Initializes title
            this.content = content.toString(); // Initializes content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves data stored in fields to file.
     *
     * @param context The {@link Context} needed to access {@link Context#getFilesDir()}
     */
    public void saveToFile(Context context) {
        try {
            if (fileName == null) { // Checks if the file name is null
                fileName = generateUniqueFileName(); // If it is null, initializes it with a new unique name
            }
            File saveFile = new File(context.getFilesDir(), fileName); // Creates a File instance of a file in the storage directory under the file name
            saveFile.createNewFile(); // Creates a new file (if necessary)

            FileWriter noteFileWriter = new FileWriter(saveFile); // Creates a new instance of FileWriter to write to the file
            noteFileWriter.append(title); // Writes the title to the file
            noteFileWriter.append(DELIMITER); // Writes the delimiter to the file to split the title and the rest of the content
            noteFileWriter.append(content); // Writes te content to the file
            noteFileWriter.close(); // Closes the FileWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a unique file name. Uses the current time in milliseconds to generate a unique id.
     *
     * @return A unique file name
     */
    private String generateUniqueFileName() {
        Calendar calendar = Calendar.getInstance(); // Creates a calendar instance
        return String.valueOf(calendar.getTimeInMillis()); // Returns the current time in milliseconds as a string to use as a file name
    }

    /**
     * Deletes file
     *
     * @param context The context needed to call {@link Context#deleteFile(String)}
     */
    public void deleteFile(Context context) {
        if (fileName != null) { // Checks whether the file name is null
            context.deleteFile(fileName); // If the file name is not null, deletes the file based on the file name
        }
    }
}
