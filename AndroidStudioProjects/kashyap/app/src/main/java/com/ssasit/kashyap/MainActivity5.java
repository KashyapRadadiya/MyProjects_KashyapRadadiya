package com.ssasit.kashyap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity5 extends AppCompatActivity {

    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mnu = getMenuInflater();
        mnu.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu1) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu2) {
            Intent i = new Intent(this, MainActivity2.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu3) {
            Intent i = new Intent(this, MainActivity3.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu4) {
            Intent i = new Intent(this, MainActivity5.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu5) {
            Intent i = new Intent(this, MainActivity4.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.menu6) {
            Intent i = new Intent(this, MainActivity6.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}