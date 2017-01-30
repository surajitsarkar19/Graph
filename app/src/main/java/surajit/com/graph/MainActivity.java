package surajit.com.graph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ThermostatGraphView graphView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphView = (ThermostatGraphView) findViewById(R.id.graphView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);

        //putDummyData();
        putDummyData1();
    }

    public void putDummyBarData(){
        //graphView.addSetpoint(55,);
    }

    public void putDummyData1(){
        graphView.addRoomTemperature(55,0);
        graphView.addRoomTemperature(85,240);
        graphView.addRoomTemperature(70,600);
        //graphView.addRoomTemperature(75,12);
        //graphView.addRoomTemperature(65,16);
    }

    @Override
    public void onClick(View v) {
        graphView.clear();
    }
}
