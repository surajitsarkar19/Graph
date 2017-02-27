package surajit.com.graph;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    GraphViewThermostat graphView;
    GraphViewLock graphViewLock;

    ZoomView zoomView;
    LinearLayout linearLayoutThermostat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //graphView = (GraphViewThermostat) findViewById(R.id.graphViewThermostat);

        //graphViewLock = (GraphViewLock) findViewById(R.id.graphViewLock);


        linearLayoutThermostat = (LinearLayout) findViewById(R.id.linearLayoutThermostat);
        zoomView = new ZoomView(this);
        graphView = new GraphViewThermostat(this);
        graphView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        graphViewLock = new GraphViewLock(this);
        graphViewLock.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        zoomView.addView(graphViewLock);
        linearLayoutThermostat.addView(zoomView);

        putDummyLock();
        putDummyData1();
        putDummyBarData();
        putDummyLock();

    }

    private void putDummyLock() {
        if(graphViewLock == null){
            return;
        }
        graphViewLock.addLock(60,"locked");
        graphViewLock.addLock(120,"locked");
        graphViewLock.addLock(140,"unlocked");
        graphViewLock.addLock(360,"locked");
        graphViewLock.addLock(480,"locked");
        graphViewLock.addLock(600,"unlocked");
        graphViewLock.addLock(23*60,"unlocked");
        graphViewLock.addLock(24*60,"unlocked");

    }

    public void putDummyBarData(){
        if(graphView == null){
            return;
        }
        graphView.addSetpoint(55,120,240, GraphData.SetPoint.HEAT);
        graphView.addSetpoint(75,360,600, GraphData.SetPoint.COOL);
        graphView.addSetpoint(70,22*60,24*60, GraphData.SetPoint.HEAT);
    }

    public void putDummyData1(){
        if(graphView == null){
            return;
        }
        graphView.addRoomTemperature(55,0);
        graphView.addRoomTemperature(85,240);
        graphView.addRoomTemperature(70,600);
        graphView.addRoomTemperature(75,12*60);
        graphView.addRoomTemperature(65,16*60);
    }

    @Override
    public void onClick(View v) {
        /*View view = LayoutInflater.from(this).inflate(R.layout.crouton_message_layout,null,false);
        view.setBackgroundColor(Color.LTGRAY);
        TextView textView = (TextView) view.findViewById(R.id.textViewMessage);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewIcon);
        textView.setText("Hello Crouton......");
        Crouton.make(this,view).show();*/
    }
}
