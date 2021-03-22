package kr.ac.kpu.game.s2016180024.morecontrols;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CheckBox firewallCheckbox;
    private TextView outTextView;
    private TextView userEditText;
    private TextView editTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firewallCheckbox = findViewById(R.id.checkbox);
        outTextView =findViewById(R.id.outTextView);
        userEditText =findViewById(R.id.editText);

        userEditText.addTextChangedListener(textWatcher);
        editTextView =findViewById(R.id.editTextView);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editTextView.setText("String length" + s.length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void onBtnApply(View view) {
        boolean checked = firewallCheckbox.isChecked();
        String text=checked ? "Using Firewall":"Not using Firewall";
        outTextView.setText(text);

        String user = userEditText.getText().toString();
        editTextView.setText("User info =" +user);
    }

    public void onCheckFirewall(View view) {
        boolean checked = firewallCheckbox.isChecked();
        String text=checked ? "Checked Firewall":"Un Checked Firewall";
        outTextView.setText(text);

    }

}