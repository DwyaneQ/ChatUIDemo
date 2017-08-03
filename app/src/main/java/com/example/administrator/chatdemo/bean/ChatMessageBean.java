package com.example.administrator.chatdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dwq on 2017/7/20/020.
 * e-mail:lomapa@163.com
 */
@Entity
public class ChatMessageBean {
    @Id
    private Long id;
    @Property(nameInDb = "UserId")
    private String UserId;
    @Property(nameInDb = "UserName")
    private String UserName;
    @Property(nameInDb = "UserHeadIcon")
    private String UserHeadIcon;
    @Property(nameInDb = "UserContent")
    private String UserContent;
    @Property(nameInDb = "time")
    private long time;
    @Property(nameInDb = "type")
    private int type;
    @Property(nameInDb = "messagetype")
    private int messagetype;
    @Property(nameInDb = "UserVoiceTime")
    private float UserVoiceTime;
    @Property(nameInDb = "UserVoicePath")
    private String UserVoicePath;
    @Property(nameInDb = "UserVoiceUrl")
    private String UserVoiceUrl;
    @Property(nameInDb = "sendState")
    private
    @ChatConst.SendState
    int sendState;
    @Property(nameInDb = "imageUrl")
    private String imageUrl;
    @Property(nameInDb = "imageIconUrl")
    private String imageIconUrl;
    @Property(nameInDb = "imageLocal")
    private String imageLocal;
    @Property(nameInDb = "isListened")
    private boolean isListened;
    @Generated(hash = 595418362)
    public ChatMessageBean(Long id, String UserId, String UserName,
            String UserHeadIcon, String UserContent, long time, int type,
            int messagetype, float UserVoiceTime, String UserVoicePath,
            String UserVoiceUrl, int sendState, String imageUrl,
            String imageIconUrl, String imageLocal, boolean isListened) {
        this.id = id;
        this.UserId = UserId;
        this.UserName = UserName;
        this.UserHeadIcon = UserHeadIcon;
        this.UserContent = UserContent;
        this.time = time;
        this.type = type;
        this.messagetype = messagetype;
        this.UserVoiceTime = UserVoiceTime;
        this.UserVoicePath = UserVoicePath;
        this.UserVoiceUrl = UserVoiceUrl;
        this.sendState = sendState;
        this.imageUrl = imageUrl;
        this.imageIconUrl = imageIconUrl;
        this.imageLocal = imageLocal;
        this.isListened = isListened;
    }
    @Generated(hash = 1557449535)
    public ChatMessageBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
    public String getUserName() {
        return this.UserName;
    }
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
    public String getUserHeadIcon() {
        return this.UserHeadIcon;
    }
    public void setUserHeadIcon(String UserHeadIcon) {
        this.UserHeadIcon = UserHeadIcon;
    }
    public String getUserContent() {
        return this.UserContent;
    }
    public void setUserContent(String UserContent) {
        this.UserContent = UserContent;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getMessagetype() {
        return this.messagetype;
    }
    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }
    public float getUserVoiceTime() {
        return this.UserVoiceTime;
    }
    public void setUserVoiceTime(float UserVoiceTime) {
        this.UserVoiceTime = UserVoiceTime;
    }
    public String getUserVoicePath() {
        return this.UserVoicePath;
    }
    public void setUserVoicePath(String UserVoicePath) {
        this.UserVoicePath = UserVoicePath;
    }
    public String getUserVoiceUrl() {
        return this.UserVoiceUrl;
    }
    public void setUserVoiceUrl(String UserVoiceUrl) {
        this.UserVoiceUrl = UserVoiceUrl;
    }
    public int getSendState() {
        return this.sendState;
    }
    public void setSendState(int sendState) {
        this.sendState = sendState;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageIconUrl() {
        return this.imageIconUrl;
    }
    public void setImageIconUrl(String imageIconUrl) {
        this.imageIconUrl = imageIconUrl;
    }
    public String getImageLocal() {
        return this.imageLocal;
    }
    public void setImageLocal(String imageLocal) {
        this.imageLocal = imageLocal;
    }
    public boolean getIsListened() {
        return this.isListened;
    }
    public void setIsListened(boolean isListened) {
        this.isListened = isListened;
    }

   


}
