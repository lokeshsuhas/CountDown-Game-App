package lokesh.countdownapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lokesh on 26-03-2016.
 */
public class ResultModel implements Parcelable {
    public static final Creator<ResultModel> CREATOR = new Creator<ResultModel>() {
        @Override
        public ResultModel createFromParcel(Parcel in) {
            return new ResultModel(in);
        }

        @Override
        public ResultModel[] newArray(int size) {
            return new ResultModel[size];
        }
    };
    private List<StepModel> steps;
    private int finalResult;

    public ResultModel() {
        steps = new ArrayList<>();
        finalResult = 0;
    }

    protected ResultModel(Parcel in) {
        finalResult = in.readInt();
        steps = new ArrayList<>();
        in.readTypedList(steps, StepModel.CREATOR);
    }

    public void addStep(StepModel model) {
        steps.add(model);
    }

    public List<StepModel> getSteps() {
        return steps;
    }

    public List<String> getStepsAsString() {
        List<String> sResult = new ArrayList<>();
        for (StepModel m : steps) {
            sResult.add(m.toString());
        }
        return sResult;
    }

    public int getLastStepValue() {
        return steps.size() > 0 ? steps.get(steps.size() - 1).getResult() : 1;
    }

    public int getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(int finalResult) {
        this.finalResult = finalResult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(finalResult);
        dest.writeTypedList(steps);
    }
}
