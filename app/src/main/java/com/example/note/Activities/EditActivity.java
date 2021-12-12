package com.example.note.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.note.Database.GroupService;
import com.example.note.Database.NoteService;
import com.example.note.Model.Note;
import com.example.note.R;
import com.example.note.UI.MsDelEditText;
import com.example.note.Util.DateHelper;
import com.example.note.Util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "Edit";
    //环境
    NoteService db;
    GroupService gdb;
    //数据
    Note needInsertNote;
    boolean isNewNote;

    MsDelEditText title;
    EditText editText_content;
    TextView textView_char_count,textView_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //得到toolBar实例
        setSupportActionBar(findViewById(R.id.tool_bar_setting));
        //显示 tool_bar左侧按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }


    //加载 tool_bar_edit.xml这个菜单文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_edit, menu);
        return true;
    }

    //处理多个按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                //返回主页
                saveToNeedInsertNote();
                Log.d(TAG, "比较needInsetNote:"+needInsertNote.toString()+"db:"+db.getNoteById(needInsertNote.getId()).toString());
                Log.d(TAG, "isEqual"+needInsertNote.equals(db.getNoteById(needInsertNote.getId())));
                if(needInsertNote.equals(db.getNoteById(needInsertNote.getId()))){
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    DialogUtil.doSthAfterChoiceDialog(EditActivity.this, "提示", "你还没有保存更改的内容,要保留吗？", R.drawable.icon_warning, new DialogUtil.DoSthListener() {
                        @Override
                        public void PositiveDoSth() {
                            //返回时确认保存
                            saveNote();
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void NegativeDoSth() {
                            //返回时不保存数据
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                break;

            case R.id.save:
                Toast.makeText(this, "保存成功❤", Toast.LENGTH_SHORT).show();
                saveNote();
                break;

        }

        return true;
    }

    private void saveToNeedInsertNote(){
        needInsertNote.setNoteTitle(title.getText().toString());
        needInsertNote.setNoteContent(editText_content.getText().toString());
        needInsertNote.setNoteLength(needInsertNote.getNoteContent().length());
    }
    private void saveNote(){
        saveToNeedInsertNote();
        if(isNewNote){
            //保存新建的内容
            db.insertNote(needInsertNote);
        }else {
            //更新内容
            db.updateNote(needInsertNote);
        }
    }

    private void init() {
        //初始化环境
        db = new NoteService(this);
        gdb = new GroupService(this);

        //数据
        needInsertNote = new Note();

        //控件
        textView_char_count = findViewById(R.id.textView_char_number);
        textView_group = findViewById(R.id.edit_group);
        setGroupText();
        textView_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO the dialog to change the group
                showChangeGroupIdDialog();
            }
        });

        title = findViewById(R.id.edit_title);

        editText_content = findViewById(R.id.edit_content);
        //编辑栏变化改变单词数
        editText_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                textView_char_count.setText(String.valueOf(editText_content.getText().toString().length()));
            }
        });

        //设置数据
        int noteId = getIntent().getIntExtra("noteId",-1);
        if(noteId == -1){

        }else{
            needInsertNote = db.getNoteById(noteId);
            title.setText(needInsertNote.getNoteTitle());
            editText_content.setText(needInsertNote.getNoteContent());
            setGroupText();
        }

    }

    public void setGroupText(){
        textView_group.setText("分组 | "+gdb.getGroupNameById(needInsertNote.getGroup()));
    }

    public void showChangeGroupIdDialog(){
        //创建对话框并绑定视图
        AlertDialog.Builder changeGroupIdDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(EditActivity.this).
                inflate(R.layout.edit_change_group_dialog, null);
        changeGroupIdDialog.setTitle("选择新的分组");
        changeGroupIdDialog.setView(dialogView);

        //获取对话框内的控件
        Spinner spinner = dialogView.findViewById(R.id.spinner_choice_new_groups);
        ArrayList<String> GroupNames = gdb.getAllGroupName(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,GroupNames);
        spinner.setAdapter(adapter);
        spinner.setSelection(GroupNames.indexOf(gdb.getGroupNameById(needInsertNote.getGroup())));

        changeGroupIdDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                needInsertNote.setGroup(gdb.getGroupIdByName(GroupNames.get(spinner.getSelectedItemPosition())));
                setGroupText();
            }
        });
        changeGroupIdDialog.show();
    }


}
