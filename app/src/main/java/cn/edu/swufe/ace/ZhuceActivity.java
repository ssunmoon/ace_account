package cn.edu.swufe.ace;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenzi.sms.ZhenziSmsClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZhuceActivity extends Activity implements View.OnClickListener{

    private int msgWhatr = 6;
    private int msgWhatw = 4;//短信发送失败/用户已存在
    TextView v1;
    TextView v2;
    TextView v3;
    TextView v4;
    TextView v5;
    TextView v6;
    RadioButton bt;
    int n;
    String yifahaoma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce);
        v1 = findViewById(R.id.shoujihao);
        v2 = findViewById(R.id.shoujihao2);
        v3 = findViewById(R.id.shoujihao3);
        v4 = findViewById(R.id.mibaowenti);
        v5 = findViewById(R.id.mibaodaan);
        v6 = findViewById(R.id.shoujihao4);
        bt = findViewById(R.id.bt_man);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fanhuidenglu)
        {
            finish();
        }
        if(v.getId()==R.id.button4)
        {
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if(msg.what == msgWhatr){
                        Toast.makeText(ZhuceActivity.this,"验证码已发送，请稍等",Toast.LENGTH_LONG).show();
                    }
                    if(msg.what == msgWhatw){
                        Toast.makeText(ZhuceActivity.this,"验证码发送失败，请检查号码后重试",Toast.LENGTH_LONG).show();
                    }
                    super.handleMessage(msg);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //使用事先申请的AppId、AppSecret初始化ZhenziSmsClient
                    ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com","101069","1aa6940e-ba2f-4782-b8ec-92dd169fd54b");
                    Message msg = handler.obtainMessage();
                    //发送短信
                    try {
                        n = 100000+(int)(Math.random()*99999);
                        yifahaoma = v1.getText().toString();
                        String result = client.send(yifahaoma, "验证码："+n+"，您正在注册 ACE记账 账号，验证码有效时间为5分钟");
                        if(result!="{\"code\":0,\"data\":\"发送成功\"}")
                        {
                            Log.i("成功", result);
                            msg.what = msgWhatr;
                        }
                        else
                        {
                            Log.i("失败", result);
                            msg.what = msgWhatw;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.what = msgWhatw;
                    }
                    handler.sendMessage(msg);
                }
            }).start();
        }
        if(v.getId()==R.id.denglu3)
        {
            if(v1.getText().toString().length()==0||v2.getText().toString().length()==0||v3.getText().toString().length()==0||v4.getText().toString().length()==0||v5.getText().toString().length()==0||v6.getText().toString().length()==0)
            {
                Toast.makeText(ZhuceActivity.this,"请填写完整后提交",Toast.LENGTH_LONG).show();
            }
            //表项完整
            else
            {
                if(!v6.getText().toString().equals(String.valueOf(n)))
                {
                    Toast.makeText(ZhuceActivity.this,"验证码错误",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(!v1.getText().toString().equals(yifahaoma))
                    {
                        Toast.makeText(ZhuceActivity.this,"电话号码已更改，请重新获取验证码",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(!v2.getText().toString().equals(v3.getText().toString()))
                        {
                            Toast.makeText(ZhuceActivity.this,"确认密码错误，请重新输入",Toast.LENGTH_LONG).show();
                        }
                        //信息全部准确
                        else
                        {
                            final Handler handler = new Handler(){
                                @Override
                                public void handleMessage(Message msg){
                                    if(msg.what == msgWhatr){
                                        Toast.makeText(ZhuceActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    if(msg.what == msgWhatw){
                                        Toast.makeText(ZhuceActivity.this,"该用户已存在",Toast.LENGTH_LONG).show();
                                    }
                                    super.handleMessage(msg);
                                }
                            };
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = handler.obtainMessage();
                                    try {
                                        Class.forName("com.mysql.jdbc.Driver");
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        String url="jdbc:mysql://rm-bp132n0r530p95d0pfo.mysql.rds.aliyuncs.com:3306/acedata?user=root&password=Sy961016&useUnicode=true&characterEncoding=UTF-8";
                                        Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                                        Log.i("成功", "连接到数据库");
                                        Statement statement = conn.createStatement();
                                        if(bt.isChecked())
                                        {
                                            statement.executeUpdate("insert into user_info values ('"+v1.getText().toString()+"','无','男','"+v2.getText().toString()+"','"+v4.getText().toString()+"','"+v5.getText().toString()+"','普通会员','')");
                                            SharedPreferences sp = getSharedPreferences(v1.getText().toString(),Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor edit = sp.edit();
                                            edit.putString("yonghu", v1.getText().toString());
                                            edit.putString("sex", "男");
                                            edit.putString("password", v2.getText().toString());
                                            edit.putString("question", v4.getText().toString());
                                            edit.putString("zhangben", "默认账本 >");
                                            edit.commit();
                                            SharedPreferences sp0 = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor edit0 = sp0.edit();
                                            edit0.putString("yonghu", v1.getText().toString());
                                            edit0.putString("status", "0");
                                            edit0.commit();
                                        }
                                        else
                                        {
                                            statement.executeUpdate("insert into user_info values ('"+v1.getText().toString()+"','无','女','"+v2.getText().toString()+"','"+v4.getText().toString()+"','"+v5.getText().toString()+"','普通会员','')");
                                            SharedPreferences sp = getSharedPreferences(v1.getText().toString(),Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor edit = sp.edit();
                                            edit.putString("yonghu", v1.getText().toString());
                                            edit.putString("sex", "女");
                                            edit.putString("password", v2.getText().toString());
                                            edit.putString("question", v4.getText().toString());
                                            edit.putString("zhangben", "默认账本 >");
                                            edit.commit();
                                            SharedPreferences sp0 = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor edit0 = sp0.edit();
                                            edit0.putString("yonghu", v1.getText().toString());
                                            edit0.putString("status", "0");
                                            edit0.commit();
                                        }
                                        msg.what = msgWhatr;
                                        conn.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        Log.i("失败", "连接不到数据库");
                                        msg.what = msgWhatw;
                                    }
                                    handler.sendMessage(msg);
                                }
                            }).start();
                        }
                    }
                }

            }

        }
    }
}
