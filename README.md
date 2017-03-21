#####先看效果图

![piechart.png](http://upload-images.jianshu.io/upload_images/3523210-2e2bf33dc7323e3e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![pieview1.gif](http://upload-images.jianshu.io/upload_images/3523210-accfa83622254be6.gif?imageMogr2/auto-orient/strip)

![piechart.gif](http://upload-images.jianshu.io/upload_images/3523210-9ff9fdf320514e37.gif?imageMogr2/auto-orient/strip)

####使用方法：
####AndroidStudio引入（https://jitpack.io/）
#####step1:Add it in your root build.gradle at the end of repositories:
````
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
````
#####step2:Add the dependency
````
dependencies {
	        compile 'com.github.ithedan:HeDan_Piechart:v2.0'
	}
````
#####布局
````
 <com.hedan.piechart_library.PieChart_View
        android:id="@+id/pie_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:padding="50dp"
        app:isAnimation="true"
        app:isTouchFlag="true"
        app:animaDuration="5000"
        app:mRadius="140dp"
        app:nameColor="#f00"
        app:name="清明时节雨纷"
        app:nameSize="16sp"
        app:nameOrientation="horizontal"
        app:pieChartWidth="70dp"
        app:alphaWidth="10dp"
        app:alphaPieColor="#fff"
        app:alphaPieTran="100"
        app:segmentAngle="0.5"
        app:startAngle="180"
        app:textColor="#000"
        app:textSize="18sp"
        app:textOrientation="path"
        app:inCricleColor="#eeeeee"
        />
````
####Activity中使用：
````
 pieView = (PieChart_View) findViewById(R.id.pie_view);
        ArrayList<PieChartBean> lists = new ArrayList<>();
        lists.add(new PieChartBean(Color.parseColor("#ee3c5d"), 300, "苹果"));
        lists.add(new PieChartBean(Color.parseColor("#ffc12c"), 50, "香蕉"));
        lists.add(new PieChartBean(Color.parseColor("#1fde94"), 599, "哈密瓜"));
        lists.add(new PieChartBean(Color.parseColor("#f5a623"), 500, "橘子"));
        lists.add(new PieChartBean(Color.parseColor("#fa734e"), 1000, "桃子"));
        lists.add(new PieChartBean(Color.parseColor("#947ddf"), 300, "葡萄"));
        lists.add(new PieChartBean(Color.parseColor("#ee3c5d"), 50, "火龙果"));
        lists.add(new PieChartBean(Color.parseColor("#f7964f"), 500, "芒果"));
        lists.add(new PieChartBean(Color.parseColor("#ff4639"), 400, "西红柿"));
        lists.add(new PieChartBean(Color.parseColor("#ff8053"), 300, "柠檬"));
        lists.add(new PieChartBean(Color.parseColor("#ee3c5d"), 5, "樱桃"));
        lists.add(new PieChartBean(Color.parseColor("#1fde94"), 500, "西瓜"));
        pieView.setData(lists);
````
######如果不需要文字 lists.add(new PieChartBean(Color.parseColor("#ee3c5d"), 300));这样不会显示文字
####先看看自定义属性attrs.xml
````
  <declare-styleable name="PieChart_View">
        <attr name="name" format="string"/>
        <attr name="nameSize" format="dimension"/>
        <attr name="nameOrientation" >
            <enum name="horizontal" value="0"/>
            <enum name="vertical" value="1"/>
        </attr>
        <attr name="nameColor" format="color"/>
        <attr name="textOrientation">
            <enum name="horizontal" value="0"/>
            <enum name="path" value="2"/>
        </attr>
        <attr name="textSize" format="dimension"/>
        <attr name="textColor" format="color"/>
        <attr name="pieChartWidth" format="dimension"/>
        <attr name="mRadius" format="dimension"/>
        <attr name="alphaWidth" format="dimension"/>
        <attr name="alphaPieTran" format="integer"/>
        <attr name="alphaPieColor" format="color"/>
        <attr name="inCricleColor" format="color"/>  
        <attr name="isAnimation" format="boolean"/>
        <attr name="animaDuration" format="integer"/>
        <attr name="isTouchFlag" format="boolean"/>
        <attr name="startAngle" format="integer"/>
        <attr name="segmentAngle" format="float"/>
    </declare-styleable>
````

|属性|说明|
|----|----|
|name|中间文字|
|nameSize|中间文字大小dp（默认16dp）|
|nameOrientation|中间文字方向（水平or垂直，默认水平）|
|nameColor|中间文字颜色（默认黑色）|
|textOrientation|内容文字方向（水平or圆弧方向path，默认圆弧方向）|
|textSize|内容文字大小dp（默认16d'p）|
|textColor|内容文字颜色（默认黑色）|
|pieChartWidth|圆饼宽度（默认是半径的一半）|
|mRadius|最大圆半径（默认是View 宽高最小值的一半）|
|alphaWidth|中间透明圆宽度（默认是圆饼宽度的0.1）|
|alphaPieTran|中间透明圆透明度（0--255）（默认80）|
|alphaPieColor|中间透明圆颜色（默认白色）|
|inCricleColor|内圆颜色（默认透明）|
|isAnimation|是否开启动画（默认开启true）|
|animaDuration|动画持续时间（默认2000）|
|isTouchFlag|是否开启触摸切割效果（默认开启true）|
|startAngle|圆饼起始角度（默认0）|
|segmentAngle|圆饼分割角度（0-0.9）（默认0）|

#####主要方法
1、onMeasure  （这个方法，只要你以前写过自定义View,基本上就是一样的套路:）
````
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
````
2、onSizeChanged（这个方法主要确定一些相关的大小）
````
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
````
3、动画
````
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
````
4、onDraw
````
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
````
5、 绘制饼图
````
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
````
6、触摸分割
 ````
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

````
7、绘制文本
````
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
````
####以上为主要方法和计算
####如有什么问题，敬请提出，十分感谢！希望越来越好，谢谢！如果喜欢，还请点击start，喜欢支持一下了，谢谢O(∩_∩)O~
