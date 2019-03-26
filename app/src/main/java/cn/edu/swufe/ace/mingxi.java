package cn.edu.swufe.ace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mingxi extends Activity implements View.OnClickListener{

    int msgWhatr=6;
    int msgWhatw1=4;
    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
    ImageView iv;
    String yonghu,image,name,id,jine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mingxi);
        iv = findViewById(R.id.imageView2mx);
        tv1 = findViewById(R.id.textView17mx);
        tv2 = findViewById(R.id.shurujinemx);
        tv3 = findViewById(R.id.shuruduixiangmx);
        tv4 = findViewById(R.id.shurufangshimx);
        tv5 = findViewById(R.id.shurushijianmx);
        tv6 = findViewById(R.id.xuanzedidianmx);
        tv7 = findViewById(R.id.xuanzechengyuanmx);
        tv8 = findViewById(R.id.xuanzezhangbenmx);
        tv9 = findViewById(R.id.shurubeizhumx);

        SharedPreferences sp = getSharedPreferences("mingxi", Activity.MODE_PRIVATE);
        yonghu = sp.getString("user_id","");
        image = sp.getString("image","");
        name = sp.getString("leixing_name","");
        id = sp.getString("jizhang_id","");
        jine = sp.getString("jine","");

        iv.setImageResource(Integer.valueOf(image));
        tv1.setText(name);
        tv2.setText(jine);

        getData();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tijiao_jiyibimx)
        {
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if(msg.what == msgWhatr){
                        Toast.makeText(mingxi.this,"删除成功",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    if(msg.what == msgWhatw1){//连接数据库失败
                        Toast.makeText(mingxi.this,"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
                    }
                    super.handleMessage(msg);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int n;
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
                        Statement st1 = conn.createStatement();
                        st1.executeUpdate("delete from jizhang where jizhang_id='"+id+"' and user_id='"+yonghu+"'");
                        n=msgWhatr;
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.i("失败", "连接不到数据库");
                        n = msgWhatw1;
                    }
                    msg.what=n;
                    handler.sendMessage(msg);
                }
            }).start();
        }
        if(v.getId()==R.id.quxiao_jiyibimx)
        {
            finish();
        }
    }

    public void getData()
    {
        data.clear();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(mingxi.this,"加载成功",Toast.LENGTH_LONG).show();
                    tv3.setText(String.valueOf(data.get(0).get("duixiang")));
                    tv4.setText(String.valueOf(data.get(0).get("fangshi")));
                    tv5.setText(String.valueOf(data.get(0).get("shijian")));
                    tv6.setText(String.valueOf(data.get(0).get("didian")));
                    tv7.setText(String.valueOf(data.get(0).get("chengyuan")));
                    tv8.setText(String.valueOf(data.get(0).get("zhangben")));
                    tv9.setText(String.valueOf(data.get(0).get("beizhu")));
                }
                if(msg.what == msgWhatw1){//连接数据库失败
                    Toast.makeText(mingxi.this,"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
                }
                super.handleMessage(msg);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                int n;
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
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT * FROM jizhang where jizhang_id='"+id+"' and user_id='"+yonghu+"'");
                    Log.i("记账id", id);
                    if(!rs1.next())
                    {
                        n=msgWhatw1;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        while(rs1.next())
                        {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("duixiang", rs1.getString("jizhang_who"));
                            m.put("fangshi", rs1.getString("fangshi_id"));
                            m.put("shijian", rs1.getString("jizhang_year")+"-"+rs1.getString("jizhang_month")+"-"+rs1.getString("jizhang_day"));
                            m.put("didian", rs1.getString("jizhang_where"));
                            m.put("chengyuan", rs1.getString("chengyuan_id"));
                            m.put("zhangben", rs1.getString("zhangben_id"));
                            m.put("beizhu", rs1.getString("jizhang_beizhu"));
                            data.add(m);
                        }
                        n = msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    n = msgWhatw1;
                }
                msg.what=n;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
