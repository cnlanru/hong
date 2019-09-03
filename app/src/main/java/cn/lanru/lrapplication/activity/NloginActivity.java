package cn.lanru.lrapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.MainActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.UserInfo;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class NloginActivity extends BaseActivity {

    private EditText ivUsername;
    private EditText ivPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //已登录 进入主页
        if (super.isLogin()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_nlogin);

        ivUsername = findViewById(R.id.username);
        ivPassword = findViewById(R.id.password);
    }

    public void mobileLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(View view) {
        String userName = String.valueOf(ivUsername.getText());
        String passWord = String.valueOf(ivPassword.getText());

        if (userName.trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
        } else if (passWord.trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            RequestParams params = new RequestParams();
            params.put("account", userName);
            params.put("password", passWord);

            HttpRequest.postNameLogin(params, new ResponseCallback() {
                @Override
                public void onSuccess(Object responseObj) {
                    UserInfo userInfo = (UserInfo) responseObj;
                    UserInfo.DataBean dataBean = userInfo.getData();

                    SharedHelper.putString(getApplicationContext(), "token", dataBean.getToken());
                    SharedHelper.putString(getApplicationContext(),"nickname", dataBean.getNickname());
                    SharedHelper.putString(getApplicationContext(),"avatar", dataBean.getAvatar());
                    SharedHelper.putInt(getApplicationContext(),"id", dataBean.getId());
                    SharedHelper.putInt(getApplicationContext(),"group_id", dataBean.getGroup_id());
                    SharedHelper.putString(getApplicationContext(),"username", dataBean.getUsername());
                    SharedHelper.putString(getApplicationContext(),"mobile", dataBean.getMobile());
                    SharedHelper.putInt(getApplicationContext(),"gender", dataBean.getGender());

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(OkHttpException failuer) {
                    Toast.makeText(getApplicationContext(), "出错了：" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
