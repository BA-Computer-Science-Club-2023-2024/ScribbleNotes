package com.veryrandomcreator.scribblenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/*
Sources:
 - https://stackoverflow.com/questions/34190716/set-fab-floating-action-button-above-keyboard (FAB above keyboard)
 */
public class MainActivity extends AppCompatActivity {
    private boolean areOptionsVisible = false;

    private EditText noteEdtTxt;
    private EditText fileNameEdtTxt;
    private CardView fileNameCard;
    private FloatingActionButton optionsBtn;
    private FloatingActionButton cancelBtn;
    private FloatingActionButton deleteBtn;
    private FloatingActionButton saveBtn;
    private String fileName; // includes extension

    public static final String FILE_CONTENTS_DELIMITER = "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEdtTxt = findViewById(R.id.noteEdtTxt);
        optionsBtn = findViewById(R.id.optionsBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        saveBtn = findViewById(R.id.saveBtn);
        fileNameEdtTxt = findViewById(R.id.fileNameEdtTxt);
        fileNameCard = findViewById(R.id.fileNameCard);

        cancelBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setVisibility(View.INVISIBLE);
        saveBtn.setVisibility(View.INVISIBLE);
        fileNameCard.setVisibility(View.INVISIBLE);

        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                if (areOptionsVisible) {
                    cancelBtn.setVisibility(View.INVISIBLE);
                    deleteBtn.setVisibility(View.INVISIBLE);
                    saveBtn.setVisibility(View.INVISIBLE);
                    fileNameCard.setVisibility(View.INVISIBLE);
                } else {
                    cancelBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.VISIBLE);
                    fileNameCard.setVisibility(View.VISIBLE);
                }
                areOptionsVisible = !areOptionsVisible;
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelBtnClick();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteBtnClick();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveBtnClick();
            }
        });
    }

    public void onCancelBtnClick() {
        vibrate();
        //exit activity
    }

    public void onDeleteBtnClick() {
        vibrate();
    }

    //https://developer.android.com/training/data-storage/app-specific#java
    public void onSaveBtnClick( ) {
        vibrate();
        File file = new File(getFilesDir(), fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(fileNameEdtTxt.getText().toString().getBytes());
            outputStream.write(FILE_CONTENTS_DELIMITER.getBytes());
            outputStream.write(noteEdtTxt.getText().toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendToast("File Saved!");
    }

    /**
     * A method to vibrate the device
     */
    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // Retrieves the vibrator service
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)); // Vibrates the device at the default amplitude for 100 milliseconds
    }

    /**
     * A method to send a toast
     *
     * @param text The text to display in the toast
     */
    public void sendToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show(); // Creates a toast with the specified text
    }
}