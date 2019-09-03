package cn.lanru.lrapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import cn.lanru.lrapplication.BaseActivity;
import cn.lanru.lrapplication.MainActivity;
import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.NewList;
import cn.lanru.lrapplication.net.HttpRequest;
import cn.lanru.lrapplication.net.OkHttpException;
import cn.lanru.lrapplication.net.RequestParams;
import cn.lanru.lrapplication.net.ResponseCallback;

public class NewShowActivity extends BaseActivity {

    private int id = 0;
    private int pid = 0;

    private TextView tvTitle;
    private TextView tvBody;
    private Spanned spanned;
    private TextView tvHits;
    private TextView tvDig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        try {
            id = bundle.getInt("id");
        } catch (Exception e) {}

        if (id <= 0) {
            Toast.makeText(getApplicationContext(), "新闻不存在", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_new_show);

        tvTitle = findViewById(R.id.title);
        tvBody = findViewById(R.id.body);
        tvHits = findViewById(R.id.tvHits);
        tvDig = findViewById(R.id.tvDig);
        iniData();
    }

    public void iniData () {
        RequestParams params = new RequestParams();
        Log.e("data_id=", Integer.toString(id));
        params.put("id", Integer.toString(id));
        HttpRequest.getNewShow(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject((String) responseObj);
                    JSONObject data = new JSONObject(jsonObject.getString("data"));
                    spanned = Html.fromHtml(data.getString("body"));
                    tvTitle.setText(data.getString("title"));
                    tvBody.setText(spanned);
                    tvHits.setText("阅读" + data.getString("hits"));
                    tvDig.setText(data.getString("dig"));

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Toast.makeText(getApplicationContext(), failuer.getEmsg(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void onNews(View view) {
        Intent intent = new Intent(getApplicationContext(), NewsActivity.class);

        Bundle bundle=new Bundle();
        bundle.putString("pid", String.valueOf(pid));
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void onClickDig(View view) {
        RequestParams params = new RequestParams();
        Log.e("data_id=", Integer.toString(id));
        params.put("id", Integer.toString(id));
        HttpRequest.getNewDig(params, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject jsonObject = new JSONObject(responseObj.toString());
                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    tvDig.setText(jsonObject.getString("data"));
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
}
