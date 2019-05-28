package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 网络列表
 * Created by Administrator on 2019/2/28.
 */

public class ListMusic implements Serializable {
    /**
     * code: 200,
     msg: "OK",
     timestamp: 1559022207666,
     data: [
     {
     singer: "李佳薇",
     name: "大火",
     id: "274696",
     time: 324,
     pic: "http://p2.music.126.net/6VshvgnaD5VBs_z4j1QKlA==/109951163188719203.jpg",
     lrc: "http://v1.itooi.cn/netease/lrc?id=274696",
     url: "http://v1.itooi.cn/netease/url?id=274696"
     }
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public class DataBean implements Serializable {

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
