package com.example.note;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private List<Note> mData;
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    public NoteAdapter(Context context, List<Note> data) {
        super(context, -1, data);
        mContext = context;
        mData = data;

        mLayoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        convertView = mLayoutInflater.inflate(R.layout.list_item, parent, false);

        //显示数据
        TextView noteTitle=convertView.findViewById(R.id.note_title);
        TextView noteTime=convertView.findViewById(R.id.note_time);
        TextView noteContent=convertView.findViewById(R.id.note_content);
        TextView textLength=convertView.findViewById(R.id.text_length);
        noteTitle.setText(mData.get(position).getNoteTitle());
        noteTime.setText(mData.get(position).getNoteTime());
        noteContent.setText("  "+mData.get(position).getNoteContent());
        textLength.setText(" "+mData.get(position).getNoteLength());

        return  convertView;

        }
}
