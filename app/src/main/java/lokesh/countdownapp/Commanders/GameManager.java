package lokesh.countdownapp.Commanders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lokesh.countdownapp.Models.BoardModel;
import lokesh.countdownapp.Models.ResultModel;
import lokesh.countdownapp.Models.StepModel;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Lokesh on 25-03-2016.
 */
public class GameManager {
    private final List<BoardModel<Integer>> largeNumbers;
    private final List<BoardModel<String>> operations;
    private Random rnd;

    /**
     * Constructor
     */
    public GameManager() {
        rnd = new Random();
        operations = new ArrayList<>(Arrays.asList(
                new BoardModel<String>("+", false, true),
                new BoardModel<String>("-", false, true),
                new BoardModel<String>("/", false, true),
                new BoardModel<String>("*", false, true)));
        largeNumbers = new ArrayList<>(Arrays.asList(
                new BoardModel<Integer>(25, true),
                new BoardModel<Integer>(50, true),
                new BoardModel<Integer>(75, true),
                new BoardModel<Integer>(100, true)));
        Observable.range(1, 10).repeat(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                largeNumbers.add(new BoardModel<Integer>(integer.intValue(), false));
            }
        });
    }

    /**
     * Gets the board numbers
     *
     * @return
     */
    public Observable<BoardModel<Integer>> getAll() {
        return Observable.from(largeNumbers);
    }

    /**
     * Gets the operations
     *
     * @return
     */
    public Observable<BoardModel<String>> getOperations() {
        return Observable.from(operations);
    }

    /**
     * Gets the random game
     *
     * @param data
     * @return
     */
    public ResultModel getGame(List<BoardModel> data) {
        List<BoardModel> cloned = new ArrayList<>(data);
        ResultModel resultModel = new ResultModel();
        int steps = getRndSteps();
        for (int x = 0; x < steps; x++) {
            if (cloned.size() != 1) {
                int leftIndex = rnd.nextInt(cloned.size());
                BoardModel<Integer> leftValue = cloned.remove(leftIndex);
                int rightIndex = rnd.nextInt(cloned.size());
                BoardModel<Integer> rightValue = cloned.remove(rightIndex);
                BoardModel<String> operation = getRndOperation();
                StepModel model = new StepModel(leftValue.getValue().intValue(), rightValue.getValue().intValue(), operation.getValue());
                cloned.add(new BoardModel(model.getResult(), false, false));
                resultModel.addStep(model);
            } else {
                break;
            }
        }

        BoardModel<Integer> last = cloned.size() == 0 ? cloned.get(0) : new BoardModel<>(resultModel.getLastStepValue());
        if (!(last.getValue() >= 1 && last.getValue() <= 999)) {
            return getGame(data);
        }
        resultModel.setFinalResult(last.getValue());
        return resultModel;
    }

    /**
     * Get the random operation
     *
     * @return
     */
    private BoardModel<String> getRndOperation() {
        return operations.get(rnd.nextInt(operations.size()));
    }

    /**
     * Get the random steps
     *
     * @return
     */
    private int getRndSteps() {
        return rnd.nextInt(10 - 1 + 1) + 1;
    }

}
