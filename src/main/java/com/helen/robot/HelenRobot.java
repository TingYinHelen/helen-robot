package com.helen.robot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helen.robot.beans.BaseMsg;
import com.helen.robot.core.Core;
import com.helen.robot.face.IMsgHandlerFace;
import com.helen.robot.netty.HttpServer;
import com.helen.robot.utils.MyHttpClient;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HelenRobot implements IMsgHandlerFace {

    private static Logger LOG = LoggerFactory.getLogger(HelenRobot.class);
    MyHttpClient myHttpClient = Core.getInstance().getMyHttpClient();
    public static final String path = "/opt/robot";

    public static void main(String[] args) throws Exception {

        LOG.info("Starting httpserver....");

        Thread netty = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new HelenRobot().startNetty();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        netty.start();

        LOG.info("Started httpserver at 8088.");

        IMsgHandlerFace msgHandler = new HelenRobot();
        Wechat wechat = new Wechat(msgHandler, HelenRobot.path);
        wechat.start();
    }

    public void startNetty() throws Exception {
        HttpServer server = new HttpServer();
        server.start(8088);
    }

    @Override
    public String textMsgHandle(BaseMsg msg) {
        String words = msg.getText();
        String lowWords = words.toLowerCase().trim();
        String key = "@" + Core.getInstance().getNickName();
        if (lowWords.indexOf(key) > -1) {
            String q = lowWords.replace(key, "");
            if (q.trim().equals("") || q.equals(" ")) {
                return "any question?";
            }
            return getAnswer(q);
        }
        return null;

    }

    public String getAnswer(String question) {
        String url = "http://127.0.0.1:8000/?q=" + question;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLrcFromFile() {
        String fileName = "/Users/glowd/spider/loopring.txt";
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String picMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String voiceMsgHandle(BaseMsg msg) {
        // String fileName = String.valueOf(new Date().getTime());
        // String voicePath = "/Users/hao/Desktop" + File.separator + fileName + ".mp3";
        // DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
        return null;
    }

    @Override
    public String viedoMsgHandle(BaseMsg msg) {
        // String fileName = String.valueOf(new Date().getTime());
        // String viedoPath = "/Users/hao/Desktop" + File.separator + fileName + ".mp4";
        // DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
        return null;
    }

    @Override
    public String nameCardMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public void sysMsgHandle(BaseMsg msg) {
        System.out.println("sysMsgHandle:" + msg);
    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        System.out.println("verifyAddFriendMsgHandle:" + msg);
        return null;
    }

    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        System.out.println("mediaMsgHandle:" + msg);

        return null;
    }
}
