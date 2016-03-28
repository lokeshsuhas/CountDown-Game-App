package lokesh.countdownapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import lokesh.countdownapp.Enums.ResultEnum;
import lokesh.countdownapp.Models.ResultModel;
import lokesh.countdownapp.ViewModel.PlayViewModel;
import lokesh.countdownapp.databinding.ActivityGameMainBinding;
import lokesh.countdownapp.databinding.DialogResultBinding;

public class PlayActivity extends BaseActivity implements PlayViewModel.GameMainViewListener {
    private Context context;
    private PlayViewModel mainViewModel;
    private ActivityGameMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mainViewModel = new PlayViewModel(getIntent(), this);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_main);
        mainBinding.setMainModel(mainViewModel);
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainViewModel.onDestroy();
    }

    private void setRecyclerView() {
        mainBinding.RecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        mainBinding.StepsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onResult(ResultEnum resultEnum, final ResultModel resultModel) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.positiveText(R.string.new_game);
        DialogResultBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_result, null, false);
        switch (resultEnum) {
            case SUCCESS:
                binding.result.setText(R.string.success_content);
                binding.resultImage.setImageResource(R.mipmap.ic_sentiment_very_satisfied_black_24dp);
                break;
            case NEUTRAL:
                binding.result.setText(R.string.neutral_content);
                binding.resultImage.setImageResource(R.mipmap.ic_sentiment_neutral_black_24dp);
                builder.negativeText(R.string.retry);
                break;
            case FAILED:
                binding.result.setText(R.string.failed_content);
                binding.resultImage.setImageResource(R.mipmap.ic_sentiment_very_dissatisfied_black_24dp);
                builder.negativeText(R.string.retry);
                break;
            case TIMESUP:
                binding.result.setText(R.string.timesup_content);
                binding.resultImage.setImageResource(R.mipmap.ic_timer_off_black_24dp);
                builder.neutralText(R.string.show_answer);
                break;
        }
        builder.cancelable(false);
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
                finish();
                startActivity(new Intent(PlayActivity.this, BoardActivity.class));
            }
        });
        builder.onNeutral(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
                new MaterialDialog.Builder(context)
                        .title(R.string.steps_for_result)
                        .items(resultModel.getStepsAsString())
                        .positiveText(R.string.new_game)
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dialog.dismiss();
                                finish();
                                startActivity(new Intent(PlayActivity.this, BoardActivity.class));
                            }
                        }).show();
            }
        });
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
                mainViewModel.clickResetHandler(null);
            }
        });
        builder.title("").customView(binding.getRoot(), false);
        builder.show();
    }

    @Override
    public void onError(String header, String message) {
        new MaterialDialog.Builder(this)
                .title(header)
                .content(message)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
