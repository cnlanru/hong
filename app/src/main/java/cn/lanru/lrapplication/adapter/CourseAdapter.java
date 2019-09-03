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
import cn.lanru.lrapplication.bean.Classes;
import cn.lanru.lrapplication.bean.Course;

//课件分类
public class CourseAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Course> mCourse;
    private int mLayoutId;

    public class ViewHolder{
        ImageView textThumbnail;
    }

    public CourseAdapter(Context mContext, List<Course> mCourse, int layoutId) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mCourse = mCourse;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mCourse.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourse.get(position);
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
            viewHolder.textThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(convertView)
                .load(mCourse.get(position).getThumbnail()).placeholder(new ColorDrawable(Color.DKGRAY))
                .error(new ColorDrawable(Color.DKGRAY))
                .into((ImageView) convertView.findViewById(R.id.ivThumbnail));

        return convertView;
    }
}
