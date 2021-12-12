package com.example.note.Model;

import com.example.note.Util.DateHelper;

import java.util.Objects;

/*
 *   model类
 * */
public class Note {

    private String noteTitle;
    private String noteContent;
    private String noteTime;
    private int noteLength;
    private int group;
    private int id;



    public Note(String noteTitle, String noteContent, String noteTime, int group, int noteLength) {

        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteTime = noteTime;
        this.group = group;
        this.noteLength = noteLength;
    }

    public Note() {
        this.noteTitle = "";
        this.noteContent = "";
        this.noteTime = DateHelper.getInstance().getDataString();
        this.group = 1;
        this.noteLength = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }


    public int getNoteLength() {
        return noteLength;
    }

    public void setNoteLength(int noteLength) {
        this.noteLength = noteLength;
    }


    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {

        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return getNoteLength() == note.getNoteLength() && getGroup() == note.getGroup() && getId() == note.getId() && getNoteTitle().equals(note.getNoteTitle()) && getNoteContent().equals(note.getNoteContent()) && getNoteTime().equals(note.getNoteTime());
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteTitle='" + noteTitle + '\'' +
                ", noteContent='" + noteContent + '\'' +
                ", noteTime='" + noteTime + '\'' +
                ", noteLength=" + noteLength +
                ", group=" + group +
                ", id=" + id +
                '}';
    }
}
