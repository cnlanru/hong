package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;

public class OrderListActivity extends BaseActivity {

    public TextView tvTxt1;
    public TextView tvTxt2;
    public TextView tvTxt3;
    public TextView tvTxt4;
    public TextView tvTxt5;

    public TextView tvLine1;
    public TextView tvLine2;
    public TextView tvLine3;
    public TextView tvLine4;
    public TextView tvLine5;


    public int status = 0;
    public Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        mContext = getApplicationContext();

        initView();
    }

    public void initView () {
        tvTxt1 = findViewById(R.id.tvTxt1);
        tvTxt2 = findViewById(R.id.tvTxt2);
        tvTxt3 = findViewById(R.id.tvTxt3);
        tvTxt4 = findViewById(R.id.tvTxt4);
        tvTxt5 = findViewById(R.id.tvTxt5);

        tvLine1 = findViewById(R.id.tvLine1);
        tvLine2 = findViewById(R.id.tvLine2);
        tvLine3 = findViewById(R.id.tvLine3);
        tvLine4 = findViewById(R.id.tvLine4);
        tvLine5 = findViewById(R.id.tvLine5);

        tvTxt1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvLine1.setBackgroundColor(Color.parseColor("#007aff"));

    }

    public void back (View view) {
        this.finish();
    }

    public void onEvaluate(View view) {
        tvTxt1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt5.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        tvLine1.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine2.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine3.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine4.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine5.setBackgroundColor(Color.parseColor("#007aff"));

        status = 5;
    }

    public void onReceiving(View view) {
        tvTxt1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt4.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvTxt5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        tvLine1.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine2.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine3.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine4.setBackgroundColor(Color.parseColor("#007aff"));
        tvLine5.setBackgroundColor(Color.parseColor("#ffffff"));

        status = 4;
    }

    public void onSend(View view) {
        tvTxt1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvTxt4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        tvLine1.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine2.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine3.setBackgroundColor(Color.parseColor("#007aff"));
        tvLine4.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine5.setBackgroundColor(Color.parseColor("#ffffff"));

        status = 3;
    }

    public void onPay(View view) {
        tvTxt1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvTxt3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        tvLine1.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine2.setBackgroundColor(Color.parseColor("#007aff"));
        tvLine3.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine4.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine5.setBackgroundColor(Color.parseColor("#ffffff"));

        status = 2;
    }

    public void allOrder(View view) {

        tvTxt1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvTxt2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt3.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt4.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvTxt5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        tvLine1.setBackgroundColor(Color.parseColor("#007aff"));
        tvLine2.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine3.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine4.setBackgroundColor(Color.parseColor("#ffffff"));
        tvLine5.setBackgroundColor(Color.parseColor("#ffffff"));

        status = 1;
    }
}
