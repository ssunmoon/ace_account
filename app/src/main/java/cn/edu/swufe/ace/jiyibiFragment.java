package cn.edu.swufe.ace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
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

public class jiyibiFragment extends android.support.v4.app.Fragment{

    Button shouru,zhichu,bt_shuaxin;
    String jz_leibie="收入",yonghu;
    GridView gridview;
    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> data_zhengshi = new ArrayList<Map<String, Object>>();
    int msgWhatr=36;
    int msgWhatw1=341;
    String[] from = { "image","name"};
    int[] to = { R.id.item_iv_jiyibi,R.id.item_tv_jiyibi};
    View view;
    int flag=0;
    private int msgWhatw = 4;//连接数据库失败
    String password,suijima;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jiyibi, null);

        SharedPreferences sp0 = getActivity().getSharedPreferences("default_user", Activity.MODE_PRIVATE);
        yonghu = sp0.getString("yonghu","");

        SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences(yonghu, Activity.MODE_PRIVATE);
        password = sharedPreferences3.getString("password","");
        suijima = sharedPreferences3.getString("suijima","");

        shouru = view.findViewById(R.id.button2);
        zhichu = view.findViewById(R.id.button5);
        bt_shuaxin = view.findViewById(R.id.button6);

        getLeixingInfo();

        shouru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity)
                {
                    Log.i("成功", "获取到主页面");
                    jz_leibie="收入";
                    shouru.setBackground(getResources().getDrawable(R.drawable.bt_xuanzhong));
                    shouru.setTextColor(getActivity().getResources().getColor(R.color.mine));
                    zhichu.setBackground(getResources().getDrawable(R.drawable.bt_nianyueri));
                    zhichu.setTextColor(getActivity().getResources().getColor(R.color.hei));
                    getLeixingInfo();
                }

            }
        });

        zhichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity)
                {
                    Log.i("成功", "获取到主页面");
                    jz_leibie="支出";
                    zhichu.setBackground(getResources().getDrawable(R.drawable.bt_xuanzhong));
                    zhichu.setTextColor(getActivity().getResources().getColor(R.color.mine));
                    shouru.setBackground(getResources().getDrawable(R.drawable.bt_nianyueri));
                    shouru.setTextColor(getActivity().getResources().getColor(R.color.hei));
                    getLeixingInfo();
                }

            }
        });

        bt_shuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity)
                {
                    jiancha();
                }

            }
        });

        return view;
    }

    public void getLeixingInfo()
    {
        data.clear();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    data_zhengshi.clear();
                    data_zhengshi.addAll(data);
                    getGrid();
                }
                if(msg.what == msgWhatw1){//连接数据库失败
                    Toast.makeText(getActivity(),"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
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
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("select * from jizhangleixing where jizhangleixing_name is not NULL and jizhangleixing_type = '"+jz_leibie+"'");
                    if(!rs1.next())
                    {
                        msg.what=msgWhatw1;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        while(rs1.next())
                        {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("image", Integer.valueOf(rs1.getString("jizhangleixing_id"),16));
                            Log.i("获取到的图片", ""+Integer.valueOf(rs1.getString("jizhangleixing_id"),16));
                            m.put("name", rs1.getString("jizhangleixing_name"));
                            Log.i("获取到的名字", ""+String.valueOf(rs1.getString("jizhangleixing_name")));
                            data.add(m);
                        }

                        Statement st2 = conn.createStatement();
                        ResultSet rs2 = st2.executeQuery("select * from zidingyi_jizhangleixing where user_id = '"+yonghu+"' and jizhangleixing_type = '"+jz_leibie+"'");
                        while(rs2.next())
                        {
                            Map<String, Object> m = new HashMap<String,Object>();
                            m.put("image", Integer.valueOf(rs2.getString("jizhangleixing_id"),16));
                            m.put("name", rs2.getString("jizhangleixing_name"));
                            data.add(m);
                        }
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

    public void getGrid()
    {
        //加载网格
        gridview = view.findViewById(R.id.jizhangleixing);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data_zhengshi, R.layout.grid_jiyibi, from, to);
        // 把Adapter放到ListView中显示
        gridview.setAdapter(adapter);
        //监听
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), (String) data_zhengshi.get(position).get("name"),Toast.LENGTH_LONG).show();
                SharedPreferences sp = getActivity().getSharedPreferences("jiyibi",Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("user_id", yonghu);
                edit.putString("leibie", jz_leibie);
                edit.putString("image", String.valueOf(data_zhengshi.get(position).get("image")));
                edit.putString("leixing_name", String.valueOf(data_zhengshi.get(position).get("name")));
                edit.commit();
                startActivity(new Intent(getActivity(),jiyibi.class));
            }
        });
    }

    public void jiancha()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    getLeixingInfo();
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
