package lokesh.countdownapp.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lokesh on 25-03-2016.
 */
public class BoardModel<T> extends BaseObservable implements Parcelable {
    public static final Creator<BoardModel> CREATOR = new Creator<BoardModel>() {
        @Override
        public BoardModel createFromParcel(Parcel in) {
            return new BoardModel(in);
        }

        @Override
        public BoardModel[] newArray(int size) {
            return new BoardModel[size];
        }
    };
    private static ClassLoader mClassLoader;
    private int tag;
    private T value;
    private boolean isLarge;
    private boolean isOperation;

    public BoardModel(T v) {
        this(v, false, false);
    }

    public BoardModel(T v, boolean isLarge) {
        this(v, isLarge, false);
    }

    public BoardModel(T v, boolean i, boolean o) {
        value = v;
        if (value != null) {
            BoardModel.mClassLoader = value.getClass().getClassLoader();
        }
        isLarge = i;
        isOperation = o;
    }

    protected BoardModel(Parcel in) {
        tag = in.readInt();
        isLarge = in.readByte() != 0;
        isOperation = in.readByte() != 0;
        value = (T) in.readValue(BoardModel.mClassLoader);
    }

    public boolean getOperation() {
        return this.isOperation;
    }

    public void setOperation(boolean o) {
        isOperation = o;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Bindable
    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Bindable
    public boolean getIsLarge() {
        return this.isLarge;
    }

    public void setIsLarge(boolean isLarge) {
        this.isLarge = isLarge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tag);
        dest.writeByte((byte) (isLarge ? 1 : 0));
        dest.writeByte((byte) (isOperation ? 1 : 0));
        dest.writeValue(value);
    }
}
