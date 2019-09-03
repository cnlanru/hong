package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.Grade;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class GradeActivity extends BaseActivity {

    private MyAdapt myAdapt;
    private ListView lv;
    private ArrayList<Grade> lists = new ArrayList<Grade>();
    private int gradeId;
    private int gradeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        lv = findViewById(R.id.category);

        init();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myAdapt.setCurrentItem(position);
                myAdapt.setClick(true);
                myAdapt.notifyDataSetChanged();
            }
        });
    }

    public void init() {
        HttpRequest.getGrade(null, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject resultJson=new JSONObject(responseObj.toString());
                    if (resultJson.getInt("code") == 1) {
                        JSONArray listArray = resultJson.getJSONArray("data");
                        for(int i =0; i < listArray.length(); i++) {
                            Grade g = new Grade();
                            g.setId(listArray.getJSONObject(i).getInt("id"));
                            g.setName(listArray.getJSONObject(i).getString("name"));
                            lists.add(g);
                        }
                        myAdapt = new MyAdapt((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        lv.setAdapter(myAdapt);

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

    //跳过
    public void skin(View view) {
        Intent intent = new Intent(getApplicationContext(), ClassesActivity.class);
        startActivity(intent);
    }

    //选择年级
    public void btnSubmit(View view) {
        
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(getApplicationContext(), "token", ""));
        params.put("level", gradeId + "");
        HttpRequest.postLevel(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject((String) responseObj);
                    if (jsonObject.getInt("code") == 1){
                        Intent intent = new Intent(getApplicationContext(), ClassesActivity.class);
                        intent.putExtra("iid", gradeIndex);
                        startActivity(intent);
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

    public class MyAdapt extends BaseAdapter {

        private LayoutInflater inflater;
        private int mCurrentItem;
        private boolean isClick = false;

        public MyAdapt(LayoutInflater inflater) {
            this.inflater = inflater;
        }
        public class ViewHolder{
            private LinearLayout listLayout;
            TextView tvName;
            TextView tvId;

            private ViewHolder(View view) {
                listLayout = (LinearLayout) view.findViewById(R.id.list);
                tvName = view.findViewById(R.id.name);
            }
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = inflater.inflate(R.layout.grade_item,null);
                viewHolder = new ViewHolder(convertView);
                viewHolder.tvName = convertView.findViewById(R.id.name);
                viewHolder.tvId = convertView.findViewById(R.id.id);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Grade lt = lists.get(position);
            viewHolder.tvName.setText(lt.getName());
            viewHolder.tvId.setText(lt.getId() + "");

            if (mCurrentItem == position){
                viewHolder.listLayout.setBackgroundColor(Color.BLUE);
                viewHolder.tvName.setTextColor(Color.WHITE);
                gradeId = lt.getId();
                gradeIndex = position;
            } else {
                viewHolder.listLayout.setBackgroundColor(Color.WHITE);
                viewHolder.tvName.setTextColor(Color.BLACK);
            }

            return convertView;
        }

        public void setCurrentItem(int currentItem) {
            this.mCurrentItem = currentItem;
        }

        public void setClick(boolean click) {
            this.isClick = click;
        }
    }

}
