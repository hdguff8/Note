package com.example.note.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.note.R;
import com.example.note.Database.GroupService;
import com.example.note.Util.*;
import com.example.note.Model.*;

import java.util.ArrayList;

public class GroupManager extends AppCompatActivity {

    private static final String TAG = "GroupManager";

    Spinner spinner;
    ListView listView;

    GroupService gdb;

    ArrayList<String> allGroupsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);

        init();
    }

    private void init(){
        gdb = new GroupService(this);

        //设置Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_group_manage));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        listView = findViewById(R.id.listview_group_manager);
        setListView();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i<2){
                    DialogUtil.tipDialog(GroupManager.this,"提示","此分组不能被删除",R.drawable.icon_warning);
                }else {
                    DialogUtil.tipAndDoSthDialog(GroupManager.this, "提示", "确认是否删除该分组？\n所有使用此分组的速记都会变成默认分组！", R.drawable.icon_warning, new DialogUtil.sureToDoSthListener() {
                        @Override
                        public void doSth() {
                            Log.d(TAG, "deleteGroupId" + gdb.getGroupIdByName(allGroupsName.get(i)));
                            gdb.deleteGroup(gdb.getGroupIdByName(allGroupsName.get(i)));
                            setListView();
                        }
                    });
                }
                return false;
            }
        });



    }

    private void setListView(){
        allGroupsName = gdb.getAllGroupName(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,allGroupsName);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_group_toolbar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent(GroupManager.this,MainActivity.class));
        }else if(item.getItemId() == R.id.manage_group_toolbar_add){
            showNewGroupDialog();
        }

        return true;
    }

    private void showNewGroupDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(GroupManager.this);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(GroupManager.this);
        inputDialog.setTitle("创建的新组名称").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gdb.insertGroup(new Group(gdb.getMaxId()+1,editText.getText().toString()));
                        setListView();
                    }
                }).show();
    }
}