package serviceApi;


import entity.LetMusic;
import entity.ListMusic;
import entity.NetMusic;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("netease/search")
    Call<NetMusic> getMusic(@Query("keyword") String keyword, @Query("type") String type, @Query("format") int format);

    @GET("netease/songList/hot")
    Call<LetMusic> getLMusic(@Query("cat") String cat, @Query("pageSize") int pageSize, @Query("orderType") String orderType, @Query("categoryType") String categoryType);

    @GET("netease/songList")
    Call<ListMusic> getListMusic(@Query("id") String id, @Query("format") int format);

}
