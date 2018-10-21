package com.helen.robot.netty;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helen.robot.HelenRobot;
import com.helen.robot.Wechat;
import com.helen.robot.face.IMsgHandlerFace;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderValues;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	private static Logger LOG = LoggerFactory.getLogger(HttpServerHandler.class);
	private volatile static boolean isNotReset = false;// 最开始可以Reset
	private static final Object RESET_LOCK = new Object();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		DefaultHttpRequest request = (DefaultHttpRequest) msg;
		String host = request.headers().get("Host");
		String uri = request.uri();
		String url = host + uri;

		LOG.info("Received from {}", url);

		FullHttpResponse response;

		if (request.uri().equals("/helen")) {
			synchronized (RESET_LOCK) {
				if (isNotReset) {
					response = doNotResponse();
				} else {
					isNotReset = true;
					response = doResetAndLogin(url);
				}
			}
		} else {
			response = doQRResponse();
		}

		ctx.write(response);
		ctx.flush();

	}

	private FullHttpResponse doNotResponse() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.wrappedBuffer("Reset is too fast,please try again 20s later!".getBytes()));
		return response;
	}

	private FullHttpResponse doResetAndLogin(String url) {

		LOG.info("Starting reset from {} ", url);

		new Thread(new Runnable() {

			@Override
			public void run() {
				relogin();
			}
		}).start();

		try {
			Thread.sleep(2000); // wait for receiving thread end at LoginServiceImpl.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return doQRResponse();
	}

	private void relogin() {
		LOG.info("Starting relogin......");
		IMsgHandlerFace msgHandler = new HelenRobot();
		Wechat wechat = new Wechat(msgHandler, HelenRobot.path);
		wechat.start();
		LOG.info("Reset success!");
		isNotReset = false;
	}

	private FullHttpResponse doQRResponse() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(getBytes()));
		response.headers().set(CONTENT_TYPE, "image/jpg");
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		return response;
	}

	public byte[] getBytes() {

		String qrPath = "/opt/robot/QR.jpg";
		byte data[] = null;

		try {
			InputStream hFile = new FileInputStream(qrPath);
			int size = hFile.available(); // 得到文件大小
			data = new byte[size];
			hFile.read(data); // 读数据
			hFile.close();

		} catch (Exception e) {
			LOG.info(e.getMessage());
		}

		return data;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}