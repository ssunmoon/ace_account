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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class wodeFragment extends android.support.v4.app.Fragment{

    View view;
    ImageView iv;
    TextView tv_nicheng,tv_yonghu,tv_sex,tv_jizhangbishu,tv_bjnicheng,tv_mima,tv_leixing,tv_zhangben,tv_chengyuan,tv_shezhiyusuan,tv_chakanyusuan,tv_jiaoyifangshi,tv_yijianfankui;
    Switch sw;
    Button bt;
    String yonghu="",nicheng="",zongbishu="",sex="";
    private int msgWhatr = 6;
    private int msgWhatw = 4;//连接数据库失败
    EditText inputServer,inputServer2;

    int zhangben_caozuo=1,zhangben_n,jiaoyifangshi_caozuo=1,jiaoyifangshi_n,chengyuan_caozuo=1,chengyuan_n,allchengyuan_n;
    String []zhangbenstr;
    String zhangben="";
    String xiangqing="";
    String yijian="";
    String []jiaoyifangshistr;
    String jiaoyifangshi="";
    String []chengyuanstr;
    String chengyuan="";
    String []all_chengyuanstr;
    String allchengyuan="";
    String shouru_m,zhichu_m="";

    int sw_flag=0;
    String month="",year="";
    String password,suijima;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_wode, null);

        SharedPreferences sharedPreferences0 = getActivity().getSharedPreferences("default_user", Activity.MODE_PRIVATE);
        yonghu = sharedPreferences0.getString("yonghu","");

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("xiangqing", Activity.MODE_PRIVATE);
        xiangqing = sharedPreferences1.getString("xiangqing","0");

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("riqi", Activity.MODE_PRIVATE);
        year = sharedPreferences2.getString("year","2019");
        month = sharedPreferences2.getString("month","05");

        SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences(yonghu, Activity.MODE_PRIVATE);
        password = sharedPreferences3.getString("password","");
        suijima = sharedPreferences3.getString("suijima","");

        iv=view.findViewById(R.id.imageView3);
        tv_nicheng=view.findViewById(R.id.textView25);
        tv_yonghu=view.findViewById(R.id.textView26);
        tv_sex=view.findViewById(R.id.textView27);
        tv_jizhangbishu=view.findViewById(R.id.textView29);
        tv_bjnicheng=view.findViewById(R.id.textView32);
        tv_mima=view.findViewById(R.id.textView35);
        tv_leixing=view.findViewById(R.id.textView37);
        tv_zhangben=view.findViewById(R.id.textView40);
        tv_chengyuan=view.findViewById(R.id.textView50);
        tv_shezhiyusuan=view.findViewById(R.id.textView36);
        tv_chakanyusuan=view.findViewById(R.id.textView33);
        tv_jiaoyifangshi=view.findViewById(R.id.textView39);
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
        getJiaoyifangshi();
        getChengyuan();
        getAllChengyuan();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiancha();
            }
        });

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
        tv_leixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("请选择操作类型")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[]{"删除","添加"}, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        zhangben_caozuo=which;
                                        if(which==0)
                                        {
                                            startActivity(new Intent(getActivity(),shanchuleixing.class));
                                        }
                                        if(which==1)
                                        {
                                            startActivity(new Intent(getActivity(),tianjialeixing.class));
                                        }
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

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

        //成员
        tv_chengyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chengyuan="";
                inputServer = new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setTitle("请选择操作类型")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[]{"删除","添加"}, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        chengyuan_caozuo=which;
                                        if(which==0&&chengyuan_n!=0)
                                        {
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("请选择")
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setSingleChoiceItems(chengyuanstr, 0,
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    chengyuan=chengyuanstr[which];
                                                                    if(chengyuan.length()!=0)
                                                                    {
                                                                        update_chengyuan();
                                                                    }
                                                                    Log.i("成员名称", chengyuan);
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
                                                    .setTitle("请输入成员名称")
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setView(inputServer)
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            chengyuan = inputServer.getText().toString();
                                                            if(chengyuan.length()!=0)
                                                            {
                                                                update_chengyuan();
                                                            }
                                                            Log.i("账本名称", chengyuan);
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
                    if(sw_flag==0)
                    {
                        yanzhengmima1();
                    }
                }
                else
                {
                    if(sw_flag==0)
                    {
                        yanzhengmima2();
                    }
                }
                sw_flag=0;
            }
        });

        //设置预算
        tv_shezhiyusuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputServer = new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setTitle("请选择成员")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(all_chengyuanstr, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        allchengyuan=all_chengyuanstr[which];
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("请输入\""+allchengyuan+"\"的收入金额")
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setView(inputServer)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        shouru_m = inputServer.getText().toString();
                                                        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//                                                        return pattern.matcher(str).matches();
                                                        if(pattern.matcher(shouru_m).matches())
                                                        {
                                                            if(shouru_m.length()==0)
                                                            {
                                                                shouru_m="0";
                                                            }
                                                            inputServer2 = new EditText(getActivity());
                                                            new AlertDialog.Builder(getActivity())
                                                                    .setTitle("请输入\""+allchengyuan+"\"的支出金额")
                                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                                    .setView(inputServer2)
                                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            zhichu_m = inputServer2.getText().toString();
                                                                            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//                                                        return pattern.matcher(str).matches();
                                                                            if(pattern.matcher(zhichu_m).matches())
                                                                            {
                                                                                if(zhichu_m.length()==0)
                                                                                {
                                                                                    zhichu_m="0";
                                                                                }
                                                                                tijiaoyusuan();
                                                                            }
                                                                            else
                                                                            {
                                                                                Toast.makeText(getActivity(),"未输入有效数字（整数），已退出预算设置",Toast.LENGTH_LONG).show();
                                                                            }
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
                                                        else
                                                        {
                                                            Toast.makeText(getActivity(),"未输入有效数字（整数），已退出预算设置",Toast.LENGTH_LONG).show();
                                                        }
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

                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //查看预算
        tv_chakanyusuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),chakanyusuan.class));
            }
        });

        //交易方式
        tv_jiaoyifangshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiaoyifangshi="";
                inputServer = new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setTitle("请选择操作类型")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[]{"删除","添加"}, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        jiaoyifangshi_caozuo=which;
                                        if(which==0&&jiaoyifangshi_n!=0)
                                        {
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("请选择")
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setSingleChoiceItems(jiaoyifangshistr, 0,
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    jiaoyifangshi=jiaoyifangshistr[which];
                                                                    if(jiaoyifangshi.length()!=0)
                                                                    {
                                                                        update_jiaoyifangshi();
                                                                    }
                                                                    Log.i("方式名称", jiaoyifangshi);
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
                                                    .setTitle("请输入方式名称")
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setView(inputServer)
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            jiaoyifangshi = inputServer.getText().toString();
                                                            if(jiaoyifangshi.length()!=0)
                                                            {
                                                                update_jiaoyifangshi();
                                                            }
                                                            Log.i("方式名称", jiaoyifangshi);
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

        //意见反馈
        tv_yijianfankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputServer = new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入意见")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(inputServer)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                yijian = inputServer.getText().toString();
                                if(yijian.length()!=0)
                                {
                                    update_yijian();
                                }
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

        //退出登录
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity)
                {
                    Log.i("成功", "获取到主页面");
                    SharedPreferences sp0 = getActivity().getSharedPreferences("default_user",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit0 = sp0.edit();
                    edit0.putString("status", "0");
                    edit0.commit();
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    getActivity().finish();
                }

            }
        });

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

    public void yanzhengmima1()
    {
        inputServer = new EditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("请输入账户密码")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(inputServer)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences0 = getActivity().getSharedPreferences(yonghu, Activity.MODE_PRIVATE);
                        if(inputServer.getText().toString().equals(sharedPreferences0.getString("password","")))
                        {
                            SharedPreferences sp1 = getActivity().getSharedPreferences("xiangqing",Activity.MODE_PRIVATE);
                            SharedPreferences.Editor edit1 = sp1.edit();
                            edit1.putString("xiangqing", "1");
                            edit1.commit();
                            Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            sw_flag=1;
                            sw.setChecked(false);
                            Toast.makeText(getActivity(),"输入的密码错误，无权限更改",Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sw_flag=1;
                        sw.setChecked(false);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void yanzhengmima2()
    {
        inputServer = new EditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("请输入账户密码")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(inputServer)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences0 = getActivity().getSharedPreferences(yonghu, Activity.MODE_PRIVATE);
                        if(inputServer.getText().toString().equals(sharedPreferences0.getString("password","")))
                        {
                            SharedPreferences sp1 = getActivity().getSharedPreferences("xiangqing",Activity.MODE_PRIVATE);
                            SharedPreferences.Editor edit1 = sp1.edit();
                            edit1.putString("xiangqing", "0");
                            edit1.commit();
                            Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            sw_flag=1;
                            sw.setChecked(true);
                            Toast.makeText(getActivity(),"输入的密码错误，无权限更改",Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sw_flag=1;
                        sw.setChecked(true);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void update_yijian()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"您的意见提交成功",Toast.LENGTH_LONG).show();
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
                    st1.executeUpdate("insert into suggestions values ('"+yonghu+"','"+new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())+"','"+yijian+"')");
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

    public void getJiaoyifangshi()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Log.i("账本个数", ""+jiaoyifangshi_n);
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
                    ResultSet rs = statement.executeQuery("select * from zidingyi_fangshi where user_id = '"+yonghu+"'");
                    rs.last();
                    if(rs.getRow()==0)
                    {
                        jiaoyifangshi_n = 0;
                    }
                    else
                    {
                        jiaoyifangshi_n = rs.getRow();
                        Log.i("成功", "连接到数据库");
                        jiaoyifangshistr = new String[jiaoyifangshi_n];
                        rs.beforeFirst();
                        int i = 0;
                        while(rs.next())
                        {
                            jiaoyifangshistr[i++] = rs.getString("fangshi_id");
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

    public void update_jiaoyifangshi()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_LONG).show();
                    getJiaoyifangshi();
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
                    if(jiaoyifangshi_caozuo==0)
                    {
                        Statement st1 = conn.createStatement();
                        st1.executeUpdate("delete from zidingyi_fangshi where user_id='"+yonghu+"' and fangshi_id='"+jiaoyifangshi+"'");
                        Log.i("成功", "删除");
                    }
                    else
                    {
                        Statement st1 = conn.createStatement();
                        st1.executeUpdate("insert into zidingyi_fangshi values ('"+yonghu+"','"+jiaoyifangshi+"')");
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

    public void getChengyuan()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Log.i("账本个数", ""+chengyuan_n);
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
                    ResultSet rs = statement.executeQuery("select * from zidingyi_chengyuan where user_id = '"+yonghu+"'");
                    rs.last();
                    if(rs.getRow()==0)
                    {
                        chengyuan_n = 0;
                    }
                    else
                    {
                        chengyuan_n = rs.getRow();
                        Log.i("成功", "连接到数据库");
                        chengyuanstr = new String[chengyuan_n];
                        rs.beforeFirst();
                        int i = 0;
                        while(rs.next())
                        {
                            chengyuanstr[i++] = rs.getString("chengyuan_id");
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

    public void update_chengyuan()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_LONG).show();
                    getChengyuan();
                    getAllChengyuan();
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
                    if(chengyuan_caozuo==0)
                    {
                        Statement st1 = conn.createStatement();
                        st1.executeUpdate("delete from zidingyi_chengyuan where user_id='"+yonghu+"' and chengyuan_id='"+chengyuan+"'");
                        Log.i("成功", "删除");
                    }
                    else
                    {
                        Statement st1 = conn.createStatement();
                        st1.executeUpdate("insert into zidingyi_chengyuan values ('"+yonghu+"','"+chengyuan+"')");
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

    public void getAllChengyuan()
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
                        allchengyuan_n = 10;
                        all_chengyuanstr = new String[allchengyuan_n];
                        all_chengyuanstr[0] = "老婆";
                        all_chengyuanstr[1] = "老公";
                        all_chengyuanstr[2] = "爸爸";
                        all_chengyuanstr[3] = "妈妈";
                        all_chengyuanstr[4] = "公公/老丈人";
                        all_chengyuanstr[5] = "婆婆/丈母娘";
                        all_chengyuanstr[6] = "儿子";
                        all_chengyuanstr[7] = "女儿";
                        all_chengyuanstr[8] = "本人";
                        all_chengyuanstr[9] = "全体成员";
                        conn.close();
                    }
                    else
                    {
                        allchengyuan_n = rs1.getRow() + 10;
                        Log.i("成功", "连接到数据库");
                        all_chengyuanstr = new String[allchengyuan_n];
                        all_chengyuanstr[0] = "老婆";
                        all_chengyuanstr[1] = "老公";
                        all_chengyuanstr[2] = "爸爸";
                        all_chengyuanstr[3] = "妈妈";
                        all_chengyuanstr[4] = "公公/老丈人";
                        all_chengyuanstr[5] = "婆婆/丈母娘";
                        all_chengyuanstr[6] = "儿子";
                        all_chengyuanstr[7] = "女儿";
                        all_chengyuanstr[8] = "本人";
                        all_chengyuanstr[9] = "全体成员";
                        rs1.beforeFirst();
                        int i = 10;
                        while(rs1.next())
                        {
                            all_chengyuanstr[i++] = rs1.getString("chengyuan_id");
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

    public void tijiaoyusuan()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"为\""+allchengyuan+"\"设置预算成功",Toast.LENGTH_LONG).show();
                }
                if(msg.what == msgWhatw){//连接数据库失败
                    Toast.makeText(getActivity(),"\""+allchengyuan+"\"在本月已有预算",Toast.LENGTH_LONG).show();
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
                    st1.executeUpdate("insert into yusuan"+year+"_"+month+" values ('"+yonghu+"','"+allchengyuan+"','"+shouru_m+"','"+zhichu_m+"')");
                    Log.i("成功", "添加");
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

    public void jiancha()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    getBasicInfo();
                    getZhangben();
                    getJiaoyifangshi();
                    getChengyuan();
                    getAllChengyuan();
                }
                if(msg.what == msgWhatw){//连接数据库失败
                    Toast.makeText(getActivity(),"您的账户已在异地登陆/密码已被修改",Toast.LENGTH_LONG).show();
                    SharedPreferences sp0 = getActivity().getSharedPreferences("default_user",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit0 = sp0.edit();
                    edit0.putString("status", "0");
                    edit0.commit();
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    getActivity().finish();
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
                        if(rs1.getString("user_password").equals(password)&&rs1.getString("user_suijima").equals(suijima))
                        {
                            n = msgWhatr;
                        }
                        else
                        {
                            n=msgWhatw;
                        }
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
}
