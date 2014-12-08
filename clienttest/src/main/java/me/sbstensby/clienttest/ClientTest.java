package me.sbstensby.clienttest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import me.sbstensby.spotipowerhost.Client;
import me.sbstensby.spotipowerhost.HostDiscoverer;
import me.sbstensby.spotipowerhost.HostDiscovererInterface;
import me.sbstensby.spotipowerhost.RemoteHostData;


public class ClientTest extends Activity implements HostDiscovererInterface {
    private Button pingButton;
    private Button addButton;
    private Button removeButton;
    private Button pauseButton;
    private Button joinOPButton;
    private HostDiscoverer disco; // Night fever, night feveeeeeer!
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_test);

        disco = new HostDiscoverer(this, getBaseContext());
        connected = false;

        pingButton = (Button)findViewById(R.id.pingButton);
        pingButton.setOnClickListener(pingButtonListener);

        addButton = (Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(addButtonListener);

        removeButton = (Button)findViewById(R.id.removeButton);
        removeButton.setOnClickListener(removeButtonListener);

        pauseButton = (Button)findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(pauseButtonListener);

        joinOPButton = (Button)findViewById(R.id.joinOPButton);
        joinOPButton.setOnClickListener(joinOPButtonListener);
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
        if (!hosts.isEmpty() && !connected)
        {
            connected = true;
            Log.i("ClientTest", "Set Host.");
            Client.getInstance().setConnectedHost(hosts.get(0));
            Log.i("ASDASDASDASDASD", hosts.get(0).name + hosts.get(0).address);
        }
    }

    private View.OnClickListener pingButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            disco.requestHosts();
        }
    };

    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Client.getInstance().sendMessage("QUEUE:ADD");
        }
    };

    private View.OnClickListener removeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Client.getInstance().sendMessage("QUEUE:REMOVE");
        }
    };

    private View.OnClickListener pauseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Client.getInstance().sendMessage("CONTROL:PAUSE");
        }
    };

    private View.OnClickListener joinOPButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Client.getInstance().sendMessage("JOINOP");
        }
    };
}
