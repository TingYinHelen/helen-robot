//package com.helen.robot.utils.tools;
//
//import com.helen.robot.beans.BaseMsg;
//import com.helen.robot.core.Core;
//import com.helen.robot.utils.MyHttpClient;
//import com.helen.robot.utils.enums.MsgTypeEnum;
//import com.helen.robot.utils.enums.URLEnum;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Logger;
//
///**
// * 下载工具类
// *
// */
//public class DownloadTools {
//	private static Logger       logger       = Logger.getLogger("DownloadTools");
//	private static Core         core         = Core.getInstance();
//	private static MyHttpClient myHttpClient = core.getMyHttpClient();
//
//	/**
//	 * 处理下载任务
//	 *
//	 */
//	public static Object getDownloadFn(BaseMsg msg, String type, String path) {
//		Map<String, String> headerMap = new HashMap<String, String>();
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		String url = "";
//		if (type.equals(MsgTypeEnum.PIC.getType())) {
//			url = String.format(URLEnum.WEB_WX_GET_MSG_IMG.getUrl(), (String) core.getLoginInfo().get("url"));
//		} else if (type.equals(MsgTypeEnum.VOICE.getType())) {
//			url = String.format(URLEnum.WEB_WX_GET_VOICE.getUrl(), (String) core.getLoginInfo().get("url"));
//		} else if (type.equals(MsgTypeEnum.VIEDO.getType())) {
//			headerMap.put("Range", "bytes=0-");
//			url = String.format(URLEnum.WEB_WX_GET_VIEDO.getUrl(), (String) core.getLoginInfo().get("url"));
//		} else if (type.equals(MsgTypeEnum.MEDIA.getType())) {
//			headerMap.put("Range", "bytes=0-");
//			url = String.format(URLEnum.WEB_WX_GET_MEDIA.getUrl(), (String) core.getLoginInfo().get("fileUrl"));
//			params.add(new BasicNameValuePair("sender", msg.getFromUserName()));
//			params.add(new BasicNameValuePair("mediaid", msg.getMediaId()));
//			params.add(new BasicNameValuePair("filename", msg.getFileName()));
//		}
//		params.add(new BasicNameValuePair("msgid", msg.getNewMsgId()));
//		params.add(new BasicNameValuePair("skey", (String) core.getLoginInfo().get("skey")));
//		HttpEntity entity = myHttpClient.doGet(url, params, true, headerMap);
//		try {
//			OutputStream out = new FileOutputStream(path);
//			byte[] bytes = EntityUtils.toByteArray(entity);
//			out.write(bytes);
//			out.flush();
//			out.close();
//			// Tools.printQr(path);
//
//		} catch (Exception e) {
//			logger.info(e.getMessage());
//			return false;
//		}
//		return null;
//	}
//
//}
