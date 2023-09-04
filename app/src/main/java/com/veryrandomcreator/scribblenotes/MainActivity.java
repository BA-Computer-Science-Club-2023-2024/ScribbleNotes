package com.veryrandomcreator.scribblenotes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

/**
 * The main menu. Contains the title, notes list, create note button, and toggle notification button.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * The {@link ActivityResultLauncher} to launch the permission request
     */
    private ActivityResultLauncher<String> requestPermissionLauncher;

    /**
     * The reference to the create note button of the layout
     */
    private FloatingActionButton createNoteBtn;

    /**
     * The reference to the toggle notification button of the layout
     */
    private FloatingActionButton notificationsBtn;

    /**
     * The adapter for the notes list, to display.
     */
    private NoteListAdapter noteListAdapter;

    /**
     * Initializes the layout views, sets the click listeners of the buttons, and more setup
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Sets the layout resource

        ListView notes = findViewById(R.id.notesLst); // Initializes a reference to the list of notes
        createNoteBtn = findViewById(R.id.createNoteBtn); // Initializes a reference to the create note FloatingActionButton
        notificationsBtn = findViewById(R.id.notificationBtn); // Initializes a reference to the toggle notification FloatingActionButton

        notificationsBtn.setOnClickListener(new View.OnClickListener() { // Handles the toggle notification button click event
            @Override
            public void onClick(View v) {
                vibrate(getApplicationContext()); // Vibrates device
                if (!hasPermissions()) { // Checks if the app has the needed permissions, and requests them if it does not
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), NotificationService.class); // Creates an intent to start the notification service
                startForegroundService(intent); // Starts the service
            }
        });
        createNoteBtn.setOnClickListener(new View.OnClickListener() { // Handles the create note button click event
            @Override
            public void onClick(View v) {
                vibrate(getApplicationContext()); // Vibrates device
                Note note = new Note("Untitled Note", ""); // Creates a new empty note
                note.saveToFile(getApplicationContext()); // Saves the note to storage
                updateNotes(); // Updates the list of notes to display new note
            }
        });

        noteListAdapter = new NoteListAdapter(getNotesTitles()); // Adapter instance for notes list, displaying list of note titles
        notes.setAdapter(noteListAdapter); // Sets the adapter instance to the notes list
        notes.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Handles the on item click event
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vibrate(getApplicationContext()); // Vibrates device
                openEditorFragment(position); // Launches the editor fragment for the note at the specified position
                setFabsVisibility(View.INVISIBLE); // Sets the visibility of the FloatingActionButtons to invisible to prevent them from displaying over fragment
            }
        });

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() { // Registers the permission request launcher, to be called to create the request dialog
            @Override
            public void onActivityResult(Boolean isAllowed) {
                if (!isAllowed) { // Checks if the permission is granted
                    sendToast("Notifications permission missing", getApplicationContext()); // If the permission is not granted, the user is notified accordingly
                } else {
                    sendToast("Permission granted", getApplicationContext()); // In the permission is granted, the user is notified accordingly
                }
            }
        });
    }

    /**
     * Checks whether the necessary permissions ({@link android.Manifest.permission#POST_NOTIFICATIONS}) are granted to the app
     *
     * @return A boolean of whether the permissions were granted
     */
    public boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Ensures API is at level required to access the POST_NOTIFICATIONS permission check
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) { // Checks if permission has been granted
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS); // If the permission has not been granted, request permission launcher is launched
                return false; // Does not currently have permission
            }
            return true; //Does have permission
        }
        return true;
    }

    /**
     * Creates the editor fragment from supplied position in list/storage
     * <p>
     * This code follows the code examples found on the <a href="https://developer.android.com/guide/fragments/create">Android Developer Website</a>,
     * which contains more information on creating Fragments
     *
     * @param position The position of the file on the clicked list
     */
    public void openEditorFragment(int position) {
        Bundle bundle = new Bundle(); // A bundle to pass data to fragment
        bundle.putInt("position", position); // Storing the position of the file to load into a note in a bundle to give to fragment

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, EditorFragment.class, bundle)
                .commit(); // Launches the fragment
    }

    /**
     * Updates the {@link MainActivity#noteListAdapter}
     */
    public void updateNotes() {
        noteListAdapter.setNoteTitles(getNotesTitles()); // Changes the list of note titles
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                noteListAdapter.notifyDataSetChanged(); // Triggers the update with the changes
            }
        });
    }

    /**
     * Gets the titles of all saved notes
     *
     * @return An array of strings containing the titles
     */
    public String[] getNotesTitles() {
        File[] storageFiles = getFilesDir().listFiles(); // The list of files in storage
        if (storageFiles == null) { // Null-checking
            System.err.println("Error while loading storage");
            return null;
        }
        String[] titles = new String[storageFiles.length]; // An empty array to hold the titles
        for (int i = 0; i < storageFiles.length; i++) { // Iterates through the files in storage
            Note temp = new Note(storageFiles[i].getName(), this); // Creates a Note instance of each file
            titles[i] = temp.getTitle(); // Uses the Note instance to get the title stored in the file, and adds it to the array
        }
        return titles; // Returns the array
    }

    /**
     * Sets the visibility of all the FloatingActionButtons to the specified visibility
     *
     * @param visibility The visibility to set the FloatingActionButtons to
     */
    public void setFabsVisibility(int visibility) {
        createNoteBtn.setVisibility(visibility); // Sets the visibility of the create note button
        notificationsBtn.setVisibility(visibility); // Sets the visibility of the toggle notification button
    }

    /**
     * A method to send a toast
     *
     * @param text The text to display in the toast
     */
    public static void sendToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show(); // Creates a toast with the specified text
    }

    /**
     * A method to vibrate the device
     */
    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); // Retrieves the vibrator service
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)); // Vibrates the device at the default amplitude for 100 milliseconds
    }
}