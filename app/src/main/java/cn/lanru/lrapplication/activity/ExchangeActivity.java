package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class ExchangeActivity extends BaseActivity {

    private Context mContext;

    private EditText etCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        etCode = findViewById(R.id.etCode);
    }

    public void back (View view) {
        this.finish();
    }

    public void btnSubmit(View view) {
        String code = etCode.getText().toString();
        if (code.trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "请输入兑换码", Toast.LENGTH_SHORT).show();
        } else {
            RequestParams params = new RequestParams();
            params.put("token", SharedHelper.getString(getApplicationContext(), "token", ""));
            params.put("code", code);
            HttpRequest.postExchange(params, new ResponseCallback() {
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
}
