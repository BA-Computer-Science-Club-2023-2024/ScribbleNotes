package com.veryrandomcreator.scribblenotes;

import static com.veryrandomcreator.scribblenotes.MainActivity.sendToast;
import static com.veryrandomcreator.scribblenotes.MainActivity.vibrate;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
Sources:
 - https://stackoverflow.com/questions/34190716/set-fab-floating-action-button-above-keyboard (FAB above keyboard)
 */

/**
 * The fragment to edit a selected note.
 * <p>
 * This code follows the code examples found on the <a href="https://developer.android.com/guide/fragments/create">Android Developer Website</a>,
 * which contains more information on creating Fragments
 */
public class EditorFragment extends Fragment {
    /**
     * A field to store the visibility state of the options buttons (save, delete, cancel)
     */
    private boolean areOptionsVisible = false;

    /**
     * The {@link Note} holding the information to display and store.
     */
    private Note note;

    /**
     * The reference to the content {@link EditText} ({@link R.id#noteEdtTxt}) of the layout
     */
    private EditText noteEdtTxt;

    /**
     * The reference to the title {@link EditText} ({@link R.id#titleEdtTxt}) of the layout
     */
    private EditText titleEdtTxt;

    /**
     * The reference to the card ({@link R.id#titleCard}) holding the title {@link EditText} ({@link R.id#titleEdtTxt}) of the layout
     */
    private CardView titleCard;

    /**
     * The reference to the options button of the layout
     */
    private FloatingActionButton optionsBtn;

    /**
     * The reference to the cancel button of the layout
     */
    private FloatingActionButton cancelBtn;

    /**
     * The reference to the delete button of the layout
     */
    private FloatingActionButton deleteBtn;

    /**
     * The reference to the save button of the layout
     */
    private FloatingActionButton saveBtn;

    /**
     * Empty constructor.
     */
    public EditorFragment() {
        super(R.layout.editor_fragment); // Calls the parent's constructor to set the layout of this fragment
    }

    /**
     * Initializes all the references to views of the layout, and sets the {@link android.view.View.OnClickListener}s
     *
     * @param view The View returned by {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) { // Sets the parent activity's OnBackPressedDispatcher callback to handle back events.
            @Override
            public void handleOnBackPressed() {
                onSaveBtnClick(); // Saves the note
                remove(); // Removes current callback
                closeFragment(); // Exits the fragment
            }
        });
        int position = requireArguments().getInt("position"); // The position of the note file in the list of notes.

        noteEdtTxt = requireActivity().findViewById(R.id.noteEdtTxt); // Initializes the note content EditText
        optionsBtn = requireActivity().findViewById(R.id.optionsBtn); // Initializes the options FloatingActionButton
        cancelBtn = requireActivity().findViewById(R.id.cancelBtn); // Initializes the cancel FloatingActionButton
        deleteBtn = requireActivity().findViewById(R.id.deleteBtn); // Initializes the delete FloatingActionButton
        saveBtn = requireActivity().findViewById(R.id.saveBtn); // Initializes the save FloatingActionButton
        titleEdtTxt = requireActivity().findViewById(R.id.titleEdtTxt); // Initializes the title EditText
        titleCard = requireActivity().findViewById(R.id.titleCard); // Initializes the card containing the title EditText

        cancelBtn.setVisibility(View.INVISIBLE); // Makes the cancel button invisible by default, until the user toggles it
        deleteBtn.setVisibility(View.INVISIBLE); // Makes the delete button invisible by default, until the user toggles it
        saveBtn.setVisibility(View.INVISIBLE); // Makes the save button invisible by default, until the user toggles it
        titleCard.setVisibility(View.INVISIBLE); // Makes the title card invisible by default, until the user toggles it

        optionsBtn.setOnClickListener(new View.OnClickListener() { // Sets the click listener on the options button, to react to click events
            @Override
            public void onClick(View v) {
                vibrate(requireActivity()); // Vibrates the device
                if (areOptionsVisible) { // Checks if the options buttons are currently visible, to make them invisible
                    cancelBtn.setVisibility(View.INVISIBLE); // Makes the cancel button invisible
                    deleteBtn.setVisibility(View.INVISIBLE); // Makes the delete button invisible
                    saveBtn.setVisibility(View.INVISIBLE); // Makes the save button invisible
                    titleCard.setVisibility(View.INVISIBLE); // Makes the title card invisible
                } else { // Responds to the options buttons being currently invisible, to make them visible
                    cancelBtn.setVisibility(View.VISIBLE); // Makes the cancel button visible
                    deleteBtn.setVisibility(View.VISIBLE); // Makes the delete button visible
                    saveBtn.setVisibility(View.VISIBLE); // Makes the save button visible
                    titleCard.setVisibility(View.VISIBLE); // Makes the title card visible
                }
                areOptionsVisible = !areOptionsVisible; // Toggles the state of the variable storing the visibility of the options buttons
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() { // Sets the listener of the cancel button
            @Override
            public void onClick(View view) {
                onCancelBtnClick(); // Responds to the cancel button click event
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() { // Sets the listener of the delete button
            @Override
            public void onClick(View view) {
                onDeleteBtnClick(); // Responds to the delete button click event
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() { // Sets the listener of the save button
            @Override
            public void onClick(View view) {
                onSaveBtnClick(); // Responds to the save button click event
            }
        });
        loadFromFile(position); // Fills the appropriate fields with the data stored in the file at the specified position
    }

    /**
     * Fills the appropriate fields with the data stored in the file at the specified position
     *
     * @param position The position of the file in the application's storage
     */
    public void loadFromFile(int position) {
        if (requireActivity().getFilesDir().listFiles() != null) { // Null check
            note = new Note(requireActivity().getFilesDir().listFiles()[position].getName(), requireActivity()); // Sets the current note to a new note with the values stored in storage

            noteEdtTxt.setText(note.getContent()); // Displays the loaded note's contents on the content EditText
            titleEdtTxt.setText(note.getTitle()); // Displays the loaded note's contents on the title EditText
        }
    }

    /**
     * Response to the {@link EditorFragment#cancelBtn} click event
     */
    public void onCancelBtnClick() {
        vibrate(requireActivity()); // Vibrates device
        closeFragment(); // Closes the fragment
    }

    /**
     * Response to the {@link EditorFragment#deleteBtn} click event
     */
    public void onDeleteBtnClick() {
        vibrate(requireActivity()); // Vibrates device
        note.deleteFile(requireActivity()); // Deletes the note in storage
        closeFragment(); // Closes the fragment
    }

    /**
     * Response to the {@link EditorFragment#saveBtn} click event
     */
    public void onSaveBtnClick() {
        vibrate(requireActivity()); // Vibrates device
        if (titleEdtTxt.getText().toString().trim().isEmpty()) { // Ensures the title field is not blank
            sendToast("Title field cannot be blank!", requireActivity()); // Sends toast notifying user of the problem
            return;
        }
        note.setContent(noteEdtTxt.getText().toString()); // Sets the content of the current note (not yet saved to storage)
        note.setTitle(titleEdtTxt.getText().toString()); // Sets the title of the current note (not yet saved to storage)
        note.saveToFile(requireActivity()); // Saves the note to storage
        sendToast("File Saved!", requireActivity()); // Notifies the user that the file is saved
    }

    /**
     * Exits the editor activity. Sets all layout references to null and updates {@link MainActivity}
     */
    public void closeFragment() {
        noteEdtTxt = null;
        titleEdtTxt = null;
        titleCard = null;
        cancelBtn = null;
        deleteBtn = null;
        saveBtn = null;
        optionsBtn = null;
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit(); // Exits fragment
        ((MainActivity) requireActivity()).updateNotes(); // Updates the note ListView
        ((MainActivity) requireActivity()).setFabsVisibility(View.VISIBLE); // Updates the visibility of the FABs
    }
}