package test.liugang.com.myqiniu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.zhubo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SWCameraStreamingActivity.class);
                intent.putExtra("stream_json_str","rtmp://pili-publish.2dyt.com/1503d/547N5I7j?e=1502454265&token=tYBGEzG7NE_D23EScw43ZTxynVkyt1IpHig5WHRY:WR5QQA8GGD4Lv4FDtetbNO8-DZI=");
                startActivity(intent);


                //OKhttp最大的优点可以和RetrofitRxjava公用
                //Okhttp多路复用，请求连接和断开连接时间
                //Ok拦截器
            }
        });
        findViewById(R.id.guanzhong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PLVideoViewActivity.class);
                intent.putExtra("videoPath","rtmp://pili-publish.2dyt.com/1503d/oX4P0IHu?e=1502454789&token=tYBGEzG7NE_D23EScw43ZTxynVkyt1IpHig5WHRY:3Sr_E2yqWgxTjjToJ71P32n-Cco=");
                startActivity(intent);
            }
        });
    }
}
