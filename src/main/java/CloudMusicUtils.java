public class CloudMusicUtils {
    public static native boolean initNativeDll();

    public static native String getCloudMusicTitle();

    public static native double getCurrentPosition();

    public static native void nativeGc();

    private static String DllLocation = "E:\\VSProjects\\BGM\\Debug\\CloudMusicBGM.dll";

    static {
        System.load(DllLocation);
    }


}
