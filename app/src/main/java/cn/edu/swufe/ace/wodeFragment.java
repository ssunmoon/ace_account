package cn.edu.swufe.ace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class wodeFragment extends android.support.v4.app.Fragment{

    View view;
    ImageView iv;
    TextView tv_nicheng,tv_yonghu,tv_sex,tv_jizhangbishu,tv_bjnicheng,tv_mima,tv_leibie,tv_zhangben,tv_shezhiyusuan,tv_chakanyusuan,tv_sheshifangshi,tv_yijianfankui;
    Switch sw;
    Button bt;
    String yonghu,nicheng,zongbishu,sex;
    private int msgWhatr = 6;
    private int msgWhatw = 4;//连接数据库失败
    EditText inputServer;

    int zhangben_caozuo=1,zhangben_n;
    String []zhangbenstr;
    String zhangben;
    String xiangqing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wode, null);

        SharedPreferences sharedPreferences0 = getActivity().getSharedPreferences("default_user", Activity.MODE_PRIVATE);
        yonghu = sharedPreferences0.getString("yonghu","");

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("xiangqing", Activity.MODE_PRIVATE);
        xiangqing = sharedPreferences1.getString("xiangqing","0");

        iv=view.findViewById(R.id.imageView3);
        tv_nicheng=view.findViewById(R.id.textView25);
        tv_yonghu=view.findViewById(R.id.textView26);
        tv_sex=view.findViewById(R.id.textView27);
        tv_jizhangbishu=view.findViewById(R.id.textView29);
        tv_bjnicheng=view.findViewById(R.id.textView32);
        tv_mima=view.findViewById(R.id.textView35);
        tv_leibie=view.findViewById(R.id.textView37);
        tv_zhangben=view.findViewById(R.id.textView40);
        tv_shezhiyusuan=view.findViewById(R.id.textView36);
        tv_chakanyusuan=view.findViewById(R.id.textView33);
        tv_sheshifangshi=view.findViewById(R.id.textView39);
        tv_yijianfankui=view.findViewById(R.id.textView34);
        sw=view.findViewById(R.id.switch1);
        bt=view.findViewById(R.id.tuichudenglu);
        inputServer = new EditText(getActivity());

        if(xiangqing.equals("0"))
        {
            sw.setChecked(false);
        }
        else
        {
            sw.setChecked(true);
        }

        getBasicInfo();
        getZhangben();

        //昵称
        tv_bjnicheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputServer = new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入昵称")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(inputServer)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nicheng = inputServer.getText().toString();
                                tv_nicheng.setText(nicheng);
                                update_nicheng();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        //密码
        tv_mima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ZhaohuiActivity.class));
            }
        });

        //类型

        //账本
        tv_zhangben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zhangben="";
                inputServer = new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setTitle("请选择操作类型")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[]{"删除","添加"}, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        zhangben_caozuo=which;
                                        if(which==0&&zhangben_n!=0)
                                        {
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("请选择")
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setSingleChoiceItems(zhangbenstr, 0,
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    zhangben=zhangbenstr[which];
                                                                    if(zhangben.length()!=0)
                                                                    {
                                                                        update_zhangben();
                                                                    }
                                                                    Log.i("账本名称", zhangben);
                                                                    dialog.dismiss();
                                                                }
                                                            }
                                                    )
                                                    .setNegativeButton("取消", null)
                                                    .show();
                                        }
                                        if(which==1)
                                        {
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("请输入账本名称")
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setView(inputServer)
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            zhangben = inputServer.getText().toString();
                                                            if(zhangben.length()!=0)
                                                            {
                                                                update_zhangben();
                                                            }
                                                            Log.i("账本名称", zhangben);
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //明细
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    SharedPreferences sp1 = getActivity().getSharedPreferences("xiangqing",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = sp1.edit();
                    edit1.putString("xiangqing", "1");
                    edit1.commit();
                }
                else
                {
                    SharedPreferences sp1 = getActivity().getSharedPreferences("xiangqing",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = sp1.edit();
                    edit1.putString("xiangqing", "0");
                    edit1.commit();
                }
            }
        });

        //设置预算


        return view;
    }

    public void getBasicInfo()
    {
        nicheng="";
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    tv_yonghu.setText(yonghu.substring(0,3)+"****"+yonghu.substring(yonghu.length()-4));
                    tv_sex.setText(sex);
                    tv_jizhangbishu.setText(zongbishu);
                    if(nicheng.length()!=0)
                    {
                        tv_nicheng.setText(nicheng);
                    }
                    if(sex.equals("男"))
                    {
                        iv.setImageResource(R.drawable.man);
                    }
                }
                if(msg.what == msgWhatw){//连接数据库失败
                    Toast.makeText(getActivity(),"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
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
                    ResultSet rs1 = st1.executeQuery("SELECT * FROM user_info where user_id='"+yonghu+"'");
                    if(!rs1.next())
                    {
                        n=msgWhatw;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        while(rs1.next())
                        {
                            nicheng=rs1.getString("user_name");
                            sex=rs1.getString("user_sex");
                        }
                        n = msgWhatr;
                    }
                    Statement st2 = conn.createStatement();
                    ResultSet rs2 = st2.executeQuery("SELECT count(*) FROM jizhang where user_id='"+yonghu +"'");
                    if(rs2.next())
                    {
                        zongbishu=rs2.getString(1);
                        n = msgWhatr;
                    }
                    else
                    {
                        n = msgWhatw;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    n = msgWhatw;
                }
                msg.what=n;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void update_nicheng()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_LONG).show();
                }
                if(msg.what == msgWhatw){//连接数据库失败
                    Toast.makeText(getActivity(),"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
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
                    st1.executeUpdate("update user_info set user_name='"+nicheng+"' where user_id='"+yonghu+"'");
                    n = msgWhatr;
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    n = msgWhatw;
                }
                msg.what=n;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void getZhangben()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Log.i("账本个数", ""+zhangben_n);
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
                        zhangben_n = 0;
                    }
                    else
                    {
                        zhangben_n = rs.getRow();
                        Log.i("成功", "连接到数据库");
                        zhangbenstr = new String[zhangben_n];
                        rs.beforeFirst();
                        int i = 0;
                        while(rs.next())
                        {
                            zhangbenstr[i++] = rs.getString("name");
                        }
                        conn.close();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void update_zhangben()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_LONG).show();
                    getZhangben();
                }
                if(msg.what == msgWhatw){//连接数据库失败
                    Toast.makeText(getActivity(),"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
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
                    if(zhangben_caozuo==0)
                    {
                        Statement st1 = conn.createStatement();
                        st1.executeUpdate("delete from zidingyi_zhangben where user_id='"+yonghu+"' and name='"+zhangben+"'");
                        Log.i("成功", "删除");

                    }
                    else
                    {
                        Statement st1 = conn.createStatement();
                        st1.executeUpdate("insert into zidingyi_zhangben values ('"+yonghu+"','"+zhangben +"')");
                        Log.i("成功", "添加");
                    }
                    n = msgWhatr;
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    n = msgWhatw;
                }
                msg.what=n;
                handler.sendMessage(msg);
            }
        }).start();
    }

//    public void getChengyuan()
//    {
//        final Handler handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg){
//                super.handleMessage(msg);
//            }
//        };
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Message msg = handler.obtainMessage();
//                try {
//                    Class.forName("com.mysql.jdbc.Driver");
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    String url="jdbc:mysql://rm-bp132n0r530p95d0pfo.mysql.rds.aliyuncs.com:3306/acedata?user=root&password=Sy961016&useUnicode=true&characterEncoding=UTF-8";
//                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
//                    Log.i("成功", "连接到数据库");
//                    Statement st1 = conn.createStatement();
//                    ResultSet rs1 = st1.executeQuery("select * from zidingyi_chengyuan where user_id = '"+yonghu+"'");
//                    rs1.last();
//                    if(rs1.getRow()==0)
//                    {
//                        chengyuan_n = 10;
//                        chengyuan = new String[chengyuan_n];
//                        chengyuan[0] = "老婆";
//                        chengyuan[1] = "老公";
//                        chengyuan[2] = "爸爸";
//                        chengyuan[3] = "妈妈";
//                        chengyuan[4] = "公公/老丈人";
//                        chengyuan[5] = "婆婆/丈母娘";
//                        chengyuan[6] = "儿子";
//                        chengyuan[7] = "女儿";
//                        chengyuan[8] = "本人";
//                        chengyuan[9] = "全体成员";
//                        conn.close();
//                        flag=1;
//                    }
//                    else
//                    {
//                        chengyuan_n = rs1.getRow() + 10;
//                        Log.i("成功", "连接到数据库");
//                        chengyuan = new String[chengyuan_n];
//                        chengyuan[0] = "老婆";
//                        chengyuan[1] = "老公";
//                        chengyuan[2] = "爸爸";
//                        chengyuan[3] = "妈妈";
//                        chengyuan[4] = "公公/老丈人";
//                        chengyuan[5] = "婆婆/丈母娘";
//                        chengyuan[6] = "儿子";
//                        chengyuan[7] = "女儿";
//                        chengyuan[8] = "本人";
//                        chengyuan[9] = "全体成员";
//                        rs1.beforeFirst();
//                        int i = 10;
//                        while(rs1.next())
//                        {
//                            chengyuan[i++] = rs1.getString("chengyuan_id");
//                        }
//                        conn.close();
//                        flag=1;
//                    }
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    Log.i("失败", "连接不到数据库");
//                }
//                handler.sendMessage(msg);
//            }
//        }).start();
//    }

}
