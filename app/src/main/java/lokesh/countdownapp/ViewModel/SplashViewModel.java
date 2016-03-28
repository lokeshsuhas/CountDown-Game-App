package lokesh.countdownapp.ViewModel;

import android.view.View;

/**
 * Created by Lokesh on 25-03-2016.
 */
public class SplashViewModel implements IViewModel {
    private SplashViewListener splashViewListener;

    /**
     * Constructor
     *
     * @param listener
     */
    public SplashViewModel(SplashViewListener listener) {
        this.splashViewListener = listener;
    }

    /**
     * Handles the new game click
     *
     * @param view
     */
    public void onNewGameClick(View view) {
        if (this.splashViewListener != null) {
            this.splashViewListener.onNewGameClicked();
        }
    }

    /**
     * Resume activity calls
     */
    @Override
    public void onResume() {

    }

    /**
     * Destroy activity calls
     */
    @Override
    public void onDestroy() {
        splashViewListener = null;
    }

    /**
     * Listeners to pass by the events to the associated activity
     */
    public interface SplashViewListener {
        void onNewGameClicked();
    }
}
