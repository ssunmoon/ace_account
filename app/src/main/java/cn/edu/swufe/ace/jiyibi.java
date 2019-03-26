package cn.edu.swufe.ace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class jiyibi extends Activity implements View.OnClickListener {

    ImageView iv;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
    Button bt1,bt2;
    String yonghu,leibie,image,leixing;
    String []fangshi;
    String []chengyuan;
    String []zhangben;
    int fangshi_n,chengyuan_n,zhangben_n;
    int flag=0;
    private CustomDatePicker mTimePicker;
    int msgWhatr=6;
    int msgWhatw=4;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiyibi);
        iv = findViewById(R.id.imageView2);
        tv1 = findViewById(R.id.textView17);
        tv2 = findViewById(R.id.shurujine);
        tv3 = findViewById(R.id.shuruduixiang);
        tv4 = findViewById(R.id.shurufangshi);
        tv5 = findViewById(R.id.shurushijian);
        tv6 = findViewById(R.id.xuanzedidian);
        tv7 = findViewById(R.id.xuanzechengyuan);
        tv8 = findViewById(R.id.xuanzezhangben);
        tv9 = findViewById(R.id.shurubeizhu);
        bt1 = findViewById(R.id.tijiao_jiyibi);
        bt2 = findViewById(R.id.quxiao_jiyibi);

        SharedPreferences sp = getSharedPreferences("jiyibi", Activity.MODE_PRIVATE);
        yonghu = sp.getString("user_id","");
        Log.i("用户", yonghu);
        leibie = sp.getString("leibie","");
        image = sp.getString("image","");
        leixing = sp.getString("leixing_name","");

        iv.setImageResource(Integer.valueOf(image));
        tv1.setText(leixing);
        getFangshi();getChengyuan();getZhangben();
        //方式
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(jiyibi.this)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(fangshi, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv4.setText(fangshi[which]);
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //成员
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(jiyibi.this)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(chengyuan, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv7.setText(chengyuan[which]);
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //账本
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(jiyibi.this)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(zhangben, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv8.setText(zhangben[which]);
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //时间
        initTimePicker();
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.show(tv5.getText().toString());
            }
        });

        }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tijiao_jiyibi)
        {
            if(tv2.getText().toString().length()!=0&&!tv4.getText().toString().equals("请选择交易方式")&&!tv5.getText().toString().equals("请选择交易时间")&&!tv7.getText().toString().equals("请选择交易成员")&&!tv8.getText().toString().equals("请选择账本"))
            {
                tijiaojieguo();
            }
            else
            {
                Toast.makeText(jiyibi.this,"请正确填写信息",Toast.LENGTH_LONG).show();
            }
        }
        if(v.getId()==R.id.quxiao_jiyibi)
        {
            finish();
        }
    }

    public void getFangshi()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
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
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("select * from zidingyi_fangshi where user_id = '"+yonghu+"'");
                    rs1.last();
                    if(rs1.getRow()==0)
                    {
                        fangshi_n = 7;
                        fangshi = new String[fangshi_n];
                        fangshi[0] = "银行卡";
                        fangshi[1] = "金融产品";
                        fangshi[2] = "现金";
                        fangshi[3] = "支付宝";
                        fangshi[4] = "微信";
                        fangshi[5] = "信用卡";
                        fangshi[6] = "QQ";
                        conn.close();
                        flag=1;
                    }
                    else
                    {
                        fangshi_n = rs1.getRow() + 7;
                        Log.i("成功", "连接到数据库");
                        fangshi = new String[fangshi_n];
                        fangshi[0] = "银行卡";
                        fangshi[1] = "金融产品";
                        fangshi[2] = "现金";
                        fangshi[3] = "支付宝";
                        fangshi[4] = "微信";
                        fangshi[5] = "信用卡";
                        fangshi[6] = "QQ";
                        rs1.beforeFirst();
                        int i = 7;
                        while(rs1.next())
                        {
                            fangshi[i++] = rs1.getString("fangshi_id");
                        }
                        conn.close();
                        flag=1;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                }
                handler.sendMessage(msg);
            }
        }).start();

    }

    public void getChengyuan()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
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
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("select * from zidingyi_chengyuan where user_id = '"+yonghu+"'");
                    rs1.last();
                    if(rs1.getRow()==0)
                    {
                        chengyuan_n = 10;
                        chengyuan = new String[chengyuan_n];
                        chengyuan[0] = "老婆";
                        chengyuan[1] = "老公";
                        chengyuan[2] = "爸爸";
                        chengyuan[3] = "妈妈";
                        chengyuan[4] = "公公/老丈人";
                        chengyuan[5] = "婆婆/丈母娘";
                        chengyuan[6] = "儿子";
                        chengyuan[7] = "女儿";
                        chengyuan[8] = "本人";
                        chengyuan[9] = "全体成员";
                        conn.close();
                        flag=1;
                    }
                    else
                    {
                        chengyuan_n = rs1.getRow() + 10;
                        Log.i("成功", "连接到数据库");
                        chengyuan = new String[chengyuan_n];
                        chengyuan[0] = "老婆";
                        chengyuan[1] = "老公";
                        chengyuan[2] = "爸爸";
                        chengyuan[3] = "妈妈";
                        chengyuan[4] = "公公/老丈人";
                        chengyuan[5] = "婆婆/丈母娘";
                        chengyuan[6] = "儿子";
                        chengyuan[7] = "女儿";
                        chengyuan[8] = "本人";
                        chengyuan[9] = "全体成员";
                        rs1.beforeFirst();
                        int i = 10;
                        while(rs1.next())
                        {
                            chengyuan[i++] = rs1.getString("chengyuan_id");
                        }
                        conn.close();
                        flag=1;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void getZhangben()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
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
                    ResultSet rs = statement.executeQuery("select * from zidingyi_zhangben where user_id = '"+yonghu+"'");
                    rs.last();
                    if(rs.getRow()==0)
                    {
                        zhangben_n = 2;
                        zhangben = new String[zhangben_n];
                        zhangben[0] = "个人账本";
                        zhangben[1] = "默认账本";
                        conn.close();
                        flag=1;
                    }
                    else
                    {
                        zhangben_n = rs.getRow() + 2;
                        Log.i("成功", "连接到数据库");
                        zhangben = new String[zhangben_n];
                        zhangben[0] = "个人账本";
                        zhangben[1] = "默认账本";
                        rs.beforeFirst();
                        int i = 2;
                        while(rs.next())
                        {
                            zhangben[i++] = rs.getString("name");
                        }
                        conn.close();
                        flag=1;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void initTimePicker() {
        String beginTime = "2018-10-17 18:00";
        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        tv5.setText(endTime);
        time=endTime;

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimePicker = new CustomDatePicker(jiyibi.this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv5.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimePicker.setCancelable(true);
        // 显示时和分
        mTimePicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimePicker.setScrollLoop(true);
        // 允许滚动动画
        mTimePicker.setCanShowAnim(true);
    }

    public void tijiaojieguo()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(jiyibi.this,"记账成功",Toast.LENGTH_LONG).show();
                    finish();
                }
                if(msg.what == msgWhatw){
                    Toast.makeText(jiyibi.this,"记账失败",Toast.LENGTH_LONG).show();
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
                    String[] str = tv5.getText().toString().split("-");
                    statement.executeUpdate("insert into jizhang values ('"+time+"','"+yonghu+"','"+tv2.getText().toString()+"','"+tv4.getText().toString()+"','"+Integer.toHexString(Integer.valueOf(image))+"','"+leibie+"','"+str[0]+"','"+str[1]+"','"+str[2]+"','"+tv8.getText().toString()+"','"+tv7.getText().toString()+"','"+tv9.getText().toString()+"','"+tv6.getText().toString()+"','"+tv3.getText().toString()+"','"+leixing+"')");
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
