package lokesh.countdownapp.ViewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lokesh.countdownapp.Adapters.adapter.ClickHandler;
import lokesh.countdownapp.Adapters.adapter.binder.ItemBinder;
import lokesh.countdownapp.Adapters.adapter.binder.ItemBinderBase;
import lokesh.countdownapp.BR;
import lokesh.countdownapp.Commanders.CommandManager;
import lokesh.countdownapp.Commanders.GameManager;
import lokesh.countdownapp.Enums.ResultEnum;
import lokesh.countdownapp.Listeners.BaseListener;
import lokesh.countdownapp.Models.BoardModel;
import lokesh.countdownapp.Models.GameStepModel;
import lokesh.countdownapp.Models.ResultModel;
import lokesh.countdownapp.Models.StepModel;
import lokesh.countdownapp.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lokesh on 26-03-2016.
 */
public class PlayViewModel extends BaseObservable implements IViewModel {
    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;
    @Bindable
    public ObservableArrayList<BoardModel> selectedModels;
    @Bindable
    public ObservableArrayList<StepModel> stepModels;
    public ObservableInt targetResult;
    public ObservableField<String> timerText;
    public ObservableInt yourResult;
    public ObservableField<String> undoButtonText;
    private ResultModel resultModel;
    private GameMainViewListener mainViewListener;
    private CountDownTimer countDownTimer;
    private GameStepModel model = null;
    private CommandManager commandManager;
    private GameManager gameManager;
    private Intent intent;
    private boolean timerHasStarted = false;

    /**
     * Constructor
     *
     * @param intent
     * @param listener
     */
    public PlayViewModel(Intent intent, GameMainViewListener listener) {
        this.intent = intent;
        selectedModels = new ObservableArrayList<>();
        stepModels = new ObservableArrayList<>();
        timerText = new ObservableField<>();
        commandManager = new CommandManager();
        gameManager = new GameManager();
        targetResult = new ObservableInt(1);
        yourResult = new ObservableInt(0);
        undoButtonText = new ObservableField<>("Undo");
        mainViewListener = listener;
        countDownTimer = new GameCountDownTimer(startTime, interval);
        yourResult.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                int state = ((ObservableInt) sender).get();
                if (state > 0) {
                    if (state == targetResult.get()) {
                        countDownTimer.cancel();
                        if (mainViewListener != null) {
                            mainViewListener.onResult(ResultEnum.SUCCESS, resultModel);
                        }
                    } else if (selectedModels.size() == 5) {

                        int r = Integer.valueOf(selectedModels.get(0).getValue().toString());
                        int result = Math.abs(r - targetResult.get());
                        if (result <= 5 && result >= 1) {
                            mainViewListener.onResult(ResultEnum.NEUTRAL, resultModel);
                        } else {
                            mainViewListener.onResult(ResultEnum.FAILED, resultModel);
                        }
                    }
                }
            }
        });
    }

    /**
     * Resume activity calls
     */
    @Override
    public void onResume() {
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
        }
        if (stepModels.size() == 0) {
            stepModels = new ObservableArrayList<>();
        }
        if (selectedModels.size() == 0) {
            selectedModels = new ObservableArrayList<>();
            loadItems();
        }
    }

    /**
     * Destroy activity calls
     */
    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        intent = null;
        selectedModels = null;
        targetResult = null;
        resultModel = null;
        mainViewListener = null;
        stepModels = null;
        timerText = null;
        yourResult = null;
        commandManager = null;
        gameManager = null;
        countDownTimer = null;
    }

    /**
     * Item binder for the game steps done by user
     *
     * @return
     */
    public ItemBinder<StepModel> gameStepItemViewBinder() {
        return new ItemBinderBase<StepModel>(BR.stepModel, R.layout.recycler_step_item);
    }

    /**
     * Item binder for the items selected from the board along with the operations
     *
     * @return
     */
    public ItemBinder<BoardViewModel> gameItemViewBinder() {
        return new ItemBinderBase<BoardViewModel>(BR.model, R.layout.recycler_board_item);
    }

    /**
     * Handles the undo operation
     *
     * @param view
     */
    public void clickUndoHandler(View view) {
        if (model != null) {
            if (model.getLeftIndex() != -1) {
                selectedModels.set(model.getLeftIndex(), new BoardModel(model.getLeftValue(), false));
            }

            if (model.getRightIndex() != -1) {
                selectedModels.set(model.getRightIndex(), new BoardModel(model.getRightValue(), false));
            }
            model = null;
        } else if (commandManager.canUndo()) {
            commandManager.undo();
        }
    }

    /**
     * Handles the reset operation, resets to original but will not reset the timer
     *
     * @param view
     */
    public void clickResetHandler(View view) {
        commandManager.clear();
        model = null;
        yourResult.set(0);
        if (stepModels.size() > 0) {
            stepModels.clear();
            selectedModels.clear();
            loadItems();
        }
    }

    /**
     * Handles the item click
     *
     * @return
     */
    public ClickHandler<BoardModel> clickItemHandler() {
        return new ClickHandler<BoardModel>() {
            @Override
            public void onClick(BoardModel viewModel) {
                if (model == null) {
                    model = new GameStepModel(PlayViewModel.this);
                }
                if (viewModel.getValue().equals("X") && selectedModels.size() <= 5) {
                    return;
                }
                if (!model.isDone()) {
                    undoButtonText.set("Clear");
                    model.set(viewModel, selectedModels.indexOf(viewModel));
                    if (model.isDone()) {
                        undoButtonText.set("Undo");
                        stepModels.add(model);
                        commandManager.executeCommand(model);
                        model = null;
                    }
                }
            }
        };
    }

    /**
     * Load the items to the panel
     */
    private void loadItems() {
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("r");
            if (bundle != null) {
                List<BoardModel> arr = bundle.getParcelableArrayList("game");
                resultModel = bundle.getParcelable("result");
                if(resultModel != null && arr != null) {
                    targetResult.set(resultModel.getFinalResult());
                    Observable.from(arr).mergeWith(gameManager.getOperations()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<BoardModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("error", e.getMessage());
                        }

                        @Override
                        public void onNext(BoardModel model) {
                            selectedModels.add(model);
                        }
                    });
                }
                else
                {
                    mainViewListener.onError("No Result","Result Steps is not found!");
                }
            }
        }
    }

    /**
     * Listeners to pass by the events to the associated activity
     */
    public interface GameMainViewListener extends BaseListener {
        /**
         * Raised when the user reaches the result or timer is up
         *
         * @param resultEnum  @{link ResultEnum}
         * @param resultModel
         */
        void onResult(ResultEnum resultEnum, ResultModel resultModel);
    }

    /**
     * Game count down timer
     */
    public class GameCountDownTimer extends CountDownTimer {
        public GameCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        /**
         * Raises once the start time is reached
         */
        @Override
        public void onFinish() {
            timerText.set("Time's up!");
            mainViewListener.onResult(ResultEnum.TIMESUP, resultModel);
        }

        /**
         * Raises on each interval
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            timerText.set(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

        }
    }

}
