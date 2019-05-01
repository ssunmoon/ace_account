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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class faxianFragment extends android.support.v4.app.Fragment{

    View view;
    Button bt1,bt2;
    TextView tv;
    int fenlei=1;
    String yonghu;//当前用户
    private int msgWhatr = 16;
    private int msgWhatw1 = 141;//连接数据库失败
    private int msgWhatw2 = 142;//无月记录
    String[] from = { "image","name","id","jine"};
    int[] to = { R.id.imageView4,R.id.textView41,R.id.textView43,R.id.textView42};
    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> data_zhenshi = new ArrayList<Map<String, Object>>();
    ListView listview;
    String password,suijima;
    private int msgWhatw = 4;//连接数据库失败
    String []str=new String[]{"账本","金额","类别","类型","方式","成员","对象","地点"};
    String []str2=new String[]{"zhangben_id","jizhang_jine","jizhang_leibie","leixing_name","fangshi_id","chengyuan_id","jizhang_who","jizhang_where"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faxian, null);

        SharedPreferences sharedPreferences0 = getActivity().getSharedPreferences("default_user", Activity.MODE_PRIVATE);
        yonghu = sharedPreferences0.getString("yonghu","");

        SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences(yonghu, Activity.MODE_PRIVATE);
        password = sharedPreferences3.getString("password","");
        suijima = sharedPreferences3.getString("suijima","");

        bt1 = view.findViewById(R.id.bt_fxchaxun);
        bt2 = view.findViewById(R.id.bt_fxjieguo);
        tv = view.findViewById(R.id.tv_chaxun);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity)
                {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("请选择查询方式")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setSingleChoiceItems(str, 0,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            bt1.setText("按"+str[which]+"查询");
                                            fenlei=which;
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .setNegativeButton("取消", null)
                            .show();
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv.getText().toString().length()==0)
                {
                    Toast.makeText(getActivity(),"请输入查询内容",Toast.LENGTH_LONG).show();
                    getAllData();
                }
                else
                {
                    if(bt1.getText().toString().equals("选择查询方式"))
                    {
                        Toast.makeText(getActivity(),"请选择查询方式",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        jiancha();
                    }
                }
            }
        });

        return view;
    }

    public void getAllData()
    {
        data.clear();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    data_zhenshi.clear();
                    data_zhenshi.addAll(data);
                    getList();
                }
                if(msg.what == msgWhatw1){//连接数据库失败
                    Toast.makeText(getActivity(),"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
                }
                if(msg.what == msgWhatw2){//无记录
                    Toast.makeText(getActivity(),"无记录",Toast.LENGTH_LONG).show();
                    data_zhenshi.clear();
                    data_zhenshi.addAll(data);
                    getList();
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
                    ResultSet rs1 = st1.executeQuery("SELECT * FROM jizhang where user_id='"+yonghu+"'");
                    if(!rs1.next())
                    {
                        n=msgWhatw2;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        while(rs1.next())
                        {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("image", Integer.valueOf(rs1.getString("jizhangleixing_id"),16));
                            m.put("name", rs1.getString("leixing_name"));
                            m.put("id", rs1.getString("jizhang_id"));
                            if(rs1.getString("jizhang_leibie").equals("收入"))
                            {
                                m.put("jine", "+ "+rs1.getString("jizhang_jine"));
                            }
                            else
                            {
                                m.put("jine", "- "+rs1.getString("jizhang_jine"));
                            }
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

    public void getData()
    {
        data.clear();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    data_zhenshi.clear();
                    data_zhenshi.addAll(data);
                    getList();
                }
                if(msg.what == msgWhatw1){//连接数据库失败
                    Toast.makeText(getActivity(),"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
                }
                if(msg.what == msgWhatw2){//无记录
                    Toast.makeText(getActivity(),"无记录",Toast.LENGTH_LONG).show();
                    data_zhenshi.clear();
                    data_zhenshi.addAll(data);
                    getList();
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
                    String str;
                    if(str2[fenlei].equals("jizhang_jine"))
                    {
                        str="='"+tv.getText().toString()+"'";
                    }
                    else
                    {
                        int i;
                        str=" like '%"+tv.getText().toString().substring(0,1)+"%'";
                        for(i=1;i<tv.getText().toString().length();i++)
                        {
                            str = str + " or "+str2[fenlei]+" like '%"+tv.getText().toString().substring(i,i+1)+"%'";
                        }
                    }
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT * FROM jizhang where user_id='"+yonghu+"' and "+str2[fenlei]+str);
                    if(!rs1.next())
                    {
                        n=msgWhatw2;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        while(rs1.next())
                        {
                            Map<String, Object> m = new HashMap<String, Object>();
                            m.put("image", Integer.valueOf(rs1.getString("jizhangleixing_id"),16));
                            m.put("name", rs1.getString("leixing_name"));
                            m.put("id", rs1.getString("jizhang_id"));
                            if(rs1.getString("jizhang_leibie").equals("收入"))
                            {
                                m.put("jine", "+ "+rs1.getString("jizhang_jine"));
                            }
                            else
                            {
                                m.put("jine", "- "+rs1.getString("jizhang_jine"));
                            }
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

    public void getList()
    {
        //加载列表
        listview = view.findViewById(R.id.faxianliebiao);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data_zhenshi, R.layout.list_mingxi, from, to);
        // 把Adapter放到ListView中显示
        listview.setAdapter(adapter);
        //监听
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), (String) data_zhenshi.get(position).get("name"),Toast.LENGTH_LONG).show();

                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("xiangqing", Activity.MODE_PRIVATE);

                if(sharedPreferences1.getString("xiangqing","0").equals("1"))
                {
                    SharedPreferences sp = getActivity().getSharedPreferences("mingxi", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("user_id", yonghu);
                    edit.putString("image", String.valueOf(data.get(position).get("image")));
                    edit.putString("leixing_name", String.valueOf(data.get(position).get("name")));
                    edit.putString("jizhang_id", String.valueOf(data.get(position).get("id")));
                    edit.putString("jine", String.valueOf(data.get(position).get("jine")));
                    edit.commit();
                    startActivity(new Intent(getActivity(),mingxi.class));
                }
                else
                {
                    Toast.makeText(getActivity(),"请到\"我的\"界面开启明细查看权限",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void jiancha()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    getData();
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
                        rs1.beforeFirst();
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
