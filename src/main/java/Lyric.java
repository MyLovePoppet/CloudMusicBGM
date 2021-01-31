import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lyric {
    private double position;
    private String text;
    private static final Pattern pattern = Pattern.compile("\\[\\d+:[0-5][0-9].\\d+]");

    public Lyric(double position, String text) {
        this.position = position;
        this.text = text;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "position=" + position +
                ", text='" + text + '\'' +
                '}';
    }
    //格式类似于     [00:00.001]{歌词}，解析即可
    public static Lyric format(String str) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            double position = 0.0;
            int leftBracket = str.indexOf('[');
            int colon = str.indexOf(':');
            int dot = str.indexOf('.');
            int rightBracket = str.indexOf(']');
            position += Integer.parseInt(str.substring(leftBracket + 1, colon)) * 60.0;
            position += Integer.parseInt(str.substring(colon + 1, dot));
            position += Integer.parseInt(str.substring(dot + 1, rightBracket)) * 0.001;
            String text = str.substring(rightBracket + 1);
            return new Lyric(position, text);
        } else {
            throw new RuntimeException("Lyric format is error!");
        }
    }
}
