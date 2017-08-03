package com.example.administrator.chatdemo.utils;

/**
 * Created by wli on 15/8/23.
 * 用来存放各种 static final 值
 */
public class Constants {

  public static final String OBJECT_ID = "objectId";
  public static final int PAGE_SIZE = 10;
  public static final String CREATED_AT = "createdAt";
  public static final String UPDATED_AT = "updatedAt";


  //TODO 还不知道这俩货是干嘛的
  public static final int ORDER_UPDATED_AT = 1;
  public static final int ORDER_DISTANCE = 0;

  private static final String LEANMESSAGE_CONSTANTS_PREFIX = "com.avoscloud.leanchatlib.";

  public static final String MEMBER_ID = getPrefixConstant("member_id");
  public static final String CONVERSATION_ID = getPrefixConstant("conversation_id");

  public static final String LEANCHAT_USER_ID = getPrefixConstant("leanchat_user_id");

  public static final String ACTIVITY_TITLE = getPrefixConstant("activity_title");

  public static final String INTENT_KEY = getPrefixConstant("intent_key");
  public static final String INTENT_VALUE = getPrefixConstant("intent_value");
  public static final String INTENT_DATA = getPrefixConstant("intent_data");



  // ImageBrowserActivity
  public static final String IMAGE_LOCAL_PATH = getPrefixConstant("image_local_path");
  public static final String IMAGE_URL = getPrefixConstant("image_url");

  //Notification
  public static final String NOTOFICATION_TAG = getPrefixConstant("notification_tag");
  public static final String NOTIFICATION_SINGLE_CHAT = Constants.getPrefixConstant("notification_single_chat");
  public static final String NOTIFICATION_GROUP_CHAT = Constants.getPrefixConstant("notification_group_chat");
  public static final String NOTIFICATION_SYSTEM = Constants.getPrefixConstant("notification_system_chat");

  public static String getPrefixConstant(String str) {
    return LEANMESSAGE_CONSTANTS_PREFIX + str;
  }
}
