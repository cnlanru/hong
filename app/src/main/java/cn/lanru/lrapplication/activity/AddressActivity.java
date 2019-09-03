package cn.lanru.lrapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.JsonBean;
import cn.lanru.lrapplication.utils.GetJsonDataUtil;

import android.os.Handler;
import android.os.Message;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;

public class AddressActivity extends BaseActivity {

    private Context mContext;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        mContext = getApplicationContext();

    }

    public void back (View view) {
        this.finish();
    }


}
