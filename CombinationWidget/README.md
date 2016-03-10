# Android组合控件
组合控件，顾名思义，多个控件组合成一个控件使用。比如，我们想要这样一个ImageView，图片的底部覆盖一个浮层，浮层上面显示一行文字，这个控件我们可以用TextView覆盖在ImageView之上实现，我们把这个控件命名为“CoverImageView”吧。

## 怎样组合
既然是组合，那么就需要一个容器把这些分散的控件装在一起，这个容器就是ViewGroup，如：LinearLayout、RelativeLayout等，所以组合控件都是继承于一个ViewGroup的。

组装的方式有两种：

1. xml文件里定义好控件效果，然后通过LayoutInflater布局到ViewGroup里面；
2. 直接在ViewGroup里面通过代码添加子控件，实现效果。  

第一种方式能够比较直观的看到组合效果，但是由于需要解析xml，所以性能上稍微差点；第二种并不能直观的看到效果，但是性能稍好。对于新手来说，可以先用第一种方式实现，等到对组合控件的实现比较熟悉后可以使用第二种方式实现。

## 基本效果实现
能实现覆盖效果的ViewGroup有RelativeLayout、FrameLayout，我们以FrameLayout为例。

xml布局如下：

``` xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <!--图片占据整个控件大小-->
    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center"
        android:scaleType="centerCrop"
        android:src="@drawable/demo"/>

    <!--文字与控件等宽、在整个控件的底部-->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:background="#64000000"
        android:gravity="center"
        android:padding="6dp"
        android:text="@string/laomengzhu"
        android:textColor="#ffffff"/>
</FrameLayout>
```

效果如下：

![base_preview](https://raw.githubusercontent.com/laomengzhu/CustomWidget/master/CombinationWidget/CoverImageView/images/base_preview.jpg)

xml已经定义好了，我们把它布局到我们的ViewGroup里面去。我们的控件“CoverImageView”继承于FrameLayout，我们实现一个“setupViews”函数，用来初始化View，并在所有构造函数里面调用这个函数。

```java
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
        View.inflate(context, R.layout.layout_civ, this);
    }
}
```

现在我们可以把这个控件放到我们的Activity里面看看效果了：

![preview_in_activity1](https://raw.githubusercontent.com/laomengzhu/CustomWidget/master/CombinationWidget/CoverImageView/images/preview_in_activity1.jpg)

## 布局优化
前面我们已经看到我们想要的效果了，但是我们在xml里面的根布局是FrameLayout，我们的CoverImageView也是继承于FrameLayout，那我们最终实现的效果会不会套了两层FrameLayout呢？

我们可以使用ADT里面的Hierarchyviewer工具查看布局层级关系，使用方法也很简单：

1. 在模拟器上运行你的程序（真机上我试了下不行）；
2. 打开Android Device Monitor，选中你程序的进程；
3. 点击右上角的树形图标，过一会我们就能看到我们当前页面的层级关系了。

我们当前页面的部分层级关系如下图：

![preview_in_activity1](https://raw.githubusercontent.com/laomengzhu/CustomWidget/master/CombinationWidget/CoverImageView/images/layers.jpg)

可以看到CoverImage下面的确还有一层FrameLayout，这一层FrameLayout是毫无用处的，这样的层级关系就会导致过度绘制问题（不懂的可以百度一下，这里就不赘述了），影响我们的控件性能。

减少视图层级，我们可以使用merge标签，它可以删减多余的层级，优化UI。我们把我们xml布局里面的FrameLayout换成merge，然后运行，看看视图层级关系。

![preview_in_activity1](https://raw.githubusercontent.com/laomengzhu/CustomWidget/master/CombinationWidget/CoverImageView/images/new_layers.jpg)

层级减少了！！！

关于布局优化，大家可以看看这个博客：[http://blog.csdn.net/xyz_lmn/article/details/14524567](http://blog.csdn.net/xyz_lmn/article/details/14524567)

## 通过代码添加控件
前面讲到，组装控件的另一个方式就是通过代码添加控件，原理也很简单，就是在我们的ViewGroup里面直接创建子控件、然后添加到我们的ViewGroup里，调整子控件的布局，达到我们想要的效果。由于少了xml解析这一步（虽然你没有解析，但是系统需要解析的），所以性能是优于第一种的。

```java
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
}
```

效果和上面完全一致。

## 自定义属性
前面我们实现了我们想要的效果，但是我们无法修改控件里面的图片、文字大小，我们需要定义控件的这些属性，当然你还可以定义文字颜色、文字背景色等属性，我们这里只挑图片、文字大小以作演示。

1. 在res/values目录下新建attrs.xml文件；
2. 在attrs.xml里面定义控件属性；
3. 在布局中使用属性

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="CoverImageView">
        <!--name：属性名称 format：属性格式-->
        <!--属性支持的格式有：
        1. reference：参考某一资源ID。
        2. color：颜色值。
        3. boolean：布尔值。
        4. dimension：尺寸值。
        5. float：浮点值。
        6. integer：整型值。
        7. string：字符串。
        8. fraction：百分数。
        9. enum：枚举值。
        10. flag：位或运算。-->
        <attr name="imgSrc" format="reference"/>
        <attr name="coverTextSize" format="dimension|reference"/>
    </declare-styleable>
</resources>
```

然后我们需要在控件中解析属性：

```java
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
```

使用属性：

```xml
<com.laomengzhu.civ.CoverImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:id="@+id/view"
    app:imgSrc="@drawable/demo1"
    app:coverTextSize="18sp"
    android:layout_centerInParent="true"/>
```

自定义控件的属性默认是在xmlns:app="http://schemas.android.com/apk/res-auto"命名空间下的。

到此，我们自定义组合控件就完成了，最终效果如下图：

![preview_in_activity1](https://raw.githubusercontent.com/laomengzhu/CustomWidget/master/CombinationWidget/CoverImageView/images/last.jpg)

