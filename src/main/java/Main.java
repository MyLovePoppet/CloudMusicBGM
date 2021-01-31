public class Main {
    public static void main(String[] args) {
        if (!CloudMusicUtils.initNativeDll()) {
            System.err.println("init error! please check your netease cloud music is on!");
            CloudMusicUtils.nativeGc();
            return;
        }
        //test
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CloudMusicUtils.updateData();
            String title = CloudMusicUtils.getCurrentMusicTitle();
            double position = CloudMusicUtils.getCurrentMusicPosition();
            String lyric = CloudMusicUtils.getCurrentLyric();
            System.out.println(title + "\t" + position + "\t" + lyric);
        }
        CloudMusicUtils.nativeGc();
    }
}
