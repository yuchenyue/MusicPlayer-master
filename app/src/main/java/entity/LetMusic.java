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
     */

    private String result;
    private int code;
    private List<DataBean> data;

    public class DataBean implements Serializable{

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

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
}
