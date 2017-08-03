package com.example.administrator.chatdemo.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.chatdemo.R;
import com.example.administrator.chatdemo.bean.ChatMessageBean;
import com.example.administrator.chatdemo.ui.ImageBrowserActivity;
import com.example.administrator.chatdemo.utils.Constants;
import com.example.administrator.chatdemo.utils.PhotoUtils;

import io.github.leibnik.chatimageview.ChatImageView;


/**
 * Created by wli on 15/9/17.
 */
public class ChatItemImageHolder extends ChatItemHolder {

    protected ChatImageView contentView;

    public ChatItemImageHolder(Context context, ViewGroup root, boolean isLeft) {
        super(context, root, isLeft);
    }

    @Override
    public void initView() {
        super.initView();
        if (isLeft) {
//            contentView.setBackgroundResource(R.drawable.rc_ic_bubble_left);
            conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_image_layout, null));
        } else {
//            contentView.setBackgroundResource(R.drawable.rc_ic_bubble_right);
            conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_right_image_layout, null));
        }
        contentView = (ChatImageView) itemView.findViewById(R.id.chat_item_image_view);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ImageBrowserActivity.class);
                intent.putExtra(Constants.IMAGE_LOCAL_PATH, message.getImageLocal());
                intent.putExtra(Constants.IMAGE_URL, message.getImageUrl());
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void bindData(Object o) {
        super.bindData(o);
//        contentView.setImageResource(0);
        ChatMessageBean message = (ChatMessageBean) o;
        if (message != null) {
            String localFilePath = message.getImageLocal();
//            if (!TextUtils.isEmpty(localFilePath)) {
//                ImageLoader.getInstance().displayImage("file://" + localFilePath, contentView);
//            } else {
            PhotoUtils.displayImageCacheElseNetwork(contentView, localFilePath, message.getImageUrl());
//            }
        }
    }
}