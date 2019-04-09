package entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/2/28.
 */

public class LetMusic implements Serializable {
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
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         *id: 2699438686,
         title: "我相信与你终将旷日持久，才敢说来日方长",
         creator: "雾与晨的杂货店",
         description: "我相信与你终将旷日持久，于是才敢说一句来日方长。 愿你能心中坦然，且以时光平安喜乐 愿你能懂得释怀，且被岁月温柔以待",
         coverImgUrl: "http://p1.music.126.net/hhXSlux5iO4y3LUTX2nl-w==/109951163967900060.jpg?param=400y400",
         songNum: 45,
         playCount: 14677
         */

        private String id;
        private String title;
        private String creator;
        private String coverImgUrl;
        private String playCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String name) {
            this.title = name;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }
    }
}
