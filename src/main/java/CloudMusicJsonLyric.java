public class CloudMusicJsonLyric {
    private boolean sgc;
    private boolean sfy;
    private boolean qfy;
    private JsonLrc lrc;
    private JsonLrc klyric;
    private JsonLrc tlyric;
    private int code;

    public CloudMusicJsonLyric(boolean sgc, boolean sfy, boolean qfy, JsonLrc lrc, JsonLrc klyric, JsonLrc tlyric, int code) {
        this.sgc = sgc;
        this.sfy = sfy;
        this.qfy = qfy;
        this.lrc = lrc;
        this.klyric = klyric;
        this.tlyric = tlyric;
        this.code = code;
    }

    public CloudMusicJsonLyric() {
    }

    public boolean isSgc() {
        return sgc;
    }

    public void setSgc(boolean sgc) {
        this.sgc = sgc;
    }

    public boolean isSfy() {
        return sfy;
    }

    public void setSfy(boolean sfy) {
        this.sfy = sfy;
    }

    public boolean isQfy() {
        return qfy;
    }

    public void setQfy(boolean qfy) {
        this.qfy = qfy;
    }

    public JsonLrc getLrc() {
        return lrc;
    }

    public void setLrc(JsonLrc lrc) {
        this.lrc = lrc;
    }

    public JsonLrc getKlyric() {
        return klyric;
    }

    public void setKlyric(JsonLrc klyric) {
        this.klyric = klyric;
    }

    public JsonLrc getTlyric() {
        return tlyric;
    }

    public void setTlyric(JsonLrc tlyric) {
        this.tlyric = tlyric;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CloudMusicJsonLyric{" +
                "sgc=" + sgc +
                ", sfy=" + sfy +
                ", qfy=" + qfy +
                ", lrc=" + lrc +
                ", klyric=" + klyric +
                ", tlyric=" + tlyric +
                ", code=" + code +
                '}';
    }

    public void setCode(int code) {
        this.code = code;
    }
}

class JsonLrc {
    private int version;
    //真正需要的部分
    private String lyric;

    public JsonLrc() {
    }

    public JsonLrc(int version, String lyric) {
        this.version = version;
        this.lyric = lyric;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "JsonLrc{" +
                "version=" + version +
                ", lyric='" + lyric + '\'' +
                '}';
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}