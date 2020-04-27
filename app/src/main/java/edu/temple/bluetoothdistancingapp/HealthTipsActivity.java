package edu.temple.bluetoothdistancingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HealthTipsActivity extends AppCompatActivity {

    Button back;
    ListView tips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

    back = findViewById(R.id.btnBack);
    tips = findViewById(R.id.tipsList);

    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent launchActivityIntent = new Intent(HealthTipsActivity.this, MainActivity.class);

            startActivity(launchActivityIntent);
        }
    });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tips, android.R.layout.simple_list_item_1);
        tips.setAdapter(adapter);

    }
}
