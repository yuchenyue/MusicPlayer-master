package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 网络列表
 * Created by Administrator on 2019/2/28.
 */

public class ListMusic implements Serializable {
    /**
     * result : SUCCESS
     * code : 200
     * data :
     *        id: "6813301314",
     *        name: "放松大脑，开始卷入一次神游",
     *        creator: "就是一个听歌der",
     *        createTime: "2019-03-27",
     *        pic: "http://p.qpic.cn/music_cover/Fe6emiag6IuVbMib3oN6yctTaRvSyia4OdJgboa93hE3bAUyeDgmgMDibA/600?n=1",
     *        playCount: "557036"
     */

    private String result;
    private int code;
    private DataBean data;

    public class DataBean implements Serializable{

        private String songListId;
        private String songListName;
        private String songListPic;
        private String songListDescription;
        private List<Song> songs;

        public class Song implements Serializable{
            private String id;
            private String name;
            private String singer;
            private String pic;
            private String lrc;
            private String url;
            private int time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSinger() {
                return singer;
            }

            public void setSinger(String singer) {
                this.singer = singer;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getLrc() {
                return lrc;
            }

            public void setLrc(String lrc) {
                this.lrc = lrc;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }
        }

        public String getSongListId() {
            return songListId;
        }

        public void setSongListId(String songListId) {
            this.songListId = songListId;
        }

        public String getSongListName() {
            return songListName;
        }

        public void setSongListName(String songListName) {
            this.songListName = songListName;
        }

        public String getSongListPic() {
            return songListPic;
        }

        public void setSongListPic(String songListPic) {
            this.songListPic = songListPic;
        }

        public String getSongListDescription() {
            return songListDescription;
        }

        public void setSongListDescription(String songListDescription) {
            this.songListDescription = songListDescription;
        }

        public List<Song> getSongs() {
            return songs;
        }

        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
