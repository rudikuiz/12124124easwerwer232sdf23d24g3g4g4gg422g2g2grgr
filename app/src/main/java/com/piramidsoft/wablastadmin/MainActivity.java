package com.piramidsoft.wablastadmin;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new FragmentDashboard(), "Dashboard");
        pagerAdapter.addFragments(new FragmentWasap(), "Blast");
        pagerAdapter.addFragments(new FragmentLogs(), "Logs");
        viewPagerHome.setAdapter(pagerAdapter);

    }

    @OnClick({R.id.lin1, R.id.lin2, R.id.lin3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin1:
                viewPagerHome.setCurrentItem(0);
                break;
            case R.id.lin2:
                viewPagerHome.setCurrentItem(1);
                break;
            case R.id.lin3:
                viewPagerHome.setCurrentItem(2);
                break;
        }
    }
}
