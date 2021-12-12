package com.example.note.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.note.Model.Group;
import com.example.note.Model.Note;

import java.util.ArrayList;

public class GroupService {
    private static final String TAG = "GDB";

    Context context;
    NoteDatabaseHelper noteDatabaseHelper;
    SQLiteDatabase db;

    public GroupService(Context context) {
        this.context = context;
        noteDatabaseHelper = new NoteDatabaseHelper(context);
        db = noteDatabaseHelper.getWritableDatabase();

        init();
    }

    //初始化
    private void init(){
        if(getAllGroup().size()==0){
            insertGroup(new Group(1,"Default"));
            insertGroup(new Group(2,"PASSWORD"));
        }
    }

    //插入一个组
    public void insertGroup(Group group){
        ContentValues cv = new ContentValues();
        cv.put("id",group.getId());
        cv.put("name",group.getName());
        db.insert("Groups",null,cv);
    }

    //更新一个组
    public void updateGroupName(int id,String newName){
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("name",newName);
        db.update("Groups",cv,"id=?",new String[]{newName});
    }

    //删除
    public void deleteGroup(int id){
        //将被删除组的所有速记分组改成默认分组
        NoteService noteService = new NoteService(context);
        ArrayList<Note> needUpdateNodes = noteService.getNotesByGroupId(id);
        for (Note note:needUpdateNodes){
            note.setGroup(1);
            noteService.updateNote(note);
        }

        db.delete("Groups","id=?",new String[]{String.valueOf(id)});
    }

    //获取组名
    public String getGroupNameById(int id){
        Cursor cursor=db.rawQuery("select * from Groups where id=?",new String[]{String.valueOf(id)});
        if(cursor.getCount()>0){
            return cursorToGroups(cursor).get(0).getName();
        }else
            return "null";
    }

    public int getGroupIdByName(String name){
        Cursor cursor=db.rawQuery("select * from Groups where name=?",new String[]{String.valueOf(name)});
        if(cursor!=null){
            return cursorToGroups(cursor).get(0).getId();
        }else
            return 0;
    }

    public int getMaxId(){
        Cursor cursor=db.rawQuery("select max(id) from Groups",null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(0));
            }
        }
        return 0;
    }

    //获取所有组
    public ArrayList<Group> getAllGroup(){
        Cursor cursor=db.rawQuery("select * from Groups ",null);
        return cursorToGroups(cursor);
    }

    public ArrayList<String> getAllGroupName(boolean isHaveExtraGroup){
        //ExtraGroup 第一个不是 Default 而是 全部
        ArrayList<String> groupsName = new ArrayList<>();
        ArrayList<Group> groups = getAllGroup();
        if(isHaveExtraGroup == true)
            groupsName.add("全部");
        for(int i=0;i< groups.size();i++){
            groupsName.add(groups.get(i).getName());
        }
        return groupsName;
    }

    @SuppressLint("Range")
    private ArrayList<Group>  cursorToGroups(Cursor cursor){
        ArrayList<Group> groups = new ArrayList<>();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                groups.add(new Group(id,name));
            }
        }
        return groups;
    }



}
