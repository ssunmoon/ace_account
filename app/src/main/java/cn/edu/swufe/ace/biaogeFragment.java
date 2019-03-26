package cn.edu.swufe.ace;

import android.app.Activity;
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

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_biaoge, null);

        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("riqi", Activity.MODE_PRIVATE);
        year = sharedPreferences1.getString("year","2019");
        month = sharedPreferences1.getString("month","03");

        SharedPreferences sharedPreferences0 = getActivity().getSharedPreferences("default_user", Activity.MODE_PRIVATE);
        yonghu = sharedPreferences0.getString("yonghu","");



//        lineChart = view.findViewById(R.id.line_chart);
//        getAxisXLables();//获取x轴的标注
//        getAxisPoints();//获取坐标点
//        initLineChart();//初始化

        getMonth();
        getMonth_zhichu();

        return view;
    }

    public void getMonth()
    {
        date = new String[31];
        for(int i=1;i<32;i++)
        {
            if(i<10)
                date[i-1]=month+"-0"+String.valueOf(i);
            else
                date[i-1]=month+"-"+String.valueOf(i);
        }
        score = new int[31];
        for(int i=0;i<31;i++)
        {
            score[i]=0;
        }

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    lineChart = view.findViewById(R.id.line_chart1);
                    getAxisXLables();//获取x轴的标注
                    getAxisPoints();//获取坐标点
                    initLineChart();//初始化
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
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT jizhang_day,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"' and jizhang_month='"+month+"' and jizhang_leibie = '收入' GROUP BY jizhang_day");
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
                            score[i-1]= score[i] + (int) Math.round(Double.parseDouble(rs1.getString(2)));
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

    public void getYear()
    {

    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        mAxisXValues.clear();
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        mPointValues.clear();
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }

    private void initLineChart() {
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
        axisX.setName("收入");  //表格名称
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

    public void getMonth_zhichu()
    {
        date_zhichu = new String[31];
        for(int i=1;i<32;i++)
        {
            if(i<10)
                date_zhichu[i-1]=month+"-0"+String.valueOf(i);
            else
                date_zhichu[i-1]=month+"-"+String.valueOf(i);
        }
        score_zhichu = new int[31];
        for(int i=0;i<31;i++)
        {
            score_zhichu[i]=0;
        }

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == msgWhatr){
                    Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_LONG).show();
                    lineChart_zhichu = view.findViewById(R.id.line_chart2);
                    getAxisXLables_zhichu();//获取x轴的标注
                    getAxisPoints_zhichu();//获取坐标点
                    initLineChart_zhichu();//初始化
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
                    Connection conn= (Connection) DriverManager.getConnection(url);//连接数据库
                    Log.i("成功", "连接到数据库");
                    Statement st1 = conn.createStatement();
                    ResultSet rs1 = st1.executeQuery("SELECT jizhang_day,SUM(jizhang_jine) FROM jizhang where user_id='"+yonghu+"' and jizhang_year='"+year+"' and jizhang_month='"+month+"' and jizhang_leibie = '支出' GROUP BY jizhang_day");
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
                            score_zhichu[i-1]= score_zhichu[i] + (int) Math.round(Double.parseDouble(rs1.getString(2)));
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

    public void getYear_zhichu()
    {

    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables_zhichu() {
        mAxisXValues_zhichu.clear();
        for (int i = 0; i < date_zhichu.length; i++) {
            mAxisXValues_zhichu.add(new AxisValue(i).setLabel(date_zhichu[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints_zhichu() {
        mPointValues_zhichu.clear();
        for (int i = 0; i < score_zhichu.length; i++) {
            mPointValues_zhichu.add(new PointValue(i, score_zhichu[i]));
        }
    }

    private void initLineChart_zhichu() {
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
        axisX.setName("支出");  //表格名称
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

}
