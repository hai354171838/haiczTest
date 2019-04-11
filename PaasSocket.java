package com.haicz;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:Haicz
 * @date:2019/04/11
 */
public class PaasSocket {
    //企业id
    private final static String ORG_NAME="1130190410055683";
    //app
    private final static String APP_NAME="haicz";
    //唯一标识符
    private final static String CLIENT_ID="YXA6ovGmcFteEemI2uPYmZeMMA";
    private final static String CLIENT_SECRET="YXA6omg2i5ZgLdeh6boru-hUwtSxYFs";
    //访问路径
    private final static String URL="https://a1.easemob.com/";
    //token
    private static String access_token;
    private static Integer expires_in;
    private static String application;
    /**
     * 获取与环信的连接，并获取token
     * @throws IOException
     * @throws JSONException
     */
    private static String getConnection() throws IOException, JSONException {
        URL urll=new URL(URL+ORG_NAME+"/"+ APP_NAME+"/token");
        HttpsURLConnection httpsUrlConnection =(HttpsURLConnection)urll.openConnection();
        httpsUrlConnection.setDoOutput(true);
        httpsUrlConnection.setDoInput(true);
        httpsUrlConnection.setRequestMethod("POST");
        httpsUrlConnection.setRequestProperty("Content-Type","application/json");
        httpsUrlConnection.addRequestProperty("Accept", "application/json");
        OutputStream os=httpsUrlConnection.getOutputStream();
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
        bw.write("{\"grant_type\":\"client_credentials\",\"client_id\":\""+CLIENT_ID+"\",\"client_secret\":\""+CLIENT_SECRET+"\"}");
        bw.flush();
        bw.close();
        //对得到的结果进行分析，使相应键获得相应值
        BufferedReader br=new BufferedReader(new InputStreamReader(httpsUrlConnection.getInputStream()));
        StringBuffer sb=new StringBuffer();
        String str=br.readLine();
        while(str!=null) {
            sb.append(str);
            str=br.readLine();
        }
        str=sb.toString();
        JSONObject jsonObject=new JSONObject(str);
        access_token=jsonObject.getString("access_token");
        expires_in=jsonObject.getInt("expires_in");
        application=jsonObject.getString("application");
        br.close();
        return access_token;
    }
    /**
     * 根据传入的参数动态执行操作
     * @param userJson
     * @param userPath
     * @param hasToken
     * @return
     * @throws IOException
     */
    private static String userHandle(String userJson,String userPath,String hasToken,String method) throws IOException{
        URL easeUrl =new URL(URL+ORG_NAME+"/"+APP_NAME+userPath);
        HttpsURLConnection httpsUrlConnection=(HttpsURLConnection)easeUrl.openConnection();
        httpsUrlConnection.setDoOutput(true);
        httpsUrlConnection.setDoInput(true);
        httpsUrlConnection.setRequestMethod(method);
        if(method.equals("POST")) {
            httpsUrlConnection.addRequestProperty("Content-Type","application/json");
        }
        httpsUrlConnection.addRequestProperty("Accept", "application/json");
        if(hasToken!=null) {
            httpsUrlConnection.addRequestProperty("Authorization", "Bearer "+hasToken);
        }
        if(userJson!=null) {
            OutputStream os=httpsUrlConnection.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
            bw.write(userJson);
            bw.flush();
            bw.close();
        }else {
            httpsUrlConnection.connect();
        }
        int code=httpsUrlConnection.getResponseCode();
        if(code==200) {
            //对得到的结果进行分析，使相应键获得相应值
            BufferedReader br=new BufferedReader(new InputStreamReader(httpsUrlConnection.getInputStream()));
            StringBuffer sb=new StringBuffer();
            String str=br.readLine();
            while(str!=null) {
                sb.append(str);
                str=br.readLine();
            }
            str=sb.toString();
            br.close();
            return str;
        }else {
            if(code==429||code==503) {
                return ":接口限流";
            }
            return Integer.toString(code);
        }

    }
    /**
     * 开放注册，添加单个用户
     * @param usermap
     * @return json字符串
     * @throws IOException
     */
    public static  String addUser(Map usermap) throws IOException {
        String userPath="/users";
        String userJson=null;
        if(usermap.get("nickname")!=null&&!usermap.get("nickname").equals("")) {
            userJson="{\"username\":\""+usermap.get("username")+"\",\"password\":\""+usermap.get("password")+"\",\"nickname\":\""+usermap.get("nickname")+"\"}";
        }else {
            userJson="{\"username\":\""+usermap.get("username")+"\",\"password\":\""+usermap.get("password")+"\"}";
        }
        String ret= userHandle(userJson, userPath,null,"POST");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==400) {
                ret=":用户已存在";
            }
        }
        return ret;
    }
    /**
     * 授权注册，添加单个用户
     * @param usermap
     * @return json
     * @throws Exception
     */
    public static String addUserByToken(Map usermap) throws Exception {
        String userPath="/users";
        String userJson=null;
        String hasToken=getConnection();
        if(usermap.get("nickname")!=null&&!usermap.get("nickname").equals("")) {
            userJson="{\"username\":\""+usermap.get("username")+"\",\"password\":\""+usermap.get("password")+"\",\"nickname\":\""+usermap.get("nickname")+"\"}";
        }else {
            userJson="{\"username\":\""+usermap.get("username")+"\",\"password\":\""+usermap.get("password")+"\"}";
        }
        String ret= userHandle(userJson, userPath,hasToken,"POST");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==400) {
                ret=":用户已存在";
            }else if(code==401) {
                ret=":未授权";
            }
        }
        return ret;

    }
    /**
     * 批量注册用户，需要权限
     * @param userList
     * @return json
     * @throws IOException
     * @throws JSONException
     */
    public static String addUsers(List<Map> userList) throws IOException, JSONException {
        String userPath="/users";
        String userJson=null;
        String hasToken=getConnection();
        StringBuffer sb=new StringBuffer("[");
        for (Map usermap : userList) {
            if(usermap.get("nickname")!=null&&!usermap.get("nickname").equals("")) {
                sb.append("{\"username\":\""+usermap.get("username")+"\",\"password\":\""+usermap.get("password")+"\",\"nickname\":\""+usermap.get("nickname")+"\"}");
            }else {
                sb.append("{\"username\":\""+usermap.get("username")+"\",\"password\":\""+usermap.get("password")+"\"}");
            }
        }
        sb.append("]");
        userJson=sb.toString();
        if(userJson.equals("[]")) {
            return null;
        }else {
            String ret= userHandle(userJson, userPath, hasToken,"POST");
            char ch=ret.charAt(0);
            if(ch!='{'&&ch!=':') {
                int code=Integer.parseInt(ret);
                if(code==400) {
                    ret=":用户已存在";
                }else if(code==401) {
                    ret=":未授权";
                }
            }
            return ret;
        }
    }
    /**
     * 查询单个用户信息
     * @param username
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String getUser(String username) throws IOException, JSONException {
        String userPath="/users/"+username;
        String hasToken=getConnection();
        String ret= userHandle(null, userPath, hasToken, "GET");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==404) {
                ret=":用户不存在";
            }else if(code==401) {
                ret=":未授权";
            }
        }
        return ret;
    }
    /**
     * 批量查询用户
     * @param limit
     * @param cursor 上一个用户游标，没有请输入null
     * @return 返回之中的cursor代表末尾用户的游标
     * @throws IOException
     * @throws JSONException
     */
    public static String getUsers(int limit,String cursor) throws IOException, JSONException {
        StringBuffer sb=new StringBuffer("/users?limit="+limit);
        if(cursor!=null) {
            sb.append("&cursor="+cursor);
        }
        String hasToken=getConnection();
        String ret=userHandle(null, sb.toString(), hasToken, "GET");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }
        }
        return ret;
    }
    /**
     * 删除单个用户
     * @param username
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String deleteUser(String username) throws IOException, JSONException {
        String userPath="/users/"+username;
        String hasToken=getConnection();
        String ret= userHandle(null, userPath, hasToken, "DELETE");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==404) {
                ret=":用户不存在";
            }else if(code==401) {
                ret=":未授权";
            }
        }
        return ret;
    }
    /**
     * 删除多个用户，建议在100-500之间
     * @param limit
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String deleteUsers(int limit) throws IOException, JSONException {
        String userPath="/users?limit="+limit;
        String hasToken=getConnection();
        String ret= userHandle(null, userPath, hasToken, "DELETE");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }
        }
        return ret;

    }
    /**
     * 重置im用户密码
     * @param username
     * @param newpassword
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String updateUserPassword(String username,String newpassword) throws IOException, JSONException {
        String userPath="/users/"+username+"/password";
        String hasToken=getConnection();
        String userJson="{\"newpassword\":\""+newpassword+"\"}";
        String ret= userHandle(userJson, userPath, hasToken, "PUT");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==404) {
                ret=":用户不存在";
            }else if(code==401) {
                ret=":未授权";
            }else if(code==400) {
                ret=":错误请求";
            }
        }
        return ret;
    }
    /**
     * 修改用户推送显示昵称
     * @param username
     * @param nickname
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String updateUserNickname(String username,String nickname) throws IOException, JSONException {
        String userPath="/users/"+username;
        String hasToken=getConnection();
        String userJson="{\"nickname\":\""+nickname+"\"}";
        String ret= userHandle(userJson, userPath, hasToken, "PUT");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==400) {
                ret=":错误请求";
            }
        }
        return ret;
    }
    /**
     * 添加给用户一个好友
     *
     * @param friend_username
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String addFriend(String ownername,String friend_username) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/contacts/users/"+friend_username;
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "POST");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户或被添加的好友不存在";
            }
        }
        return ret;
    }
    /**
     * 解除用户一个好友关系
     *
     * @param friend_username
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String rmFriend(String ownername,String friend_username) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/contacts/users/"+friend_username;
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "DELETE");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户或被解除的好友不存在";
            }
        }
        return ret;
    }
    /**
     * 获取用户的好友列表
     * @param ownername
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String getFriends(String ownername) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/contacts/users/";
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "GET");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户或要查看的好友不存在";
            }
        }
        return ret;
    }
    /**
     * 获取用户黑名单
     * @param ownername
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String getBlocks(String ownername) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/blocks/users";
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "GET");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户不存在";
            }
        }
        return ret;
    }
    /**
     * 往im用户的黑名单加人
     * @param ownername
     * @param blocks 用户黑名单的集合 存储的是用户名，非昵称
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String addBlocks(String ownername,String [] blocks) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/blocks/users";
        StringBuffer sb =new StringBuffer();
        for (int i = 0; i < blocks.length; i++) {
            if(i<blocks.length-1) {
                sb.append("\""+blocks[i]+"\",");
            }else {
                sb.append("\""+blocks[i]+"\"");
            }
        }
        String userJson="{\"usernames\":["+sb.toString()+"]}";
        String hasToken=getConnection();
        String ret=userHandle(userJson, userPath, hasToken, "POST");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户不存在";
            }else if(code==400) {
                ret=":被添加的用户不存在";
            }
        }
        return ret;
    }
    /**
     * 从im用户的黑名单减人，一次减一个
     * @param ownername
     * @param blocked_username
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String deleteBlock(String ownername,String blocked_username) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/blocks/users/"+blocked_username;
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "DELETE");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户或被减的好友不存在";
            }
        }
        return ret;
    }
    /**
     * 查看用户在线状态
     * @param ownername
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String getStatus(String ownername) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/status";
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "GET");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户不存在";
            }
        }
        return ret;
    }
    /**
     * 查看用户离线消息数
     * @param ownername
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String getInfoOffline(String ownername) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/offline_msg_count";
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "GET");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户不存在";
            }
        }
        return ret;
    }
    /**
     * 查询某条离线消息状态
     * @param ownername
     * @param msg_id 消息id，消息id可以通过获取聊天记录查询
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String getOneInfoOffline(String ownername,String msg_id) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/offline_msg_status/"+msg_id;
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "GET");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==404) {
                ret=":此用户不存在";
            }
        }
        return ret;
    }
    /**
     * 用户账号禁用
     * @param ownername
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String mkUserDeactivate(String ownername) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/deactivate";
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "POST");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==400) {
                ret=":此用户不存在";
            }
        }
        return ret;
    }
    /**
     * 用户账号解禁
     * @param ownername
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String mkUserActivate(String ownername) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/activate";
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "POST");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }else if(code==400) {
                ret=":此用户不存在";
            }
        }
        return ret;
    }
    /**
     * 强制用户下线
     * @param ownername
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String mkUserDisconnect(String ownername) throws IOException, JSONException {
        String userPath="/users/"+ownername+"/disconnect";
        String hasToken=getConnection();
        String ret=userHandle(null, userPath, hasToken, "GET");
        char ch=ret.charAt(0);
        if(ch!='{'&&ch!=':') {
            int code=Integer.parseInt(ret);
            if(code==401) {
                ret=":未授权";
            }
        }
        return ret;
    }
    public static void main(String[] args) throws IOException, JSONException {
        Map<String,String> usermap=new HashMap<String,String>();
        usermap.put("username", "222");
        usermap.put("password", "123456");
        usermap.put("nickname", "aaaa");
        PaasSocket p=new PaasSocket();
        String [] strs= {"aaa","ccc"};
        String s = PaasSocket.addUser(usermap);
        //System.out.println(PaasSocket.getUsers(20, null));
        //String str=PaasSocket.updateUserNickname("haiczw", "likeyou");
        try {
            //System.out.println(str);
            System.out.println(s);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
