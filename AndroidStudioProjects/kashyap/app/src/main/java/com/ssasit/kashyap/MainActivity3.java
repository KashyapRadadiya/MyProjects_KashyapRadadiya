package com.ssasit.kashyap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import androidx.appcompat.widget.Toolbar;

public class MainActivity3 extends AppCompatActivity {

    private DatePicker duedatepicker;
    private TextView resulttextview;
    private Button calculatebtn;
    private Toolbar tb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // UI components
        duedatepicker = findViewById(R.id.duedatepicker);
        resulttextview = findViewById(R.id.resulttextview);
        calculatebtn = findViewById(R.id.calculatebtn);

        // set btn click listener
       calculatebtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               calculateOverdueDays();
           }
       });

    }



    private void calculateOverdueDays() {

        // get current date
        Calendar currentDate = Calendar.getInstance();

        // get selected date from picker
        int dueDay = duedatepicker.getDayOfMonth();
        int dueMonth = duedatepicker.getMonth();
        int dueYear = duedatepicker.getYear();

        Calendar dueDate = Calendar.getInstance();
        dueDate.set(dueYear,dueMonth,dueDay);

        // diff between current and due date
        long currentTime = currentDate.getTimeInMillis();
        long dueTime = dueDate.getTimeInMillis();

        long difference = currentTime - dueTime;
        long daysDifference = difference / (1000*60*60*24);

        if(daysDifference > 0){
            resulttextview.setText("Book is Overdue "+daysDifference+" Days.");
        } else if (daysDifference == 0) {
            resulttextview.setText("Today is Overdue Date.");
        }else {
            resulttextview.setText("No Overdue. Book is due in "+Math.abs(daysDifference)+" Days.");
        }

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