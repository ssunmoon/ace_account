package cn.edu.swufe.ace;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenzi.sms.ZhenziSmsClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ZhaohuiActivity extends Activity implements View.OnClickListener{

    private int msgWhatr = 6;
    private int msgWhatw = 4;//短信发送失败
    private int msgWhatw1 = 41;//连接数据库失败
    private int msgWhatw2 = 42;//用户名/密码错误
    private int msgWhatw3 = 43;//用户不存在
    TextView v1;
    TextView v2;
    TextView v3;
    TextView v4;
    TextView v5;
    TextView v6;
    int n;
    String yifahaoma;
    String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhaohui);
        v1 = findViewById(R.id.shoujihao_zh);
        v2 = findViewById(R.id.xinmima);
        v3 = findViewById(R.id.querenxinmima_zh);
        v4 = findViewById(R.id.mibaowenti_zh);
        v5 = findViewById(R.id.mibaodaan_zh);
        v6 = findViewById(R.id.yanzhengma_zh);
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
                question = sharedPreferences.getString("question","");
                if(question.length()!=0)
                {
                    v4.setText(question);
                }
                else
                {
                    v4.setText("点击提交后显示");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fanhuidenglu_zh)
        {
            finish();
        }
        if(v.getId()==R.id.huoqu_zh)
        {
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if(msg.what == msgWhatr){
                        Toast.makeText(ZhaohuiActivity.this,"验证码已发送，请稍等",Toast.LENGTH_LONG).show();
                    }
                    if(msg.what == msgWhatw){
                        Toast.makeText(ZhaohuiActivity.this,"验证码发送失败，请检查号码后重试",Toast.LENGTH_LONG).show();
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
                        String result = client.send(yifahaoma, "验证码："+n+"，您正在更改 ACE记账 账号密码，验证码有效时间为5分钟");
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
        if(v.getId()==R.id.tijiao_zh)
        {
            if(v1.getText().toString().length()==0)
            {
                Toast.makeText(ZhaohuiActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(v4.getText().toString().equals("点击提交后显示"))
                {
                    final Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            if(msg.what == msgWhatr){//登录成功
                                Toast.makeText(ZhaohuiActivity.this,"密保问题加载成功",Toast.LENGTH_LONG).show();
                            }
                            if(msg.what == msgWhatw1){//连接数据库失败
                                Toast.makeText(ZhaohuiActivity.this,"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
                            }
                            if(msg.what == msgWhatw3){//用户不存在
                                Toast.makeText(ZhaohuiActivity.this,"用户不存在",Toast.LENGTH_LONG).show();
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
                                ResultSet rs = statement.executeQuery("select * from user_info where user_id = '"+v1.getText().toString()+"'");
                                if(!rs.next())//用户不存在
                                {
                                    msg.what = msgWhatw3;
                                }
                                else
                                {
                                    v4.setText(rs.getString("user_question"));
                                    msg.what = msgWhatr;
                                    conn.close();
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
                else
                {
                    if(v2.getText().toString().length()==0||v3.getText().toString().length()==0||v5.getText().toString().length()==0||v6.getText().toString().length()==0)
                    {
                        Toast.makeText(ZhaohuiActivity.this,"请填写完整后提交",Toast.LENGTH_LONG).show();
                    }
                    //表项完整
                    //表项完整
                    else
                    {
                        if(!v6.getText().toString().equals(String.valueOf(n)))
                        {
                            Toast.makeText(ZhaohuiActivity.this,"验证码错误",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(!v1.getText().toString().equals(yifahaoma))
                            {
                                Toast.makeText(ZhaohuiActivity.this,"电话号码已更改，请重新获取验证码",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                if(!v2.getText().toString().equals(v3.getText().toString()))
                                {
                                    Toast.makeText(ZhaohuiActivity.this,"确认密码错误，请重新输入",Toast.LENGTH_LONG).show();
                                }
                                //判断密保答案
                                else
                                {
                                    final Handler handler = new Handler(){
                                        @Override
                                        public void handleMessage(Message msg){
                                            if(msg.what == msgWhatr){
                                                Toast.makeText(ZhaohuiActivity.this,"修改密码成功",Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                            if(msg.what == msgWhatw){
                                                Toast.makeText(ZhaohuiActivity.this,"密保问题错误",Toast.LENGTH_LONG).show();
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
                                                ResultSet rs = statement.executeQuery("select * from user_info where user_id = '"+v1.getText().toString()+"'");
                                                rs.next();
                                                //密保答案不正确
                                                if(!v5.getText().toString().equals(rs.getString("user_answ")))
                                                {
                                                    msg.what = msgWhatw;
                                                }
                                                //信息全部准确
                                                else
                                                {
                                                    Statement statement1 = conn.createStatement();
                                                    statement1.executeUpdate("update user_info set user_password='"+v2.getText().toString()+"' where user_id='"+v1.getText().toString()+"'");
                                                    SharedPreferences sp = getSharedPreferences(v1.getText().toString(),Activity.MODE_PRIVATE);
                                                    SharedPreferences.Editor edit = sp.edit();
                                                    edit.putString("yonghu", v1.getText().toString());
                                                    edit.putString("sex", rs.getString("user_sex"));
                                                    edit.putString("password", rs.getString("user_password"));
                                                    edit.putString("question", rs.getString("user_question"));
                                                    edit.commit();
                                                    msg.what = msgWhatr;
                                                }
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
    }
}
