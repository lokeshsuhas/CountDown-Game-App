package lokesh.countdownapp.Models;

import lokesh.countdownapp.Commanders.Command;
import lokesh.countdownapp.ViewModel.PlayViewModel;

/**
 * Created by Lokesh on 26-03-2016.
 */
public class GameStepModel extends StepModel implements Command {
    private int leftIndex;
    private int rightIndex;
    private PlayViewModel model;

    public GameStepModel(PlayViewModel viewModel) {
        super(-1, -1, "");
        this.model = viewModel;
        this.leftIndex = -1;
        this.rightIndex = -1;
    }

    public int getLeftIndex() {
        return this.leftIndex;
    }

    public void setLeftIndex(int index) {
        this.leftIndex = index;
    }

    public int getRightIndex() {
        return this.rightIndex;
    }

    public void setRightIndex(int index) {
        this.rightIndex = index;
    }

    public void set(BoardModel m, int index) {
        if (m.getOperation()) {
            this.setOperation(m.getValue().toString());
        } else {
            model.selectedModels.set(index, new BoardModel<String>("X", false, false));
            if (this.getLeftIndex() == -1) {
                this.setLeftIndex(index);
            } else {
                this.setRightIndex(index);
            }

            if (this.getLeftValue() == -1) {
                this.setLeftValue(Integer.valueOf(m.getValue().toString()));
            } else {
                this.setRightValue(Integer.valueOf(m.getValue().toString()));
            }
        }
    }

    public boolean isDone() {
        return this.getRightIndex() != -1 &&
                this.getLeftIndex() != -1 &&
                !this.getOperation().equals("") &&
                this.getLeftValue() != -1 &&
                this.getRightValue() != -1;
    }

    @Override
    public void execute() {
        model.selectedModels.set(this.leftIndex, new BoardModel<Integer>(this.getResult(), false, false));
        model.selectedModels.remove(this.rightIndex);
        model.yourResult.set(this.getResult());
    }

    @Override
    public void undo() {
        model.selectedModels.add(this.rightIndex, new BoardModel<Integer>(this.getRightValue(), false, false));
        model.selectedModels.set(this.leftIndex, new BoardModel<Integer>(this.getLeftValue(), false, false));
        model.stepModels.remove(model.stepModels.size() - 1);
        if (model.stepModels.size() == 0) {
            model.yourResult.set(0);
        } else {
            model.yourResult.set(this.getResult());
        }
    }
}
