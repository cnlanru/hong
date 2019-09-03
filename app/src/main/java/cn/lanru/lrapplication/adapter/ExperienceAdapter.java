package cn.lanru.lrapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.lanru.lrapplication.R;
import cn.lanru.lrapplication.bean.Course;
import cn.lanru.lrapplication.bean.Experience;

//体验课
public class ExperienceAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Experience> mDatas;
    private int mLayoutId;

    public class ViewHolder{
        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvTips;
    }

    public ExperienceAdapter(Context mContext, List<Experience> mDatas, int layoutId) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mDatas = mDatas;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(mLayoutId, null, true);
            viewHolder = new ViewHolder();
            viewHolder.ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvTips = convertView.findViewById(R.id.tvTips);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(mDatas.get(position).getTitle());
        viewHolder.tvTips.setText(mDatas.get(position).getNumber() + "+人正在学习");

        Glide.with(convertView)
                .load(mDatas.get(position).getThumbnail()).placeholder(new ColorDrawable(Color.DKGRAY))
                .error(new ColorDrawable(Color.DKGRAY))
                .into((ImageView) convertView.findViewById(R.id.ivThumbnail));

        return convertView;
    }
}
