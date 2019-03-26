package cn.edu.swufe.ace;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends Activity{

    SharedPreferences sharedPreferences;
    private int msgWhatr = 6;
    private int msgWhatw1 = 41;//连接数据库失败
    private int msgWhatw2 = 42;//用户名/密码错误
    private int msgWhatw3 = 43;//用户不存在

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences0 = getSharedPreferences("default_user", Activity.MODE_PRIVATE);
                String yonghu = sharedPreferences0.getString("yonghu","");
                String status = sharedPreferences0.getString("status","0");
                if(status.equals("0"))
                {
                    startActivity(new Intent(StartActivity.this,LoginActivity.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(StartActivity.this,MainActivity.class));
                    finish();
//                    sharedPreferences = getSharedPreferences(yonghu, Activity.MODE_PRIVATE);
//                    final Handler handler = new Handler(){
//                        @Override
//                        public void handleMessage(Message msg){
//                            if(msg.what == msgWhatr){//登录成功
//                                Toast.makeText(StartActivity.this,"登录成功",Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(StartActivity.this,MainActivity.class));
//                                finish();
//                            }
//                            if(msg.what == msgWhatw1){//连接数据库失败
//                                Toast.makeText(StartActivity.this,"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(StartActivity.this,LoginActivity.class));
//                                finish();
//                            }
//                            if(msg.what == msgWhatw2){//用户名/密码错误
//                                Toast.makeText(StartActivity.this,"用户名/密码错误",Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(StartActivity.this,LoginActivity.class));
//                                finish();
//                            }
//                            if(msg.what == msgWhatw3){//用户不存在
//                                Toast.makeText(StartActivity.this,"用户不存在",Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(StartActivity.this,LoginActivity.class));
//                                finish();
//                            }
//                            super.handleMessage(msg);
//                        }
//                    };
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Message msg = handler.obtainMessage();
//                            try {
//                                Class.forName("com.mysql.jdbc.Driver");
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                            try {
//                                String url="jdbc:mysql://rm-bp132n0r530p95d0pfo.mysql.rds.aliyuncs.com:3306/acedata?user=root&password=Sy961016&useUnicode=true&characterEncoding=UTF-8";
//                                Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
//                                Log.i("成功", "连接到数据库");
//                                Statement statement = conn.createStatement();
//                                ResultSet rs = statement.executeQuery("select * from user_info where user_id = '"+sharedPreferences.getString("yonghu","")+"'");
//                                if(!rs.next())//用户不存在
//                                {
//                                    msg.what = msgWhatw3;
//                                }
//                                else
//                                {
//                                    if(sharedPreferences.getString("password","").equals(rs.getString("user_password")))
//                                    {
//                                        SharedPreferences sp = getSharedPreferences(sharedPreferences.getString("yonghu",""),Activity.MODE_PRIVATE);
//                                        SharedPreferences.Editor edit = sp.edit();
//                                        edit.putString("yonghu", sharedPreferences.getString("yonghu",""));
//                                        edit.putString("sex", rs.getString("user_sex"));
//                                        edit.putString("password", rs.getString("user_password"));
//                                        edit.putString("question", rs.getString("user_question"));
//                                        edit.commit();
//                                        msg.what = msgWhatr;
//                                        conn.close();
//                                    }
//                                    else
//                                    {
//                                        msg.what = msgWhatw2;
//                                    }
//                                }
//
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                                Log.i("失败", "连接不到数据库");
//                                msg.what = msgWhatw1;
//                            }
//                            handler.sendMessage(msg);
//                        }
//                    }).start();
                }
            }
        };
        timer.schedule(timerTask,2000);
    }
}
