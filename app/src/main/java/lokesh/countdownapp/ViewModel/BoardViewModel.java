package lokesh.countdownapp.ViewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.view.View;

import java.util.List;

import lokesh.countdownapp.Adapters.adapter.ClickHandler;
import lokesh.countdownapp.Adapters.adapter.binder.ItemBinder;
import lokesh.countdownapp.Adapters.adapter.binder.ItemBinderBase;
import lokesh.countdownapp.BR;
import lokesh.countdownapp.Commanders.GameManager;
import lokesh.countdownapp.Listeners.BaseListener;
import lokesh.countdownapp.Models.BoardModel;
import lokesh.countdownapp.Models.ResultModel;
import lokesh.countdownapp.R;
import lokesh.countdownapp.Utils.Constants;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lokesh on 25-03-2016.
 */
public class BoardViewModel extends BaseObservable implements IViewModel {
    @Bindable
    public ObservableArrayList<BoardModel> selected;
    @Bindable
    public ObservableArrayList<BoardModel> boardData;
    public ObservableField<Integer> numbersCount;

    private GameViewListener gameViewListener;
    private GameManager manager;
    private Context context;

    /**
     * Constructor
     * @param context
     * @param listener
     */
    public BoardViewModel(Context context, GameViewListener listener) {
        this.gameViewListener = listener;
        this.context = context;
        this.boardData = new ObservableArrayList<>();
        selected = new ObservableArrayList<>();
        numbersCount = new ObservableField<>(Constants.SELECT_COUNT);
        manager = new GameManager();
    }

    /**
     * Item binder to load the selected items from the board
     * @return
     */
    public ItemBinder<BoardViewModel> selectedItemViewBinder() {
        return new ItemBinderBase<BoardViewModel>(BR.gameModel, R.layout.recycler_selected_item);
    }

    /**
     * Item binder to load the items to the board
     * @return
     */
    public ItemBinder<BoardViewModel> boardItemViewBinder() {
        return new ItemBinderBase<BoardViewModel>(BR.model, R.layout.recycler_board_item);
    }

    /**
     * Handles the click event of selected items
     * @return
     */
    public ClickHandler<BoardModel> clickSelectedItemHandler() {
        return new ClickHandler<BoardModel>() {
            @Override
            public void onClick(BoardModel viewModel) {
                selected.remove(viewModel);
                numbersCount.set(Constants.SELECT_COUNT - selected.size());
            }
        };
    }

    /**
     * Handles the click event of board items
     * @return
     */
    public ClickHandler<BoardModel> clickBoardItemHandler() {
        return new ClickHandler<BoardModel>() {
            @Override
            public void onClick(BoardModel gModel) {
                if (gModel != null) {
                    if (selected.size() < Constants.SELECT_COUNT) {
                        selected.add(gModel);
                    } else {
                        selected.remove(0);
                        selected.add(gModel);
                    }
                }
                numbersCount.set(Constants.SELECT_COUNT - selected.size());
            }
        };
    }

    /**
     * Handles the click event of start game
     * @param view
     */
    public void onStartGameClick(View view) {
        ResultModel resultModel = manager.getGame(selected);
        if (gameViewListener != null) {
            gameViewListener.onConfirmation(selected, resultModel);
        }
    }

    /**
     * Resume calls from activity
     */
    @Override
    public void onResume() {
        boardData = new ObservableArrayList<>();
        manager.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BoardModel>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BoardModel gameModel) {
                boardData.add(gameModel);
            }
        });
    }

    /**
     * Destroy calls from activity
     */
    @Override
    public void onDestroy() {
        context = null;
        boardData = null;
    }

    /**
     * Listeners to pass by the events to the associated activity
     */
    public interface GameViewListener extends BaseListener {
        /**
         * Raised to get confirmation on the selection made
         * @param selected
         * @param resultModel
         */
        void onConfirmation(List<BoardModel> selected, ResultModel resultModel);
    }
}
