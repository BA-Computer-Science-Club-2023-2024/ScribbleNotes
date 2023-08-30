package com.veryrandomcreator.scribblenotes;

import static com.veryrandomcreator.scribblenotes.MainActivity.sendToast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
Sources:
 - https://stackoverflow.com/questions/34190716/set-fab-floating-action-button-above-keyboard (FAB above keyboard)
 */


//https://developer.android.com/guide/fragments/create
/**
 * The fragment to edit a selected note.
 */
public class EditorFragment extends Fragment {
    private boolean areOptionsVisible = false;

    private Note note;

    private EditText noteEdtTxt;
    private EditText titleEdtTxt;
    private CardView titleCard;
    private FloatingActionButton optionsBtn;
    private FloatingActionButton cancelBtn;
    private FloatingActionButton deleteBtn;
    private FloatingActionButton saveBtn;

    public EditorFragment() {
        super(R.layout.editor_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onSaveBtnClick();
                remove();
                closeFragment();
            }
        });
        System.out.println("HITfdf");
        int position = requireArguments().getInt("position");

        noteEdtTxt = requireActivity().findViewById(R.id.noteEdtTxt);
        optionsBtn = requireActivity().findViewById(R.id.optionsBtn);
        cancelBtn = requireActivity().findViewById(R.id.cancelBtn);
        deleteBtn = requireActivity().findViewById(R.id.deleteBtn);
        saveBtn = requireActivity().findViewById(R.id.saveBtn);
        titleEdtTxt = requireActivity().findViewById(R.id.titleEdtTxt);
        titleCard = requireActivity().findViewById(R.id.titleCard);

        cancelBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setVisibility(View.INVISIBLE);
        saveBtn.setVisibility(View.INVISIBLE);
        titleCard.setVisibility(View.INVISIBLE);

        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                if (areOptionsVisible) {
                    cancelBtn.setVisibility(View.INVISIBLE);
                    deleteBtn.setVisibility(View.INVISIBLE);
                    saveBtn.setVisibility(View.INVISIBLE);
                    titleCard.setVisibility(View.INVISIBLE);
                } else {
                    cancelBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.VISIBLE);
                    titleCard.setVisibility(View.VISIBLE);
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
        loadFromFile(position);
    }

    public void loadFromFile(int position) {
        if (requireActivity().getFilesDir().listFiles() != null) {
            note = new Note(requireActivity().getFilesDir().listFiles()[position].getName(), requireActivity());

            noteEdtTxt.setText(note.getContent());
            titleEdtTxt.setText(note.getTitle());
            System.out.println("context: " + note.getContent());
        }
    }

    public void onCancelBtnClick() {
        vibrate();
        closeFragment();
    }

    public void onDeleteBtnClick() {
        vibrate();
        note.deleteFile(requireActivity());
        closeFragment();
    }

    //https://developer.android.com/training/data-storage/app-specific#java
    public void onSaveBtnClick() {
        vibrate();
        if (titleEdtTxt.getText().toString().trim().isEmpty()) {
            System.err.println("Title field cannot be blank!");
            return;
        }
        titleEdtTxt.setText(titleEdtTxt.getText().toString());
        note.setContent(noteEdtTxt.getText().toString());
        note.setTitle(titleEdtTxt.getText().toString());
        note.saveToFile(requireActivity());
        sendToast("File Saved!", requireActivity());
    }

    /**
     * A method to vibrate the device
     */
    public void vibrate() {
        Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE); // Retrieves the vibrator service
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)); // Vibrates the device at the default amplitude for 100 milliseconds
    }

    /**
     * Exits the editor activity
     */
    public void closeFragment() {
        noteEdtTxt = null;
        titleEdtTxt = null;
        titleCard = null;
        cancelBtn = null;
        deleteBtn = null;
        saveBtn = null;
        optionsBtn = null;
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        ((MainActivity) requireActivity()).updateNotes();
        ((MainActivity) requireActivity()).setFabsVisibility(View.VISIBLE);
    }
}