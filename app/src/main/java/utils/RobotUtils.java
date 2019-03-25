package utils;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import entity.ChatMessage;
import entity.Result;


public class RobotUtils {

    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "ba585e8f3db74bfba4c1d4edb7a86144";

    /**
     * 发送消息到服务器
     *
     * @param message ：发送的消息
     * @return：消息对象
     */
    public static ChatMessage sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        String gsonResult = doGet(message);
        Gson gson = new Gson();
        if (gsonResult != null) {
            try {
                Result result = gson.fromJson(gsonResult, Result.class);
                if (result.getUrl() == null) {
                    chatMessage.setMessage(result.getText());
                } else {
                    chatMessage.setMessage(result.getText() + result.getUrl());

                }
            } catch (Exception e) {
                chatMessage.setMessage("服务器繁忙，请稍候再试...");
            }
        }
        chatMessage.setData(new Date());
        chatMessage.setType(ChatMessage.Type.INCOUNT);
        return chatMessage;
    }

    /**
     * get请求
     *
     * @param message ：发送的话
     * @return：数据
     */
    public static String doGet(String message) {
        String result = "";
        String url = setParmat(message);
        System.out.println("------------url = " + url);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL urlNet = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlNet.openConnection();
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("GET");

            is = connection.getInputStream();
            baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            System.out.println("------------result = " + baos.size());
            baos.flush();
            result = new String(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("------------result = " + result);
        return result;
    }

    /**
     * 设置参数
     *
     * @param message : 信息
     * @return ： url
     */
    private static String setParmat(String message) {
        String url = "";
        try {
            url = URL + "?key=" + API_KEY + "&info="
                    + URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}