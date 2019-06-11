package com.swufe.wy.yaoyiyao;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorMnager; //传感器
    private Vibrator vibrator; //手机震动
    private static String str[] = {"石头", "剪刀", "布"};
    private static int pics[] = {R.mipmap.shitou, R.mipmap.jiandao, R.mipmap.bu}; //以int型定义图片资源

    private TextView text;
    private ImageView img;
    private static final String TAG = "MainActivity";
    private static final int SENSOR_SHAKE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.txtlabel);
        img = findViewById(R.id.imageView);

        sensorMnager = (SensorManager) getSystemService(SENSOR_SERVICE); //获得传感器服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    protected void onResume(){
        super.onResume();
        if (sensorMnager !=null){  //注册监听器
            sensorMnager.registerListener(sensorEventListener,sensorMnager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
            //第一个参数时Listener，第二个是所得传感器类型，第三个是参数值获取传感器信息的频率
        }
    }

    protected void onStop(){
        super.onStop();
        if (sensorMnager != null){  //取消监听器
            sensorMnager.unregisterListener(sensorEventListener);
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //传感器信息改变时执行该方法
            float[] values=event.values;
            float x = values[0];  //x轴方向重力加速度，向右为正
            float y = values[1];  //y轴方向重力加速度，向右为正
            float z = values[2];  //z轴方向重力加速度，向右为正
            Log.i(TAG, "x["+ x + "] y[" + y +"] z[" + z +"]");
            //一般手机在这三个方向重力加速度达到40度：摇晃手机状态

            int meduValue = 10; //不同手机厂商这个数值可能不同
            if(Math.abs(x)>meduValue||Math.abs(y)>meduValue||Math.abs(z)>meduValue){
                vibrator.vibrate(200);
                Message msg=new Message();
                msg.what=SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    Handler handler=new Handler(){

        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 10:
                    Log.i(TAG, "handleMessage: 检测到摇晃，执行操作！");
                    Random r = new Random();
                    int num = Math.abs(r.nextInt())%3;
                    text.setText(str[num]);
                    img.setImageResource(pics[num]);
                    break;
            }
        }
    };
}
