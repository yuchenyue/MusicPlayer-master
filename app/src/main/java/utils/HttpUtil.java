package utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import serviceApi.Api;

/**
 * Created by Administrator on 2019/2/28.
 */

public class HttpUtil {
    public static void requestStringData(int topid,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://route.showapi.com/213-4?showapi_appid=88100&topid=\"+topid+\"&showapi_sign=0a363fadf62f4216ab2e865d17368a8b")
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static Api getWebMusic() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.bzqll.com/music/netease/")
                .build();
        Api mApi = retrofit.create(Api.class);
        return mApi;
    }
}
