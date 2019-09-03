package cn.lanru.lrapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.Constant;
import cn.lanru.lrapplication.MainActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.UserInfo;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.CountDownTextView;
import cn.lanru.lrapplication.utils.SharedHelper;

public class LoginActivity extends BaseActivity {
    private CountDownTextView mCountDownTextView;
    private EditText mMobile;
    private EditText mCapchat;
    private TextView mUserpass;
    private Button btnSubmit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //已登录 进入主页
        if (super.isLogin()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        mCountDownTextView = findViewById(R.id.tvCountDown);
        mMobile = findViewById(R.id.mobile);
        mCapchat = findViewById(R.id.captcha);
        mUserpass = findViewById(R.id.userpass);
        btnSubmit = findViewById(R.id.btnSubmit);
        count();//发送验证码
    }

    //注册
    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    //手机登录
    public void login(View view) {

        String mobile = mMobile.getText().toString();
        String code = mCapchat.getText().toString();

        String regex = "^1\\d{10}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        boolean isMatch = m.matches();
        if (!isMatch) {
            Toast.makeText(getApplicationContext(), "请填入正确的手机号", Toast.LENGTH_SHORT).show();
        } else if (code.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填入验证码", Toast.LENGTH_SHORT).show();
        } else {
            RequestParams params = new RequestParams();
            params.put("mobile", mobile);
            params.put("captcha", code);
            HttpRequest.postMobileLogin(params, new ResponseCallback() {
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

    //用户名密码登录
    public void userLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), NloginActivity.class);
        startActivity(intent);
        finish();
    }

    //发送验证码
    private void count() {
        mCountDownTextView.setNormalText("获取验证码")
                .setCountDownText("重新获取(", ")")
                .setCloseKeepCountDown(true)//关闭页面保持倒计时开关
                .setCountDownClickable(true)//倒计时期间点击事件是否生效开关
                .setShowFormatTime(true)//是否格式化时间
                .setOnCountDownFinishListener(new CountDownTextView.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
                        Toast.makeText(getApplicationContext(), "倒计时完毕", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mobile = String.valueOf(mMobile.getText());
                        Pattern r = Pattern.compile("^$");

                        String regex = "^1\\d{10}$";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(mobile);
                        boolean isMatch = m.matches();
                        if (!isMatch) {
                            Toast.makeText(getApplicationContext(), "请填入正确的手机号", Toast.LENGTH_SHORT).show();
                        } else {
                            RequestParams params = new RequestParams();
                            params.put("mobile", mobile);
                            params.put("event", "mobilelogin");
                            HttpRequest.getMobileCode(params, new ResponseCallback() {
                                @Override
                                public void onSuccess(Object responseObj) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseObj.toString());
                                        if (jsonObject.getInt("code") == 1) {
                                            Toast.makeText(getApplicationContext(), "短信已发送", Toast.LENGTH_SHORT).show();
                                            mCountDownTextView.startCountDown(Constant.SEND_TIME);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "错误:" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(OkHttpException failuer) {

                                    Toast.makeText(getApplicationContext(), "失败：" + failuer.getEmsg(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }
}
