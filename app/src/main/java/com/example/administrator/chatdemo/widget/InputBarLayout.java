package com.example.administrator.chatdemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.chatdemo.R;
import com.example.administrator.chatdemo.adapter.ChatEmotionGridAdapter;
import com.example.administrator.chatdemo.adapter.ChatEmotionPagerAdapter;
import com.example.administrator.chatdemo.utils.EmotionHelper;
import com.example.administrator.chatdemo.utils.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dwq on 2017/7/19/019.
 * e-mail:lomapa@163.com
 */

public class InputBarLayout extends LinearLayout {
    private EditText mEditTextContent;
    private ImageView ivMore;
    private ImageView ivEmoji;
    private ImageView ivVoice;
    private ViewPager emotionPager;
    private AudioRecordButton voiceBtn;
    private LinearLayout llyEmojiGroup;
    private TextView tvSend;
    private ChatBottomView cbvOther;

    private CircleIndicator ciBanner;

    private OnMessageSendListener onMessageSendListener;

    private OnBottomIconClickListener onBottomIconClickListener;

    public void setOnMessageSendListener(OnMessageSendListener onMessageSendListener) {
        this.onMessageSendListener = onMessageSendListener;
    }

    public void setOnBottomIconClickListener(OnBottomIconClickListener onBottomIconClickListener) {
        this.onBottomIconClickListener = onBottomIconClickListener;
    }

    public InputBarLayout(Context context) {
        super(context);
        initView(context);
    }

    public InputBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public InputBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.chat_input_bottom_bar_layout, this);
        mEditTextContent = (EditText) findViewById(R.id.et_msg);
        ivMore = (ImageView) findViewById(R.id.iv_more);
        ivEmoji = (ImageView) findViewById(R.id.iv_emoji);
        ivVoice = (ImageView) findViewById(R.id.iv_voice);
        emotionPager = (ViewPager) findViewById(R.id.vPager);
        voiceBtn = (AudioRecordButton) findViewById(R.id.voice_btn);
        llyEmojiGroup = (LinearLayout) findViewById(R.id.lly_emoji_group);
        tvSend = (TextView) findViewById(R.id.tv_send);
        cbvOther = (ChatBottomView) findViewById(R.id.cbv_other);
        ciBanner = (CircleIndicator) findViewById(R.id.ci_banner);

        initListener(context);
    }

    private void initListener(final Context context) {
        mEditTextContent.setOnKeyListener(onKeyListener);

        ivVoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (voiceBtn.getVisibility() == View.GONE) {
                    ivEmoji.setBackgroundResource(R.mipmap.ic_btn_emoji);
                    ivMore.setBackgroundResource(R.mipmap.ic_btn_more);
                    mEditTextContent.setVisibility(View.GONE);
                    llyEmojiGroup.setVisibility(View.GONE);
                    cbvOther.setVisibility(View.GONE);
                    voiceBtn.setVisibility(View.VISIBLE);
                    KeyBoardUtils.hideKeyBoard(context,
                            mEditTextContent);
                    ivVoice.setBackgroundResource(R.mipmap.ic_btn_keybroad);
                } else {
                    mEditTextContent.setVisibility(View.VISIBLE);
                    voiceBtn.setVisibility(View.GONE);
                    ivVoice.setBackgroundResource(R.mipmap.ic_btn_voice);
                    KeyBoardUtils.showKeyBoard(context, mEditTextContent);
                }
            }

        });

        ivMore.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                llyEmojiGroup.setVisibility(View.GONE);
                if (cbvOther.getVisibility() == View.GONE
                        ) {
                    mEditTextContent.setVisibility(View.VISIBLE);
                    ivMore.setFocusable(true);
                    voiceBtn.setVisibility(View.GONE);
                    ivEmoji.setBackgroundResource(R.mipmap.ic_btn_emoji);
                    ivVoice.setBackgroundResource(R.mipmap.ic_btn_voice);
                    cbvOther.setVisibility(View.VISIBLE);
                    KeyBoardUtils.hideKeyBoard(context,
                            mEditTextContent);
                } else {
                    cbvOther.setVisibility(View.GONE);
                    KeyBoardUtils.showKeyBoard(context, mEditTextContent);
                }
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 发送消息
                String message = mEditTextContent.getText().toString().trim();
                if (onMessageSendListener != null && !TextUtils.isEmpty(message)) {
                    onMessageSendListener.sendMessage(message);
                    mEditTextContent.setText("");
                }
            }
        });

        mEditTextContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  收起部分布局
                llyEmojiGroup.setVisibility(View.GONE);
                cbvOther.setVisibility(View.GONE);
                ivEmoji.setBackgroundResource(R.mipmap.ic_btn_emoji);
                ivVoice.setBackgroundResource(R.mipmap.ic_btn_voice);
            }

        });

        cbvOther.setOnHeadIconClickListener(new HeadIconSelectorView.OnHeadIconClickListener() {
            @Override
            public void onClick(int from) {
                if (onBottomIconClickListener != null)
                    onBottomIconClickListener.onIconClickListener(from);

            }
        });


        ivEmoji.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  显示或收起表情列表
                cbvOther.setVisibility(View.GONE);
                if (llyEmojiGroup.getVisibility() == View.GONE) {
                    mEditTextContent.setVisibility(View.VISIBLE);
                    voiceBtn.setVisibility(View.GONE);
                    ivVoice.setBackgroundResource(R.mipmap.ic_btn_voice);
                    llyEmojiGroup.setVisibility(View.VISIBLE);
                    ivEmoji.setBackgroundResource(R.mipmap.ic_btn_keybroad);
                    KeyBoardUtils.hideKeyBoard(context,
                            mEditTextContent);
                } else {
                    llyEmojiGroup.setVisibility(View.GONE);
                    ivEmoji.setBackgroundResource(R.mipmap.ic_btn_emoji);
                    KeyBoardUtils.showKeyBoard(context, mEditTextContent);
                }
            }
        });

        ivEmoji.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  显示或收起表情列表
                cbvOther.setVisibility(View.GONE);
                if (llyEmojiGroup.getVisibility() == View.GONE) {
                    mEditTextContent.setVisibility(View.VISIBLE);
                    voiceBtn.setVisibility(View.GONE);
                    ivVoice.setBackgroundResource(R.mipmap.ic_btn_voice);
                    llyEmojiGroup.setVisibility(View.VISIBLE);
                    ivEmoji.setBackgroundResource(R.mipmap.ic_btn_keybroad);
                    KeyBoardUtils.hideKeyBoard(context,
                            mEditTextContent);
                } else {
                    llyEmojiGroup.setVisibility(View.GONE);
                    ivEmoji.setBackgroundResource(R.mipmap.ic_btn_emoji);
                    KeyBoardUtils.showKeyBoard(context, mEditTextContent);
                }
            }
        });

        initEmotionPager();

        voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onStart() {
                if (onMessageSendListener != null)
                    onMessageSendListener.onVoiceRecordStart();
            }

            @Override
            public void onFinished(float seconds, String filePath) {
                if (onMessageSendListener != null)
                    onMessageSendListener.sendVoice(seconds, filePath);
            }
        });
    }

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                // 发送消息
                String message = mEditTextContent.getText().toString().trim();
                if (onMessageSendListener != null && !TextUtils.isEmpty(message)) {
                    onMessageSendListener.sendMessage(message);
                    mEditTextContent.setText("");
                }
                return true;
            }
            return false;
        }
    };

    /**
     * 初始化 emotionPager
     */
    private void initEmotionPager() {
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < EmotionHelper.emojiGroups.size(); i++) {
            views.add(getEmotionGridView(i));
        }
        ChatEmotionPagerAdapter pagerAdapter = new ChatEmotionPagerAdapter(views);
        emotionPager.setOffscreenPageLimit(4);
        emotionPager.setAdapter(pagerAdapter);
        ciBanner.setViewPager(emotionPager);
    }

    private View getEmotionGridView(int pos) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View emotionView = inflater.inflate(R.layout.chat_emotion_gridview, null, false);
        GridView gridView = (GridView) emotionView.findViewById(R.id.gridview);
        final ChatEmotionGridAdapter chatEmotionGridAdapter = new ChatEmotionGridAdapter(getContext());
        List<String> pageEmotions = EmotionHelper.emojiGroups.get(pos);
        chatEmotionGridAdapter.setDatas(pageEmotions);
        gridView.setAdapter(chatEmotionGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String emotionText = (String) parent.getAdapter().getItem(position);
                int start = mEditTextContent.getSelectionStart();
                StringBuilder sb = new StringBuilder(mEditTextContent.getText());
                sb.replace(mEditTextContent.getSelectionStart(), mEditTextContent.getSelectionEnd(), emotionText);
                mEditTextContent.setText(sb.toString());

                CharSequence info = mEditTextContent.getText();
                if (info != null) {
                    Spannable spannable = (Spannable) info;
                    Selection.setSelection(spannable, start + emotionText.length());
                }
            }
        });
        return gridView;
    }

    public void hideBottomLayout() {
        llyEmojiGroup.setVisibility(GONE);
        ivEmoji.setBackgroundResource(R.mipmap.ic_btn_emoji);
        cbvOther.setVisibility(GONE);
    }

    public interface OnMessageSendListener {
        void sendMessage(String message);

        void sendVoice(float seconds, String filePath);

        void onVoiceRecordStart();
    }

    public interface OnBottomIconClickListener {
        void onIconClickListener(int from);
    }
}
