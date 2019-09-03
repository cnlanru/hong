package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.adapter.CourseAdapter;
import cn.lanru.lrapplication.adapter.ExperienceAdapter;
import cn.lanru.lrapplication.bean.Course;
import cn.lanru.lrapplication.bean.Experience;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class MiddleActivity extends BaseActivity {

    private Context mContext;
    private EditText iptKeyword;

    private List<Experience> experiencesDb = new ArrayList<>();
    private GridView gvExperiences;
    private ExperienceAdapter experienceAdapter;

    private List<Course> CourseDb = new ArrayList<>();
    private GridView gvCourse;
    private CourseAdapter courseAdapter;

    private List<Course> CourseDb1 = new ArrayList<>();
    private GridView gvCourse1;
    private CourseAdapter courseAdapter1;

    private List<Course> CourseDb2 = new ArrayList<>();
    private GridView gvCourse2;
    private CourseAdapter courseAdapter2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        mContext = getApplicationContext();
        super.menuInit("1");
        super.menuClick();

        //搜索
        iptKeyword = findViewById(R.id.iptKeyword);
        iptKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)  {
                    String keyword = String.valueOf(iptKeyword.getText()).trim();
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keyword", keyword);
                    bundle.putInt("cid", 3);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return false;
            }
        });

        //我的体验课
        gvExperiences = findViewById(R.id.gvExperience);
        initExperience();
        gvExperiences.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experience data = experiencesDb.get(position);
                if (data.getId() > 0) {

                    Intent intent = new Intent(mContext, PointActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("id", data.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        //导航
        gvCourse = findViewById(R.id.gvCategory);
        gvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course data = CourseDb.get(position);
                if (data.getId() > 0) {
                    Intent intent = new Intent(mContext, ListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("id", data.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        gvCourse1 = findViewById(R.id.gvCategory1);
        gvCourse1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course data = CourseDb1.get(position);
                if (data.getId() > 0) {
                    Intent intent = new Intent(mContext, ListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("id", data.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        gvCourse2 = findViewById(R.id.gvCategory2);
        gvCourse2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course data = CourseDb2.get(position);
                if (data.getId() > 0) {
                    Intent intent = new Intent(mContext, ListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("id", data.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        initCourse();

        initBanner(); //banner图

    }

    //我的体验课
    public void initExperience () {
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(mContext, "token", ""));
        params.put("limit", "8");

        HttpRequest.getExperience(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());

                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        Experience item = new Experience();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setNumber(listArray.getJSONObject(i).getInt("number"));
                        item.setThumbnail(listArray.getJSONObject(i).getString("thumbnail"));
                        item.setTitle(listArray.getJSONObject(i).getString("title"));
                        experiencesDb.add(item);
                    }

                    experienceAdapter = new ExperienceAdapter(mContext, experiencesDb, R.layout.item_experience);
                    gvExperiences.setAdapter(experienceAdapter);

                    MiddleActivity.super.setHorizontalGridView(experiencesDb.size(), gvExperiences);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });

    }

    //课件分类
    public void initCourse () {
        RequestParams params = new RequestParams();
        params.put("pid", "3");
        params.put("limit", "12");
        HttpRequest.getClassCategory(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());

                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        Course item = new Course();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setName(listArray.getJSONObject(i).getString("name"));
                        item.setThumbnail(listArray.getJSONObject(i).getString("thumbnail"));

                        if (i < 4) {
                            CourseDb.add(item);
                        } else if (i > 3 && i < 5) {
                            CourseDb1.add(item);
                        } else {
                            CourseDb2.add(item);
                        }

                    }

                    courseAdapter = new CourseAdapter(mContext, CourseDb, R.layout.item_course);
                    gvCourse.setAdapter(courseAdapter);

                    courseAdapter1 = new CourseAdapter(mContext, CourseDb1, R.layout.item_course);
                    gvCourse1.setAdapter(courseAdapter1);

                    courseAdapter2 = new CourseAdapter(mContext, CourseDb2, R.layout.item_course);
                    gvCourse2.setAdapter(courseAdapter2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });

    }


    public void initBanner() {
        RequestParams params = new RequestParams();
        params.put("pid", "4");
        HttpRequest.getAd(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject((String) responseObj);

                    JSONArray listArray = jsonObject.getJSONArray("data");

                    Glide.with(mContext)
                            .load(listArray.getJSONObject(0).getString("remark")).placeholder(new ColorDrawable(Color.DKGRAY))
                            .error(new ColorDrawable(Color.DKGRAY))
                            .into((ImageView) findViewById(R.id.s_banner));

                } catch (Exception e) {}
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });

    }


    public void smallSchool(View view) {
        Intent intent = new Intent(mContext, EduActivity.class);
        startActivity(intent);
        finish();
    }

    public void middleSchool(View view) {
        Intent intent = new Intent(mContext, SchoolActivity.class);
        startActivity(intent);
        finish();
    }

    public void bigSchool(View view) {
        Intent intent = new Intent(mContext, MiddleActivity.class);
        startActivity(intent);
        finish();
    }

    //点击
    public void onClickShuoxue(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_名师数学");
        startActivity(intent);
    }

    /*public void onClickShuoxue(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_名师数学");
        startActivity(intent);
    }*/

    //
}
