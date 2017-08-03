package com.example.administrator.chatdemo.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.chatdemo.R;
import com.example.administrator.chatdemo.bean.ChatMessageBean;
import com.example.administrator.chatdemo.listener.PlayButtonClickListener;
import com.example.administrator.chatdemo.widget.PlayButton;


/**
 * Created by wli on 15/9/17.
 */
public class ChatItemAudioHolder extends ChatItemHolder {

    public PlayButton playButton;
    protected TextView durationView;
    private ImageView ivAudioUnread;
    private String path;

    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private int mMaxItemWith;

    public ChatItemAudioHolder(Context context, ViewGroup root, boolean isLeft) {
        super(context, root, isLeft);
    }

    @Override
    public void initView() {
        super.initView();
        // 获取系统宽度
        WindowManager wManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.2f);
        if (isLeft) {
            conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_left_audio_layout, null));
            ivAudioUnread = (ImageView) itemView.findViewById(R.id.chat_item_audio_unread);
        } else {
            conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_right_audio_layout, null));
        }
        playButton = (PlayButton) itemView.findViewById(R.id.chat_item_audio_play_btn);
        durationView = (TextView) itemView.findViewById(R.id.chat_item_audio_duration_view);
    }

    @Override
    public void bindData(Object o) {
        super.bindData(o);
        if (o instanceof ChatMessageBean) {
            final ChatMessageBean audioMessage = (ChatMessageBean) o;
            durationView.setText(String.format("%.0f\"", audioMessage.getUserVoiceTime()));
            ViewGroup.LayoutParams layoutParams = playButton.getLayoutParams();
            layoutParams.width = (int) (mMinItemWith + mMaxItemWith / 60f
                    * audioMessage.getUserVoiceTime());
            playButton.setLayoutParams(layoutParams);
            String localFilePath = audioMessage.getUserVoicePath();
            final boolean leftSide = audioMessage.getType() == 1;
            if (!TextUtils.isEmpty(localFilePath)) {
                path = localFilePath;
            } else {
                path = audioMessage.getUserVoiceUrl();
//        LocalCacheUtils.downloadFileAsync(audioMessage.getUserVoiceUrl(), path);
            }
            playButton.setPath(path);
            if (leftSide && ivAudioUnread != null)
                if (audioMessage.getIsListened()) {
                    ivAudioUnread.setVisibility(View.INVISIBLE);
                }
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PlayButtonClickListener(playButton, ivAudioUnread, audioMessage, leftSide, getContext(), path).onClick(v);
                }
            });
        }
    }

}