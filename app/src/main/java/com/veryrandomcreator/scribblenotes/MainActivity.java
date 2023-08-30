package com.veryrandomcreator.scribblenotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    /**
     * Using a {@link androidx.recyclerview.widget.RecyclerView} would be a more efficient form of making a list of notes,
     * however {@link ListView} is less overwhelming to understand. (For teaching purposes)
     */
    private ListView notes;
    private FloatingActionButton createNoteBtn;
    private FloatingActionButton notificationsBtn;
    private NoteListAdapter noteListAdapter;

    public static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notes = findViewById(R.id.notesLst);
        createNoteBtn = findViewById(R.id.createNoteBtn);
        notificationsBtn = findViewById(R.id.notificationBtn);

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!checkPermissions()) {
                        sendToast("Please accept permissions", getApplicationContext());
                        return;
                    }
                    //check if service is running to allow toggling
                    Intent intent = new Intent(getApplicationContext(), NotificationService.class);
                    startForegroundService(intent);

                }
            }
        });
        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note("Untitled Note", "");
                note.saveToFile(getApplicationContext());
                updateNotes();
            }
        });

        noteListAdapter = new NoteListAdapter(getNotesTitles());
        notes.setAdapter(noteListAdapter);
        notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditorFragment(position);
                setFabsVisibility(View.INVISIBLE);
                System.out.println("HIT");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                sendToast("Notifications permission missing", getApplicationContext());
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    //https://developer.android.com/guide/fragments/create
    public void openEditorFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainerView, EditorFragment.class, bundle)
                .commit();
    }

    public void updateNotes() {
        noteListAdapter.setNoteTitles(getNotesTitles());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                noteListAdapter.notifyDataSetChanged();
            }
        });
    }

    public String[] getNotesTitles() {
        File[] storageFiles = getFilesDir().listFiles();
        if (storageFiles == null) {
            System.err.println("Error while loading storage");
            return null;
        }
        String[] titles = new String[storageFiles.length];
        for (int i = 0; i < storageFiles.length; i++) {
            Note temp = new Note(storageFiles[i].getName(), this);
            titles[i] = temp.getTitle();
        }
        return titles;
    }

    public void setFabsVisibility(int visibility) {
        createNoteBtn.setVisibility(visibility);
        notificationsBtn.setVisibility(visibility);
    }

    /**
     * A method to send a toast
     *
     * @param text The text to display in the toast
     */
    public static void sendToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show(); // Creates a toast with the specified text
    }
}