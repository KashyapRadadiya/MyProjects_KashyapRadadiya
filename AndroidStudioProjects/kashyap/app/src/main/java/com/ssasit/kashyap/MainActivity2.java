package com.ssasit.kashyap;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    private EditText amountInput;

    private Spinner fromCurrencySpinner , toCurrencySpinner;

    private TextView resultView;

    private Button convertButton;

    private final double USD_TO_INR = 87.15;


    private final double INR_TO_USD = 1 / USD_TO_INR ;

    private final double EUR_TO_INR  = 94.24;


    private final double INR_TO_EUR  = 1 / EUR_TO_INR;
    private final double EUR_TO_USD = 1.08;
    private final double USD_TO_EUR  = 1 / EUR_TO_USD;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        amountInput = findViewById(R.id.amtInput);
        fromCurrencySpinner  = findViewById(R.id.fromCurrencySpinner);
        toCurrencySpinner  = findViewById(R.id.toCurrencySpinner);
        resultView  = findViewById(R.id.resultView);
        convertButton  = findViewById(R.id.convertButton);

        String[] currencis = {"INR" , "USD" , "EUR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1, currencis );
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        fromCurrencySpinner.setAdapter(adapter);
        toCurrencySpinner.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfomConversion();
            }
        });


    }

    private void perfomConversion() {
        String amtStr = amountInput.getText().toString();
        if(amtStr.isEmpty()){
            Toast.makeText(this,"Please Enter a  Amount",Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amtStr);
        String fromCurrency = fromCurrencySpinner.getSelectedItem().toString();
        String toCurrency = toCurrencySpinner.getSelectedItem().toString();

        double convertedAmt = convertCurrency(amount , fromCurrency , toCurrency);
        resultView.setText(String.format("%.2f %s" , convertedAmt , toCurrency));
    }

    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        if(fromCurrency.equals("INR") && toCurrency.equals("USD")){
            return amount * INR_TO_USD;
        } else if (fromCurrency.equals("USD") && toCurrency.equals("INR")) {
            return amount * USD_TO_INR;
        } else if (fromCurrency.equals("INR") && toCurrency.equals("EUR")) {
            return  amount * INR_TO_EUR;
        } else if (fromCurrency.equals("EUR") && toCurrency.equals("INR")) {
            return  amount * EUR_TO_INR;
        } else if (fromCurrency.equals("USD") && toCurrency.equals("EUR")) {
            return  amount * USD_TO_EUR;
        } else if (fromCurrency.equals("EUR") && toCurrency.equals("USD")) {
            return amount * EUR_TO_USD;
        }
        return amount;
    }
}