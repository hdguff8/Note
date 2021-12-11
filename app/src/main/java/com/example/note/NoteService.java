package com.example.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class NoteService {
    Context context;
    NoteDatabaseHelper noteDatabaseHelper;
    SQLiteDatabase db;
    int idIndex=0;//起始id

    public NoteService(Context context) {
        this.context = context;
        noteDatabaseHelper = new NoteDatabaseHelper(context);
        db = noteDatabaseHelper.getWritableDatabase();
        initList();
    }

    //插入一个数据
    public void insertNote(Note note){
        ContentValues cv = new ContentValues();

        //获取最大的id值
        ArrayList<Note> Notes = getAllNote();
        int lastID = 1;
        for (Note current_note:Notes){
            if (lastID <= current_note.getId())
                lastID = current_note.getId()+1;
        }
//        Log.d("Note id test", "insertNote: id "+lastID);

        cv.put("id",lastID);
        cv.put("title",note.getNoteTitle());
        cv.put("content",note.getNoteContent());
        cv.put("time",note.getNoteTime());
        cv.put("note_group",note.getGroup());
        cv.put("note_length",note.getNoteLength());

        db.insert(NoteDatabaseHelper.table_name,null,cv);
    }

    //暂未使用
    public void updateNote(Note newNote,int num){
        ContentValues values = new ContentValues();
        values.put("title", newNote.getNoteTitle());
        values.put("content", newNote.getNoteContent());
        values.put("time",newNote.getNoteTime() );
        values.put("note_group",newNote.getGroup());
        values.put("note_length", newNote.getNoteLength());
        db.update(NoteDatabaseHelper.table_name, values, "title = ?", new String[]{newNote.getNoteTitle()});
        db.update(NoteDatabaseHelper.table_name, values, "content = ?", new String[]{newNote.getNoteContent()});
        db.update(NoteDatabaseHelper.table_name, values, "time = ?", new String[]{newNote.getNoteTime()});
        db.update(NoteDatabaseHelper.table_name, values, "note_group = ?", new String[]{String.valueOf(newNote.getGroup())});
        db.update(NoteDatabaseHelper.table_name, values, "note_length = ?", new String[]{String.valueOf(newNote.getNoteLength())});
    }

    //根据Note信息更新id
    public void updateNoteByTitle(String titleName, Note note){
        db.execSQL("update "+NoteDatabaseHelper.table_name+" set id=? where title=? and content=? and time=?",
                new Object[]{note.getId(),note.getNoteTitle(),note.getNoteContent(),note.getNoteTime()});
    }

    public void initList(){//初始化第一条note
        if (getAllNote().size()==0){
            //设置时间
            Calendar calendar = Calendar.getInstance();
            //月  月份从0开始 —> +1
            int month = calendar.get(Calendar.MONTH) + 1;
            //日
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //周(本月第几周)
//            int week = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
            //小时
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            //分
            int minute = calendar.get(Calendar.MINUTE);
            //秒
            int second = calendar.get(Calendar.SECOND);
            ContentValues cv = new ContentValues();
            //默认显示的一条note
            cv.put("title","我是阿俊");
            cv.put("content","今天也想帮你记录生活鸭");
            cv.put("time",month+"月"+day+"日"+hour+"时:"+minute+"分:"+second+"秒");
            cv.put("note_group",0);//0为默认分组
            cv.put("note_length",11);//初始化的这条note的content字数为11
            db.insert(NoteDatabaseHelper.table_name,null,cv);
        }
    }

//  用于获取 数据
    public ArrayList<Note> getAllNote(){
        Cursor cursor = db.rawQuery("select * from "+NoteDatabaseHelper.table_name+"",null);
        return CursorToNotes(cursor);
    }

//    暂时不用此方法(用于测试)
    public String allNotesSearch(){

        Cursor cursor=db.rawQuery("select * from note ",null);

        StringBuilder stringBuilder = new StringBuilder();

//       getCount() -> 返回游标中的行数
        if (cursor.getCount()>0){

            System.out.println("note表中有"+cursor.getCount()+"条数据");

            ArrayList<Note> notes = CursorToNotes(cursor);

            for(int i=0;i<notes.size();i++){

                stringBuilder.append(notes.toString()).append("\n");

            }
        }
        return stringBuilder.toString();
    }
//删除 note的方法
    public void deleteNote(Note note){//删除当前note

        //删除条件
        String whereClause = "id=?";

        //删除条件参数
        String[] whereArgs = {String.valueOf(note.getId())};

        //执行删除
        db.delete(NoteDatabaseHelper.table_name,whereClause,whereArgs);

        //获取所有 比被删note的id 大的所有id
        ArrayList<Note> oldNotes = CursorToNotes(

                db.rawQuery("select * from "+NoteDatabaseHelper.table_name+" where id>?"
                        ,new String[]{String.valueOf(note.getId())}));

        //把所有符合条件的note的id减一 填补被删note的id空缺
        for (int i=0;i<oldNotes.size();i++){

            oldNotes.get(i).setId(oldNotes.get(i).getId()-1);

            updateNoteByTitle(oldNotes.get(i).getNoteTitle(),oldNotes.get(i));

        }

    }

//非常重要 ！！用来查询note全部信息
    public ArrayList<Note> CursorToNotes(Cursor cursor){

        ArrayList<Note> notes = new ArrayList<>();

        //moveToNext() -> Move the cursor to the next row.
        //如果游标已经超过结果集中的最后一项，此方法将返回false。
        while (cursor.moveToNext()){//范围 ：游标一直到最后一项

            Note note = new Note();

            //id
            int id = cursor.getInt(idIndex);
            note.setId(id);

            //设置 标题 显示在listView
            note.setNoteTitle(cursor.getString(cursor.getColumnIndex("title")));

            //设置 内容 显示在listView
            note.setNoteContent(cursor.getString(cursor.getColumnIndex("content")));

            //设置 时间 显示在listView
            note.setNoteTime(cursor.getString(cursor.getColumnIndex("time")));

            //设置 分组 显示在show和edit界面
            note.setGroup(Integer.parseInt(cursor.getString(cursor.getColumnIndex("note_group"))));

            //设置 note内容的字数长度 显示在show和edit界面
            note.setNoteLength(Integer.parseInt(cursor.getString(cursor.getColumnIndex("note_length"))));

            //加入 arrayList
            notes.add(note);
        }
        return notes;
    }
}

