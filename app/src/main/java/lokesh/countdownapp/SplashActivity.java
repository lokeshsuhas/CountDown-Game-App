package lokesh.countdownapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import lokesh.countdownapp.ViewModel.SplashViewModel;
import lokesh.countdownapp.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity implements SplashViewModel.SplashViewListener {
    private SplashViewModel model;
    private Context context;
    private ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        model = new SplashViewModel(this);
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        splashBinding.setViewModel(model);
    }

    @Override
    public void onNewGameClicked() {
        finish();
        startActivity(new Intent(SplashActivity.this, BoardActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.onDestroy();
    }
}
