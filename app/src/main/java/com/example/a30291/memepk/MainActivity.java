package com.example.a30291.memepk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvProgress)
    BloodProgressTextView tvProgress;
    @BindView(R.id.bloodAnimationView)
    BloodAnimationView bloodAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvProgress)
    public void onViewClicked() {
        tvProgress.setProgress(tvProgress.getProgress() + 10);
        BloodAnimationView.Blood blood=new BloodAnimationView.Blood();
        blood.setText("-2000");
        bloodAnimationView.addData(blood);
    }

    public void cancel(View view) {
        bloodAnimationView.cancel();
    }
}
