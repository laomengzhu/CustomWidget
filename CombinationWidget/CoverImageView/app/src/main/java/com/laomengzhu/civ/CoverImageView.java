package com.laomengzhu.civ;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by laomengzhu on 2016/3/10.
 * QQ: 1147904198
 * Email: laomengzhu@126.com
 */
public class CoverImageView extends FrameLayout {

    public CoverImageView(Context context) {
        super(context);
        setupViews(context, null);
    }

    public CoverImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupViews(context, attrs);
    }

    public CoverImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CoverImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupViews(context, attrs);
    }

    private void setupViews(Context context, AttributeSet attrs) {
        //将xml布局到ViewGroup里面来
        /*View.inflate(context, R.layout.layout_civ, this);*/

        //通过代码添加子控件
        LayoutParams lp;

        ImageView imageView = new ImageView(context);
        //设置缩放模式
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.demo);
        //设置子控件布局参数
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //将子控件加入ViewGroup里
        addView(imageView, lp);

        TextView textView = new TextView(context);
        //设置内边距
        int padding;
        if (isInEditMode()) {
            padding = 18;
        } else {
            padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
                    getResources().getDisplayMetrics());
        }
        textView.setPadding(padding, padding, padding, padding);
        textView.setBackgroundColor(Color.parseColor("#64000000"));
        textView.setTextColor(Color.WHITE);
        //文字居中
        textView.setGravity(Gravity.CENTER);
        textView.setText(R.string.laomengzhu);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        addView(textView, lp);

        //解析控件属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CoverImageView);

        //解析图片资源ID
        int resId = ta.getResourceId(R.styleable.CoverImageView_imgSrc, -1);
        if (resId != -1) {
            imageView.setImageResource(resId);
        }

        //解析文字大小
        resId = ta.getResourceId(R.styleable.CoverImageView_coverTextSize, -1);
        int textSize = 0;
        if (resId != -1) {
            if (!isInEditMode()) {
                textSize = getResources().getDimensionPixelSize(resId);
            }
        } else {
            textSize = ta.getDimensionPixelSize(R.styleable.CoverImageView_coverTextSize, 0);
        }
        if (textSize > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        //释放
        ta.recycle();
    }
}
