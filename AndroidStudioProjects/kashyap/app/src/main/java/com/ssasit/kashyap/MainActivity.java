package com.ssasit.kashyap;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    TextView tvdisplay;
    double n1 = 0, n2 = 0;
    String op = "";
    boolean opset = false;
    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        tvdisplay = findViewById(R.id.tvdisplay);
        setButtonListeners();
    }

    private void setButtonListeners() {
        setNumberButtonClickListeners(R.id.btn0, "0");
        setNumberButtonClickListeners(R.id.btn1, "1");
        setNumberButtonClickListeners(R.id.btn2, "2");
        setNumberButtonClickListeners(R.id.btn3, "3");
        setNumberButtonClickListeners(R.id.btn4, "4");
        setNumberButtonClickListeners(R.id.btn5, "5");
        setNumberButtonClickListeners(R.id.btn6, "6");
        setNumberButtonClickListeners(R.id.btn7, "7");
        setNumberButtonClickListeners(R.id.btn8, "8");
        setNumberButtonClickListeners(R.id.btn9, "9");

        setOperationButtonClickListener(R.id.btnmod, "%");
        setOperationButtonClickListener(R.id.btndiv, "/");
        setOperationButtonClickListener(R.id.btnmul, "*");
        setOperationButtonClickListener(R.id.btnmin, "-");
        setOperationButtonClickListener(R.id.btnadd, "+");

        findViewById(R.id.btnac).setOnClickListener(v -> clearDisplay());
        findViewById(R.id.btnequal).setOnClickListener(v -> calculateResult());
        findViewById(R.id.btnback).setOnClickListener(v -> backspace());
    }

    private void setOperationButtonClickListener(int id, String operation) {
        findViewById(id).setOnClickListener(v -> {
            n1 = Double.parseDouble(tvdisplay.getText().toString());
            op = operation;
            opset = true;
            tvdisplay.setText("");
        });
    }

    private void setNumberButtonClickListeners(int id, String number) {
        findViewById(id).setOnClickListener(v -> {
            if (opset) {
                tvdisplay.setText("");
                opset = false;
            }
            String currentDisplay = tvdisplay.getText().toString();
            tvdisplay.setText(currentDisplay.equals("0") ? number : currentDisplay + number);
        });
    }

    void clearDisplay() {
        tvdisplay.setText("0");
        n1 = n2 = 0;
        op = "";
        opset = false;
    }

    void backspace() {
        String text = tvdisplay.getText().toString();
        tvdisplay.setText(text.length() > 1 ? text.substring(0, text.length() - 1) : "0");
    }

    void calculateResult() {
        n2 = Double.parseDouble(tvdisplay.getText().toString());
        double result = 0;

        switch (op) {
            case "+":
                result = n1 + n2;
                break;
            case "-":
                result = n1 - n2;
                break;
            case "*":
                result = n1 * n2;
                break;
            case "/":
                if (n2 != 0) {
                    result = n1 / n2;
                } else {
                    tvdisplay.setText("Error");
                    return;
                }
                break;
            case "%":
                result = n1 % n2;
                break;
            default:
                result = n2;
                break;
        }

        tvdisplay.setText(String.valueOf(result));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
