package com.example.note.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class DialogUtil {

    public static void tipDialog(Context context, String title, String tipStr, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setIcon(icon).setTitle(title)
                .setMessage(tipStr).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }

    //点击确定可以跳转页面的dialog
    public static void gotoActivityTipDialog(final Context context, String title, String tipStr, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(tipStr).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.startActivity(intent);
                    }
                });
        builder.create().show();
    }

    public interface sureToDoSthListener{
        void doSth();
    }

    //点击确定后执行其他函数的dialog
    public static void tipAndDoSthDialog(Context context, String title, String tipStr, int icon, final sureToDoSthListener sureToDoSthListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setIcon(icon).setTitle(title)
                .setMessage(tipStr).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sureToDoSthListener.doSth();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    public interface DoSthListener{
        void PositiveDoSth();
        void NegativeDoSth();
    }
    //点击确定后执行其他函数的dialog
    public static void doSthAfterChoiceDialog(Context context, String title, String tipStr, int icon, final DoSthListener DoSthListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setIcon(icon).setTitle(title)
                .setMessage(tipStr).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DoSthListener.PositiveDoSth();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DoSthListener.NegativeDoSth();
                    }
                });
        builder.create().show();
    }
}
