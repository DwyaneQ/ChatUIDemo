package com.example.administrator.chatdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.chatdemo.adapter.ChatRecyclerAdapter;
import com.example.administrator.chatdemo.bean.ChatConst;
import com.example.administrator.chatdemo.bean.ChatMessageBean;
import com.example.administrator.chatdemo.bean.ChatMessageType;
import com.example.administrator.chatdemo.bean.DaoSession;
import com.example.administrator.chatdemo.utils.ImageCheckoutUtil;
import com.example.administrator.chatdemo.utils.PathUtils;
import com.example.administrator.chatdemo.widget.ChatBottomView;
import com.example.administrator.chatdemo.widget.InputBarLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceChatActivity extends AppCompatActivity {

    private View activityRootView;

    private static final int SDK_PERMISSION_REQUEST = 127;
    private static final int IMAGE_SIZE = 100 * 1024;// 300kb
    private String permissionInfo;
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private boolean CAN_RECORD_AUDIO = true;
    private String camPicPath;
    private InputBarLayout inputbarLayout;
    private RecyclerView rvChat;
    private LinearLayoutManager layoutManager;

    public List<ChatMessageBean> messageList = new ArrayList<>();
    private ChatRecyclerAdapter chatRecyclerAdapter;

    public String userName = "Dwq";//聊天对象昵称

    public static final int SEND_OK = 0x1110;
    public static final int REFRESH = 0x0011;
    public static final int RECERIVE_OK = 0x1111;
    public static final int PULL_TO_REFRESH_DOWN = 0x0111;
    private SendMessageHandler sendMessageHandler;
    private String content;
    private DaoSession daoSession;
    private String voiceFilePath;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_chat);
        initView();
        initListener();
        initData();
        getPersimmions();
    }


    private void initView() {
        activityRootView = findViewById(R.id.layout_tongbao_rl);
        inputbarLayout = (InputBarLayout) findViewById(R.id.input_bar);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        rvChat = (RecyclerView) findViewById(R.id.rv_chat);
        layoutManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(layoutManager);
    }

    private void initListener() {
        inputbarLayout.setOnBottomIconClickListener(new InputBarLayout.OnBottomIconClickListener() {
            @Override
            public void onIconClickListener(int from) {
                switch (from) {
                    case ChatBottomView.FROM_CAMERA:// 相机
                        if (!CAN_WRITE_EXTERNAL_STORAGE) {
                            Toast.makeText(ServiceChatActivity.this, "权限未开通\n请到设置中开通相册权限", Toast.LENGTH_SHORT).show();
                        } else {
                            camPicPath = PathUtils.getSavePicPath(ServiceChatActivity.this);
                            Intent openCameraIntent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri uri = Uri.fromFile(new File(camPicPath));
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(openCameraIntent,
                                    ChatBottomView.FROM_CAMERA);
                        }
                        break;
                    case ChatBottomView.FROM_GALLERY:// 相册
                        PictureSelector.create(ServiceChatActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .imageSpanCount(4)// 每行显示个数
                                .selectionMode(PictureConfig.SINGLE)
                                .previewImage(true)
                                .compress(true)
                                .isCamera(false)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                }
            }
        });

        inputbarLayout.setOnMessageSendListener(new InputBarLayout.OnMessageSendListener() {
            @Override
            public void sendMessage(final String msg) {// 发送文字消息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        messageList.add(getTbub(userName, ChatMessageType.TextMessageType.getType(), msg, null, null,
//                                null, null, null, 0f, ChatConst.COMPLETED));
                        chatRecyclerAdapter.addMessage(getTbub(userName, 0, ChatMessageType.TextMessageType.getType(),
                                msg, null, null, null, null, null, 0f, ChatConst.COMPLETED));
                        sendMessageHandler.sendEmptyMessage(SEND_OK);
                        ServiceChatActivity.this.content = msg;
                        receriveHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                }).start();
            }

            @Override
            public void sendVoice(float seconds, String voicePath) {// 发送语音消息
                sendAudio(seconds, voicePath);
            }

            @Override
            public void onVoiceRecordStart() {
                chatRecyclerAdapter.pausePlayer();
            }
        });

        rvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                inputbarLayout.hideBottomLayout();
                return false;
            }
        });
    }

    private void initData() {

        daoSession = ((App) getApplication()).getDaoSession();
        loadLocalMessage();
        chatRecyclerAdapter = new ChatRecyclerAdapter(this, messageList);
        chatRecyclerAdapter.resetRecycledViewPoolSize(rvChat);

        rvChat.setAdapter(chatRecyclerAdapter);
        rvChat.scrollToPosition(messageList.size() - 1);
        sendMessageHandler = new SendMessageHandler(this);

    }

    private void loadLocalMessage() {
        if (messageList != null) {
            messageList.clear();
        }
        List<ChatMessageBean> chatMessageBeen = daoSession.loadAll(ChatMessageBean.class);
        messageList.addAll(chatMessageBeen);
    }


    @TargetApi(23)
    protected void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 麦克风权限
            if (addPermission(permissions, Manifest.permission.RECORD_AUDIO)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SDK_PERMISSION_REQUEST:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission Denied
                    CAN_WRITE_EXTERNAL_STORAGE = false;
                    Toast.makeText(this, "禁用图片权限将导致发送图片功能无法使用！", Toast.LENGTH_SHORT)
                            .show();
                }
                if (perms.get(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    CAN_RECORD_AUDIO = false;
                    Toast.makeText(this, "禁用录制音频权限将导致语音功能无法使用！", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ChatBottomView.FROM_CAMERA:
                    FileInputStream is = null;
                    try {
                        is = new FileInputStream(camPicPath);
                        File camFile = new File(camPicPath); // 图片文件路径
                        if (camFile.exists()) {
                            int size = ImageCheckoutUtil
                                    .getImageSize(ImageCheckoutUtil
                                            .getLoacalBitmap(camPicPath));
//                            if (size > IMAGE_SIZE) {
//                                showDialog(camPicPath);
//                                Toast.makeText(this, "ImgPath=" + camPicPath, Toast.LENGTH_SHORT).show();
//                            } else {
                            sendImage(camPicPath);
//                                Toast.makeText(this, "ImgPath=" + camPicPath, Toast.LENGTH_SHORT).show();
//                            }
                        } else {
                            Toast.makeText(this, "该文件不存在", Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        // 关闭流
                        try {
                            is.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    LocalMedia media = selectList.get(0);
                    String path = media.getPath();
                    sendImage(path);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                    mDesignCenterView.refreshSelectedPicture(selectList);
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(this, "操作取消", Toast.LENGTH_SHORT).show();
        }
    }

    public ChatMessageBean getTbub(String username, int type, int messageType,
                                   String Content, String imageIconUrl, String imageUrl,
                                   String imageLocal, String userVoicePath, String userVoiceUrl,
                                   Float userVoiceTime, @ChatConst.SendState int sendState) {
        ChatMessageBean tbub = new ChatMessageBean();
        tbub.setUserName(username);
        long time = System.currentTimeMillis();
        Log.i("serviceChat", "currentTime:" + time);
        tbub.setTime(time);
        tbub.setType(type);
        tbub.setMessagetype(messageType);
        tbub.setUserContent(Content);
        tbub.setImageIconUrl(imageIconUrl);
        tbub.setImageUrl(imageUrl);
        tbub.setUserVoicePath(userVoicePath);
        tbub.setUserVoiceUrl(userVoiceUrl);
        tbub.setUserVoiceTime(userVoiceTime);
        tbub.setSendState(sendState);
        tbub.setImageLocal(imageLocal);
        daoSession.insert(tbub);

        return tbub;
    }

    private String filePath = "";

    protected void sendImage(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatRecyclerAdapter.addMessage(getTbub(userName, 0, ChatMessageType.ImageMessageType.getType(), null, null, null, filePath, null, null,
                        0f, ChatConst.COMPLETED));
//                imageList.add(tblist.get(tblist.size() - 1).getImageLocal());
//                imagePosition.put(tblist.size() - 1, imageList.size() - 1);
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                ServiceChatActivity.this.filePath = filePath;
                receriveHandler.sendEmptyMessageDelayed(1, 3000);
            }
        }).start();
    }


    /**
     * 发送语音
     */
    protected void sendAudio(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatRecyclerAdapter.addMessage(getTbub(userName, 0, ChatMessageType.AudioMessageType.getType(), null, null, null, null, filePath,
                        null, seconds, ChatConst.COMPLETED));
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                ServiceChatActivity.this.seconds = seconds;
                voiceFilePath = filePath;
                receriveHandler.sendEmptyMessageDelayed(2, 3000);
            }
        }).start();
    }


    /**
     * 为了模拟接收延迟
     */
    private Handler receriveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    receriveMsgText(content);
                    break;
//                case 1:
//                    receriveImageText(filePath);
//                    break;
                case 2:
                    receriveVoiceText(seconds, voiceFilePath);
                    break;
                default:
                    break;
            }
        }
    };

    private void receriveMsgText(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message = "回复：" + content;
                ChatMessageBean tbub = new ChatMessageBean();
                tbub.setUserName(userName);
                long time = System.currentTimeMillis();
                tbub.setUserContent(message);
                tbub.setMessagetype(ChatMessageType.TextMessageType.getType());
                tbub.setTime(time);
                tbub.setType(1);
                tbub.setSendState(ChatConst.COMPLETED);
                chatRecyclerAdapter.addMessage(tbub);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
                daoSession.insert(tbub);
            }
        }).start();
    }

    /**
     * 接收语音
     */
    float seconds = 0.0f;

    private void receriveVoiceText(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatMessageBean tbub = new ChatMessageBean();
                tbub.setUserName(userName);
                long time = System.currentTimeMillis();
                tbub.setTime(time);
                tbub.setUserVoiceTime(seconds);
                tbub.setUserVoicePath(filePath);
                tbub.setType(1);
                tbub.setMessagetype(ChatMessageType.AudioMessageType.getType());
                tbub.setSendState(ChatConst.COMPLETED);
                chatRecyclerAdapter.addMessage(tbub);
                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
                daoSession.insert(tbub);
            }
        }).start();
    }

    static class SendMessageHandler extends Handler {
        WeakReference<ServiceChatActivity> mActivity;

        SendMessageHandler(ServiceChatActivity activity) {
            mActivity = new WeakReference<ServiceChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            ServiceChatActivity theActivity = mActivity.get();
            if (theActivity != null) {
                switch (msg.what) {
//                    case REFRESH:
//                        theActivity.tbAdapter.isPicRefresh = true;
//                        theActivity.tbAdapter.notifyDataSetChanged();
//                        int position = theActivity.tbAdapter.getItemCount() - 1 < 0 ? 0 : theActivity.tbAdapter.getItemCount() - 1;
//                        theActivity.myList.smoothScrollToPosition(position);
//                        break;
                    case SEND_OK:
//                        theActivity.chatRecyclerAdapter.isPicRefresh = true;
//                        theActivity.chatRecyclerAdapter.addMessage;
                        theActivity.chatRecyclerAdapter.notifyItemInserted(theActivity.messageList
                                .size() - 1);
                        theActivity.rvChat.smoothScrollToPosition(theActivity.chatRecyclerAdapter.getItemCount() - 1);
                        break;
                    case RECERIVE_OK:
//                        theActivity.chatRecyclerAdapter.isPicRefresh = true;
                        theActivity.chatRecyclerAdapter.notifyItemInserted(theActivity.messageList
                                .size() - 1);
                        theActivity.rvChat.smoothScrollToPosition(theActivity.chatRecyclerAdapter.getItemCount() - 1);
                        break;
//                    case PULL_TO_REFRESH_DOWN:
//                        theActivity.pullList.refreshComplete();
//                        theActivity.tbAdapter.notifyDataSetChanged();
//                        theActivity.rvChat.smoothScrollToPosition(theActivity.position - 1);
//                        theActivity.isDown = false;
//                        break;
                    default:
                        break;
                }
            }
        }

    }
}

