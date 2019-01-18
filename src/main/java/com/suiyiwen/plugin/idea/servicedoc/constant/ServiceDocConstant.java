package com.suiyiwen.plugin.idea.servicedoc.constant;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:01
 */
public interface ServiceDocConstant {

    String DEFAULT_VERSION = "1.0.0";
    String TITLE_GENERATE_DIALOG = "ServiceDoc";
    String NOTIFICATION_GROUP_DISPLAY_ID = "ServiceDoc";
    String NOTIFICATION_TITLE = "ServiceDoc Notification";
    String NOTIFICATION_FOCUS_CONTENT = "please focus over java method";
    String NOTIFICATION_NOT_INTERFACE_CONTENT = "this is not a interface";
    String NOTIFICATION_NOT_PUBLIC_METHOD_CONTENT = "this is not a public method";
    String TAG_PREFIX = "service";
    String TAG_TEXT_OPEN_PAREN = "(";
    String TAG_TEXT_CLOSE_PAREN = ")";
    String TAG_TEXT_OPEN_BRACE = "{";
    String TAG_TEXT_CLOSE_BRACE = "}";
    String CHAR_DOT = ".";
    String CHAR_COMMA = ",";
    String TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP = "result";
    int OBJECT_RESOLVE_MAX_DEPTH = 4;
    int OBJECT_RESOLVE_DEPTH_START = 1;
}
