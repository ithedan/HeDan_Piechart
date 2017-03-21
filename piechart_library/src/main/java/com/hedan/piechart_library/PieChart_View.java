package com.hedan.piechart_library;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by DengXiao on 2017/3/13.
 */

public class PieChart_View extends View {

    private ArrayList<PieChartBean> mDatas = new ArrayList<>();
    private String pieName;//圆饼图名称
    private int nameSize;//圆饼图名称文字大小
    private int nameColor;//圆饼图名称颜色
    private int nameOrientation;//圆饼图文字的方向
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int PiePath = 2;

    private int textSize;//文字大小
    private int textColor;//文字颜色
    private int textOrientation;//文字方向


    private int mRadius;//边缘半径
    private float pieChartWidth;//圆饼宽度
    private float alphaWidth;//透明圆宽度
    private int alphaPieColor;//透明圆颜色
    private int alphaPieTran;//透明圆透明度
    private int inCricleColor;//内圆颜色

    private Paint arcPaint;//圆饼画笔
    private Paint textPaint;//文字画笔\
    private Paint inCriclePaint;//内圆画笔
    private Paint alphaPait;//透明园画笔

    private int centerX, centerY;//中心坐标
    private int mWidth, mHeight;//宽高

    private RectF outRecF;//外圆边缘区域
    private RectF inRecF;//内圆边缘区域
    private RectF alphaRecf;//透明圆边缘区域
    private RectF outTextRecF;//外边文字边缘区域
    private RectF touchOutRecF;//触摸后边缘区域
    private RectF touchAlphaRecF;//触摸后边缘区域
    private RectF touchOutTextRecF;//触摸后边缘区域

    private boolean isAnimation;//是否开启动画
    private boolean isTouchFlag;//是否开启点击时间
    private float[] pieAngles;//用于存放叠加角度
    private int angleTag = -1;//叠加角度tag
    private TimeInterpolator timeInterpolator = new AccelerateDecelerateInterpolator();//动画方法


    private long animaDuration = 2000;//默认动画时间
    private float animatedValue;
    private float drawStartAngle;//起始绘制的角度
    private float segmentAngle;//分割角度（0.1--0.9）
    private float outRadius;//圆饼中心
    private float textPaintHeight;//文字高度
    private float inRadius;//内圆半径

    private float sumPercent = 0;
    private float sumValues = 0;
    private float minAnge = 1;
    private Path textPath = new Path();


    public PieChart_View(Context context) {
        this(context, null);
    }

    public PieChart_View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAtrrs(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 自定义属性动画
     */
    private void initAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 360);
        anim.setDuration(animaDuration);
        anim.setInterpolator(timeInterpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
    }

    /**
     * 获取自定义属性
     */
    private void initAtrrs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PieChart_View, defStyleAttr, 0);
        pieName = array.getString(R.styleable.PieChart_View_name);
        nameSize = array.getDimensionPixelOffset(R.styleable.PieChart_View_nameSize, dp2px(16));
        nameColor = array.getColor(R.styleable.PieChart_View_nameColor, Color.BLACK);
        nameOrientation = array.getInt(R.styleable.PieChart_View_nameOrientation, HORIZONTAL);
        textSize = array.getDimensionPixelOffset(R.styleable.PieChart_View_textSize, dp2px(16));
        textColor = array.getColor(R.styleable.PieChart_View_textColor, Color.BLACK);
        textOrientation = array.getInt(R.styleable.PieChart_View_textOrientation, PiePath);
        pieChartWidth = array.getDimensionPixelOffset(R.styleable.PieChart_View_pieChartWidth, 0);
        mRadius = array.getDimensionPixelOffset(R.styleable.PieChart_View_mRadius, 0);
        alphaWidth = array.getDimensionPixelOffset(R.styleable.PieChart_View_alphaWidth, 0);
        alphaPieColor = array.getColor(R.styleable.PieChart_View_alphaPieColor, Color.WHITE);
        alphaPieTran = array.getInteger(R.styleable.PieChart_View_alphaPieTran, 80);
        inCricleColor = array.getColor(R.styleable.PieChart_View_inCricleColor, Color.TRANSPARENT);
        isAnimation = array.getBoolean(R.styleable.PieChart_View_isAnimation, true);
        isTouchFlag = array.getBoolean(R.styleable.PieChart_View_isTouchFlag, true);
        drawStartAngle = array.getInteger(R.styleable.PieChart_View_startAngle, 0);
        segmentAngle = array.getFloat(R.styleable.PieChart_View_segmentAngle, 0);
        animaDuration = (long)array.getInteger(R.styleable.PieChart_View_animaDuration, 2000);
        array.recycle();
    }

    /**
     * 初始化一些方法
     */
    private void initView() {
        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setAntiAlias(true);

        alphaPait = new Paint();
        alphaPait.setStyle(Paint.Style.STROKE);
        alphaPait.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);

        inCriclePaint = new Paint();
        inCriclePaint.setAntiAlias(true);
        inCriclePaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        if (wideMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            width = wideSize;
        } else {
            width = mRadius == 0 ? getWidth() : mRadius * 2 + getPaddingLeft() + getPaddingRight();
            if (wideMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, wideSize);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            height = heightSize;
        } else {
            height = mRadius == 0 ? getHeight() : mRadius * 2 + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width == 0 ? height : width, height == 0 ? width : height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = (int) (Math.min(w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingTop() - getPaddingBottom()) * 1.0f / 2);
        centerX = (w + getPaddingLeft() - getPaddingRight()) / 2;
        centerY = (h + getPaddingTop() - getPaddingBottom()) / 2;
        if (pieChartWidth == 0) {
            pieChartWidth = mRadius * 0.5f;
        }
        if (alphaWidth == 0) {
            alphaWidth = pieChartWidth * 0.1f;
        }
        outRadius = mRadius - pieChartWidth / 2;
        outRecF = new RectF(-outRadius, -outRadius, outRadius, outRadius);
        touchOutRecF = new RectF(-outRadius - mRadius / 10, -outRadius - mRadius / 10, outRadius + mRadius / 10, outRadius + mRadius / 10);

        inRadius = mRadius - pieChartWidth;
        inRecF = new RectF(-inRadius, -inRadius, inRadius, inRadius);

        float alphaRadius = inRadius + alphaWidth / 2;
        alphaRecf = new RectF(-alphaRadius, -alphaRadius, alphaRadius, alphaRadius);
        touchAlphaRecF = new RectF(-alphaRadius - mRadius / 10, -alphaRadius - mRadius / 10, alphaRadius + mRadius / 10, alphaRadius + mRadius / 10);


        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textPaintHeight = (-fontMetrics.ascent - fontMetrics.descent) / 2;

        outTextRecF = new RectF(-mRadius - textPaintHeight, -mRadius - textPaintHeight, mRadius + textPaintHeight, mRadius + textPaintHeight);
        touchOutTextRecF = new RectF(-mRadius - textPaintHeight - mRadius / 10, -mRadius - textPaintHeight - mRadius / 10, mRadius + textPaintHeight + mRadius / 10, mRadius + textPaintHeight + mRadius / 10);

        if (isAnimation) {
            initAnimator();
        } else {
            animatedValue = 360;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);//一定画布原点到中间
        canvas.save();
        canvas.rotate(getStartangle(drawStartAngle));
        drawPieChart(canvas);
        drawText(canvas);
        canvas.restore();
        drawCenterText(TextUtils.isEmpty(pieName) ? "HeDan" : pieName, canvas, textPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouchFlag && mDatas.size() > 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float eventX = event.getX() - (mWidth / 2);
                    float eventY = event.getY() - (mHeight / 2);
                    float touchAngle = 0;
                    if (eventX < 0 && eventY < 0) {//180-270
                        touchAngle += 360;
                    } else if (eventX > 0 && eventY < 0) {//270-360
                        touchAngle += 360;
                    }
                    touchAngle += (float) Math.toDegrees(Math.atan2(eventY, eventX));
                    touchAngle = touchAngle - getStartangle(drawStartAngle);
                    if (touchAngle < 0) {
                        touchAngle = touchAngle + 360;
                    }
                    float touchRadius = (float) Math.sqrt(eventX * eventX + eventY * eventY);
                    if (touchRadius < mRadius && touchRadius > inRadius) {
                        angleTag = -Arrays.binarySearch(pieAngles, touchAngle) - 1;
                        invalidate();
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    angleTag = -1;
                    invalidate();
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 绘制饼图
     */
    private void drawPieChart(Canvas canvas) {
        if (mDatas.size() == 0) {
            return;
        }
        float startAngle = 0;
        float sumPercentage = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            PieChartBean ben = mDatas.get(i);
            arcPaint.setColor(ben.getPieColor());
            arcPaint.setStrokeWidth(pieChartWidth);

            alphaPait.setStrokeWidth(alphaWidth);
            alphaPait.setColor(alphaPieColor);
            alphaPait.setAlpha(getAlphaPieTran(alphaPieTran));

            float sweepAngle = ben.getPercentage();
            sumPercentage += sweepAngle;
            pieAngles[i] = sumPercentage;
            float drawAngle = animatedValue - startAngle;
            if (Math.min(sweepAngle, drawAngle) >= 0) {
                float sweepNewAngle = Math.min(sweepAngle, drawAngle) - segmentAngle;
                drawInCricle(canvas,startAngle, Math.min(sweepAngle, drawAngle),i);
                if (angleTag == i) {
                    canvas.drawArc(touchOutRecF, startAngle,  sweepNewAngle, false, arcPaint);
                    canvas.drawArc(touchAlphaRecF, startAngle,  sweepNewAngle, false, alphaPait);
                } else {
                    canvas.drawArc(outRecF, startAngle, sweepNewAngle, false, arcPaint);
                    canvas.drawArc(alphaRecf, startAngle, sweepNewAngle, false, alphaPait);
                }
            }
            startAngle += sweepAngle;
        }
    }

    /**
     * 绘制内圆
     */
    private void drawInCricle(Canvas canvas,float startAngle,float sweepAngle,int i) {
        if (inCricleColor == Color.TRANSPARENT) {
            return;
        }
        inCriclePaint.setColor(inCricleColor);
        if(angleTag == i){
            canvas.drawArc(touchAlphaRecF, startAngle, sweepAngle, true, inCriclePaint);
        }else{
            canvas.drawArc(inRecF, startAngle, sweepAngle, true, inCriclePaint);
        }
    }

    //绘制文本
    private void drawText(Canvas canvas) {
        if (mDatas.size() == 0) {
            return;
        }
        float startAngle = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            PieChartBean ben = mDatas.get(i);
            float sweepAngle = ben.getPercentage();
            if (isAnimation) {
                float textAngle = getTextAngle(ben.getPieString());
                float startNewAngle = (startAngle + sweepAngle / 2) - textAngle / 2;
                if (animatedValue >= startNewAngle) {
                    drawText(startAngle, ben.getPercentage(), canvas, ben.getPieString(), i);//绘制文本
                }
            } else {
                drawText(startAngle, ben.getPercentage(), canvas, ben.getPieString(), i);//绘制文本
            }
            startAngle += sweepAngle;
        }
    }

    //绘制中心名称
    private void drawCenterText(String text, Canvas canvas, Paint paint) {
        TextPaint textPaint=new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(nameColor);
        textPaint.setTextSize(nameSize);
        int width=(int)Math.sqrt(((2*inRadius)*(2*inRadius))/2);
        StaticLayout staticLayout=new StaticLayout(text,textPaint,width, Layout.Alignment.ALIGN_NORMAL,1.1f,0,false);
        if (animatedValue == 360) {
            if (nameOrientation == HORIZONTAL) {
                canvas.save();
                canvas.translate(0,-staticLayout.getHeight()/2);
                staticLayout.draw(canvas);
                canvas.restore();
            } else if (nameOrientation == VERTICAL) {
                drawVTextView(text, canvas, paint);
            }
        }
    }

    //绘制垂直方向文字
    private void drawVTextView(String texts, Canvas canvas, Paint paint) {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(nameColor);
        paint.setTextSize(nameSize);
        //每个字符居中
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top=fontMetrics.ascent;
        float bottom=fontMetrics.bottom;
        char[] chars1 = texts.toCharArray();
        int length=chars1.length;
        float total=(length-1)*(-top+bottom)+(-fontMetrics.ascent+fontMetrics.descent);
        float offset=total/2-bottom;
        for(int i=0;i<length;i++){
            float yAxis=-(length-i-1)*(-top+bottom)+offset;
            canvas.drawText(chars1[i]+"",0,yAxis,paint);
        }

    }


    //绘制文字
    private void drawText(float startAngle, float sweepAngle, Canvas canvas, String text, int i) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        if (textOrientation == PiePath) {
            float needAngle = getNeedAngle(sweepAngle, text);
            if (angleTag == i) {
                textPath.addArc(needAngle == sweepAngle ? touchOutRecF : touchOutTextRecF, needAngle == sweepAngle ? startAngle : (startAngle + sweepAngle / 2) - needAngle / 2, needAngle == sweepAngle ? sweepAngle : needAngle);
            } else {
                textPath.addArc(needAngle == sweepAngle ? outRecF : outTextRecF, needAngle == sweepAngle ? startAngle : (startAngle + sweepAngle / 2) - needAngle / 2, needAngle == sweepAngle ? sweepAngle : needAngle);
            }
            canvas.drawTextOnPath(text, textPath, 0, 0, textPaint);
            textPath.reset();
        } else if (textOrientation == HORIZONTAL) {
            float r = outRadius;
            float offsetY = textPaintHeight;
            if (getNeedAngle(sweepAngle, text) != sweepAngle) {
                r = mRadius;
                if (startAngle > 0 && startAngle < 90) {
                    textPaint.setTextAlign(Paint.Align.LEFT);
                    offsetY += offsetY;
                } else if (startAngle > 90 && startAngle < 180) {
                    textPaint.setTextAlign(Paint.Align.RIGHT);
                    offsetY += offsetY;
                } else if (startAngle > 180 && startAngle < 270) {
                    textPaint.setTextAlign(Paint.Align.RIGHT);
                    offsetY = 0;
                } else if (startAngle > 270 && startAngle < 360) {
                    textPaint.setTextAlign(Paint.Align.LEFT);
                    offsetY = 0;
                } else {
                    offsetY = textPaintHeight;
                }
            }
            if (angleTag == i) {
                r = r + mRadius / 10;
            }
            int textCX = (int) (Math.cos(Math.toRadians(startAngle + sweepAngle / 2)) * r);
            int textCY = (int) (Math.sin(Math.toRadians(startAngle + sweepAngle / 2)) * r);
            canvas.drawText(text, textCX, textCY + offsetY, textPaint);
        }


       /* float r = outRadius;
        float textWidth = textWidth(text, textPaint);
        if (arcLength(sweepAngle, outRadius) < textWidth) {
            r = mRadius;
        }
        int textCX = (int) (Math.cos(Math.toRadians(startAngle + sweepAngle / 2)) * r);
        int textCY = (int) (Math.sin(Math.toRadians(startAngle + sweepAngle / 2)) * r);
        canvas.drawText(text, textCX, textCY + textPaintHeight, textPaint);

        float textWidth = textWidth(text, textPaint);
        if (arcLength(sweepAngle, outRadius) < textWidth) {
            float angle = (float) (textWidth * 360 / (2 * 3.14 * outRadius));
            float offsetAngle = angle / 2;
            textPath.addArc(outTextRecF, (startAngle + sweepAngle / 2) - offsetAngle, angle);
        } else {
            textPath.addArc(outRecF, startAngle, sweepAngle);
        }*/

    }

    public void setData(ArrayList<PieChartBean> mDatas) {
        this.mDatas = mDatas;
        initData(mDatas);
        invalidate();
    }

    private void initData(ArrayList<PieChartBean> mDatas) {
        if (mDatas.size() == 0) {
            return;
        }
        pieAngles = new float[mDatas.size()];
        //计算总值和最大值
        float maxValues = 0;
        if (sumValues == 0) {
            for (PieChartBean ben : mDatas) {
                float pieValue = ben.getPieValue();
                sumValues += pieValue;
                maxValues = maxValues > ben.getPieValue() ? maxValues : ben.getPieValue();
            }
        }
        //分配角度
        for (PieChartBean ben : mDatas) {
            float pieValue = ben.getPieValue();
            float percent = (Math.round((pieValue / sumValues * 360)));
            if (percent <= minAnge - 1 && pieValue != 0) {
                percent = minAnge;
            }
            ben.setPercentage(percent);
            sumPercent += percent;
        }
        //闭合角度
        if (sumPercent != 360) {
            for (PieChartBean ben : mDatas) {
                if (ben.getPieValue() == maxValues) {
                    float offsetAngle = ben.getPercentage() - (sumPercent - 360);
                    ben.setPercentage(offsetAngle);
                }
            }
        }
    }

    //文字宽度
    private float textWidth(String string, Paint paint) {
        return paint.measureText(string + "");
    }

    //计算角度
    private float getNeedAngle(float sweepAngle, String text) {
        float angle = 0;
        float textWidth = textWidth(text, textPaint);
        if ((float) Math.toRadians(sweepAngle) * outRadius <= textWidth(text, textPaint)) {
            angle = (float) (textWidth * 360 / (2 * 3.14 * outRadius));
        } else {
            angle = sweepAngle;
        }
        return angle;
    }
    //文字角度
    private float getTextAngle(String text) {
        float textWidth = textWidth(text, textPaint);
        return (float) (textWidth * 360 / (2 * 3.14 * outRadius));
    }

    private int getAlphaPieTran(int alpha) {
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 255) {
            alpha = 255;
        }
        return alpha;
    }
    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }
    /**
     * 设置是否开启动画，默认开启
     */
    public PieChart_View setIsAnimated(boolean isAnimation) {
        this.isAnimation = isAnimation;
        return this;
    }

    /**
     * 设置是否开启触摸点击 默认开启
     */
    public PieChart_View setIsTouchFlag(boolean isTouchFlag) {
        this.isTouchFlag = isTouchFlag;
        return this;
    }

    /**
     * 设置动画类型
     */
    public PieChart_View setTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.timeInterpolator = timeInterpolator;
        return this;
    }

    /**
     * 设置中间文字颜色
     */
    public PieChart_View setNameColor(int color) {
        this.nameColor = color;
        return this;
    }

    /**
     * 设置中间文字大小
     */
    public PieChart_View setNameSize(int size) {
        this.nameSize = dp2px(size);
        return this;
    }

    /**
     * 设置中间文字方向默认水平方向
     */
    public PieChart_View setNameOrientation(int orientation) {
        this.nameOrientation = orientation;
        return this;
    }

    /**
     * 设置内容文字颜色
     */
    public PieChart_View setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    /**
     * 设置内容文字大小
     */
    public PieChart_View setTextSize(int size) {
        this.textSize = dp2px(size);
        return this;
    }

    /**
     * 设置内容文字方向
     */
    public PieChart_View setTextOrientation(int orientation) {
        this.textOrientation = orientation;

        return this;
    }

    /**
     * 设置起始角度
     */
    public PieChart_View setStartAngle(float startAngle) {
        this.drawStartAngle = getStartangle(startAngle);
        ;
        return this;
    }

    private float getStartangle(float startAngle) {
        if (startAngle < 0) {
            startAngle = startAngle + 360;
        }
        if (startAngle > 360) {
            startAngle = startAngle - 360;
        }
        return startAngle;
    }

    /**
     * 动画
     */
    public PieChart_View startAnimator() {
        initAnimator();
        return this;
    }


}
