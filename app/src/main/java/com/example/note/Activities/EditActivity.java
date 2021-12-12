package com.example.note.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.note.Database.NoteService;
import com.example.note.Model.Note;
import com.example.note.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private Toolbar tool_bar_setting;

    private EditText edit_content;//note内容
    private EditText edit_title;//note标题
    private TextView current_content_length;//当前note内容的字数
    private int group = 0;//默认分组标识为0

    List<Note> mNotes = new ArrayList<>();//装notes的大抽屉~
    private Calendar calendar;
    NoteService service;

    ImageView icon;
    TextView message;

    int choice;

    private void showSingDialog() {

        final String[] items = {"默认分组", "学习", "工作"};

        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(EditActivity.this, R.style.oval_dialog);

        singleChoiceDialog.setTitle("请选择便签分组：");

        //第二个参数是默认的选项
        singleChoiceDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                choice = which;
            }

        });

        singleChoiceDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (choice != -1) {

                    Toast.makeText(EditActivity.this, "你选择了" + items[choice], Toast.LENGTH_SHORT).show();

                    group = choice;

                }
            }
        });

        singleChoiceDialog.show();
    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation
//            改变这个活动的期望方向。
//            如果活动当前在前台或以其他方式影响屏幕方向，屏幕将立即改变(可能导致活动重新启动)。
//            否则，这将在下次活动可见时使用。
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        listener();
    }

    @SuppressLint("SetTextI18n")
    private void init() {

        //绑定 Toolbar
        tool_bar_setting = findViewById(R.id.tool_bar_setting);

        //得到toolBar实例
        setSupportActionBar(tool_bar_setting);

        //显示 tool_bar左侧按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        service = new NoteService(this);
        edit_content = findViewById(R.id.edit_content);
        edit_title = findViewById(R.id.edit_title);
        current_content_length = findViewById(R.id.current_content_length);

        showSingDialog();
    }

    private void listener() {

        //动态监听note内容的输入状态
        edit_content.addTextChangedListener(new TextWatcher() {

            //内容变化前触发
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //没改变edit之前的文字长度
                current_content_length.setText(String.valueOf(0));

                Toast.makeText(getApplicationContext(), "等待输入", Toast.LENGTH_SHORT).show();

            }

            //内容变化中触发
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Toast.makeText(getApplicationContext(), "正在输入中......", Toast.LENGTH_SHORT).show();
            }

            //内容变化后
            @Override
            public void afterTextChanged(Editable s) {
                //动态获取 当前note内容的字数
                int current_length = edit_content.getText().toString().length();
                //动态显示 当前note内容的字数
                current_content_length.setText(String.valueOf(current_length));
                Toast.makeText(getApplicationContext(), "结束 输入状态", Toast.LENGTH_SHORT).show();


            }
        });


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
                Toast.makeText(this, "返回主页❤", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.save:
                Toast.makeText(this, "保存成功❤", Toast.LENGTH_SHORT).show();
                //保存新建的内容
                saveAddNote();
                break;

        }

        return true;
    }

    /*
     * 保存新建的便签信息，并且返回到主页
     * */
    private void saveAddNote() {
        //便签标题
        String title = edit_title.getText().toString();

        //便签内容
        String content = edit_content.getText().toString();

        //获取 当前便签字数 -> 转成int 保存到数据库
        int content_length = Integer.parseInt(current_content_length.getText().toString());

        ///设置时间
        Calendar calendar = Calendar.getInstance();

        //月  月份从0开始 —> +1
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        //分
        int minute = calendar.get(Calendar.MINUTE);

        //秒
        int second = calendar.get(Calendar.SECOND);

        //标题或内容不能为空
        if (title.equals("") || content_length == 0) {

            //显示标题或内容不能都为空信息
            showCannotNull();

        } else {

            //存入数据库
            SaveNotesInDataBase(title, content, month, day, hour, minute, second, group, content_length);

            //显示添加成功信息
            showAddSucceed();
        }
    }

    private void showAddSucceed() {

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.toast_view, (ViewGroup) findViewById(R.id.custom_toast));
        icon = view.findViewById(R.id.icon);

        message = view.findViewById(R.id.message);

        message.setText(R.string.add_note_success);

        Toast toast = new Toast(EditActivity.this);

        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.setDuration(Toast.LENGTH_LONG);

        toast.setView(view);

        toast.show();

        Intent intent = new Intent(EditActivity.this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    //自定义Toast方法-标题或内容不能都为空
    private void showCannotNull() {

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.toast_view, (ViewGroup) findViewById(R.id.custom_toast));

        icon = view.findViewById(R.id.icon);

        message = view.findViewById(R.id.message);

        message.setText(R.string.title_content_null);

        Toast toast = new Toast(EditActivity.this);

        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.setDuration(Toast.LENGTH_LONG);

        toast.setView(view);

        toast.show();

    }

    //数据插入数据库
    private void SaveNotesInDataBase(String title, String content, int month, int day, int hour, int minute, int second, int group, int length) {//将数据插入数据库

        //把数据插入模型类
        Note note = new Note(title, content, month + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒", group, length);

        //把此条note加入我们的notes大抽屉
        mNotes.add(note);

        //本地数据库插入此条note数据
        service.insertNote(note);

    }


}
