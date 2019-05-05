package entity;

/**
 * 本地
 * Created by Administrator on 2019/1/14.
 */

public class Music {
    //歌曲id
    public long id;
    //歌名
    public String song;
    //歌唱者
    public String songer;
    //歌曲地址
    public String url;
    //歌曲时间长度
    public int duration;
    //歌曲大小
    public long size;
    //专辑图片
    public String album;
    //专辑图片ID
    public long album_id;

    public Music(long id, String song, String songer, String url, int duration, long size, String album, long album_id) {
        super();
        this.id = id;
        this.song = song;
        this.songer = songer;
        this.url = url;
        this.duration = duration;
        this.size = size;
        this.album = album;
        this.album_id = album_id;
    }

    public Music() {

    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSonger(String songer) {
        this.songer = songer;
    }

    public String getSonger() {
        return songer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }
}
