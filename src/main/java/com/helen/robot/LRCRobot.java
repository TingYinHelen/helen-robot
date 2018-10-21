//package com.helen.robot;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.logging.Logger;
//
//import com.helen.robot.beans.BaseMsg;
//import com.helen.robot.core.Core;
//import com.helen.robot.face.IMsgHandlerFace;
//import com.helen.robot.utils.MyHttpClient;
//
///**
// * LRC机器人
// */
//public class LRCRobot implements IMsgHandlerFace {
//	Logger logger = Logger.getLogger("TulingRobot");
//	MyHttpClient myHttpClient = Core.getInstance().getMyHttpClient();
//
//	@Override
//	public String textMsgHandle(BaseMsg msg) {
//		String words = msg.getText();
//		String lowWords = words.toLowerCase();
//		if (lowWords.indexOf("lrc") > -1) {
//			return getLrcFromFile();
//		}
//		return null;
//
//	}
//
//	public String getLrcFromFile() {
//		String fileName = "/Users/glowd/spider/loopring.txt";
//		String encoding = "UTF-8";
//		File file = new File(fileName);
//		Long filelength = file.length();
//		byte[] filecontent = new byte[filelength.intValue()];
//		try {
//			FileInputStream in = new FileInputStream(file);
//			in.read(filecontent);
//			in.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			return new String(filecontent, encoding);
//		} catch (UnsupportedEncodingException e) {
//			System.err.println("The OS does not support " + encoding);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	@Override
//	public String picMsgHandle(BaseMsg msg) {
//		return null;
//	}
//
//	@Override
//	public String voiceMsgHandle(BaseMsg msg) {
//		// String fileName = String.valueOf(new Date().getTime());
//		// String voicePath = "/Users/hao/Desktop" + File.separator + fileName + ".mp3";
//		// DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
//		return null;
//	}
//
//	@Override
//	public String viedoMsgHandle(BaseMsg msg) {
//		// String fileName = String.valueOf(new Date().getTime());
//		// String viedoPath = "/Users/hao/Desktop" + File.separator + fileName + ".mp4";
//		// DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
//		return null;
//	}
//
//	public static void main(String[] args) {
//		IMsgHandlerFace msgHandler = new LRCRobot();
//		Wechat wechat = new Wechat(msgHandler, "/Users/glowd");
//		wechat.start();
//	}
//
//	@Override
//	public String nameCardMsgHandle(BaseMsg msg) {
//		return null;
//	}
//
//	@Override
//	public void sysMsgHandle(BaseMsg msg) {
//	}
//
//	@Override
//	public String verifyAddFriendMsgHandle(BaseMsg msg) {
//		return null;
//	}
//
//	@Override
//	public String mediaMsgHandle(BaseMsg msg) {
//		return null;
//	}
//
//}
