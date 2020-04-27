package edu.temple.bluetoothdistancingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter BTAdapter;

    Button scanbutton;
    Button tips;
    ListView availableDevices;
    ArrayList<String> devicesList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        scanbutton = findViewById(R.id.btnScan);
        tips = findViewById(R.id.btnTips);
        availableDevices = findViewById(R.id.availableDevices);


        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivityIntent = new Intent(MainActivity.this, HealthTipsActivity.class);

                startActivity(launchActivityIntent);
            }
        });

        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTAdapter.startDiscovery();
            }
        });

        IntentFilter newintent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothreciever, newintent);


        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, devicesList);
        availableDevices.setAdapter(adapter);
    }

    BroadcastReceiver bluetoothreciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();

                int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                Toast.makeText(getApplicationContext(),"  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
                if(rssi >= -20){

                    new AlertDialog.Builder(getApplicationContext())
                            .setTitle("DANGER")
                            .setMessage("You are too close to " + name + ", please get away.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    name += " Extreme Danger: Move Away";
                    adapter.notifyDataSetChanged();
                }
                else if(rssi >= -50){
                    name += " Risk: Try to get further away";
                }
                else{
                    name += " Safe: Maintain Current Distance";
                }

                devicesList.add(name);
                adapter.notifyDataSetChanged();

            }


        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(bluetoothreciever);
    }


}
