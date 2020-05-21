package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PracticalTest02MainActivity extends AppCompatActivity {



    private Button setButton = null;
    private Button resetButton = null;
    private Button pollButton = null;
    private TextView oraTextView = null;
    private TextView minutTextView = null;
    private TextView info = null;

    private ServerThread serverThread = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        oraTextView = (EditText) findViewById(R.id.edit_ore);
        minutTextView = (EditText) findViewById(R.id.edit_minute);
        setButton = (Button)findViewById(R.id.set_button);
        resetButton = (Button)findViewById(R.id.reset_button);
        pollButton = (Button)findViewById(R.id.poll_button);
        info = (TextView) findViewById(R.id.textView);


    }
}
