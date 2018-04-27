# helen-robot - Java版本的机器人 - 代码fork自https://github.com/yaphone/itchat4j

# 项目介绍
在itchat4j项目基础上添加了以下功能

1. 为了linux服务器部署方便，我开启了一个HttpServer，端口8088，有两个指令，
2. http://127.0.0.1:8088，会把本地的qrCode以流的形式发送给客户端，如果你有一个外网地址，你在任意地点，通过浏览器直接就可以扫描登录
3. http://127.0.0.1:8088/helen，这个可以重新登录。如果你二维码2分钟内没有扫描就会失效，然后可以通过这个指令重新登录，然后重新扫描。或者我现在可能不想使用这个机器人了，我就退出。过了一段时间我想玩机器人，但是我此刻又没有服务器可以重启。所有可以通过这个指令重新登录，然后重新扫描
4. 为了实现上述功能，对代码做了一些优化

# 项目流程
1. MessageTools 处理消息的类，例如发送用户消息，发送用户图片等<br>
2. Core是单例核心类，里面保存了所有和用户相关的信息，包括消息，联系人，群组，公众号，服务号等 <br>
3. 刚开始会开启一个处理消息的线程Wechat.startHandleMsgThread，从Core.msgList列表中获取消息，然后处理，处理完之后就删除消息，然后1s之后继续处理消息 <br>
4. 登录,会一直等待，直到手机确认成功，大概125s超时。如果没有登录成功，可以通过浏览器输入http://127.0.0.1:8088/helen，进行重新登录 <br>
5. 登录成功之后，会进行一些微信数据收集，1）登陆成功，微信初始化，2）开启微信状态通知<br>
6. 登录成功之后，同时还会开启一个接收线程，LoginServiceImpl.startReceiving

# 整体描述
整个流程就是有一个核心类Core，保存所有微信数据，还有各种状态，有一个接收线程LoginServiceImpl.startReceiving负责接收微信发送的数据，接收到之后放入Core.msgList中，然后处理线程Wechat.startHandleMsgThread，获取Core.msgList里面的消息然后处理，然后使用MessageTools根据需要给用户或者群里面发送信息。

# 部署

1. qrcode必须放置到部署服务器的  /opt/robot 目录，当然可以修改代码，推荐保持不变

2. 下载工程，编译打包

    ```
    mvn clean package
    ```
3. 将编译好的robot-0.0.1-jar-with-dependencies.jar ，拷贝到 /opt/robot 目录，然后启动

    ```
    nohup java -jar robot-0.0.1-jar-with-dependencies.jar &
    ```
4. 打开浏览器，http://127.0.0.1:8088，手机微信登录，就可以使用了
5. 直接手机微信退出
6. http://127.0.0.1:8088/helen, reset success!
7. 重复第 4 步，http://127.0.0.1:8088，手机微信登录，就可以使用了

# demo

1. 我已经部署到我的域名下了，http://www.glowd.cn
2. 重置：  http://glowd.cn:8088/helen
3. 扫码:   http://www.glowd.cn:8088
4. 扫码成功之后，对自己账号发送，@微信名 你好XXXX，会回复图灵机器人的对话。也可以在群里面@扫码微信号的微信名 发送信息。
