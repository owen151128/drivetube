package kr.owens.drivetube.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kr.owens.drivetube.R;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
