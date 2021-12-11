package com.example.note;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

public class MsDelEditText extends androidx.appcompat.widget.AppCompatEditText {

    private Drawable imgClear;
    private Context mContext;

    public MsDelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init() {
        imgClear = mContext.getResources().getDrawable(R.drawable.close);
//        TextWatcher 针对 EditText 监听的方法
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                内容变化前触发
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                内容变化中触发
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                内容变化后触发
                setDrawable();
                getTextNumber();
            }
        });
    }

    private void getTextNumber() {
        if (length()>1){
            Toast.makeText(getContext(),"当前输入的字数为："+length(), Toast.LENGTH_SHORT).show();
        }
    }

    //绘制删除图片
    private void setDrawable(){
        if (length() < 1)//如果 无 文本
            //可以在上、下、左、右设置图标，
            // 如果不想在某个地方显示，则设置为null。
            //图标的宽高将会设置为固有宽高，
            // 既自动通过getIntrinsicWidth和getIntrinsicHeight获取。
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        else
            //有 文本 出现 关闭 图片
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgClear, null);
    }

    //当触摸范围在右侧时，触发删除方法，隐藏叉叉
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(imgClear != null && event.getAction() == MotionEvent.ACTION_UP)
        {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            //getGlobalVisibleRect() 是view可见区域相对与屏幕来说的坐标位置.
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (rect.contains(eventX, eventY))
                //清空文字
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        //垃圾回收
        super.finalize();
    }

}

