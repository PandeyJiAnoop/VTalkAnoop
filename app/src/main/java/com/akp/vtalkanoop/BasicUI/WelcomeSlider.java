package com.akp.vtalkanoop.BasicUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akp.vtalkanoop.Home.DashboardScreen;
import com.akp.vtalkanoop.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;
public class WelcomeSlider extends AppCompatActivity implements View.OnClickListener{
    private ViewPager viewPager;
    TextView login_button;
    //LinearLayout
    private CirclePageIndicator indicator;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 300;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_slider);
        findViewById();
        setListner();
    }

    private void setListner() {
        login_button.setOnClickListener(this);
    }

    private void findViewById() {
        indicator = findViewById(R.id.indicator);
        //viewPager
        viewPager = findViewById(R.id.viewPager);
        login_button = findViewById(R.id.login_button);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(6 * density);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 6-1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_button:
                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                break; } }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Integer [] images = {R.drawable.bfive,R.drawable.bone,R.drawable.bfour,R.drawable.bthree,R.drawable.btwo};
        @Override
        public int getCount() {
            return images.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_layout, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(images[position]);
            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity(); // or finish();
    }
}