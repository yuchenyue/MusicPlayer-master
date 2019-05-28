package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 歌单
 * Created by Administrator on 2019/2/28.
 */

public class LetMusic implements Serializable {
    /**
     * result : SUCCESS
     * code : 200
     * data :
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public class DataBean implements Serializable{

        private String id;
        private String name;
        private String description;
        private String coverImgUrl;
        private String trackCount;
        private String playCount;
        private List<Creater> creater;

        public class Creater implements Serializable{
            private String nickname;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public String getTrackCount() {
            return trackCount;
        }

        public void setTrackCount(String trackCount) {
            this.trackCount = trackCount;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public List<Creater> getCreater() {
            return creater;
        }

        public void setCreater(List<Creater> creater) {
            this.creater = creater;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
}
