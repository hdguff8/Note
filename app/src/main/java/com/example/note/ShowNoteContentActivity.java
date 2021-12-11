package com.example.note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ShowNoteContentActivity extends AppCompatActivity {

    private Toolbar tool_bar_setting;

    private EditText show_title;//note的标题
    private EditText show_content;//note的内容
    private TextView note_group;//note的分组
    private TextView textLength;//note的内容的字数长度

    NoteService service;//方便调用 数据操作的方法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note_content);
        init();
        listener();
        showNoteContent();
    }

    private void init() {

        //绑定 Toolbar
        tool_bar_setting = findViewById(R.id.tool_bar_setting);

        //得到toolBar实例
        setSupportActionBar(tool_bar_setting);

        //显示 tool_bar左侧按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        show_title = findViewById(R.id.show_title);
        show_content = findViewById(R.id.show_content);
        note_group=findViewById(R.id.note_group);
        textLength=findViewById(R.id.textLength);

    }

    private void listener() {

    }

    //加载 tool_bar_show.xml这个菜单文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_show, menu);
        return true;
    }

    //处理多个按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                //返回主页
                Toast.makeText(this, "返回主页❤", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowNoteContentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.collect:
                Toast.makeText(this, "收藏成功❤", Toast.LENGTH_SHORT).show();
                // TODO: 2021/12/9 写一个收藏当前note的方法
                collectNoteSucceed();
                break;

            case R.id.rewrite:
                Toast.makeText(this, "重写note❤", Toast.LENGTH_SHORT).show();
                // TODO: 2021/12/9 写一个重写当前note的方法
                rewriteNote();
                break;

        }

        return true;
    }

    //重写当前note的标题或内容
    private void rewriteNote() {
    }

    //收藏note
    private void collectNoteSucceed() {

    }

    @SuppressLint("SetTextI18n")
    private void showNoteContent() {//展示 note
        service = new NoteService(this);
        ArrayList<Note> data = service.getAllNote();
        /*
         * getIntent() -> 返回启动此活动
         *
         * getExtras() -> 获取页面传递过来的参数数组
         *
         * getInt() -> 返回与给定键关联的值，如果给定键不存在所需类型的映射，则返回0。
         * */
        int position = getIntent().getExtras().getInt("index");//获取当前位置

        if (!data.isEmpty()) {
            //note标题
            String currentTitle = data.get(position).getNoteTitle();
            //note内容
            String currentContent = data.get(position).getNoteContent();
            //note分组
            int currentNoteGroup=data.get(position).getGroup();

            //note内容的字数长度
            int currentNoteLength=data.get(position).getNoteLength();

            show_title.setText(currentTitle);

            show_content.setText(currentContent);

            //显示分组
            switch (currentNoteGroup){
                case 0:note_group.setText("未分组");break;
                case 1:note_group.setText("学习");break;
                case 2:note_group.setText("工作");break;
            }


            textLength.setText(String.valueOf(currentNoteLength));


        }
    }
}
