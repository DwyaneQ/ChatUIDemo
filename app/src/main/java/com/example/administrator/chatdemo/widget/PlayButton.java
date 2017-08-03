package com.example.administrator.chatdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.chatdemo.R;
import com.example.administrator.chatdemo.bean.ChatMessageBean;


/**
 * Created by lzw on 14-9-22.
 */
public class PlayButton extends TextView implements View.OnClickListener {
    private String path;
    private boolean leftSide;
    private AnimationDrawable anim;


    private ChatMessageBean messageBean;

    public PlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        leftSide = getLeftFromAttrs(context, attrs);
        setLeftSide(leftSide);
        setOnClickListener(this);
    }

    public void setLeftSide(boolean leftSide) {
        this.leftSide = leftSide;
        stopRecordAnimation();
    }

    public boolean getLeftFromAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatPlayBtn);
        boolean left = true;
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.ChatPlayBtn_left) {
                left = typedArray.getBoolean(attr, true);
            }
        }
        return left;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMessageBean(ChatMessageBean messageBean) {
        this.messageBean = messageBean;
    }

    @Override
    public void onClick(View v) {

//        new PlayButtonClickListener(this, leftSide, getContext(), path).onClick(this);
    }

    public void startRecordAnimation() {
        Log.i("PlayButton", "startRecordAnimation" + leftSide);
        setCompoundDrawablesWithIntrinsicBounds(leftSide ? R.drawable.chat_anim_voice_left : 0,
                0, !leftSide ? R.drawable.chat_anim_voice_right : 0, 0);
        anim = (AnimationDrawable) getCompoundDrawables()[leftSide ? 0 : 2];
        anim.start();
    }

    //
    public void stopRecordAnimation() {
        Log.i("PlayButton", "stopRecordAnimation" + leftSide);
        setCompoundDrawablesWithIntrinsicBounds(leftSide ? R.drawable.chat_voice_right3 : 0,
                0, !leftSide ? R.drawable.chat_voice_left3 : 0, 0);
        if (anim != null) {
            anim.stop();
        }
    }


}
