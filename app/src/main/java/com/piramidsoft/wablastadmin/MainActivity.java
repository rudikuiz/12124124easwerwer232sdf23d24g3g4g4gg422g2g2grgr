package com.piramidsoft.wablastadmin;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piramidsoft.wablastadmin.Utils.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.lin1)
    LinearLayout lin1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.lin2)
    LinearLayout lin2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.lin3)
    LinearLayout lin3;
    @BindView(R.id.viewPagerHome)
    ViewPager viewPagerHome;
    @BindView(R.id.label1)
    TextView label1;
    @BindView(R.id.label2)
    TextView label2;
    @BindView(R.id.label3)
    TextView label3;
    @BindView(R.id.image4)
    ImageView image4;
    @BindView(R.id.label4)
    TextView label4;
    @BindView(R.id.lin4)
    LinearLayout lin4;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        lin4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        initView();
    }

    private void initView() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new FragmentDashboard(), "Dashboard");
        pagerAdapter.addFragments(new FragmentWasap(), "Blast");
//        pagerAdapter.addFragments(new FragmentLogs(), "Logs");
        pagerAdapter.addFragments(new IntresepFragment(), "Intercept");
        viewPagerHome.setAdapter(pagerAdapter);

        viewPagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                getSupportActionBar().setTitle(pagerAdapter.getPageTitle(position));

                switch (position) {
                    case 0:
                        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lin4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 1:
                        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lin4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 2:
                        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        lin4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
//                    case 3:
//                        lin1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                        lin2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                        lin3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                        lin4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick({R.id.lin1, R.id.lin2, R.id.lin3, R.id.lin4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin1:
                viewPagerHome.setCurrentItem(0);
                break;
            case R.id.lin2:
                viewPagerHome.setCurrentItem(1);
                break;
            case R.id.lin4:
                viewPagerHome.setCurrentItem(3);
                break;
            case R.id.lin3:
                viewPagerHome.setCurrentItem(2);
                break;
        }
    }
}
