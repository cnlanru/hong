package cn.lanru.lrapplication.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import java.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airsaid.pickerviewlibrary.CityPickerView;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.MainActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.School;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PersonalActivity extends BaseActivity {


    private Context mContext;
    private String birthday;
    private String choosing_grade;
    private TimePickerView pvTime;

    private List<School> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    private EditText etMobile;
    private TextView tv_choosing_grade;
    private TextView tv_tvBirthday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mContext = getApplicationContext();

        etMobile = findViewById(R.id.etMobile);
        tv_choosing_grade = findViewById(R.id.choosing_grade);
        tv_tvBirthday = findViewById(R.id.tvBirthday);

        initData();
    }

    public ArrayList<School> parseData(String result) {//Gson 解析
        ArrayList<School> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                School entity = gson.fromJson(data.optJSONObject(i).toString(), School.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public void initData() {

        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(mContext, "token", ""));
        params.put("key", "update");
        HttpRequest.getUserInfo(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject((String) responseObj);
                    JSONObject data = new JSONObject(jsonObject.getString("data"));

                    etMobile.setText(data.getString("recommend_mobile"));
                    tv_choosing_grade.setText(data.getString("level"));
                    tv_tvBirthday.setText(data.getString("birthday"));

                } catch (Exception ex) {}
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });


        HttpRequest.getChoosingGrade(null, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {

                    JSONObject mJsonObj = new JSONObject((String) responseObj);
                    ArrayList<School> jsonBean = parseData(mJsonObj.getString("data"));
                    options1Items = jsonBean;

                    for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
                        ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                        ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                        for (int c = 0; c < jsonBean.get(i).getSon().size(); c++) {//遍历该省份的所有城市
                            String cityName = jsonBean.get(i).getSon().get(c).getName();
                            cityList.add(cityName);//添加城市
                            //ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                            //city_AreaList.addAll(jsonBean.get(i).getSon().get(c).getSon());
                            //province_AreaList.add(city_AreaList);//添加该省所有地区数据
                        }
                        if (cityList.size() == 0) {
                            cityList.add("无");
                        }
                        Log.e("cityList", String.valueOf(cityList));
                        options2Items.add(cityList);

                        /**
                         * 添加地区数据
                         */
                        //options3Items.add(province_AreaList);
                    }



                } catch (Exception e) {}
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }


    public void close (View view) {

        Intent intent = new Intent(PersonalActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void onClickChoosing(View view) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                /*String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";*/
                String opt3tx = "";

                        String tx = opt1tx + opt2tx + opt3tx;

                choosing_grade = opt1tx + "-" + opt2tx;
                TextView tv_choosing_grade = findViewById(R.id.choosing_grade);
                tv_choosing_grade.setText(choosing_grade);

                //Toast.makeText(mContext, tx, Toast.LENGTH_SHORT).show();
            }
        })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("选择")//确认按钮文字
                .setTitleText("选择年级")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        Log.e("options1Items", String.valueOf(options1Items));
        Log.e("options1Items", String.valueOf(options2Items));
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器*/
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }

    //生日
    public void showDatePickerDialog(View view) {
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                TextView tvBirthday = findViewById(R.id.tvBirthday);
                birthday = getTime(date);
                tvBirthday.setText(birthday);
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("选择")//确认按钮文字
                .setTitleText("选择生日")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .build();
        pvTime.show(view);


    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Log.e("format", format.format(date));
        return format.format(date);
    }

    public void onClickSubmit(View view) {
        if (birthday != null && birthday.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填入选择生日", Toast.LENGTH_SHORT).show();
        } else if (choosing_grade != null && choosing_grade.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填入选择年级", Toast.LENGTH_SHORT).show();
        } else {
            EditText etMobile = findViewById(R.id.etMobile);
            RequestParams params = new RequestParams();
            params.put("birthday", birthday);
            params.put("choosing_grade", choosing_grade);
            params.put("mobile", etMobile.getText() + "");
            params.put("token", SharedHelper.getString(mContext, "token", ""));
            HttpRequest.postUserInfo(params, new ResponseCallback() {
                @Override
                public void onSuccess(Object responseObj) {
                    try {
                        JSONObject mJosn = new JSONObject((String) responseObj);
                        Toast.makeText(getApplicationContext(), mJosn.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(PersonalActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(OkHttpException failuer) {

                }
            });

        }
    }

}
