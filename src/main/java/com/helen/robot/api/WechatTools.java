package com.helen.robot.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.helen.robot.core.Core;
import com.helen.robot.utils.enums.StorageLoginInfoEnum;
import com.helen.robot.utils.enums.URLEnum;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小工具，如获好友列表等
 * 
 *
 */
public class WechatTools {
    private static Logger LOG  = LoggerFactory.getLogger(WechatTools.class);

    private static Core   core = Core.getInstance();

    /**
     * 根据用户名发送文本消息
     * 
     */
    public static void sendMsgByUserName(String msg, String toUserName) {
        MessageTools.sendMsgById(msg, toUserName);
    }

    /**
     * <p>
     * 通过RealName获取本次UserName
     * </p>
     * <p>
     * 如NickName为"yaphone"，则获取UserName=
     * "@1212d3356aea8285e5bbe7b91229936bc183780a8ffa469f2d638bf0d2e4fc63"，
     * 可通过UserName发送消息
     * </p>
     * 
     */
    public static String getUserNameByNickName(String nickName) {
        for (JSONObject o : core.getContactList()) {
            if (o.getString("NickName").equals(nickName)) {
                return o.getString("UserName");
            }
        }
        return null;
    }

    /**
     * 返回好友昵称列表
     * 
     */
    public static List<String> getContactNickNameList() {
        List<String> contactNickNameList = new ArrayList<String>();
        for (JSONObject o : core.getContactList()) {
            contactNickNameList.add(o.getString("NickName"));
        }
        return contactNickNameList;
    }

    /**
     * 返回好友完整信息列表
     * 
     */
    public static List<JSONObject> getContactList() {
        return core.getContactList();
    }

    /**
     * 返回群列表
     * 
     */
    public static List<JSONObject> getGroupList() {
        return core.getGroupList();
    }

    /**
     * 获取群ID列表
     * 
     */
    public static List<String> getGroupIdList() {
        return core.getGroupIdList();
    }

    /**
     * 获取群NickName列表
     * 
     */
    public static List<String> getGroupNickNameList() {
        return core.getGroupNickNameList();
    }

    /**
     * 根据groupIdList返回群成员列表
     * 
     */
    public static JSONArray getMemberListByGroupId(String groupId) {
        return core.getGroupMemeberMap().get(groupId);
    }

    /**
     * 退出微信
     * 
     */
    public static void logout() {
        webWxLogout();
    }

    private static boolean webWxLogout() {
        String url = String.format(URLEnum.WEB_WX_LOGOUT.getUrl(),
            core.getLoginInfo().get(StorageLoginInfoEnum.url.getKey()));
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("redirect", "1"));
        params.add(new BasicNameValuePair("type", "1"));
        params.add(new BasicNameValuePair("skey",
            (String) core.getLoginInfo().get(StorageLoginInfoEnum.skey.getKey())));
        try {
            HttpEntity entity = core.getMyHttpClient().doGet(url, params, false, null);
            EntityUtils.toString(entity, Consts.UTF_8); // 无消息
            return true;
        } catch (Exception e) {
            LOG.debug(e.getMessage());
        }
        return false;
    }

    public static void setUserInfo() {
        for (JSONObject o : core.getContactList()) {
            core.getUserInfoMap().put(o.getString("NickName"), o);
            core.getUserInfoMap().put(o.getString("UserName"), o);
        }
    }

    /**
     * 
     * 根据用户昵称设置备注名称
     * 
     */
    public static void remarkNameByNickName(String nickName, String remName) {
        String url = String.format(URLEnum.WEB_WX_REMARKNAME.getUrl(),
            core.getLoginInfo().get("url"),
            core.getLoginInfo().get(StorageLoginInfoEnum.pass_ticket.getKey()));
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String, Object> msgMap_BaseRequest = new HashMap<String, Object>();
        msgMap.put("CmdId", 2);
        msgMap.put("RemarkName", remName);
        msgMap.put("UserName", core.getUserInfoMap().get(nickName).get("UserName"));
        msgMap_BaseRequest.put("Uin", core.getLoginInfo().get(StorageLoginInfoEnum.wxuin.getKey()));
        msgMap_BaseRequest.put("Sid", core.getLoginInfo().get(StorageLoginInfoEnum.wxsid.getKey()));
        msgMap_BaseRequest.put("Skey", core.getLoginInfo().get(StorageLoginInfoEnum.skey.getKey()));
        msgMap_BaseRequest.put("DeviceID",
            core.getLoginInfo().get(StorageLoginInfoEnum.deviceid.getKey()));
        msgMap.put("BaseRequest", msgMap_BaseRequest);
        try {
            String paramStr = JSON.toJSONString(msgMap);
            core.getMyHttpClient().doPost(url, paramStr);
            // String result = EntityUtils.toString(entity, Consts.UTF_8);
            LOG.info("修改备注" + remName);
        } catch (Exception e) {
            LOG.error("remarkNameByUserName", e);
        }
    }

    /**
     * 获取微信在线状态
    
     */
    public static boolean getWechatStatus() {
        return core.isAlive();
    }

}
