package me.sbstensby.hosttest;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import me.sbstensby.spotipowerhost.HostServer;



public class HostingTest extends Activity {
    private Button startButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting_test);
        startButton = (Button)findViewById(R.id.startHost);
        stopButton = (Button)findViewById(R.id.stopHost);

        startButton.setOnClickListener(startListener);
        stopButton.setOnClickListener(stopListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hosting_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private OnClickListener startListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            HostServer.getInstance().startHosting("TestHost By Mr. Cool!");
            startButton.setEnabled(false);
            startButton.setClickable(false);
            stopButton.setEnabled(true);
            stopButton.setClickable(true);
        }
    };

    private OnClickListener stopListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            HostServer.getInstance().stopHosting();
            stopButton.setEnabled(false);
            stopButton.setClickable(false);
            startButton.setEnabled(true);
            startButton.setClickable(true);
        }
    };
}
