package surajit.com.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

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

    private void initView(){
        axisWidth = 3;
        paddingTop = 0;
        paddingBottom = 150;
        paddingLeft = 50;
        paddingRight = 50;

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
        textPaint.setTextSize(30);
        textSize = (int)textPaint.getTextSize();
    }

    public void addLock(int minute, String status){
        points.add(new LockData(minute,status));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateSize(canvas);
        if (isInEditMode()) {
            canvas.drawColor(Color.rgb(200, 200, 200));
            //canvas.drawText("GraphView: No Preview available", canvas.getWidth()/2, canvas.getHeight()/2, paint);
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

        for(int i=0; i<timeTop.length; i++){
            Rect lineRect = getRect((int)x,(int)y,3,lineHeight);
            canvas.drawRect(lineRect, paint);

            String text1 = timeTop[i];
            String text2 = timeBottom[i];
            int textPadding = 20 + textSize;

            int txtHeight = height - paddingBottom;
            canvas.drawText(text1,x,txtHeight+textPadding,textPaint);
            canvas.drawText(text2,x,txtHeight+textPadding + textPadding,textPaint);

            x+=space;
        }
    }

    private void calculateSize(Canvas canvas){
        width = canvas.getWidth();
        height = canvas.getHeight();
        graphWidth = width - paddingLeft - paddingRight;
        graphHeight = height - paddingTop - paddingBottom;
        barHeight = graphHeight/2 - 30;
        barWidth = 20;
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
