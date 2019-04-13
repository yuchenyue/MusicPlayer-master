package serviceApi;

import entity.LetMusic;
import entity.ListMusic;
import entity.NetMusic;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("search")
    Call<NetMusic> getMusic(@Query("key") String key, @Query("s") String s, @Query("type") String type, @Query("limit") int limit, @Query("offset") int page);

    @POST("hotSongList")
    Call<LetMusic> getLMusic(@Query("key") String key, @Query("limit") int limit, @Query("offset") int offset);

    @POST("songList")
    Call<ListMusic> getListMusic(@Query("key") String key, @Query("id") String id, @Query("limit") int limit, @Query("offset") int offset);
}
