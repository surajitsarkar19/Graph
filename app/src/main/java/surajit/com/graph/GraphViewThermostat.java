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

/**
 * Created by Surajit Sarkar on 30/1/17.
 * Company : Bitcanny Technologies Pvt. Ltd.
 * Email   : surajit@bitcanny.com
 */

public class GraphViewThermostat extends View {

    private Paint paint;
    private Paint textPaint;
    private Paint linePaint;
    private List<GraphData> linePoints,barPoints;
    private int axisWidth;
    //private int axisPadding;
    private int paddingLeft, paddingBottom;
    private int width,graphWidth;
    private int height,graphHeight;
    private int textSize;
    private int backgroundColor;
    private int heatColor,coolColor,axisColor;

    public GraphViewThermostat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        linePoints = new ArrayList<>();
        barPoints = new ArrayList<>();
        backgroundColor = Color.WHITE;
        heatColor = Color.RED;
        coolColor = Color.BLUE;
        axisColor = Color.BLACK;

        /*axisWidth = 10;
        paddingLeft = 120;
        paddingBottom = 150;*/

        axisWidth = getDP(3);
        paddingLeft = getDP(50);
        paddingBottom = getDP(50);

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        linePaint = new Paint(); //for bar graph
        linePaint.setTextAlign(Paint.Align.CENTER);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(getSP(10));
        textSize = (int)textPaint.getTextSize();
    }

    private void calculateSize(Canvas canvas){
        width = canvas.getWidth();
        height = canvas.getHeight();
        graphWidth = width - paddingLeft - axisWidth - getDP(30);
        graphHeight = height - paddingBottom - axisWidth - getDP(20);
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

    public void addRoomTemperature(int temp, int min){
        if(temp<=100) {
            linePoints.add(new GraphData(min, temp));
        }
        invalidate();
    }

    public void clear(){
        linePoints.clear();
        barPoints.clear();
        invalidate();
    }

    public void addSetpoint(int temp, int startTime, int endTime, GraphData.SetPoint type){
        if(temp<=100) {
            barPoints.add(new GraphData(startTime, endTime, temp,type));
        }
        invalidate();
    }

    private void drawGraphElements(Canvas canvas){
        textPaint.setColor(axisColor);
        String text = "Set Point:";
        int textWidth = Math.round(textPaint.measureText(text));
        canvas.drawText(text,textWidth/2,height-(paddingBottom /3)+textSize/2,textPaint);
        drawAxis(canvas);
        drawLineGraph(canvas);
        drawBarGraph(canvas);
    }

    public void drawLineGraph(Canvas canvas){
        if(linePoints.size()>0){
            GraphData data = null;
            for(GraphData graphData : linePoints){
                if(data == null){
                    data = graphData;
                    continue;
                }
                canvas.drawLine(getX(data.getX()),getY(data.getY()),getX(graphData.getX()),getY(graphData.getY()),linePaint);
                data = graphData;
            }
        }

        /*GraphData data = new GraphData(120,60);
        int x = getX(data.getX());
        int y = getY(data.getY());
        canvas.drawCircle(x,y,5,paint);*/
    }

    private int getX(int val){
        int x = 0;
        double val1 = 0;
        if(val < 24*60){
            val1 = (double)graphWidth/(24*60);
            val1*=val;
        } else{
            val1 = graphWidth;
        }
        x = (int)val1;
        x+= paddingLeft;
        //x+=axisWidth;
        return x;
    }

    private int getY(int val){
        int y = 0;
        double val1 = 0;
        if(val<100) {
            val1 = (double)graphHeight/100;
            val1*=val;
        } else{
            val1 = graphHeight;
        }
        y = (int) val1;
        y+= paddingBottom;
        //y+=axisWidth;
        y = height - y;
        return y;
    }

    public void drawBarGraph(Canvas canvas){
        if(barPoints.size()>0){
            for(GraphData data : barPoints){
                int color = getBarColor(data.getType());
                int left = getX(data.getMinX());
                int top = getY(data.getY());
                int right = getX(data.getMaxX());
                int bottom = paddingBottom + axisWidth;
                Rect rect = new Rect(left,top,right,height-bottom);
                paint.setColor(color);
                canvas.drawRect(rect, paint);

                //draw setpoint circle
                String text = ""+data.getY();
                int circleWidth = Math.round(textPaint.measureText(text));
                int cx = right - rect.width()/2;
                int cy = height- paddingBottom /3;
                canvas.drawCircle(cx,cy,circleWidth,paint);
                textPaint.setColor(Color.WHITE);
                canvas.drawText(text,cx,cy+textSize/3,textPaint);
            }
        }

        /*GraphData data = new GraphData(120,240,60, GraphData.SetPoint.HEAT);
        int color = getBarColor(data.getType());
        int left = getX(data.getMinX());
        int top = getY(data.getY());
        int right = getX(data.getMaxX());
        int bottom = axisPadding + axisWidth;
        Rect rect = new Rect(left,top,right,height-bottom);
        paint.setColor(color);
        canvas.drawRect(rect, paint);*/

        //Rect xAxis = getRect(123,250,197, 60);
        //canvas.drawRect(xAxis, paint);
    }

    private int getBarColor(GraphData.SetPoint type){
        if(type == GraphData.SetPoint.HEAT){
            return heatColor;
        } else{
            return coolColor;
        }
    }

    private void drawAxis(Canvas canvas){
        drawXAxis(canvas);
        drawYAxis(canvas);
    }

    private void drawXAxis(Canvas canvas){
        paint.setColor(axisColor);
        textPaint.setColor(axisColor);
        Rect xAxis = getRect(paddingLeft,height-axisWidth- paddingBottom,width- paddingLeft, axisWidth);
        canvas.drawRect(xAxis, paint);
        int lineHeight = axisWidth + 3; //marker line height
        int textPadding = lineHeight + textSize; // text of each marker ; text always draw as bottom up
        //draw markings
        float space = (float)graphWidth/12;
        float x = paddingLeft;
        float y = height- paddingBottom -lineHeight;
        for(int i=0;  i<=24; i+=2){
            Rect lineRect = getRect((int)x,(int)y,3,lineHeight);
            canvas.drawRect(lineRect, paint);

            canvas.drawText(""+i,x,y+textPadding,textPaint);
            x+=space;
        }

        canvas.drawText("hr",width-textPaint.measureText("hr"),y+textPadding,textPaint);
    }

    private void drawYAxis(Canvas canvas){
        paint.setColor(axisColor);
        textPaint.setColor(axisColor);
        Rect yAxis = getRect(paddingLeft,0, axisWidth,height- paddingBottom);
        canvas.drawRect(yAxis, paint);

        int lineWidth = axisWidth + 3; //marker line height
        int lineHeight = 3;
        //int textPadding = lineWidth + textSize; // text of each marker ; text always draw as bottom up

        int textPadding = paddingLeft - textSize;
        //draw markings
        float space = (float)graphHeight/10;
        float x = paddingLeft;
        float y = height- paddingBottom -axisWidth;
        for(int i=0;  i<=100; i+=10){
            if(i==0 || i==90 || i==100 ) {
                y-=space;
                continue;
            }
            Rect lineRect = getRect((int)x,(int)y,lineWidth,lineHeight);
            canvas.drawRect(lineRect, paint);

            canvas.drawText(""+i,textPadding,y+textSize/2-lineHeight,textPaint);

            y-=space;
        }

        canvas.drawText("°F",textPadding,textSize,textPaint);
    }

    private Rect getRect(int x, int y, int width, int height){
        Rect rectangle = new Rect(x, y, x+width, y+height);
        return rectangle;
    }

    private int getDP(int dp){
        float pixel = (float)dp*getResources().getDisplayMetrics().density;
        return (int)pixel;
    }

    private int getSP(int sp){
        float pixel = (float)sp*getResources().getDisplayMetrics().scaledDensity;
        return (int)pixel;
    }

}
