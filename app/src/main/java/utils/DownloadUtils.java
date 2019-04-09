package utils;

public class DownloadUtils {

    public interface OnDownloadListener {
        public void onDownload(String musics);
        public void onFailed(String errot);
    }
}