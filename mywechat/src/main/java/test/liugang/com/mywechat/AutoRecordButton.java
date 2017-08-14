package test.liugang.com.mywechat;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * @ Description:
 * @ Date:2017/8/12
 * @ Author:刘刚
 */

public class AutoRecordButton extends Button implements AudioManager.AudioStateListener {
    private boolean isRecording=false; //已经开始录音
    private boolean mReady;//是否触发longclick;
    private float mTime;
    private AudioManager mAudioManager;
    private static final int DISTANCE_Y_CANCLE=50;
    private static final int STATE_NORMAL=1;
    private static final int STATE_REORDING=2;
    private static final int STATE_WANT_TO_CANCEL=3;
    /**
     * 获取音量大小的Runnable
     */
    private Runnable mgetVoiceLevelRunnable=new Runnable() {
        @Override
        public void run() {
           while (isRecording){
               try {
                   Thread.sleep(100);
                   mTime+=0.1f;
                   mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }
    };
     private int mCurState=STATE_NORMAL;

    private DialogManager mDialogManager;
    public AutoRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager=new DialogManager(getContext());


        String dir= Environment.getExternalStorageDirectory()+"/imooc_recorder_audios";
        mAudioManager=AudioManager.getInstance(dir);
        mAudioManager.setOnAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mReady=true;
                 mAudioManager.prepareAudio();
                return false;
            }
        });
    }
    //录音完成后的回调
  public  interface  AudioFinishRecorderListener{
     void onFinish(float seconds,String filePath);
  }
  private AudioFinishRecorderListener mListener;
    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener){
        mListener=listener;
    }

    public AutoRecordButton(Context context) {
      this(context,null);
    }
    private static final int MSG_AUDIO_PREPARED=0x100 ;
    private static final int MSG_VOICE_CHANGE=0x111 ;
    private static final int MSG_DIALOG_DISMISS=0x112 ;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
          switch (msg.what){
              case MSG_AUDIO_PREPARED:
                  mDialogManager.showRecordingDialog();
                  isRecording=true;
                  new Thread(mgetVoiceLevelRunnable).start();
                  break;
              case MSG_VOICE_CHANGE:
                  mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                  break;
              case MSG_DIALOG_DISMISS:
                  mDialogManager.dissMissDialog();
                  break;
          }
        }
    };
    @Override
    public void wellPrepared() {
       mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        int x= (int) event.getX();
        int y= (int) event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                // TODO: 2017/8/12
                isRecording=true;
                changeState(STATE_REORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if(isRecording){
                    if (wantToCancle(x,y)){
                        changeState(STATE_WANT_TO_CANCEL);
                    }else {
                        changeState(STATE_REORDING);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (!mReady){
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording ||mTime<0.6f){
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS,1300);
                } else if (mCurState==STATE_REORDING){
                    //callbacktoAct
                    mDialogManager.dissMissDialog();
                    mAudioManager.release();

                    if (mListener!=null){
                        mListener.onFinish(mTime,mAudioManager.getCurrentFilePath());
                    }
                }else if (mCurState==STATE_WANT_TO_CANCEL){  //正常录制结束
                        mDialogManager.dissMissDialog();
                        mAudioManager.cancel();
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
    }
    /**
     * 恢复状态以及标志位
     */

    private void reset() {
          isRecording=false;
          mReady=false;
          mTime=0;
          changeState(STATE_NORMAL);
    }

    private boolean  wantToCancle(int x, int y) {
        if (x<0||x>getWidth()){
               return  true;
        }
         if (y<-DISTANCE_Y_CANCLE||y>getHeight()+DISTANCE_Y_CANCLE){
             return true;
         }
        return false;
    }

    private void changeState(int stateReording) {
        if (mCurState!=stateReording){
            mCurState=stateReording;
            switch (stateReording){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_record_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_REORDING:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_recorder_recording);
                    if (isRecording){
                        //Dialog.recording
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_recorder_want_cancel);
                    // TODO: 2017/8/12
                     mDialogManager.wantToCancel();
                    break;
            }
        }
    }


}
