package cn.edu.swufe.ace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mingxiFragment extends android.support.v4.app.Fragment{

    View view;
    String yonghu;//当前用户
    private int msgWhatr = 16;
    private int msgWhatw1 = 141;//连接数据库失败
    private int msgWhatw2 = 142;//无月记录
    private int msgWhatw3 = 143;//无日记录
    private CustomDatePicker mDatePicker;
    Button riqixuanze_month;
    TextView riqixuanze_year,mingxiriqi,tv19,tv18,tv24,tv21,tv22,tv_shuaxin;
    int flag=0;
    String[] from = { "image","name","id","jine"};
    int[] to = { R.id.imageView4,R.id.textView41,R.id.textView43,R.id.textView42};
    String year,month,day;
    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> data_zhenshi = new ArrayList<Map<String, Object>>();
    ListView listview;
    double rishouru=0,rizhichu=0,yueshouru=0,yuezhichu=0,yueyue=0;
    String password,suijima;
    private int msgWhatw = 4;//连接数据库失败

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mingxi, null);

        SharedPreferences sharedPreferences0 = getActivity().getSharedPreferences("default_user", Activity.MODE_PRIVATE);
        yonghu = sharedPreferences0.getString("yonghu","");

        SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences(yonghu, Activity.MODE_PRIVATE);
        password = sharedPreferences3.getString("password","");
        suijima = sharedPreferences3.getString("suijima","");

        tv18=view.findViewById(R.id.textView18);//月支出
        tv19=view.findViewById(R.id.textView19);//月收入
        tv21=view.findViewById(R.id.textView21);//日收入
        tv22=view.findViewById(R.id.textView22);//日余额
        tv24=view.findViewById(R.id.textView24);//yue余额
        tv_shuaxin=view.findViewById(R.id.textView13);

        //日期按钮
        riqixuanze_month = view.findViewById(R.id.riqixuanze);
        riqixuanze_year = view.findViewById(R.id.textView14);
        mingxiriqi = view.findViewById(R.id.textView20);
        initDatePicker();
        riqixuanze_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity)
                {
                    Log.i("成功", "获取到主页面");
                    String str = riqixuanze_year.getText().toString()+"-"+riqixuanze_month.getText().toString();
                    mDatePicker.show(str);
                }
            }
        });

        tv_shuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiancha();
            }
        });

        //list显示
        getData();

        return view;
    }

    public void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();
        String str0;
        str0 = DateFormatUtils.long2Str(endTimestamp, false);
        String []riqistr0 = str0.split("-");
        year = riqistr0[0];
        month = riqistr0[1];
        day = riqistr0[2];

        SharedPreferences sp = getActivity().getSharedPreferences("riqi",Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("year", year);
        edit.putString("month", month);
        edit.commit();

        riqixuanze_year.setText(riqistr0[0]);
        riqixuanze_month.setText(riqistr0[1]+"-"+riqistr0[2]);
        try {
            mingxiriqi.setText(riqistr0[1]+"月"+riqistr0[2]+"日  "+getWeek(str0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String str1;
                str1 = DateFormatUtils.long2Str(timestamp, false);
                String []riqistr1 = str1.split("-");
                year = riqistr1[0];
                month = riqistr1[1];
                day = riqistr1[2];
                //重新加载
                getData();
                riqixuanze_year.setText(riqistr1[0]);
                riqixuanze_month.setText(riqistr1[1]+"-"+riqistr1[2]);
                try {
                    mingxiriqi.setText(riqistr1[1]+"月"+riqistr1[2]+"日  "+getWeek(str1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private String getWeek(String time) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(time));
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1)
        {
            dayForWeek = 7;
        }
        else
        {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        if(dayForWeek==1)
            return "星期一";
        if(dayForWeek==2)
            return "星期二";
        if(dayForWeek==3)
            return "星期三";
        if(dayForWeek==4)
            return "星期四";
        if(dayForWeek==5)
            return "星期五";
        if(dayForWeek==6)
            return "星期六";
        else
            return "星期日";
    }

    public void getData()
    {
        rishouru=0;
        rizhichu=0;
        yueshouru=0;
        yuezhichu=0;
        yueyue=0;
        data.clear();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    data_zhenshi.clear();
                    data_zhenshi.addAll(data);
                    tv19.setText(""+yueshouru);
                    tv18.setText(""+yuezhichu);
                    yueyue=yueshouru-yuezhichu;
                    yueyue = (double) Math.round(yueyue * 100) / 100;
                    tv24.setText(""+yueyue);
                    tv21.setText("日收入："+rishouru);
                    tv22.setText("日支出："+rizhichu);
                    getList();
                }
                if(msg.what == msgWhatw1){//连接数据库失败
                    Toast.makeText(getActivity(),"数据库连接失败，请检查网络",Toast.LENGTH_LONG).show();
                }
                if(msg.what == msgWhatw2){//无月记录
                    Toast.makeText(getActivity(),"无月记录",Toast.LENGTH_LONG).show();
                    data_zhenshi.clear();
                    data_zhenshi.addAll(data);
                    tv19.setText(""+yueshouru);
                    tv18.setText(""+yuezhichu);
                    yueyue=yueshouru-yuezhichu;
                    yueyue = (double) Math.round(yueyue * 100) / 100;
                    tv24.setText(""+yueyue);
                    tv21.setText("日收入："+rishouru);
                    tv22.setText("日支出："+rizhichu);
                    getList();
                }
                if(msg.what == msgWhatw3){//无日记录
                    Toast.makeText(getActivity(),"无日记录",Toast.LENGTH_LONG).show();
                    data_zhenshi.clear();
                    data_zhenshi.addAll(data);
                    tv19.setText(""+yueshouru);
                    tv18.setText(""+yuezhichu);
                    yueyue=yueshouru-yuezhichu;
                    yueyue = (double) Math.round(yueyue * 100) / 100;
                    tv24.setText(""+yueyue);
                    tv21.setText("日收入："+rishouru);
                    tv22.setText("日支出："+rizhichu);
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
                    ResultSet rs1 = st1.executeQuery("SELECT * FROM jizhang where user_id='"+yonghu+"' and jizhang_year ='"+year+"' and jizhang_month='"+month+"' and jizhang_day LIKE '"+day+"%'");
                    if(!rs1.next())
                    {
                        n=msgWhatw3;
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
                                rishouru = rishouru +  Double.parseDouble(rs1.getString("jizhang_jine"));
                            }
                            else
                            {
                                m.put("jine", "- "+rs1.getString("jizhang_jine"));
                                rizhichu = rizhichu +  Double.parseDouble(rs1.getString("jizhang_jine"));
                            }
                            data.add(m);
                        }
                        n = msgWhatr;
                    }
                    Statement st2 = conn.createStatement();
                    ResultSet rs2 = st2.executeQuery("SELECT * FROM jizhang where user_id='"+yonghu+"' and jizhang_year ='"+year+"' and jizhang_month='"+month+"'");
                    if(!rs2.next())
                    {
                        n=msgWhatw2;
                    }
                    else
                    {
                        rs2.beforeFirst();
                        while(rs2.next())
                        {
                            if(rs2.getString("jizhang_leibie").equals("收入"))
                            {
                                yueshouru = yueshouru +  Double.parseDouble(rs2.getString("jizhang_jine"));
                            }
                            else
                            {
                                yuezhichu = yuezhichu +  Double.parseDouble(rs2.getString("jizhang_jine"));
                            }
                        }
                        n = msgWhatr;
                    }
                    flag=1;
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    n = msgWhatw1;
                    flag=1;
                }
                msg.what=n;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void getList()
    {
        //加载列表
        listview = view.findViewById(R.id.mingxiliebiao);
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
                    SharedPreferences sp = getActivity().getSharedPreferences("mingxi",Activity.MODE_PRIVATE);
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
                    initDatePicker();
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
