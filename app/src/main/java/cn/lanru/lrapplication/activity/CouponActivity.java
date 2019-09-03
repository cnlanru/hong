package cn.lanru.lrapplication.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.adapter.CourseAdapter;
import cn.lanru.lrapplication.bean.CardList;
import cn.lanru.lrapplication.bean.Coupon;
import cn.lanru.lrapplication.bean.NewList;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;
import cn.lanru.lrapplication.utils.SharedHelper;

public class CouponActivity extends BaseActivity {

    public Context mContext;
    private EditText etCode;

    private TextView noTxt;
    private TextView noLine;
    private TextView onTxt;
    private TextView onLine;
    private TextView oldTxt;
    private TextView oldLine;

    private int status = 0;

    private ListView lvCoupon;
    private List<Coupon> couponDb = new ArrayList<>();
    private CouponAdapter couponAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        mContext = getApplicationContext();

        etCode = findViewById(R.id.etCode);
        initView();
        initData();
    }

    public void back (View view) {
        this.finish();
    }

    public void btnSubmit(View view) {
        String code = etCode.getText().toString();
        if (code.trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "请输入兑换码", Toast.LENGTH_SHORT).show();
        }
    }

    public void initData() {
        RequestParams params = new RequestParams();
        params.put("token", SharedHelper.getString(mContext, "token", ""));
        params.put("status", status + "");
        HttpRequest.getUserExchange(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {

                try  {

                    JSONObject jsonObject = new JSONObject(responseObj.toString());

                    JSONArray listArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < listArray.length(); i++) {
                        Coupon item = new Coupon();
                        item.setId(listArray.getJSONObject(i).getInt("id"));
                        item.setTitle(listArray.getJSONObject(i).getString("title"));
                        item.setTips(listArray.getJSONObject(i).getString("type"));
                        item.setMoney(listArray.getJSONObject(i).getString("money"));
                        item.setMin_money(listArray.getJSONObject(i).getString("min_money"));
                        item.setTips(listArray.getJSONObject(i).getString("tips"));
                        item.setBegin_validity(listArray.getJSONObject(i).getString("begin_validity"));
                        item.setEnd_validity(listArray.getJSONObject(i).getString("end_validity"));
                        item.setStatus(listArray.getJSONObject(i).getString("status"));
                        item.setEndTime(listArray.getJSONObject(i).getString("EndTime"));
                        item.setTimes(listArray.getJSONObject(i).getString("Times"));
                        couponDb.add(item);
                    }

                    couponAdapter = new CouponAdapter(mContext, couponDb);
                    lvCoupon.setAdapter(couponAdapter);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(getApplicationContext(), failuer.getEmsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initView() {
        noTxt = findViewById(R.id.noTxt);
        noLine = findViewById(R.id.noLine);
        onTxt = findViewById(R.id.onTxt);
        onLine = findViewById(R.id.onLine);
        oldTxt = findViewById(R.id.oldTxt);
        oldLine = findViewById(R.id.oldLine);

        lvCoupon = findViewById(R.id.lvCoupon);

        noTxt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        noLine.setBackgroundColor(Color.parseColor("#007aff"));
    }

    public void onClickNo(View view) {
        noTxt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        noLine.setBackgroundColor(Color.parseColor("#007aff"));

        onTxt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        onLine.setBackgroundColor(Color.parseColor("#ffffff"));

        oldTxt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        oldLine.setBackgroundColor(Color.parseColor("#ffffff"));

        status = 0;
    }

    public void onClickOn(View view) {
        noTxt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        noLine.setBackgroundColor(Color.parseColor("#ffffff"));

        onTxt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        onLine.setBackgroundColor(Color.parseColor("#007aff"));

        oldTxt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        oldLine.setBackgroundColor(Color.parseColor("#ffffff"));

        status = 1;
    }

    public void onClickOld(View view) {

        noTxt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        noLine.setBackgroundColor(Color.parseColor("#ffffff"));

        onTxt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        onLine.setBackgroundColor(Color.parseColor("#ffffff"));

        oldTxt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        oldLine.setBackgroundColor(Color.parseColor("#007aff"));

        status = 2;

    }

    public class CouponAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<Coupon> mData;
        private int mCurrentItem;

        public CouponAdapter(Context context, List<Coupon> mData) {
            this.mContext = context;
            this.mData = mData;
            this.mInflater = LayoutInflater.from(this.mContext);
            this.mCurrentItem = 0;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_coupon, null, true);
                viewHolder = new ViewHolder();

                viewHolder.ivTitle = convertView.findViewById(R.id.ivTitle);
                viewHolder.ivPrice = convertView.findViewById(R.id.ivPrice);
                viewHolder.ivTips = convertView.findViewById(R.id.ivTips);
                viewHolder.ivType = convertView.findViewById(R.id.ivType);
                viewHolder.ivEndTime = convertView.findViewById(R.id.ivEndTime);
                viewHolder.ivTimes = convertView.findViewById(R.id.ivTimes);
                viewHolder.ivStatus = convertView.findViewById(R.id.ivStatus);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.ivTitle.setText(mData.get(position).getTitle());
            viewHolder.ivPrice.setText(mData.get(position).getMoney());
            viewHolder.ivTips.setText(mData.get(position).getTips());

            viewHolder.ivType.setText(mData.get(position).getType());
            viewHolder.ivEndTime.setText(mData.get(position).getEndTime());
            viewHolder.ivTimes.setText(mData.get(position).getTimes());

            if (mData.get(position).getStatus() == "0") {
                viewHolder.ivStatus.setImageResource(R.mipmap.coupon_r);
            } else {
                viewHolder.ivStatus.setImageResource(R.mipmap.coupon_o);
            }

            return convertView;
        }

        public void setCurrentItem(int currentItem) {
            this.mCurrentItem = currentItem;
        }

        public class ViewHolder{
            TextView ivTitle;
            TextView ivPrice;
            TextView ivTips;
            TextView ivType;
            TextView ivEndTime;
            TextView ivTimes;
            ImageView ivStatus;
        }


    }

}
