package lokesh.countdownapp.Enums;

/**
 * Created by Lokesh on 27-03-2016.
 */
public enum ResultEnum {
    SUCCESS("Success"),
    NEUTRAL("Neutral"),
    FAILED("Failed"),
    TIMESUP("TimesUp");

    private String stringValue;

    ResultEnum(String toString) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
