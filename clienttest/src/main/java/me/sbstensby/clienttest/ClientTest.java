package me.sbstensby.clienttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import me.sbstensby.spotipowerhost.HostDiscoverer;
import me.sbstensby.spotipowerhost.HostDiscovererInterface;
import me.sbstensby.spotipowerhost.RemoteHostData;


public class ClientTest extends Activity implements HostDiscovererInterface {
    private Button pingButton;
    private HostDiscoverer disco; // Night fever, night feveeeeeer!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_test);

        disco = new HostDiscoverer(this, getBaseContext());

        pingButton = (Button)findViewById(R.id.pingButton);
        pingButton.setOnClickListener(pingButtonListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_test, menu);
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

    @Override
    public void pushBackHosts(ArrayList<RemoteHostData> hosts) {

    }

    private View.OnClickListener pingButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            disco.requestHosts();
        }
    };
}
