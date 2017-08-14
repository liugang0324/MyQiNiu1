package test.liugang.com.mywechat;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private ListView mListView;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder>mDatas=new ArrayList<>();
    private AutoRecordButton mAutoRecordButton;
    private View mAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView= (ListView) findViewById(R.id.id_listview);
        mAutoRecordButton= (AutoRecordButton) findViewById(R.id.record_btn);
        mAutoRecordButton.setAudioFinishRecorderListener(new AutoRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(filePath,seconds);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size()-1);
            }
        });
        mAdapter=new RecorderAdapter(this,mDatas);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAnimView!=null){
                    mAnimView.setBackgroundResource(R.drawable.adj);
                    mAnimView=null;
                }
                //播放动画音频
                mAnimView = view.findViewById(R.id.id_recorder_anim);
                mAnimView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable anim= (AnimationDrawable) mAnimView.getBackground();
                anim.start();

                MediaManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                         mAnimView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
     MediaManager.resmue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    MediaManager.release();
    }

    class Recorder{
        float time;
        String filePath;

        public Recorder(String filePath, float time) {
            super();
            this.filePath = filePath;
            this.time = time;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }
    }
}
