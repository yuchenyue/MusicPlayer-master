package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索
 * Created by Administrator on 2019/2/28.
 */

public class NetMusic implements Serializable {
    /**
     * result : SUCCESS
     * code : 200
     * data : [{"id":"471403427","name":"我喜欢上你时的内心活动","singer":"陈绮贞","pic":"https://api.bzqll.com/music/netease/pic?id=471403427&key=579621905","lrc":"https://api.bzqll.com/music/netease/lrc?id=471403427&key=579621905","url":"https://api.bzqll.com/music/netease/url?id=471403427&key=579621905"}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

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

    public static class DataBean implements Serializable {
        /**
         * id : 471403427
         * name : 我喜欢上你时的内心活动
         * singer : 陈绮贞
         * pic : https://api.bzqll.com/music/netease/pic?id=471403427&key=579621905
         * lrc : https://api.bzqll.com/music/netease/lrc?id=471403427&key=579621905
         * url : https://api.bzqll.com/music/netease/url?id=471403427&key=579621905
         */

        private String id;
        private String name;
        private String singer;
        private int time;
        private String pic;
        private String lrc;
        private String url;

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
}
