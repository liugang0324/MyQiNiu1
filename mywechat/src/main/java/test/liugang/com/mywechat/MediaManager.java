package test.liugang.com.mywechat;

import android.media.*;

import java.io.IOException;

/**
 * @ Description:
 * @ Date:2017/8/14
 * @ Author:刘刚
 */
public class MediaManager {
    public static MediaPlayer mMediaPlayer;
    private static boolean isPause;

    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
       if (mMediaPlayer==null){
           mMediaPlayer=new MediaPlayer();
           mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
               @Override
               public boolean onError(MediaPlayer mp, int what, int extra) {
                   mMediaPlayer.reset();
                   return false;
               }
           });
       }else {
           mMediaPlayer.reset();
       }

        try {
            mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void pause(){
        if (mMediaPlayer!=null&&mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            isPause=true;
        }
    }

    public static void resmue(){
        if (mMediaPlayer!=null&&isPause){
            mMediaPlayer.start();
            isPause=false;
        }
    }
    public static void release(){
        if (mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
    }
}
