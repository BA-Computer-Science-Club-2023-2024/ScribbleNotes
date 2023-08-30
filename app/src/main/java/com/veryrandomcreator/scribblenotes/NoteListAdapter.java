package com.veryrandomcreator.scribblenotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//https://developer.android.com/reference/android/widget/ListView
public class NoteListAdapter extends BaseAdapter {
    private String[] noteTitles;

    public NoteListAdapter(String[] noteTitles) {
        this.noteTitles = noteTitles;
    }

    public void setNoteTitles(String[] noteTitles) {
        this.noteTitles = noteTitles;
    }

    @Override
    public int getCount() {
        return noteTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return noteTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = parent.getContext().getSystemService(LayoutInflater.class).inflate(R.layout.note_list_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.note_title_lst_item)).setText((CharSequence) getItem(position));
        return convertView;
    }
}
