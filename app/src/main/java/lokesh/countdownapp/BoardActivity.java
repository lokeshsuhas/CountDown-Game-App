package lokesh.countdownapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import lokesh.countdownapp.Models.BoardModel;
import lokesh.countdownapp.Models.ResultModel;
import lokesh.countdownapp.Utils.Constants;
import lokesh.countdownapp.ViewModel.BoardViewModel;
import lokesh.countdownapp.databinding.ActivityGameBoardBinding;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BoardActivity extends BaseActivity implements BoardViewModel.GameViewListener {
    private ActivityGameBoardBinding gameBinding;
    private BoardViewModel model;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        model = new BoardViewModel(context, this);
        gameBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_board);
        gameBinding.setViewModel(model);
        setRecyclerView();
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

    @Override
    public void onConfirmation(final List<BoardModel> selected, final ResultModel resultModel) {
        final List<Integer> d = new ArrayList<>();
        Observable.from(selected).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BoardModel>() {
            @Override
            public void onCompleted() {
                new MaterialDialog.Builder(context)
                        .title(R.string.confirmation_title)
                        .content(String.format(getResources().getString(R.string.selected_content), TextUtils.join(",", d)))
                        .positiveText(R.string.start)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dialog.dismiss();
                                finish();
                                Intent intent = new Intent(BoardActivity.this, PlayActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("result", resultModel);
                                bundle.putParcelableArrayList("game", (ArrayList<? extends Parcelable>) selected);
                                intent.putExtra("r", bundle);
                                startActivity(intent);
                            }
                        })
                        .negativeText(R.string.cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BoardModel gameModel) {
                d.add(Integer.valueOf(gameModel.getValue().toString()));
            }
        });

    }

    private void setRecyclerView() {
        gameBinding.RecyclerView.setLayoutManager(new GridLayoutManager(context, Constants.COLUMN_PORTRAIT));
        gameBinding.activityUsersRecycler.setLayoutManager(new GridLayoutManager(context, Constants.SELECT_COUNT));
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
