package cn.lanru.lrapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class RegisterActivity extends BaseActivity {

    private TextView ivMobile;
    private TextView ivCaptcha;
    private RadioButton ivAgree;
    private CountDownTextView mCountDownTextView;
    private EditText invitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //已登录 进入主页
        if (super.isLogin()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_register);

        ivCaptcha = findViewById(R.id.captcha);
        ivMobile = findViewById(R.id.mobile);
        ivAgree = findViewById(R.id.btnMan);
        invitation = findViewById(R.id.invitation);
        mCountDownTextView = findViewById(R.id.tvCountDown);

        count();//发送验证码
    }

    //注册
    public void register(View iv) {
        String mobile = ivMobile.getText().toString();
        String captcha = ivCaptcha.getText().toString();
        String invitationStr = invitation.getText().toString();

        String regex = "^1\\d{10}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        boolean isMatch = m.matches();
        if (!isMatch) {
            Toast.makeText(getApplicationContext(), "请填入正确的手机号", Toast.LENGTH_SHORT).show();
        }else if (captcha.trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "请填入验证码", Toast.LENGTH_SHORT).show();
        } else if (ivAgree.isChecked() == false) {
            Toast.makeText(getApplicationContext(), "请勾选协议", Toast.LENGTH_SHORT).show();
        } else {
            RequestParams params = new RequestParams();
            params.put("mobile", mobile);
            params.put("captcha", captcha);
            params.put("invitation", invitationStr);
            HttpRequest.postRegister(params, new ResponseCallback() {
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

                    //Toast.makeText(LoginActivity.this, "请求成功"+dataBean.getAvatar(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), GradeActivity.class);
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

    //登录
    public void login(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
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
                        String mobile = String.valueOf(ivMobile.getText());
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
