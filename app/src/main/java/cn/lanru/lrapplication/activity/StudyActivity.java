package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class StudyActivity extends BaseActivity {

    public Context mContext;

    public int isSignin = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        mContext = getApplicationContext();
        super.menuInit("3");
        super.menuClick();

        initData();
    }

    public void initData() {
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(mContext, "token", ""));
        HttpRequest.getSigninCheck(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {

                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    TextView signinTxt = findViewById(R.id.signinTxt);
                    signinTxt.setText("点击打卡  +" + jsonObject.getString("msg") + "学豆");
                    isSignin = jsonObject.getInt("data");

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(getApplicationContext(), failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back (View view) {
        this.finish();
    }

    public void onSignin(View view) {
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(mContext, "token", ""));
        HttpRequest.postSignin(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {

                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(getApplicationContext(), failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
