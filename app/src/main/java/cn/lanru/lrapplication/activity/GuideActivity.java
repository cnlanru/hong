package cn.lanru.lrapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;
import cn.lanru.lrapplication.utils.UIUtils;

public class GuideActivity extends BaseActivity {

    private ViewPager mVp_Guide;
    private View mGuideRedPoint;
    private LinearLayout mLlGuidePoints;
    public TextView bt_startExp;

    private int disPoints;
    private int currentItem;
    private MyAdapter adapter;
    private List<ImageView> guids;
    //向导界面的图片
    private int[] mPics = new int[]{};

    public static class BitmapList {
        static List<String> list = new ArrayList<String>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        UIUtils.init(getApplicationContext());
        bt_startExp = findViewById(R.id.bt_startExp);
        initImages();
    }

    private void  initImages() {
        RequestParams params = new RequestParams();
        params.put("pid", "1");
        HttpRequest.getAd(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.d("ad_tag", String.valueOf(responseObj));
                try {
                    JSONObject jsonObject = new JSONObject((String) responseObj);
                    BitmapList.list = new ArrayList<>();
                    if (jsonObject.getInt("code") == 1) {
                        JSONArray listArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < listArray.length(); i++) {
                            String bitmap = listArray.getJSONObject(i).getString("remark");
                            BitmapList.list.add(bitmap);
                        }
                        initView();
                        initData();
                        initEvent();
                    } else {
                        Log.d("ad_error:", jsonObject.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Log.d("ad_error:", failuer.getEmsg());
            }
        });
    }

    protected void openActivity() {
        String token = SharedHelper.getString(getApplicationContext(), "token", "");
        if(token.length() == 0) {
            Intent intent = new Intent(getApplicationContext(), SchoolActivity.class);
            startActivity(intent);
            finish();
        } else {
            RequestParams params = new RequestParams();
            params.put("token", token);
            params.put("key", "card_id");
            HttpRequest.getUserInfo(params, new ResponseCallback() {
                @Override
                public void onSuccess(Object responseObj) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseObj.toString());
                        if (jsonObject.getInt("data") > 0) {
                            Intent intent = new Intent(getApplicationContext(), StudyActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), SchoolActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                }

                @Override
                public void onFailure(OkHttpException failuer) {

                }
            });
        }
    }

    //跳过
    public void enter(View view) {

        this.openActivity();
    }


    private void initView() {
        mVp_Guide = (ViewPager) findViewById(R.id.vp_guide);
        mGuideRedPoint = findViewById(R.id.v_guide_redpoint);
        mLlGuidePoints = (LinearLayout) findViewById(R.id.ll_guide_points);
    }

    private void initData() {

        // viewpaper adapter适配器
        guids = new ArrayList<ImageView>();

        //创建viewpager的适配器
        for (int i = 0; i < BitmapList.list.size(); i++) {
            ImageView iv_temp = new ImageView(getApplicationContext());
            iv_temp.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getApplicationContext()).load(BitmapList.list.get(i)).into(iv_temp);

            //添加界面的数据
            guids.add(iv_temp);

            //灰色的点在LinearLayout中绘制：
            //获取点
            View v_point = new View(getApplicationContext());
            v_point.setBackgroundResource(R.drawable.point_smiple);//灰点背景色
            //设置灰色点的显示大小
            int dip = 10;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dp2Px(dip), UIUtils.dp2Px(dip));
            //设置点与点的距离,第一个点除外
            if (i != 0)
                params.leftMargin = 47;
            v_point.setLayoutParams(params);

            mLlGuidePoints.addView(v_point);
        }

        // 创建viewpager的适配器
        adapter = new MyAdapter(getApplicationContext(), guids);
        // 设置适配器
        mVp_Guide.setAdapter(adapter);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        //监听界面绘制完成
        mGuideRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                //取消注册界面而产生的回调接口
                mGuideRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //计算点于点之间的距离
                disPoints = (mLlGuidePoints.getChildAt(1).getLeft() - mLlGuidePoints.getChildAt(0).getLeft());
            }
        });

        //滑动事件监听滑动距离，点更随滑动。
        mVp_Guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                //当前viewpager显示的页码
                //如果viewpager滑动到第三页码（最后一页），显示进入的button
                if (position == guids.size() - 1) {
                    bt_startExp.setVisibility(View.VISIBLE);//设置按钮的显示
                    bt_startExp.setBackground(getResources().getDrawable(R.drawable.button_empty_guide));
                } else {
                    //隐藏该按钮
                    bt_startExp.setVisibility(View.GONE);
                }
                currentItem = position;
            }

            /**
             *页面滑动调用，拿到滑动距离设置视图的滑动状态
             * @param position 当前页面位置
             * @param positionOffset 移动的比例值
             * @param positionOffsetPixels 便宜的像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //计算红点的左边距
                float leftMargin = disPoints * (position + positionOffset);
                //设置红点的左边距
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mGuideRedPoint.getLayoutParams();
                //对folat类型进行四舍五入，
                layoutParams.leftMargin = Math.round(leftMargin);
                //设置位置
                mGuideRedPoint.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //给页面设置触摸事件
        mVp_Guide.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float endX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;
                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentItem == (guids.size() - 1) && startX - endX >= (width / 4)) {
                            //进入主页
                            openActivity();

                            //这部分代码是切换Activity时的动画，看起来就不会很生硬
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            finish();
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public class MyAdapter extends PagerAdapter {
        private List<ImageView> mGuids;

        MyAdapter(Context ctx, List<ImageView> guids) {
            this.mGuids = guids;
        }

        @Override
        public int getCount() {
            return mGuids.size();// 返回数据的个数
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;// 过滤和缓存的作用
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);//从viewpager中移除掉
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // container viewpaper
            //获取View
            View child = mGuids.get(position);
            // 添加View
            container.addView(child);
            return child;
        }
    }
}
