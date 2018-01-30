package com.helen.robot.face;

import com.helen.robot.beans.BaseMsg;

/**
 * 消息处理接口
 *
 */
public interface IMsgHandlerFace {
    String textMsgHandle(BaseMsg msg);

    /**
     * 处理图片消息
     */
    String picMsgHandle(BaseMsg msg);

    /**
     * 处理声音消息
     * 
     */
    String voiceMsgHandle(BaseMsg msg);

    /**
     * 处理小视频消息
     * 
     */
    String viedoMsgHandle(BaseMsg msg);

    /**
     * 处理名片消息
     */
    String nameCardMsgHandle(BaseMsg msg);

    /**
     * 处理系统消息
     */
    void sysMsgHandle(BaseMsg msg);

    /**
     * 处理确认添加好友消息
    
     */
    String verifyAddFriendMsgHandle(BaseMsg msg);

    /**
     * 处理收到的文件消息
     */
    String mediaMsgHandle(BaseMsg msg);

}
