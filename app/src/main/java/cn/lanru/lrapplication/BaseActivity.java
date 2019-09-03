package cn.lanru.lrapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;


import org.json.JSONException;
import org.json.JSONObject;

import cn.lanru.lrapplication.activity.EduActivity;
import cn.lanru.lrapplication.activity.LoginActivity;
import cn.lanru.lrapplication.activity.NewsActivity;
import cn.lanru.lrapplication.activity.ShopActivity;
import cn.lanru.lrapplication.activity.StudyActivity;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;
import cn.lanru.lrapplication.view.TabView;
import cn.lanru.lrapplication.view.TabViewA;

public class BaseActivity extends AppCompatActivity {

    // 权限
    private static final int REQUEST_PERMISSION = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.INTERNET,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();

    }


    //导航栏初始化
    public void menuInit(String currId) {
        View vMenu = findViewById(R.id.comm_menu);
        if (vMenu != null) {
            TabView iVTabEdu = vMenu.findViewById(R.id.eduClick);
            TabView iVTabShop = vMenu.findViewById(R.id.shopClick);
            TabViewA iVTabStudy = vMenu.findViewById(R.id.studyClick);
            TabView iVTabNews = vMenu.findViewById(R.id.newsClick);
            TabView iVTabMy = vMenu.findViewById(R.id.myClick);

            iVTabEdu.setIconAndText(R.mipmap.i_edu, R.mipmap.i_edu_on, "教育");
            iVTabShop.setIconAndText(R.mipmap.i_shop, R.mipmap.i_shop_on, "商城");
            iVTabStudy.setIconAndText(R.mipmap.study, R.mipmap.study, "学习");
            iVTabNews.setIconAndText(R.mipmap.i_news, R.mipmap.i_news_on, "资讯");
            iVTabMy.setIconAndText(R.mipmap.i_my, R.mipmap.i_my_on, "我的");

            switch (currId) {
                case "2":
                    iVTabEdu.setProgress(0);
                    iVTabShop.setProgress(1);
                    iVTabNews.setProgress(0);
                    iVTabMy.setProgress(0);
                    break;
                case "3":
                    iVTabEdu.setProgress(0);
                    iVTabShop.setProgress(0);
                    iVTabNews.setProgress(0);
                    iVTabMy.setProgress(0);
                    break;
                case "4":
                    iVTabEdu.setProgress(0);
                    iVTabShop.setProgress(0);
                    iVTabNews.setProgress(1);
                    iVTabMy.setProgress(0);
                    break;
                case "5":
                    iVTabEdu.setProgress(0);
                    iVTabShop.setProgress(0);
                    iVTabNews.setProgress(0);
                    iVTabMy.setProgress(1);
                    break;
                default:
                    iVTabEdu.setProgress(1);
                    iVTabShop.setProgress(0);
                    iVTabNews.setProgress(0);
                    iVTabMy.setProgress(0);
            }
        }
    }

    //导航菜单
    public void menuClick() {
        View vMenu = findViewById(R.id.comm_menu);

        if (vMenu != null) {
            //教育
            vMenu.findViewById(R.id.eduClick).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EduActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            //商城
            vMenu.findViewById(R.id.shopClick).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            //学习
            vMenu.findViewById(R.id.studyClick).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), StudyActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            //新闻
            vMenu.findViewById(R.id.newsClick).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            //我的
            vMenu.findViewById(R.id.myClick).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    //退出
    public void logout() {
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(getApplicationContext(), "token", ""));
        HttpRequest.getLogOut(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(String.valueOf(responseObj));
                    if (result.getInt("code") == 1) {
                        SharedHelper.putString(getApplicationContext(), "token", "");
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }

    //判断用户是否登录
    public boolean isLogin() {
        String token = SharedHelper.getString(getApplicationContext(), "token", "");

        return token.length() == 0 ? false : true;
    }



    /**
     * 初始化权限
     */
    private void initPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            boolean isGranted = true;
            for (String permission : PERMISSIONS) {
                int result = ActivityCompat.checkSelfPermission(this, permission);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                    break;
                }
            }

            if (!isGranted) {
                // 还没有的话，去申请权限
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = result == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    break;
                }
            }

            if (!granted) {
                // 没有赋予权限
            } else {
                // 已经赋予过权限了
            }
        }
    }

    public void setHorizontalGridView(int size, GridView gridView) {
        int length = size;
        //一个界面要显示的几个Item
        int AnInterfaceNum=3;
        //每个Item的间距(注:如果间距过大,但屏幕宽度不够,多出的部份会被无视)
        int spcing = 30;
        //计算当个Item的宽度( 屏幕宽度 减去- 一个屏幕下要总item数量的间距之和 最后除/ 单个屏幕要显示几个Item)
        int itemWidth = ((getResources().getDisplayMetrics().widthPixels) - ((AnInterfaceNum - 1) * spcing)) / AnInterfaceNum;
        //这里笔者并不理解为什么网上有些代码这里需要用到屏幕密度,但会影响我最终效果,就注释掉
        //        float density = dm.density;
        //
        //        int gridviewWidth = (int) (size * (length) * density)+((size-1)*30);
        //        int itemWidth = (int) ((length) * density);
        //笔者更具实际情况改写如下:
        //GridView总长度
        int gridviewWidth = (length * (itemWidth)) + ((length - 1) * spcing);

        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(params);           // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth);         // 设置列表项宽
        gridView.setHorizontalSpacing(spcing);      // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(length);             // 设置列数量=列表集合数

    }

}
