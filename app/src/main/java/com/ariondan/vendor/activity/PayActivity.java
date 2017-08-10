package com.ariondan.vendor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import com.ariondan.vendor.R;

import me.alexrs.wavedrawable.WaveDrawable;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        findViewById(R.id.layout_nfc).setOnClickListener(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ImageView imageNfc = (ImageView) findViewById(R.id.image_pay_nfc);
        ImageView imageBackground = (ImageView) findViewById(R.id.image_pay_background);
        WaveDrawable waveDrawable = new WaveDrawable(getResources().getColor(R.color.colorPrimary), 500);
        WaveDrawable waveBackground = new WaveDrawable(getResources().getColor(R.color.colorPrimary), 1000);
        imageNfc.setBackgroundDrawable(waveDrawable);
        imageBackground.setBackgroundDrawable(waveBackground);
        waveDrawable.setWaveInterpolator(new AccelerateDecelerateInterpolator());
        waveBackground.setWaveInterpolator(new AnticipateOvershootInterpolator());
        waveDrawable.startAnimation();
        waveBackground.startAnimation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_nfc:
                finish();
                break;
        }
    }
}
