package serviceApi;

import entity.NetMusic;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("search")
    Call<NetMusic> getMusic(@Query("key") String key, @Query("s") String s, @Query("type") String type, @Query("limit") int limit, @Query("offset") int page);

}
