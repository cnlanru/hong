package cn.lanru.lrapplication;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.lanru.lrapplication.activity.AgentActivity;
import cn.lanru.lrapplication.activity.CardActivity;
import cn.lanru.lrapplication.activity.CommissionActivity;
import cn.lanru.lrapplication.activity.CouponActivity;
import cn.lanru.lrapplication.activity.ExchangeActivity;
import cn.lanru.lrapplication.activity.ExperienceActivity;
import cn.lanru.lrapplication.activity.FavActivity;
import cn.lanru.lrapplication.activity.HistoryActivity;
import cn.lanru.lrapplication.activity.IntegralActivity;
import cn.lanru.lrapplication.activity.LoginActivity;
import cn.lanru.lrapplication.activity.OrderActivity;
import cn.lanru.lrapplication.activity.OrderListActivity;
import cn.lanru.lrapplication.activity.PersonalActivity;
import cn.lanru.lrapplication.activity.QrcodeActivity;
import cn.lanru.lrapplication.activity.ShopActivity;
import cn.lanru.lrapplication.activity.StudyActivity;
import cn.lanru.lrapplication.activity.TeamActivity;
import cn.lanru.lrapplication.activity.WorkActivity;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;


public class MainActivity extends BaseActivity implements OnRefreshListener {

    private String currId;
    Intent intent;

    final Intent params = getIntent();

    private Context mContext;

    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();


        Bundle bundle = this.getIntent().getExtras();
        try {
            currId = bundle.getString("fid");
        } catch (Exception e) {
            currId = "5";
        }

        if (!super.isLogin()) {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        super.menuInit(currId);
        super.menuClick();

        smartRefreshLayout = findViewById(R.id.Main_SRLayout);
        initView();
        initAd();
        initUser();
        viewsAddListener();
    }

    private void viewsAddListener() {
        smartRefreshLayout.setOnRefreshListener(this);
    }

    //会员等级处理
    public void initUser () {
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(mContext, "token", ""));
        params.put("key", "agent_group");
        HttpRequest.getUserInfo(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject((String) responseObj);
                    JSONObject data = new JSONObject(jsonObject.getString("data"));

                    TextView llAgent = findViewById(R.id.llAgent);
                    TextView llGroup = findViewById(R.id.llGroup);

                    if (data.getInt("agent") == 1) {
                        llAgent.setVisibility(View.GONE);
                    }
                    if (data.getInt("group") == 1) {
                        llGroup.setVisibility(View.GONE);
                    }

                    TextView tvCardTxt = findViewById(R.id.tvCardTxt);
                    TextView tvAgentTxt = findViewById(R.id.tvAgentTxt);
                    tvCardTxt.setText(data.getString("group_name"));
                    tvAgentTxt.setText(data.getString("agent_name"));

                } catch (Exception e) {}
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }
    //banner
    public void initAd() {
        RequestParams params = new RequestParams();
        params.put("pid", "4");
        HttpRequest.getAd(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject((String) responseObj);

                    JSONArray listArray = jsonObject.getJSONArray("data");

                    Glide.with(getApplicationContext())
                            .load(listArray.getJSONObject(0).getString("remark")).placeholder(new ColorDrawable(Color.DKGRAY))
                            .error(new ColorDrawable(Color.DKGRAY))
                            .into((ImageView) findViewById(R.id.my_banner));

                } catch (Exception e) {}
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }
    public void initView() {
        try {
            RequestOptions options = new RequestOptions().error(R.drawable.user_face)
                    .bitmapTransform(new RoundedCorners(100));
            Glide.with(getApplicationContext())
                    .load(SharedHelper.getString(getApplicationContext(), "avatar", "")).apply(options)
                    .into((ImageView) findViewById(R.id.face));

            TextView nicname = findViewById(R.id.nickname);
            nicname.setText(SharedHelper.getString(getApplicationContext(), "username", ""));

        } catch (Exception ex) {
            Log.e("error=", ex.getMessage());
        }
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initAd();
        initUser();
        smartRefreshLayout.finishRefresh();
        Toast.makeText(mContext, "刷新完成！", Toast.LENGTH_SHORT).show();
    }

    //代理
    public void onClickAgent(View view) {
        intent = new Intent(mContext, AgentActivity.class);
        startActivity(intent);
    }
    //会员等级
    public void onClickGroup(View view) {
        intent = new Intent(mContext, CardActivity.class);
        startActivity(intent);
    }
    //设置个人资料
    public void setting (View view) {
        Intent intent = new Intent(getApplicationContext(), PersonalActivity.class);
        startActivity(intent);
    }
    //佣金
    public void onClickYong(View view) {
        intent = new Intent(mContext, CommissionActivity.class);
        startActivity(intent);
    }
    //退出
    public void logout(View view) {
        super.logout();
    }
    //学习卡
    public void onClickCard(View view) {
        intent = new Intent(mContext, CardActivity.class);
        startActivity(intent);
    }
    //学习进度
    public void onClickMyStudy(View view){
        intent = new Intent(mContext, StudyActivity.class);
        startActivity(intent);
    }
    //收藏
    public void onClickFav(View view){
        intent = new Intent(mContext, FavActivity.class);
        startActivity(intent);
    }
    //历史
    public void onClickHistory(View view) {
        intent = new Intent(mContext, HistoryActivity.class);
        startActivity(intent);
    }
    //我的订单
    public void onClickOrder(View view) {
        intent = new Intent(mContext, OrderListActivity.class);
        startActivity(intent);
    }
    //我的佣金
    public void onClickCommission(View view) {
        intent = new Intent(mContext, CommissionActivity.class);
        startActivity(intent);
    }
    //我的作业
    public void onClickWork(View view) {
        intent = new Intent(mContext, WorkActivity.class);
        startActivity(intent);
    }
    //我的团队
    public void onClickTeam(View view) {
        intent = new Intent(mContext, TeamActivity.class);
        startActivity(intent);
    }
    //兑换
    public void onClickExchange(View view) {
        intent = new Intent(mContext, ExchangeActivity.class);
        startActivity(intent);
    }
    //积分商城
    public void onClickIntegral(View view) {
        intent = new Intent(mContext, ShopActivity.class);
        startActivity(intent);
    }
    //我的优惠券
    public void onClickCoupon(View view) {
        intent = new Intent(mContext, CouponActivity.class);
        startActivity(intent);
    }
    //个人二维码
    public void onClickQrcode(View view) {
        intent = new Intent(mContext, QrcodeActivity.class);
        startActivity(intent);
    }

}
