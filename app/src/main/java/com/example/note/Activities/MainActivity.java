package com.example.note.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.note.Adapter.NoteAdapter;
import com.example.note.Database.NoteService;
import com.example.note.Model.Note;
import com.example.note.R;
import com.example.note.Util.LogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private Toolbar tool_bar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private FloatingActionButton add_note;//底部的增加按钮


    private ListView list_view;//note列表

    //适配器
    NoteAdapter mAdapter;

    //操作数据库
    NoteService service;

    ImageView icon;
    TextView message;

    final private int OPEN = 111;

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        listener();

        //动态权限申请
        int permisson = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permisson != PackageManager.PERMISSION_GRANTED) {
            //动态去申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // TODO: 2021/12/9 其中的一个权限相关的操作（暂时没做）
        }
    }

    private void init() {

        //绑定 NavigationView控件
        navView = findViewById(R.id.navView);

        //绑定 FloatingActionButton控件
        add_note = findViewById(R.id.add_note);

        //绑定 DrawerLayout控件
        drawerLayout = findViewById(R.id.drawer_layout);

        //绑定 Toolbar控件
        tool_bar = findViewById(R.id.tool_bar);

        //得到toolBar实例
        setSupportActionBar(tool_bar);

        //显示 tool_bar左侧按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置 tool_bar左侧按钮图标
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.nav_icon_small);

        //便签列表
        list_view = findViewById(R.id.list_view);

        //更新列表
        service = new NoteService(this);

        upListData();

    }


    //加载 menu.xml这个菜单文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //处理多个按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //按下home按钮 打开滑动菜单
                //openDrawer() 传入gravity参数 确保这里的行为和xml中定义的一致
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.help:
                //显示 软件使用方法
                // TODO: 2021/12/9 写一个dialog显示软件使用方法
                Toast.makeText(this, "软件使用方法介绍", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    private void upListData() {

        //给适配器绑上数据
        ArrayList<Note> data = service.getAllNote();

        mAdapter = new NoteAdapter(MainActivity.this, data);

        //给listView绑定适配器
        list_view.setAdapter(mAdapter);

        //跟新listView
        mAdapter.notifyDataSetChanged();
        list_view.setSelection(service.getAllNote().size() - 1);
    }

    private void listener() {

        //设置 一个菜单选项 选中事件 的监听器
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            //当用户点击了任意菜单，回调到onNavigationItemSelected方法中
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                //显示 当前点击了哪个item
                switch (item.getItemId()) {
                    case R.id.note_group:
                        // TODO: 2021/12/9 这里需要一个询问想查看的分组的dialog
                        Toast.makeText(getApplication(), "你点击了note_group", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.note_collect:
                        // TODO: 2021/12/9 这里需要做一个收藏功能
                        Toast.makeText(getApplication(), "你点击了note_collect", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.note_setting:
                        // TODO: 2021/12/9 这里需要做一个收藏功能
                        Toast.makeText(getApplication(), "你点击了note_setting", Toast.LENGTH_SHORT).show();
                        break;
                }

                //关闭 滑动菜单
                drawerLayout.closeDrawers();

                return true;
            }
        });

//        监听 FloatingActionButton控件
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到新建便签页面
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });


        //长按删除
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //测试语句 检查删除完还有几条note
                LogUtil.d("MainActivity","onItemLongClick: "+service.getAllNote().size());

                ArrayList<Note> data = service.getAllNote();
                //如果不为空则可进行删除
                if (!data.isEmpty()) {

                    service.deleteNote(data.get(position));
                    //给listView绑定适配器
                    list_view.setAdapter(mAdapter);
                    //跟新listView
                    mAdapter.notifyDataSetChanged();
                    list_view.setSelection(service.getAllNote().size() - 1);

                    //显示删除成功
                    showDelSucceed();

                    //更新列表
                    upListData();
                } else {

                    //显示没有可以删除的东西信息
                    showNoneDel();
                }
                return true;
            }
        });

        //点击显示细节内容
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ShowNoteContentActivity.class);
                intent.putExtra("index", position);
                startActivity(intent);
                ArrayList<Note> data = service.getAllNote();
//        测试要用的数据
//        String currentTitle = data.get(position).getNoteTitle();
//        String currentContent = data.get(position).getNoteContent();
//        int currentNoteGroup = data.get(position).getGroup();
//        int currentNoteLength = data.get(position).getNoteLength();
                int currentId = data.get(position).getId();

                Toast.makeText(getApplicationContext(), "id :" + currentId, LENGTH_SHORT).show();
//      更详细的测试语句
//      Toast.makeText(MainActivity.this, "id :"+currentId+",标题： " + currentTitle + ",内容： " + currentContent + ",分组： " + currentNoteGroup+",字数： "+currentNoteLength, Toast.LENGTH_SHORT).show();

            }
        });
    }

    //显示没有可以删除的东西
    private void showNoneDel() {
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.toast_view, (ViewGroup) findViewById(R.id.custom_toast));
        icon = view1.findViewById(R.id.icon);
        message = view1.findViewById(R.id.message);
        message.setText(R.string.nothing_to_del);
        Toast toast = new Toast(MainActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view1);
        toast.show();
    }

    //显示删除成功信息
    private void showDelSucceed() {
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.toast_view, (ViewGroup) findViewById(R.id.custom_toast));
        icon = view1.findViewById(R.id.icon);
        message = view1.findViewById(R.id.message);
        message.setText(R.string.del_success);
        Toast toast = new Toast(MainActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view1);
        toast.show();
    }

}