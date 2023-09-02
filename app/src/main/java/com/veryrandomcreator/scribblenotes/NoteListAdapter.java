package com.veryrandomcreator.scribblenotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * The adapter for {@link R.id#notesLst}. Supplies the list with the data to display
 * <p>
 * The following code contains snippets found on the <a href="https://developer.android.com/reference/android/widget/ListView">Android Developer Website</a>.
 * For more information about using ListView, and the better alternative, RecyclerView, visit the developer website
 */
public class NoteListAdapter extends BaseAdapter {
    /**
     * An array of strings storing the titles of the notes to display
     */
    private String[] noteTitles;

    /**
     * A constructor initializing the note title array
     * @param noteTitles An array of strings storing the note titles
     */
    public NoteListAdapter(String[] noteTitles) {
        this.noteTitles = noteTitles;
    }

    /**
     * Sets the {@link NoteListAdapter#noteTitles} field
     * @param noteTitles New array
     */
    public void setNoteTitles(String[] noteTitles) {
        this.noteTitles = noteTitles;
    }

    /**
     * Returns the length of array
     * @return The length of the array
     */
    @Override
    public int getCount() {
        return noteTitles.length;
    }

    /**
     * Retrieves the item at the given position
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The item at the given position
     */
    @Override
    public Object getItem(int position) {
        return noteTitles[position];
    }

    /**
     * Is supposed to return an item id, but is unnecessary for the project's current use
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return the id
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Creates a {@link R.layout#note_list_item} inflated view to display the specific title
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) { // Checks if the view is null
            convertView = parent.getContext().getSystemService(LayoutInflater.class).inflate(R.layout.note_list_item, parent, false); // If it is null, inflate a view of the list item
        }
        ((TextView) convertView.findViewById(R.id.note_title_lst_item)).setText((CharSequence) getItem(position)); // Sets the text of the view in the list item
        return convertView; // Returns the created view
    }
}
