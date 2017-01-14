package cimb.niaga.app.billsplit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cimb.niaga.app.billsplit.R;

/**
 * Created by 8ldavid on 1/14/2017.
 */

public class SummaryActivity extends AppCompatActivity {

    TextView nickname, price;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.summary_activity);

        nickname = (TextView) findViewById(R.id.input_nick);
        price = (TextView) findViewById(R.id.input_price);
        submit_btn = (Button) findViewById(R.id.btn_submit);

        //set next enter
        nickname.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });
    }

    public void nextActivity() {
        Intent i = new Intent(SummaryActivity.this, HomeActivity.class);
        Toast.makeText(getApplication(), "Sent", Toast.LENGTH_LONG).show();
        startActivity(i);
        finish();


    }
}