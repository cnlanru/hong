package cn.lanru.lrapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;

public class PointActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
    }

    public void back (View view) {
        PointActivity.this.finish();
    }
}
