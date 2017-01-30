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

public class ThermostatGraphView extends View {

    private Paint paint;
    private Paint textPaint;
    private Paint linePaint;
    private List<GraphData> linePoints,barPoints;
    private int axisWidth;
    private int axisPadding;
    private int width,graphWidth;
    private int height,graphHeight;
    private int textSize;
    private int backgroundColor;

    public ThermostatGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()){
            initView();
        }
    }

    private void initView(){
        linePoints = new ArrayList<>();
        barPoints = new ArrayList<>();
        backgroundColor = Color.WHITE;
        axisWidth = 10;
        axisPadding = 50;

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setTextAlign(Paint.Align.CENTER);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4);
        linePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(30);
        textSize = (int)textPaint.getTextSize();
    }

    private void calculateSize(Canvas canvas){
        width = canvas.getWidth();
        height = canvas.getHeight();
        graphWidth = width - axisPadding - 50;
        graphHeight = height - axisPadding - 50;
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

    public void addSetpoint(int temp, int startTime, int endTime){
        if(temp<=100) {
            barPoints.add(new GraphData(startTime, endTime, temp));
        }
    }

    private void drawGraphElements(Canvas canvas){
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
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
        x+=axisPadding;
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
        y+=axisPadding;
        //y+=axisWidth;
        y = height - y;
        return y;
    }

    public void drawBarGraph(Canvas canvas){
        /*if(barPoints.size()>0){
            for(GraphData graphData : linePoints){

            }
        }*/

        GraphData data = new GraphData(120,240,60);
    }

    private void drawAxis(Canvas canvas){
        drawXAxis(canvas);
        drawYAxis(canvas);
    }

    private void drawXAxis(Canvas canvas){
        Rect xAxis = getRect(axisPadding,height- axisWidth-axisPadding,width-axisPadding, axisWidth);
        canvas.drawRect(xAxis, paint);
        int lineHeight = axisWidth + 5; //marker line height
        int textPadding = lineHeight + textSize; // text of each marker ; text always draw as bottom up

        //draw markings
        int space = graphWidth/12;
        int x = axisPadding;
        int y = height-axisPadding-lineHeight;
        for(int i=0;  i<=24; i+=2){
            Rect lineRect = getRect(x,y,5,lineHeight);
            canvas.drawRect(lineRect, paint);

            canvas.drawText(""+i,x,y+textPadding,textPaint);
            x+=space;
        }
    }

    private void drawYAxis(Canvas canvas){
        Rect yAxis = getRect(axisPadding,0, axisWidth,height-axisPadding);
        canvas.drawRect(yAxis, paint);

        int lineWidth = axisWidth + 5; //marker line height
        int textPadding = lineWidth + textSize; // text of each marker ; text always draw as bottom up

        //draw markings
        int space = graphHeight/10;
        int x = axisPadding;
        int y = height-axisPadding-axisWidth;
        for(int i=0;  i<=100; i+=10){
            if(i==0 && i==90 && i==100) {
                //continue;
            }
            Rect lineRect = getRect(x,y,lineWidth,5);
            canvas.drawRect(lineRect, paint);

            canvas.drawText(""+i,textPadding-20,y,textPaint);

            y-=space;
        }
    }

    private Rect getRect(int x, int y, int width, int height){
        Rect rectangle = new Rect(x, y, x+width, y+height);
        return rectangle;
    }

    /*@Override
    protected void onDraw(Canvas canvas) {
        float border = 20;
        float horstart = border * 2;
        float height = getHeight();
        float width = getWidth() - 1;
        float max = getMax();
        float min = getMin();
        float diff = max - min;
        float graphheight = height - (2 * border);
        float graphwidth = width - (2 * border);

        paint.setTextAlign(Align.LEFT);
        int vers = verlabels.length - 1;
        for (int i = 0; i < verlabels.length; i++) {
            paint.setColor(Color.DKGRAY);
            float y = ((graphheight / vers) * i) + border;
            canvas.drawLine(horstart, y, width, y, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(verlabels[i], 0, y, paint);
        }
        int hors = horlabels.length - 1;
        for (int i = 0; i < horlabels.length; i++) {
            paint.setColor(Color.DKGRAY);
            float x = ((graphwidth / hors) * i) + horstart;
            canvas.drawLine(x, height - border, x, border, paint);
            paint.setTextAlign(Align.CENTER);
            if (i == horlabels.length - 1)
                paint.setTextAlign(Align.RIGHT);
            if (i == 0)
                paint.setTextAlign(Align.LEFT);
            paint.setColor(Color.WHITE);
            canvas.drawText(horlabels[i], x, height - 4, paint);
        }

        paint.setTextAlign(Align.CENTER);
        canvas.drawText(title, (graphwidth / 2) + horstart, border - 4, paint);

        if (max != min) {
            paint.setColor(Color.LTGRAY);
            if (type == BAR) {
                float datalength = values.length;
                float colwidth = (width - (2 * border)) / datalength;
                for (int i = 0; i < values.length; i++) {
                    float val = values[i] - min;
                    float rat = val / diff;
                    float h = graphheight * rat;
                    canvas.drawRect((i * colwidth) + horstart, (border - h)
                            + graphheight, ((i * colwidth) + horstart)
                            + (colwidth - 1), height - (border - 1), paint);
                }
            } else {
                float datalength = values.length;
                float colwidth = (width - (2 * border)) / datalength;
                float halfcol = colwidth / 2;
                float lasth = 0;
                for (int i = 0; i  0)
                canvas.drawLine(((i - 1) * colwidth) + (horstart + 1)
                                + halfcol, (border - lasth) + graphheight,
                        (i * colwidth) + (horstart + 1) + halfcol,
                        (border - h) + graphheight, paint);
                lasth = h;
            }
        }
    }*/

}
