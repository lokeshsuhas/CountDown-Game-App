package lokesh.countdownapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.NonNull;

import java.util.Random;

/**
 * Created by Lokesh on 25-03-2016.
 */
public class Helpers {

    private static Random random;

    public static int getRnd() {
        if (random == null) {
            random = new Random();
        }
        return random.nextInt();
    }

    /***
     * Get the device screen width
     *
     * @param context
     * @return int
     */
    public static int getScreenWidth(@NonNull Context context) {
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    /***
     * Get the device screen height
     *
     * @param context
     * @return int
     */
    public static int getScreenHeight(@NonNull Context context) {
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    /***
     * Check whether the orientation is in landscape or portrait
     *
     * @param context
     * @return boolean
     */
    public static boolean isInLandscapeMode(@NonNull Context context) {
        boolean isLandscape = false;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
        }
        return isLandscape;
    }

    /**
     * Helps to do the math operation
     * @param left
     * @param right
     * @param operation
     * @return
     */
    public static int doMath(int left, int right, String operation) {
        switch (operation) {
            case "+":
                return (left + right);
            case "-":
                return (left - right);
            case "*":
                return (left * right);
            case "/":
                if (right == 0)
                    return 0;
                else {
                    return (left / right);
                }

        }

        return 0;
    }
}
