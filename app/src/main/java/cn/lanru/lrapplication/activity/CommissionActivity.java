package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;

public class CommissionActivity extends BaseActivity {

    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);

        mContext = getApplicationContext();
    }

    public void back (View view) {
        this.finish();
    }
}
