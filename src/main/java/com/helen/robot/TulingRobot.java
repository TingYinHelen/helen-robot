//package com.helen.robot;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.helen.robot.beans.BaseMsg;
//import com.helen.robot.core.Core;
//import com.helen.robot.face.IMsgHandlerFace;
//import com.helen.robot.netty.HttpServer;
//import com.helen.robot.utils.MyHttpClient;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 图灵机器人
// */
//public class TulingRobot implements IMsgHandlerFace {
//
//    private static Logger LOG = LoggerFactory.getLogger(TulingRobot.class);
//    MyHttpClient myHttpClient = Core.getInstance().getMyHttpClient();
//    String url = "http://www.tuling123.com/openapi/api";
//    String apiKey = "f24d27991bdd4ae39b7bca27481651b5";
//
//    @Override
//    public String textMsgHandle(BaseMsg msg) {
//        String words = msg.getText();
//        String key = "@" + Core.getInstance().getNickName();
//        String result = "";
//        if (words.indexOf(key) > -1) {
//            String q = words.replace(key, "");
//            if (q.trim().equals("") || q.equals(" ")) {
//                return "any question?";
//            }
//
//            Map<String, String> paramMap = new HashMap<>();
//            paramMap.put("key", apiKey);
//            paramMap.put("info", q);
//            paramMap.put("userid", "123456");
//            String paramStr = JSON.toJSONString(paramMap);
//
//            try {
//                HttpEntity entity = myHttpClient.doPost(url, paramStr);
//                result = EntityUtils.toString(entity, "UTF-8");
//                JSONObject obj = JSON.parseObject(result);
//                if (obj.getString("code").equals("100000")) {
//                    result = obj.getString("text");
//                } else {
//                    result = "我听不清你在说什么";
//                }
//            } catch (Exception e) {
//                LOG.info(e.getMessage());
//            }
//            return result;
//        }
//        return null;
//
//    }
//
//    @Override
//    public String picMsgHandle(BaseMsg msg) {
//        return null;
//    }
//
//    @Override
//    public String voiceMsgHandle(BaseMsg msg) {
//        // String fileName = String.valueOf(new Date().getTime());
//        // String voicePath = "/Users/hao/Desktop" + File.separator + fileName + ".mp3";
//        // DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
//        return null;
//    }
//
//    @Override
//    public String viedoMsgHandle(BaseMsg msg) {
//        // String fileName = String.valueOf(new Date().getTime());
//        // String viedoPath = "/Users/hao/Desktop" + File.separator + fileName + ".mp4";
//        // DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
//        return null;
//    }
//
//    public static void main(String[] args) {
//
//        LOG.info("Starting httpserver....");
//
//        Thread netty = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    new TulingRobot().startNetty();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        netty.start();
//
//        LOG.info("started Httpserver at 8088.");
//        
//        //第一次启动必须先扫码，将接收消息的线程启动起来
//
//        IMsgHandlerFace msgHandler = new TulingRobot();
//        Wechat wechat = new Wechat(msgHandler, HelenRobot.path);
//        wechat.start();
//    }
//
//    public void startNetty() throws Exception {
//        HttpServer server = new HttpServer();
//        server.start(8088);
//    }
//
//    @Override
//    public String nameCardMsgHandle(BaseMsg msg) {
//        return null;
//    }
//
//    @Override
//    public void sysMsgHandle(BaseMsg msg) {}
//
//    @Override
//    public String verifyAddFriendMsgHandle(BaseMsg msg) {
//        return null;
//    }
//
//    @Override
//    public String mediaMsgHandle(BaseMsg msg) {
//        return null;
//    }
//
//}
