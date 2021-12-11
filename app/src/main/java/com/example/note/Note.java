package com.example.note;

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

    public String toString() {
        //测试
        return String.valueOf(getId());
    }


}
