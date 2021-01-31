import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Format;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class LyricTest {
    Gson gson = new Gson();

    //C:\Users\shuqy\AppData\Local\Netease\CloudMusic\webdata\file
    @Test
    public void testGetCurrentMusicID() {
        String historyPath = System.getenv("LOCALAPPDATA") + "\\Netease\\CloudMusic\\webdata\\file\\history";
        System.out.println(historyPath);
        boolean isIdOK = false, isNameOK = false;
        String id = "";
        String name = "";
        try {
            JsonReader jsonReader = gson.newJsonReader(Files.newBufferedReader(Paths.get(System.getenv("LOCALAPPDATA"), "Netease\\CloudMusic\\webdata\\file\\history")));
            jsonReader.beginArray();
            jsonReader.beginObject();
            String jsonName = jsonReader.nextName();
            if (jsonName.equals("track")) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    jsonName = jsonReader.nextName();
                    if (jsonName.equals("id")) {
                        id = jsonReader.nextString();
                        isIdOK = true;
                    } else if (jsonName.equals("name")) {
                        name = jsonReader.nextString();
                        isNameOK = true;
                    } else {
                        jsonReader.skipValue();
                    }
                    if (isIdOK && isNameOK) {
                        break;
                    }
                }
            }

            jsonReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(id + "\t" + name);
    }

    //https://music.163.com/api/song/lyric?id=1445556953&lv=1&kv=1&tv=-1
    @Test
    public void generateLyricByMusicID() {
        String id = "1445556953";
        String url = MessageFormat.format("https://music.163.com/api/song/lyric?id={0}&lv=1&kv=1&tv=-1", id);
        HttpClient httpClient = HttpClient.newBuilder().build();
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
            System.out.println(lyricList);
            System.out.println(getCurrentPositionLyric(lyricList.listIterator(), 166));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String getCurrentPositionLyric(ListIterator<Lyric> iterator, double position) {
        if (!iterator.hasNext() && !iterator.hasPrevious()) {
            return "";
        }
        if (!iterator.hasNext()) {
            return iterator.previous().getText();
        }
        Lyric prev = iterator.next();
        Lyric next = iterator.next();
        while (!(prev.getPosition() <= position && position < next.getPosition())) {
            if (!iterator.hasNext()) {
                return next.getText();
            }
            if (!iterator.hasPrevious()) {
                return prev.getText();
            }
            if (position < prev.getPosition()) {
                next = prev;
                iterator.previous();
                prev = iterator.previous();
            } else {
                prev = next;
                next = iterator.next();
            }
        }
        return prev.getText();
    }
}
