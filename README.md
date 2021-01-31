网易云音乐版本：2.5.2

由于网易云音乐是x86版本，所以JNI需使用x86编译成dll，并使用x86版本的jdk来运行。

| 版本 | 功能                     | 时间      |
| ---- | ------------------------ | --------- |
| 1.0  | 获取当前歌曲进度以及歌词 | 2021/1/31 |

# 有关偏移地址0x8669C8的得出方式
***以2.5.2版本网易云音乐为例***

使用CE修改器监视当前歌曲播放的时间（网易云音乐内播放时间是double类型），得出绝对地址`0x6E5869C8`。

![](https://i.niupic.com/images/2021/01/31/9aCE.png)

接着点击“找出是什么改写了该地址”，点击网易云播放，后暂停，找到相关代码，点击“显示反汇编程序”：

![](https://i.niupic.com/images/2021/01/31/9aDj.png)

![](https://i.niupic.com/images/2021/01/31/9aDm.png)

从上图可得出基址为`cloudmusic.dll`模块的基址，偏移计算为`0x6E5869C8-0x6DD20000=0x8669C8`。

在后续程序(C++)中只需要找到网易云音乐进程内`cloudmusic.dll`的模块的基址，然后加上偏移`0x8669C8`即是网易云音乐当前播放歌曲的实时进度，该进度是double类型，只需要读取8个字节，然后转化为double数据即可。