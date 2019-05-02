package cn.edu.swufe.ace;

import android.app.Activity;
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
import android.widget.Button;
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

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class biaogeFragment extends android.support.v4.app.Fragment{

    private LineChartView lineChart;

    String[] date;//X轴的标注
    int[] score;//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private LineChartView lineChart_zhichu;
    String[] date_zhichu;//X轴的标注
    int[] score_zhichu;//图表的数据点
    private List<PointValue> mPointValues_zhichu = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues_zhichu = new ArrayList<AxisValue>();
    View view;
    String year,month;
    private int msgWhatr = 16;
    private int msgWhatw1 = 141;//连接数据库失败
    String yonghu;

    private PieChartView piechart1;
    private PieChartData pie_data1;         //存放数据
    int numValues1=0;
    float[] pie_numb1;
    String[] pie_name1;
    List<SliceValue> values1 = new ArrayList<SliceValue>();

    private PieChartView piechart2;
    private PieChartData pie_data2;         //存放数据
    int numValues2=0;
    float[] pie_numb2;
    String[] pie_name2;
    List<SliceValue> values2 = new ArrayList<SliceValue>();

    private PieChartView piechart3;
    private PieChartData pie_data3;         //存放数据
    int numValues3=0;
    float[] pie_numb3;
    String[] pie_name3;
    List<SliceValue> values3 = new ArrayList<SliceValue>();

    private PieChartView piechart4;
    private PieChartData pie_data4;         //存放数据
    int numValues4=0;
    float[] pie_numb4;
    String[] pie_name4;
    List<SliceValue> values4 = new ArrayList<SliceValue>();

    private PieChartView piechart5;
    private PieChartData pie_data5;         //存放数据
    int numValues5=0;
    float[] pie_numb5;
    String[] pie_name5;
    List<SliceValue> values5 = new ArrayList<SliceValue>();

    private PieChartView piechart6;
    private PieChartData pie_data6;         //存放数据
    int numValues6=0;
    float[] pie_numb6;
    String[] pie_name6;
    List<SliceValue> values6 = new ArrayList<SliceValue>();

    private PieChartView piechart7;
    private PieChartData pie_data7;         //存放数据
    int numValues7=0;
    float[] pie_numb7;
    String[] pie_name7;
    List<SliceValue> values7 = new ArrayList<SliceValue>();

    private PieChartView piechart8;
    private PieChartData pie_data8;         //存放数据
    int numValues8=0;
    float[] pie_numb8;
    String[] pie_name8;
    List<SliceValue> values8 = new ArrayList<SliceValue>();

    private boolean hasLabels1 = true;                   //是否有标签
    private boolean hasLabelsOutside1 = false;           //标签是否在扇形外面
    private boolean hasCenterCircle1 = true;            //是否有中心圆
    private boolean hasCenterText11 = true;             //是否有中心的文字
    private boolean hasCenterText21 = false;             //是否有中心的文字2
    private boolean isExploded1 = false;                  //是否是炸开的图像
    private boolean hasLabelForSelected1 = false;         //选中的扇形显示标签

    private ColumnChartView barchart;
    private ColumnChartData bardata;
    public final static String[] week = new String[]{"收入", "支出"};
    private List<Float> list = new ArrayList<>();

    int[] colorData = {Color.parseColor("#46c099"),
            Color.parseColor("#8180ff"),
            Color.parseColor("#50b2ef"),
            Color.parseColor("#aad8fb"),
            Color.parseColor("#f7d878"),
            Color.parseColor("#c8e9a0"),
            Color.parseColor("#f4a277")};//颜色可以默认，chart有个colorUtil工具类，随机颜色

    Button bt1,bt2,bt_shuaxin;
    int month_year=1;
    private int msgWhatw = 4;//连接数据库失败
    String password,suijima;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_biaoge, null);
        //得到年月
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("riqi", Activity.MODE_PRIVATE);
        year = sharedPreferences1.getString("year","2019");
        month = sharedPreferences1.getString("month","05");
        //得到用户
        SharedPreferences sharedPreferences0 = getActivity().getSharedPreferences("default_user", Activity.MODE_PRIVATE);
        yonghu = sharedPreferences0.getString("yonghu","");

        SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences(yonghu, Activity.MODE_PRIVATE);
        password = sharedPreferences3.getString("password","");
        suijima = sharedPreferences3.getString("suijima","");

        bt1 = view.findViewById(R.id.biaoge_yue);
        bt2 = view.findViewById(R.id.biaoge_nian);
        bt_shuaxin = view.findViewById(R.id.bt_biaoge);

        getInfo();

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity)
                {
                    Log.i("成功", "获取到主页面");
                    month_year=1;
                    bt1.setBackground(getResources().getDrawable(R.drawable.bt_xuanzhong));
                    bt1.setTextColor(getActivity().getResources().getColor(R.color.mine));
                    bt2.setBackground(getResources().getDrawable(R.drawable.bt_nianyueri));
                    bt2.setTextColor(getActivity().getResources().getColor(R.color.hei));
                    getInfo();
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity)
                {
                    Log.i("成功", "获取到主页面");
                    month_year=2;
                    bt2.setBackground(getResources().getDrawable(R.drawable.bt_xuanzhong));
                    bt2.setTextColor(getActivity().getResources().getColor(R.color.mine));
                    bt1.setBackground(getResources().getDrawable(R.drawable.bt_nianyueri));
                    bt1.setTextColor(getActivity().getResources().getColor(R.color.hei));
                    getInfo();
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

    public void getInfo()
    {
        //得到各类图表
        get_shouru();
        get_zhichu();

        piechart1 = (PieChartView) view.findViewById(R.id.piechart1);
        get_shouru_zhangben();

        piechart2 = (PieChartView) view.findViewById(R.id.piechart2);
        get_shouru_leixing();

        piechart3 = (PieChartView) view.findViewById(R.id.piechart3);
        get_shouru_fangshi();

        piechart4 = (PieChartView) view.findViewById(R.id.piechart4);
        get_shouru_chengyuan();

        piechart5 = (PieChartView) view.findViewById(R.id.piechart5);
        get_zhichu_zhangben();

        piechart6 = (PieChartView) view.findViewById(R.id.piechart6);
        get_zhichu_leixing();

        piechart7 = (PieChartView) view.findViewById(R.id.piechart7);
        get_zhichu_fangshi();

        piechart8 = (PieChartView) view.findViewById(R.id.piechart8);
        get_zhichu_chengyuan();

        barchart= (ColumnChartView) view.findViewById(R.id.barchart);
        barchart.setZoomEnabled(true);//允许手势缩放
        bar_initData();
    }

    //折线-收入
    public void get_shouru()
    {
        if(month_year==1)
        {
            int flag=0;
            if(month.equals("01")||month.equals("03")||month.equals("05")||month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12"))
            {
                flag=31;
            }
            if(month.equals("04")||month.equals("06")||month.equals("09")||month.equals("11"))
            {
                flag=30;
            }
            if(month.equals("02"))
            {
                if(Integer.valueOf(month)%4==0)
                {
                    flag=29;
                }
                else
                {
                    flag=28;
                }
            }
            date = new String[flag];
            for(int i=1;i<=flag;i++)
            {
                if(i<10)
                    date[i-1]=month+"-0"+String.valueOf(i);
                else
                    date[i-1]=month+"-"+String.valueOf(i);
            }
            score = new int[flag];
            for(int i=0;i<flag;i++)
            {
                score[i]=0;
            }
        }
        else
        {
            date = new String[12];
            for(int i=1;i<=12;i++)
            {
                if(i<10)
                    date[i-1]=year+"-0"+String.valueOf(i);
                else
                    date[i-1]=year+"-"+String.valueOf(i);
            }
            score = new int[12];
            for(int i=0;i<12;i++)
            {
                score[i]=0;
            }
        }

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    lineChart = view.findViewById(R.id.line_chart1);
                    get_shouru_AxisXLables();//获取x轴的标注
                    get_shouru_AxisPoints();//获取坐标点
                    get_shouru_initLineChart();//初始化
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
                int n;
                Message msg = handler.obtainMessage();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    String url="jdbc:mysql://rm-bp132n0r530p95d0pfo.mysql.rds.aliyuncs.com:3306/acedata?user=root&password=Sy961016&useUnicode=true&characterEncoding=UTF-8";
                    String str,str0;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                        str0="jizhang_day";
                    }
                    else
                    {
                        str=" ";
                        str0="jizhang_month";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT "+str0+",SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '收入' GROUP BY "+str0+"");
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        while(rs1.next())
                        {
                            int i;
                            if(rs1.getString(1).substring(0,1).equals("0"))
                            {
                                i=Integer.valueOf(rs1.getString(1).substring(1,2));
                            }
                            else
                            {
                                i=Integer.valueOf(rs1.getString(1).substring(0,2));
                            }
                            score[i-1]= score[i-1] + (int) Math.round(Double.parseDouble(rs1.getString(2)));
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();

    }

    /**
     * 设置X 轴的显示
     */
    private void get_shouru_AxisXLables()
    {
        mAxisXValues.clear();
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void get_shouru_AxisPoints()
    {
        mPointValues.clear();
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }

    private void get_shouru_initLineChart()
    {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
//        axisX.setName("收入");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        axisY.setTextColor(Color.WHITE);
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    //收入-饼状-账本
    private void get_shouru_zhangben()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    values1.clear();
                    for (int i = 0; i < pie_numb1.length; ++i) {
                        SliceValue sliceValue = new SliceValue(pie_numb1[i], colorData[i%7]);
                        sliceValue.setLabel(pie_name1[i]+(int)sliceValue.getValue());//设置label
                        values1.add(sliceValue);
                    }
                    pie_data1 = new PieChartData(values1);
                    pie_data1.setHasLabels(hasLabels1);
                    pie_data1.setHasLabelsOnlyForSelected(hasLabelForSelected1);
                    pie_data1.setHasLabelsOutside(hasLabelsOutside1);
                    pie_data1.setHasCenterCircle(hasCenterCircle1);

                    if (isExploded1) {
                        pie_data1.setSlicesSpacing(24);
                    }
                    if (hasCenterText11) {
                        pie_data1.setCenterText1("账 本");//设置中心文字1
                    }
                    if (hasCenterText21) {
                        pie_data1.setCenterText2("Charts (Roboto Italic)");//设置中心文字2
                    }
                    piechart1.setPieChartData(pie_data1);
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT zhangben_id,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '收入' GROUP BY zhangben_id");
                    rs1.last();
                    numValues1=rs1.getRow();
                    pie_numb1=new float[numValues1];
                    pie_name1=new String[numValues1];
                    rs1.beforeFirst();
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        int i=0;
                        while(rs1.next())
                        {
                            pie_name1[i]=rs1.getString(1);
                            pie_numb1[i]=Float.parseFloat(rs1.getString(2));
                            i++;
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //收入-饼状-类型
    private void get_shouru_leixing()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    values2.clear();
                    for (int i = 0; i < pie_numb2.length; ++i) {
                        SliceValue sliceValue = new SliceValue(pie_numb2[i], colorData[i%7]);
                        sliceValue.setLabel(pie_name2[i]+(int)sliceValue.getValue());//设置label
                        values2.add(sliceValue);
                    }
                    pie_data2 = new PieChartData(values2);
                    pie_data2.setHasLabels(hasLabels1);
                    pie_data2.setHasLabelsOnlyForSelected(hasLabelForSelected1);
                    pie_data2.setHasLabelsOutside(hasLabelsOutside1);
                    pie_data2.setHasCenterCircle(hasCenterCircle1);

                    if (isExploded1) {
                        pie_data2.setSlicesSpacing(24);
                    }
                    if (hasCenterText11) {
                        pie_data2.setCenterText1("类 型");//设置中心文字1
                    }
                    if (hasCenterText21) {
                        pie_data2.setCenterText2("Charts (Roboto Italic)");//设置中心文字2
                    }
                    piechart2.setPieChartData(pie_data2);
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT leixing_name,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '收入' GROUP BY leixing_name");
                    rs1.last();
                    numValues2=rs1.getRow();
                    pie_numb2=new float[numValues2];
                    pie_name2=new String[numValues2];
                    rs1.beforeFirst();
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        int i=0;
                        while(rs1.next())
                        {
                            pie_name2[i]=rs1.getString(1);
                            pie_numb2[i]=Float.parseFloat(rs1.getString(2));
                            i++;
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //收入-饼状-方式
    private void get_shouru_fangshi()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    values3.clear();
                    for (int i = 0; i < pie_numb3.length; ++i) {
                        SliceValue sliceValue = new SliceValue(pie_numb3[i], colorData[i%7]);
                        sliceValue.setLabel(pie_name3[i]+(int)sliceValue.getValue());//设置label
                        values3.add(sliceValue);
                    }
                    pie_data3 = new PieChartData(values3);
                    pie_data3.setHasLabels(hasLabels1);
                    pie_data3.setHasLabelsOnlyForSelected(hasLabelForSelected1);
                    pie_data3.setHasLabelsOutside(hasLabelsOutside1);
                    pie_data3.setHasCenterCircle(hasCenterCircle1);

                    if (isExploded1) {
                        pie_data3.setSlicesSpacing(24);
                    }
                    if (hasCenterText11) {
                        pie_data3.setCenterText1("方 式");//设置中心文字1
                    }
                    if (hasCenterText21) {
                        pie_data3.setCenterText2("Charts (Roboto Italic)");//设置中心文字2
                    }
                    piechart3.setPieChartData(pie_data3);
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT fangshi_id,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '收入' GROUP BY fangshi_id");
                    rs1.last();
                    numValues3=rs1.getRow();
                    pie_numb3=new float[numValues3];
                    pie_name3=new String[numValues3];
                    rs1.beforeFirst();
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        int i=0;
                        while(rs1.next())
                        {
                            pie_name3[i]=rs1.getString(1);
                            pie_numb3[i]=Float.parseFloat(rs1.getString(2));
                            i++;
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //收入-饼状-成员
    private void get_shouru_chengyuan()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    values4.clear();
                    for (int i = 0; i < pie_numb4.length; ++i) {
                        SliceValue sliceValue = new SliceValue(pie_numb4[i], colorData[i%7]);
                        sliceValue.setLabel(pie_name4[i]+(int)sliceValue.getValue());//设置label
                        values4.add(sliceValue);
                    }
                    pie_data4 = new PieChartData(values4);
                    pie_data4.setHasLabels(hasLabels1);
                    pie_data4.setHasLabelsOnlyForSelected(hasLabelForSelected1);
                    pie_data4.setHasLabelsOutside(hasLabelsOutside1);
                    pie_data4.setHasCenterCircle(hasCenterCircle1);

                    if (isExploded1) {
                        pie_data4.setSlicesSpacing(24);
                    }
                    if (hasCenterText11) {
                        pie_data4.setCenterText1("成 员");//设置中心文字1
                    }
                    if (hasCenterText21) {
                        pie_data4.setCenterText2("Charts (Roboto Italic)");//设置中心文字2
                    }
                    piechart4.setPieChartData(pie_data4);
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT chengyuan_id,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '收入' GROUP BY chengyuan_id");
                    rs1.last();
                    numValues4=rs1.getRow();
                    pie_numb4=new float[numValues4];
                    pie_name4=new String[numValues4];
                    rs1.beforeFirst();
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        int i=0;
                        while(rs1.next())
                        {
                            pie_name4[i]=rs1.getString(1);
                            pie_numb4[i]=Float.parseFloat(rs1.getString(2));
                            i++;
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //折线-支出
    public void get_zhichu()
    {
        if(month_year==1)
        {
            int flag=0;
            if(month.equals("01")||month.equals("03")||month.equals("05")||month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12"))
            {
                flag=31;
            }
            if(month.equals("04")||month.equals("06")||month.equals("09")||month.equals("11"))
            {
                flag=30;
            }
            if(month.equals("02"))
            {
                if(Integer.valueOf(month)%4==0)
                {
                    flag=29;
                }
                else
                {
                    flag=28;
                }
            }
            date_zhichu = new String[flag];
            for(int i=1;i<=flag;i++)
            {
                if(i<10)
                    date_zhichu[i-1]=month+"-0"+String.valueOf(i);
                else
                    date_zhichu[i-1]=month+"-"+String.valueOf(i);
            }
            score_zhichu = new int[flag];
            for(int i=0;i<flag;i++)
            {
                score_zhichu[i]=0;
            }
        }
        else
        {
            date_zhichu = new String[12];
            for(int i=1;i<=12;i++)
            {
                if(i<10)
                    date_zhichu[i-1]=year+"-0"+String.valueOf(i);
                else
                    date_zhichu[i-1]=year+"-"+String.valueOf(i);
            }
            score_zhichu = new int[12];
            for(int i=0;i<12;i++)
            {
                score_zhichu[i]=0;
            }
        }

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    lineChart_zhichu = view.findViewById(R.id.line_chart2);
                    get_zhichu_AxisXLables();//获取x轴的标注
                    get_zhichu_AxisPoints();//获取坐标点
                    get_zhichu_initLineChart();//初始化
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
                int n;
                Message msg = handler.obtainMessage();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    String url="jdbc:mysql://rm-bp132n0r530p95d0pfo.mysql.rds.aliyuncs.com:3306/acedata?user=root&password=Sy961016&useUnicode=true&characterEncoding=UTF-8";
                    String str,str0;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                        str0="jizhang_day";
                    }
                    else
                    {
                        str=" ";
                        str0="jizhang_month";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT "+str0+",SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '支出' GROUP BY "+str0+"");
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        while(rs1.next())
                        {
                            int i;
                            if(rs1.getString(1).substring(0,1).equals("0"))
                            {
                                i=Integer.valueOf(rs1.getString(1).substring(1,2));
                            }
                            else
                            {
                                i=Integer.valueOf(rs1.getString(1).substring(0,2));
                            }
                            score_zhichu[i-1]= score_zhichu[i-1] + (int) Math.round(Double.parseDouble(rs1.getString(2)));
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();

    }

    /**
     * 设置X 轴的显示
     */
    private void get_zhichu_AxisXLables()
    {
        mAxisXValues_zhichu.clear();
        for (int i = 0; i < date_zhichu.length; i++) {
            mAxisXValues_zhichu.add(new AxisValue(i).setLabel(date_zhichu[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void get_zhichu_AxisPoints()
    {
        mPointValues_zhichu.clear();
        for (int i = 0; i < score_zhichu.length; i++) {
            mPointValues_zhichu.add(new PointValue(i, score_zhichu[i]));
        }
    }

    private void get_zhichu_initLineChart()
    {
        Line line = new Line(mPointValues_zhichu).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
//        axisX.setName("支出");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues_zhichu);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        axisY.setTextColor(Color.WHITE);
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart_zhichu.setInteractive(true);
        lineChart_zhichu.setZoomType(ZoomType.HORIZONTAL);
        lineChart_zhichu.setMaxZoom((float) 2);//最大方法比例
        lineChart_zhichu.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart_zhichu.setLineChartData(data);
        lineChart_zhichu.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart_zhichu.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart_zhichu.setCurrentViewport(v);
    }

    //支出-饼状-账本
    private void get_zhichu_zhangben()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    values5.clear();
                    for (int i = 0; i < pie_numb5.length; ++i) {
                        SliceValue sliceValue = new SliceValue(pie_numb5[i], colorData[i%7]);
                        sliceValue.setLabel(pie_name5[i]+(int)sliceValue.getValue());//设置label
                        values5.add(sliceValue);
                    }
                    pie_data5 = new PieChartData(values5);
                    pie_data5.setHasLabels(hasLabels1);
                    pie_data5.setHasLabelsOnlyForSelected(hasLabelForSelected1);
                    pie_data5.setHasLabelsOutside(hasLabelsOutside1);
                    pie_data5.setHasCenterCircle(hasCenterCircle1);

                    if (isExploded1) {
                        pie_data5.setSlicesSpacing(24);
                    }
                    if (hasCenterText11) {
                        pie_data5.setCenterText1("账 本");//设置中心文字1
                    }
                    if (hasCenterText21) {
                        pie_data5.setCenterText2("Charts (Roboto Italic)");//设置中心文字2
                    }
                    piechart5.setPieChartData(pie_data5);
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT zhangben_id,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '支出' GROUP BY zhangben_id");
                    rs1.last();
                    numValues5=rs1.getRow();
                    pie_numb5=new float[numValues5];
                    pie_name5=new String[numValues5];
                    rs1.beforeFirst();
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        int i=0;
                        while(rs1.next())
                        {
                            pie_name5[i]=rs1.getString(1);
                            pie_numb5[i]=Float.parseFloat(rs1.getString(2));
                            i++;
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //支出-饼状-类型
    private void get_zhichu_leixing()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    values6.clear();
                    for (int i = 0; i < pie_numb6.length; i++) {
                        SliceValue sliceValue = new SliceValue(pie_numb6[i], colorData[i%7]);
                        sliceValue.setLabel(pie_name6[i]+(int)sliceValue.getValue());//设置label
                        values6.add(sliceValue);
                    }
                    pie_data6 = new PieChartData(values6);
                    pie_data6.setHasLabels(hasLabels1);
                    pie_data6.setHasLabelsOnlyForSelected(hasLabelForSelected1);
                    pie_data6.setHasLabelsOutside(hasLabelsOutside1);
                    pie_data6.setHasCenterCircle(hasCenterCircle1);

                    if (isExploded1) {
                        pie_data6.setSlicesSpacing(24);
                    }
                    if (hasCenterText11) {
                        pie_data6.setCenterText1("类 型");//设置中心文字1
                    }
                    if (hasCenterText21) {
                        pie_data6.setCenterText2("Charts (Roboto Italic)");//设置中心文字2
                    }
                    piechart6.setPieChartData(pie_data6);
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT leixing_name,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '支出' GROUP BY leixing_name");
                    rs1.last();
                    numValues6=rs1.getRow();
                    pie_numb6=new float[numValues6];
                    pie_name6=new String[numValues6];
                    rs1.beforeFirst();
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        int i=0;
                        while(rs1.next())
                        {
                            pie_name6[i]=rs1.getString(1);
                            pie_numb6[i]=Float.parseFloat(rs1.getString(2));
                            i++;
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //支出-饼状-方式
    private void get_zhichu_fangshi()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    values7.clear();
                    for (int i = 0; i < pie_numb7.length; ++i) {
                        SliceValue sliceValue = new SliceValue(pie_numb7[i], colorData[i%7]);
                        sliceValue.setLabel(pie_name7[i]+(int)sliceValue.getValue());//设置label
                        values7.add(sliceValue);
                    }
                    pie_data7 = new PieChartData(values7);
                    pie_data7.setHasLabels(hasLabels1);
                    pie_data7.setHasLabelsOnlyForSelected(hasLabelForSelected1);
                    pie_data7.setHasLabelsOutside(hasLabelsOutside1);
                    pie_data7.setHasCenterCircle(hasCenterCircle1);

                    if (isExploded1) {
                        pie_data7.setSlicesSpacing(24);
                    }
                    if (hasCenterText11) {
                        pie_data7.setCenterText1("方 式");//设置中心文字1
                    }
                    if (hasCenterText21) {
                        pie_data7.setCenterText2("Charts (Roboto Italic)");//设置中心文字2
                    }
                    piechart7.setPieChartData(pie_data7);
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT fangshi_id,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '支出' GROUP BY fangshi_id");
                    rs1.last();
                    numValues7=rs1.getRow();
                    pie_numb7=new float[numValues7];
                    pie_name7=new String[numValues7];
                    rs1.beforeFirst();
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        int i=0;
                        while(rs1.next())
                        {
                            pie_name7[i]=rs1.getString(1);
                            pie_numb7[i]=Float.parseFloat(rs1.getString(2));
                            i++;
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //支出-饼状-成员
    private void get_zhichu_chengyuan()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    values8.clear();
                    for (int i = 0; i < pie_numb8.length; ++i) {
                        SliceValue sliceValue = new SliceValue(pie_numb8[i], colorData[i%7]);
                        sliceValue.setLabel(pie_name8[i]+(int)sliceValue.getValue());//设置label
                        values8.add(sliceValue);
                    }
                    pie_data8 = new PieChartData(values8);
                    pie_data8.setHasLabels(hasLabels1);
                    pie_data8.setHasLabelsOnlyForSelected(hasLabelForSelected1);
                    pie_data8.setHasLabelsOutside(hasLabelsOutside1);
                    pie_data8.setHasCenterCircle(hasCenterCircle1);

                    if (isExploded1) {
                        pie_data8.setSlicesSpacing(24);
                    }
                    if (hasCenterText11) {
                        pie_data8.setCenterText1("成 员");//设置中心文字1
                    }
                    if (hasCenterText21) {
                        pie_data8.setCenterText2("Charts (Roboto Italic)");//设置中心文字2
                    }
                    piechart8.setPieChartData(pie_data8);
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT chengyuan_id,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"and jizhang_leibie = '支出' GROUP BY chengyuan_id");
                    rs1.last();
                    numValues8=rs1.getRow();
                    pie_numb8=new float[numValues8];
                    pie_name8=new String[numValues8];
                    rs1.beforeFirst();
                    if(!rs1.next())
                    {
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        int i=0;
                        while(rs1.next())
                        {
                            pie_name8[i]=rs1.getString(1);
                            pie_numb8[i]=Float.parseFloat(rs1.getString(2));
                            i++;
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //支出和收入
    private void bar_initData()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
//                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    setFirstChart();
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
                    String str;
                    if(month_year==1)
                    {
                        str=" and jizhang_month='"+month+"' ";
                    }
                    else
                    {
                        str=" ";
                    }
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT jizhang_leibie,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"'"+str+"GROUP BY jizhang_leibie ORDER BY jizhang_leibie DESC");
                    if(!rs1.next())
                    {
                        list.add((float) 0);
                        list.add((float) 0);
                        msg.what=msgWhatr;
                    }
                    else
                    {
                        Log.i("成功", "连接到数据库");
                        rs1.beforeFirst();
                        list.clear();
                        while(rs1.next())
                        {
                            list.add(Float.parseFloat(rs1.getString(2)));
                        }
                        msg.what=msgWhatr;
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.i("失败", "连接不到数据库");
                    msg.what=msgWhatw1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void setFirstChart()
    {
        // 使用的 7列，每列1个subcolumn。
        int numSubcolumns = 1;
        int numColumns = 2;
        int[] barcolor = {Color.parseColor("#c8e9a0"),Color.parseColor("#f4a277")};
        //定义一个圆柱对象集合
        List<Column> columns = new ArrayList<Column>();
        //子列数据集合
        List<SubcolumnValue> values;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        //遍历列数numColumns
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            //遍历每一列的每一个子列
            for (int j = 0; j < numSubcolumns; ++j) {
                //为每一柱图添加颜色和数值
                float f = list.get(i);
                values.add(new SubcolumnValue(f,barcolor[i]));
            }
            //创建Column对象
            Column column = new Column(values);
            //这一步是能让圆柱标注数据显示带小数的重要一步 让我找了好久问题
            //作者回答https://github.com/lecho/hellocharts-android/issues/185
            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
            column.setFormatter(chartValueFormatter);
            //是否有数据标注
            column.setHasLabels(true);
            //是否是点击圆柱才显示数据标注
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
            //给x轴坐标设置描述
            axisValues.add(new AxisValue(i).setLabel(week[i]));
        }
        //创建一个带有之前圆柱对象column集合的ColumnChartData
        bardata= new ColumnChartData(columns);

        //定义x轴y轴相应参数
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("");//轴名称
        axisY.hasLines();//是否显示网格线
        axisY.setTextColor(Color.WHITE);
//        axisY.setTextColor(Color.GRAY);//颜色

        axisX.hasLines();
        axisX.setTextColor(Color.GRAY);
        axisX.setValues(axisValues);
        //把X轴Y轴数据设置到ColumnChartData 对象中
        bardata.setAxisXBottom(axisX);
        bardata.setAxisYLeft(axisY);
        //给表填充数据，显示出来
        barchart.setColumnChartData(bardata);
    }

    public void jiancha()
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    getInfo();
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
