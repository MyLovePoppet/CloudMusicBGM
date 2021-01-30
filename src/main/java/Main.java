public class Main {
    public static void main(String[] args) {
        if (!CloudMusicUtils.initNativeDll()) {
            System.err.println("init error! please check your netease cloud music is on!");
            CloudMusicUtils.nativeGc();
            return;
        }
        System.out.println(CloudMusicUtils.getCloudMusicTitle());
        System.out.println(CloudMusicUtils.getCurrentPosition());
        CloudMusicUtils.nativeGc();
    }
}
