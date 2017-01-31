package surajit.com.graph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    GraphViewThermostat graphView;
    GraphViewLock graphViewLock;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphView = (GraphViewThermostat) findViewById(R.id.graphViewThermostat);
        button = (Button) findViewById(R.id.button);
        graphViewLock = (GraphViewLock) findViewById(R.id.graphViewLock);

        button.setOnClickListener(this);

        //putDummyData();
        putDummyData1();
        putDummyBarData();
        putDummyLock();
    }

    private void putDummyLock() {
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
        graphView.addSetpoint(55,120,240, GraphData.SetPoint.HEAT);
        graphView.addSetpoint(75,360,600, GraphData.SetPoint.COOL);
        graphView.addSetpoint(70,22*60,24*60, GraphData.SetPoint.HEAT);
    }

    public void putDummyData1(){
        graphView.addRoomTemperature(55,0);
        graphView.addRoomTemperature(85,240);
        graphView.addRoomTemperature(70,600);
        graphView.addRoomTemperature(75,12*60);
        graphView.addRoomTemperature(65,16*60);
    }

    @Override
    public void onClick(View v) {
        graphView.clear();
    }
}
