package com.helen.robot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helen.robot.api.WechatTools;
import com.helen.robot.controller.LoginController;
import com.helen.robot.core.CoreInfo;
import com.helen.robot.core.MsgCenter;
import com.helen.robot.face.IMsgHandlerFace;

/**
 * 
 * 整个流程就是有一个核心类Core，保存所有微信数据，还有各种状态，有一个接收线程LoginServiceImpl.startReceiving负责接收微信发送的数据，接收到之后放入Core.msgList中，
 * 然后处理线程Wechat.startHandleMsgThread，获取Core.msgList里面的消息然后处理，然后使用MessageTools根据需要给用户或者群里面发送信息。
 * 
 * 为了linux服务器部署方便，我开启了一个HttpServer，端口8088，有两个指令，
 * http://127.0.0.1:8088，会把本地的qrCode以流的形式发送给客户端，如果你有一个外网地址，你在任意地点，通过浏览器直接就可以扫描登录
 * http://127.0.0.1:8088/helen，这个可以重新登录。如果你二维码2分钟内没有扫描就会失效，然后可以通过这个指令重新登录，然后重新扫描。或者我现在可能不想使用这个机器人了，我就退出。
 * 过了一段时间我想玩机器人，但是我此刻又没有服务器可以重启。所有可以通过这个指令重新登录，然后重新扫描
 * 
 * 目的：1.可以linux部署  2.可以在任何时候地点登录，只要有手机就可以
 * 
 * 
 * 0 MessageTools 处理消息的类，例如发送用户消息，发送用户图片等<br>
 * 1. Core是单例核心类，里面保存了所有和用户相关的信息，包括消息，联系人，群组，公众号，服务号等 <br>
 * 2. 刚开始会开启一个处理消息的线程Wechat.startHandleMsgThread，从Core.msgList列表中获取消息，然后处理，处理完之后就删除消息，然后1s之后继续处理消息 <br>
 * 3. 登录,会一直等待，直到手机确认成功，大概125s超时。如果没有登录成功，可以通过浏览器输入http://127.0.0.1:8088/helen，进行重新登录 <br>
 * 4. 登录成功之后，会进行一些微信数据收集，1）登陆成功，微信初始化，2）开启微信状态通知<br>
 * 5. 登录成功之后，同时还会开启一个接收线程，LoginServiceImpl.startReceiving
 * 
 * 
 * 
 * 
 * @author glowd
 * @date 2018/04/25
 */
public class Wechat {
    private static final Logger LOG = LoggerFactory.getLogger(Wechat.class);
    private final IMsgHandlerFace msgHandler;
    private final CoreInfo coreInfo;
    private final String qrPath;

    public Wechat(IMsgHandlerFace msgHandler, String qrPath) {
        System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
        this.msgHandler = msgHandler;
        this.qrPath = qrPath;
        this.coreInfo=new CoreInfo();
    }

    /***
     * 1.开启处理消息的线程 <br>
     * 2.登录
     */
    public void start() {
        LOG.info("+++++++++++++++++++开启接收消息的线程+++++++++++++++++++++");
        startHandleMsgThread();
        LOG.info("+++++++++++++++++++开始登录+++++++++++++++++++++");
        login();
    }

    /***
     * 开启接收消息的线程
     */
    private void startHandleMsgThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MsgCenter.handleMsg(msgHandler,coreInfo);
            }
        }).start();
    }

    /***
     * 登录
     */
    private void login() {
        LoginController login = new LoginController(this.coreInfo);
        login.login(this.qrPath);
        List contactList = new WechatTools(this.coreInfo).getContactList();
        
        System.out.println("---------------contactList" + contactList );
    }

}
