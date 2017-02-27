package surajit.com.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surajit Sarkar on 31/1/17.
 * Company : Bitcanny Technologies Pvt. Ltd.
 * Email   : surajit@bitcanny.com
 */

public class GraphViewLock extends View {

    private int width,graphWidth,axisWidth;
    private int height,graphHeight;
    private Paint paint,textPaint;
    private int textSize;
    private String[] timeTop = {"12","2","4","6","8","10","12","2","4","6","8","10","12"};
    private String[] timeBottom = {"AM","AM","AM","AM","AM","AM","PM","PM","PM","PM","PM","PM","AM"};
    private int paddingTop,paddingBottom,paddingLeft,paddingRight;
    private int barHeight;
    private int barWidth;
    private List<LockData> points;
    private int axisColor,colorLock,colorUnlock;

    public GraphViewLock(Context context) {
        super(context);
        initView();
    }

    public GraphViewLock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private int getDP(int dp){
        float pixel = (float)dp*getResources().getDisplayMetrics().density;
        return (int)pixel;
    }

    private int getSP(int sp){
        float pixel = (float)sp*getResources().getDisplayMetrics().scaledDensity;
        return (int)pixel;
    }

    private void initView(){

        axisWidth = getDP(3);
        paddingTop = getDP(0);
        paddingBottom = getDP(80);
        paddingLeft = getDP(10);
        paddingRight = getDP(10);

        points = new ArrayList<>();
        colorLock = Color.GREEN;
        colorUnlock = Color.RED;
        axisColor = Color.BLACK;

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(getSP(12));
        textSize = (int)textPaint.getTextSize();
    }

    /**
     *
     * @param minute 24 hour time in minutes
     * @param status locked/unlocked
     */

    public void addLock(int minute, String status){
        points.add(new LockData(minute,status));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateSize(canvas);
        if (isInEditMode()) {
            canvas.drawColor(Color.rgb(200, 200, 200));
            String text = "No Preview available";
            canvas.drawText(text, textPaint.measureText(text), 50, textPaint);
        } else {
            drawGraphElements(canvas);
        }
    }

    private void drawGraphElements(Canvas canvas) {
        drawAxis(canvas);
        drawBar(canvas);
    }

    private void drawBar(Canvas canvas) {
        for(LockData data : points){
            int x = getX(data.time);
            if(isLocked(data.mode)){
                drawLockBar(canvas,x);
            } else{
                drawUnlockBar(canvas,x);
            }
        }

        /*LockData data = new LockData(120,"unlocked");
        int x = getX(data.time);
        if(isLocked(data.mode)){
            drawLockBar(canvas,x);
        } else{
            drawUnlockBar(canvas,x);
        }*/
    }

    private void drawLockBar(Canvas canvas,int x){
        paint.setColor(colorLock);
        int y = height - graphHeight/2 - paddingBottom;
        Rect rect = getRect(x-barWidth/2,y,barWidth,barHeight);
        canvas.drawRect(rect,paint);
    }

    private void drawUnlockBar(Canvas canvas,int x){
        paint.setColor(colorUnlock);
        int y = height - graphHeight/2 - paddingBottom - barHeight;
        Rect rect = getRect(x-barWidth/2,y,barWidth,barHeight);
        canvas.drawRect(rect,paint);
    }

    private void drawAxis(Canvas canvas){
        paint.setColor(axisColor);
        Rect rect = getRect(paddingLeft,height - graphHeight/2 - paddingBottom,graphWidth,3);
        canvas.drawRect(rect,paint);
        float space = (float)graphWidth/(timeTop.length-1);
        float x = paddingLeft;
        int lineHeight = axisWidth+6;
        float y = height - graphHeight/2 - paddingBottom - lineHeight;
        int textPadding = getDP(5) + textSize;
        int txtHeight = height - paddingBottom;

        for(int i=0; i<timeTop.length; i++){
            Rect lineRect = getRect((int)x,(int)y,3,lineHeight);
            canvas.drawRect(lineRect, paint);

            String text1 = timeTop[i];
            String text2 = timeBottom[i];
            canvas.drawText(text1,x,txtHeight+textPadding,textPaint);
            canvas.drawText(text2,x,txtHeight+textPadding*2,textPaint);

            x+=space;
        }

        drawMarkings(canvas);
    }

    private void drawMarkings(Canvas canvas){
        int textPadding = getDP(5) + textSize;
        int txtHeight = height - paddingBottom;
        String text4 = "Locked";
        String text5 = "Unlocked";
        float text4Width = textPaint.measureText(text4);
        float text5Width = textPaint.measureText(text5);

        float totalTextWidth = text4Width + text5Width  + textSize *2 + getDP(20);
        float x1 = width/2 - totalTextWidth/2;
        float y1 = txtHeight+textPadding*4;
        paint.setColor(colorLock);
        canvas.drawCircle(x1,y1,textSize/2,paint);
        x1+=textSize;
        canvas.drawText(text4,x1+text4Width/2,y1+textSize/3,textPaint);
        paint.setColor(colorUnlock);
        x1+=text4Width+getDP(20);
        canvas.drawCircle(x1,y1,textSize/2,paint);
        x1+=textSize;
        canvas.drawText(text5,x1+text5Width/2,y1+textSize/3,textPaint);
    }

    private void calculateSize(Canvas canvas){
        width = canvas.getWidth();
        height = canvas.getHeight();
        graphWidth = width - paddingLeft - paddingRight;
        graphHeight = height - paddingTop - paddingBottom;
        barHeight = graphHeight/2 - getDP(10);
        barWidth = (graphWidth/12)/4;
    }

    private int getX(int val){
        int x = 0;
        double val1 = 0;
        if(val < 24*60){
            val1 = (double)graphWidth/(24*60);
            val1*=val;
        } else{
            //val1 = graphWidth;
            val1 = barWidth/2;
        }
        x = (int)val1;
        x+=paddingLeft;
        //x+=axisWidth;
        return x;
    }

    private Rect getRect(int x, int y, int width, int height){
        Rect rectangle = new Rect(x, y, x+width, y+height);
        return rectangle;
    }

    private int getTextWidth(String text){
        return (int)textPaint.measureText(text);
    }

    private boolean isLocked(String mode){
        if(mode.equalsIgnoreCase("locked")){
            return true;
        } else{
            return false;
        }
    }

    private class LockData{
        String mode;
        int time;
        LockData(int time, String mode){
            this.mode = mode;
            this.time = time;
        }
    }

}
