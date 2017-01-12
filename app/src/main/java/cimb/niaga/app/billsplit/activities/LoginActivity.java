package cimb.niaga.app.billsplit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cimb.niaga.app.billsplit.R;

public class LoginActivity extends AppCompatActivity {

    EditText nickname, phone, email;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login_activity);

        nickname = (EditText) findViewById(R.id.input_nick);
        phone = (EditText) findViewById(R.id.input_phone);
        email = (EditText) findViewById(R.id.input_email);
        submit_btn = (Button) findViewById(R.id.btn_submit);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    nextActivity();
                }
            }
        });
    }

    public void nextActivity()
    {
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        Toast.makeText(getApplication(), "Success", Toast.LENGTH_LONG).show();
        startActivity(i);
        finish();


    }

    public boolean validate() {
        boolean valid = true;

        String _nickname = nickname.getText().toString();
        String _phone = phone.getText().toString();
        String _email = email.getText().toString();


        if (_nickname.isEmpty()) {
            nickname.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            nickname.setError(null);
        }

        if (_phone.isEmpty()) {
            phone.setError("please insert your phone number");
            valid = false;
        } else {
            phone.setError(null);
        }

        if (_email.isEmpty()) {
            email.setError("please insert your valid email");
            valid = false;
        } else {
            email.setError(null);
        }

        return valid;
    }
}
