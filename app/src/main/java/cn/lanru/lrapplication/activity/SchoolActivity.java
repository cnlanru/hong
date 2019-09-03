package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.adapter.ClassesAdapter;
import cn.lanru.lrapplication.adapter.CourseAdapter;
import cn.lanru.lrapplication.adapter.ExperienceAdapter;
import cn.lanru.lrapplication.bean.Classes;
import cn.lanru.lrapplication.bean.Course;
import cn.lanru.lrapplication.bean.Experience;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.CheckApkExist;
import cn.lanru.lrapplication.utils.SharedHelper;

public class SchoolActivity extends BaseActivity {

    private List<Course> CourseDb = new ArrayList<>();
    private GridView gvCourse;
    private CourseAdapter courseAdapter;

    private List<Classes> zidianDb = new ArrayList<>();
    private GridView gvZidian;
    private ClassesAdapter zidianAdapter;

    private List<Classes> yuwenDb = new ArrayList<>();
    private GridView gvYuwen;
    private ClassesAdapter yuwenAdapter;

    private List<Classes> shuoxueDb = new ArrayList<>();
    private GridView gvShuoxue;
    private ClassesAdapter shuoxueAdapter;

    private List<Classes> englishDb = new ArrayList<>();
    private GridView gvEnglish;
    private ClassesAdapter englishAdapter;

    private List<Experience> experiencesDb = new ArrayList<>();
    private GridView gvExperiences;
    private ExperienceAdapter experienceAdapter;

    private Context mContext;
    private EditText iptKeyword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_school);

        super.menuInit("1");
        super.menuClick();

        gvCourse = findViewById(R.id.gvCategory);
        initCourse();

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
                    bundle.putInt("cid", 2);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return false;
            }
        });

        //分类
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

        //字典
        gvZidian = findViewById(R.id.gvZidian);
        initZidian();
        gvZidian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classes data = zidianDb.get(position);
                if (data.getTitle() != null && CheckApkExist.checkAppExist(mContext)) {
                    Intent intent = new Intent();
                    intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
                    intent.putExtra("tag", data.getTitle());
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, R.string.app_tips, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //语文
        gvYuwen = findViewById(R.id.gvYuwen);
        initYuwen();
        gvYuwen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classes data = yuwenDb.get(position);
                if (data.getTitle() != null && CheckApkExist.checkAppExist(mContext)) {
                    Intent intent = new Intent();
                    intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
                    intent.putExtra("tag", data.getTitle());
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, R.string.app_tips, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //数学
        gvShuoxue = findViewById(R.id.gvShuoxue);
        initShuoxue();
        gvShuoxue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classes data = shuoxueDb.get(position);
                if (data.getTitle() != null && CheckApkExist.checkAppExist(mContext)) {
                    Intent intent = new Intent();
                    intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
                    intent.putExtra("tag", data.getTitle());
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, R.string.app_tips, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //英语
        gvEnglish = findViewById(R.id.gvEnglish);
        initEnglish();
        gvEnglish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classes data = englishDb.get(position);
                if (data.getTitle() != null && CheckApkExist.checkAppExist(mContext)) {
                    Intent intent = new Intent();
                    intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
                    intent.putExtra("tag", data.getTitle());
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext, R.string.app_tips, Toast.LENGTH_SHORT).show();
                }
            }
        });

        initBanner(); //banner图

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

                    SchoolActivity.super.setHorizontalGridView(experiencesDb.size(), gvExperiences);

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
        params.put("pid", "3");
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

    //课件分类
    public void initCourse () {
        RequestParams params = new RequestParams();
        params.put("pid", "2");
        params.put("limit", "4");
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
                        CourseDb.add(item);
                    }

                    courseAdapter = new CourseAdapter(mContext, CourseDb, R.layout.item_course);
                    gvCourse.setAdapter(courseAdapter);
                    Log.e("CourseDb=", String.valueOf(CourseDb));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });

    }

    //字典大全
    public void initZidian() {

        RequestParams params = new RequestParams();
        params.put("cid", "11");
        params.put("flag", "1");
        params.put("limit", "6");

        HttpRequest.getClasses(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());

                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        Classes item = new Classes();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setTitle(listArray.getJSONObject(i).getString("title"));
                        item.setThumbnail(listArray.getJSONObject(i).getString("thumbnail"));
                        zidianDb.add(item);
                    }

                    zidianAdapter = new ClassesAdapter(mContext, zidianDb, R.layout.item_zidian);
                    gvZidian.setAdapter(zidianAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });


    }

    //语文
    public void initYuwen() {

        RequestParams params = new RequestParams();
        params.put("cid", "8");
        params.put("flag", "1");
        params.put("limit", "6");

        HttpRequest.getClasses(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());

                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        Classes item = new Classes();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setTitle(listArray.getJSONObject(i).getString("title"));
                        item.setThumbnail(listArray.getJSONObject(i).getString("thumbnail"));
                        yuwenDb.add(item);
                    }

                    yuwenAdapter = new ClassesAdapter(mContext, yuwenDb, R.layout.item_zidian);
                    gvYuwen.setAdapter(yuwenAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });


    }

    //数学
    public void initShuoxue() {

        RequestParams params = new RequestParams();
        params.put("cid", "9");
        params.put("flag", "1");
        params.put("limit", "6");

        HttpRequest.getClasses(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());

                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        Classes item = new Classes();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setTitle(listArray.getJSONObject(i).getString("title"));
                        item.setThumbnail(listArray.getJSONObject(i).getString("thumbnail"));
                        shuoxueDb.add(item);
                    }

                    shuoxueAdapter = new ClassesAdapter(mContext, shuoxueDb, R.layout.item_zidian);
                    gvShuoxue.setAdapter(shuoxueAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });


    }

    //英语
    public void initEnglish() {

        RequestParams params = new RequestParams();
        params.put("cid", "10");
        params.put("flag", "1");
        params.put("limit", "4");

        HttpRequest.getClasses(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());

                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        Classes item = new Classes();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setTitle(listArray.getJSONObject(i).getString("title"));
                        item.setThumbnail(listArray.getJSONObject(i).getString("thumbnail"));
                        englishDb.add(item);
                    }

                    englishAdapter = new ClassesAdapter(mContext, englishDb, R.layout.item_english);
                    gvEnglish.setAdapter(englishAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void onClickMingShi(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_名师英语");
        startActivity(intent);
    }

    public void onClickTong(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_同近反义词典");
        startActivity(intent);
    }

    public void onClickQuan(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_小学生全功能");
        startActivity(intent);
    }

    public void onClickYoudao(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_小学生全功能");
        startActivity(intent);
    }

    public void onClickZhonghua(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_中华成语词典");
        startActivity(intent);
    }

    public void onClickGuhan(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_古汉语词典");
        startActivity(intent);
    }


    public void onClickYinghan(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_英汉大词典");
        startActivity(intent);
    }

    public void onClickGuoxue(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_国学经典");
        startActivity(intent);
    }

    public void onClickZuowen(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_作文指导");
        startActivity(intent);
    }

    public void onClickChengyu(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_成语典故");
        startActivity(intent);
    }
    public void onClickYingbiao(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_音标学习");
        startActivity(intent);
    }
    public void onClickHanzi(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_汉字学习");
        startActivity(intent);
    }
    //小学_拼音学习
    public void onClickPingyin(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_拼音学习");
        startActivity(intent);
    }

    public void onClickTeacher(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_名师语文");
        startActivity(intent);
    }

    public void onClickTiku(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_精选题库");
        startActivity(intent);
    }

    public void onClickRead(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_课外阅读");
        startActivity(intent);
    }

    public void onClickCihui(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_分类词汇");
        startActivity(intent);
    }

    public void onClickEnglish(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_点读英语");
        startActivity(intent);
    }
    //小学_速算闯关
    public void onClickSuan(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_速算闯关");
        startActivity(intent);
    }
    //小学_阶梯应用题
    public void onClickJie(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_阶梯应用题");
        startActivity(intent);
    }
    //小学_几何图形
    public void onClickJihe(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_几何图形");
        startActivity(intent);
    }
    //小学_奥数课堂
    public void onClickAoshu(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.wyt.classxiaoyou", "com.wyt.classxiaoyou.activity.MainActivity");
        intent.putExtra("tag","小学_奥数课堂");
        startActivity(intent);
    }


}
