package cn.edu.swufe.ace;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends Activity implements View.OnClickListener{

    private int msgWhatr = 6;
    private int msgWhatw1 = 41;//连接数据库失败
    private int msgWhatw2 = 42;//用户名/密码错误
    private int msgWhatw3 = 43;//用户不存在
    TextView v1,v2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        v1 = findViewById(R.id.yonghuming);
        v2 = findViewById(R.id.mima);
        SharedPreferences sharedPreferences0 = getSharedPreferences("default_user", Activity.MODE_PRIVATE);
        String yonghu0 = sharedPreferences0.getString("yonghu","");
        if(yonghu0.length()!=0)
        {
            SharedPreferences sharedPreferences = getSharedPreferences(yonghu0, Activity.MODE_PRIVATE);
            String yonghu = sharedPreferences.getString("yonghu","");
            v1.setText(yonghu);
            String sex = sharedPreferences.getString("sex","");
            if(sex.equals("男"))
            {
                ImageView imageView = findViewById(R.id.logo);
                imageView.setImageResource(R.drawable.man);
            }
        }
        v1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences sharedPreferences = getSharedPreferences(v1.getText().toString(), Activity.MODE_PRIVATE);
                String sex = sharedPreferences.getString("sex","");
                if(sex.length()!=0)
                {
                    if(sex.equals("男"))
                    {
                        ImageView imageView = findViewById(R.id.logo);
                        imageView.setImageResource(R.drawable.man);
                    }
                    if(sex.equals("女"))
                    {
                        ImageView imageView = findViewById(R.id.logo);
                        imageView.setImageResource(R.drawable.woman);
                    }
                }
                else
                {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.denglu)//登录
        {
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if(msg.what == msgWhatr){//登录成功
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                        SharedPreferences sp0 = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor edit0 = sp0.edit();
                        edit0.putString("yonghu", v1.getText().toString());
                        edit0.putString("status", "1");
                        edit0.commit();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                    if(msg.what == msgWhatw1){//连接数据库失败
                        Toast.makeText(LoginActivity.this,"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
                    }
                    if(msg.what == msgWhatw2){//用户名/密码错误
                        Toast.makeText(LoginActivity.this,"用户名/密码错误",Toast.LENGTH_LONG).show();
                    }
                    if(msg.what == msgWhatw3){//用户不存在
                        Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_LONG).show();
                    }
                    super.handleMessage(msg);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    v1 = findViewById(R.id.yonghuming);
                    v2 = findViewById(R.id.mima);
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
                        ResultSet rs = statement.executeQuery("select * from user_info where user_id = '"+v1.getText().toString()+"'");
                        if(!rs.next())//用户不存在
                        {
                            msg.what = msgWhatw3;
                        }
                        else
                        {
                            if(v2.getText().toString().equals(rs.getString("user_password")))
                            {
                                int n = 1+(int)(Math.random()*99999);
                                SharedPreferences sp = getSharedPreferences(v1.getText().toString(),Activity.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("yonghu", v1.getText().toString());
                                edit.putString("sex", rs.getString("user_sex"));
                                edit.putString("password", rs.getString("user_password"));
                                edit.putString("question", rs.getString("user_question"));
                                edit.putString("suijima", String.valueOf(n));
                                edit.commit();
                                Statement st = conn.createStatement();
                                st.executeUpdate("update user_info set user_suijima='"+n+"' where user_id='"+v1.getText().toString()+"'");
                                msg.what = msgWhatr;
                                conn.close();
                            }
                            else
                            {
                                msg.what = msgWhatw2;
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.i("失败", "连接不到数据库");
                        msg.what = msgWhatw1;
                    }
                    handler.sendMessage(msg);
                }
            }).start();
        }
        if(v.getId()==R.id.zhuce)//注册
        {
            startActivity(new Intent(LoginActivity.this,ZhuceActivity.class));
        }
        if(v.getId()==R.id.wangji)///忘记密码
        {
            startActivity(new Intent(LoginActivity.this,ZhaohuiActivity.class));
        }
    }
}
