package cn.edu.swufe.ace;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] tabText = {"明细", "图表", "记一笔", "查找", "我的"};
    //未选中icon
    private int[] normalIcon = {R.drawable.mingxi, R.drawable.biaoge, R.drawable.add_image, R.drawable.find, R.drawable.me};
    //选中时icon
    private int[] selectIcon = {R.drawable.mingxi1, R.drawable.biaoge1, R.drawable.add_image, R.drawable.find1, R.drawable.me1};
    private List<Fragment> fragments = new ArrayList<>();
    private Handler mHandler = new Handler();
    private boolean flag = true;
    private EasyNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBar = findViewById(R.id.navigationBar);

        fragments.add(new mingxiFragment());
        fragments.add(new biaogeFragment());
        fragments.add(new jiyibiFragment());
        fragments.add(new faxianFragment());
        fragments.add(new wodeFragment());

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .lineColor(Color.parseColor("#F5F5F5"))
                .fragmentList(fragments)
                .anim(null)
                .addLayoutRule(EasyNavigationBar.RULE_BOTTOM)
                .addLayoutBottom(0)
                .addAlignBottom(true)
                .addAsFragment(true)
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        Log.e("onTabClickEvent", position + "");
                        if (position == 2) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //＋ 旋转动画
                                    if (flag) {
                                        navigationBar.getAddImage().animate().rotation(45).setDuration(400);
                                    } else {
                                        navigationBar.getAddImage().animate().rotation(0).setDuration(400);
                                    }
                                    flag = !flag;
                                }
                            });
                        }
                        return false;
                    }
                })
                .canScroll(false)
                .mode(EasyNavigationBar.MODE_ADD)
                .build();
    }

    @Override
    public void onClick(View v) {

    }
}
