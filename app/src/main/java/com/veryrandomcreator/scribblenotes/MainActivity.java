package com.veryrandomcreator.scribblenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

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
    }
}