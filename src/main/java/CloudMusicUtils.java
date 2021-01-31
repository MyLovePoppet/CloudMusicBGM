import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CloudMusicUtils {
    public static native boolean initNativeDll();

    private static native String getCloudMusicTitle();

    private static native double getCurrentPosition();

    public static native void nativeGc();

    private static String DllLocation = "E:\\VSProjects\\BGM\\Debug\\CloudMusicBGM.dll";

    static {
        System.load(DllLocation);
    }

    private static String currentMusicID = "";
    private static String currentMusicTitle = "";
    private static double currentMusicPosition = 0.0;
    private static String currentLyric = "";
    private static List<Lyric> currentMusicLyricList = Collections.emptyList();
    public static ListIterator<Lyric> lyricIterator = currentMusicLyricList.listIterator();

    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    private static String getCurrentMusicID() {
        boolean isIdOK = false;
        String id = "";
        try {
            //C:\Users\[userName]\AppData\Local\Netease\CloudMusic\webdata\file\history
            //上述文件是一个json数据格式，存放的是播放的历史记录，第一条即是最新播放的数据，根据该数据可以找到歌曲id
            //进而找到歌词所在
            //该方式的缺点是网易云音乐客户端在播放新的歌曲的时候不会立即刷新该文件，会有个2s~3s左右的延迟，暂时不知道怎么解决
            JsonReader jsonReader = gson.newJsonReader(Files.newBufferedReader(Paths.get(System.getenv("LOCALAPPDATA"), "Netease\\CloudMusic\\webdata\\file\\history")));
            jsonReader.beginArray();
            jsonReader.beginObject();
            String jsonName = jsonReader.nextName();
            if (jsonName.equals("track")) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    jsonName = jsonReader.nextName();
                    if (jsonName.equals("id")) {
                        id = jsonReader.nextString();//找到第一个id
                        isIdOK = true;
                    } else {
                        jsonReader.skipValue();
                    }
                    if (isIdOK) {
                        break;
                    }
                }
            }
            jsonReader.close();
            System.out.println(id);
            return id;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static List<Lyric> getCurrentMusicLyricList(String musicID) {
        String url = MessageFormat.format("https://music.163.com/api/song/lyric?id={0}&lv=1&kv=1&tv=-1", musicID);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            CloudMusicJsonLyric jsonLyric = gson.fromJson(httpResponse.body(), CloudMusicJsonLyric.class);
            String[] lyricStr = jsonLyric.getLrc().getLyric().split("\n");
            List<Lyric> lyricList = new LinkedList<>();
            for (String s : lyricStr) {
                lyricList.add(Lyric.format(s));
            }
            return lyricList;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    //使用List的迭代器进行寻找Position，增加效率
    private static String getCurrentPositionLyric(ListIterator<Lyric> iterator, double position) {
        if (!iterator.hasNext() && !iterator.hasPrevious()) {
            return "";
        }
        if (!iterator.hasNext()) {
            return iterator.previous().getText();
        }
        Lyric prev = iterator.next();
        if (!iterator.hasNext()) {
            return prev.getText();
        }
        Lyric next = iterator.next();
        //找到position所在的位置
        while (!(prev.getPosition() <= position && position < next.getPosition())) {
            if (!iterator.hasNext()) {
                return next.getText();
            }
            if (!iterator.hasPrevious()) {
                return prev.getText();
            }
            //向前查找
            if (position < prev.getPosition()) {
                next = prev;
                iterator.previous();
                prev = iterator.previous();
            } else {//向后查找
                prev = next;
                next = iterator.next();
            }
        }
        return prev.getText();
    }

    /**
     * 更新内容
     */
    public static void updateData() {
        String currentID = getCurrentMusicID();
        //根据id来判断，有一定延迟，在2s左右，暂时无法解决
        if (!currentMusicID.equals(currentID)) {
            currentMusicID = currentID;
            currentMusicTitle = getCloudMusicTitle();
            currentMusicLyricList = getCurrentMusicLyricList(currentMusicID);
            lyricIterator = currentMusicLyricList.listIterator();
        }
        currentMusicPosition = getCurrentPosition();
        currentLyric = getCurrentPositionLyric(lyricIterator, currentMusicPosition);
    }

    public static String getCurrentMusicTitle() {
        return currentMusicTitle;
    }

    public static double getCurrentMusicPosition() {
        return currentMusicPosition;
    }

    public static String getCurrentLyric() {
        return currentLyric;
    }
}
