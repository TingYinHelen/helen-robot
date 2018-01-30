package com.helen.robot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.helen.robot.beans.BaseMsg;
import com.helen.robot.core.Core;
import com.helen.robot.face.IMsgHandlerFace;
import com.helen.robot.utils.MyHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 图灵机器人
 */
public class TulingRobot implements IMsgHandlerFace {
    Logger       logger       = Logger.getLogger("TulingRobot");
    MyHttpClient myHttpClient = Core.getInstance().getMyHttpClient();
    String       url          = "http://www.tuling123.com/openapi/api";
    String       apiKey       = "f24d27991bdd4ae39b7bca27481651b5";

    @Override
    public String textMsgHandle(BaseMsg msg) {
        String words = msg.getText();
        if (words.indexOf("test") > -1) {
            String text = words.replace("test", "");
            String result = "";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("key", apiKey);
            paramMap.put("info", text);
            paramMap.put("userid", "123456");
            String paramStr = JSON.toJSONString(paramMap);
            try {
                HttpEntity entity = myHttpClient.doPost(url, paramStr);
                result = EntityUtils.toString(entity, "UTF-8");
                JSONObject obj = JSON.parseObject(result);
                if (obj.getString("code").equals("100000")) {
                    result = obj.getString("text");
                } else {
                    result = "我听不清你在说什么";
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            return result;
        }
        return null;

    }

    @Override
    public String picMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String voiceMsgHandle(BaseMsg msg) {
        //        String fileName = String.valueOf(new Date().getTime());
        //        String voicePath = "/Users/hao/Desktop" + File.separator + fileName + ".mp3";
        //        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
        return null;
    }

    @Override
    public String viedoMsgHandle(BaseMsg msg) {
        //        String fileName = String.valueOf(new Date().getTime());
        //        String viedoPath = "/Users/hao/Desktop" + File.separator + fileName + ".mp4";
        //        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
        return null;
    }

    public static void main(String[] args) {
        IMsgHandlerFace msgHandler = new TulingRobot();
        Wechat wechat = new Wechat(msgHandler, "/Users/glowd");
        wechat.start();
    }

    @Override
    public String nameCardMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public void sysMsgHandle(BaseMsg msg) {
    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        return null;
    }

}
