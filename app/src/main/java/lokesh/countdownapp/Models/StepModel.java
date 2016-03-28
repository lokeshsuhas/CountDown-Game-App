package lokesh.countdownapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import lokesh.countdownapp.Utils.Helpers;

/**
 * Created by Lokesh on 26-03-2016.
 */
public class StepModel implements Parcelable {
    public static final Creator<StepModel> CREATOR = new Creator<StepModel>() {
        @Override
        public StepModel createFromParcel(Parcel in) {
            return new StepModel(in);
        }

        @Override
        public StepModel[] newArray(int size) {
            return new StepModel[size];
        }
    };
    private int leftValue;
    private int rightValue;
    private String operation;

    public StepModel(int left, int right, String operation) {
        this.leftValue = left;
        this.rightValue = right;
        this.operation = operation;
    }

    protected StepModel(Parcel in) {
        leftValue = in.readInt();
        rightValue = in.readInt();
        operation = in.readString();
    }

    public int getLeftValue() {
        return this.leftValue;
    }

    public void setLeftValue(int value) {
        this.leftValue = value;
    }

    public int getRightValue() {
        return this.rightValue;
    }

    public void setRightValue(int value) {
        this.rightValue = value;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getResult() {
        return Helpers.doMath(this.leftValue, this.rightValue, this.operation);
    }

    public String toString() {
        return "( " + this.getLeftValue() + " " + this.getOperation() + " " + this.getRightValue() + " ) = " + this.getResult();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(leftValue);
        dest.writeInt(rightValue);
        dest.writeString(operation);
    }
}
