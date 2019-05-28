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

    public static Api getWebMusic() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl("https://api.itooi.cn/music/netease/")
                .baseUrl("https://v1.itooi.cn/")

                .build();
        Api mApi = retrofit.create(Api.class);
        return mApi;
    }

}
