package lokesh.countdownapp.Commanders;

/**
 * Created by Lokesh on 26-03-2016.
 */
public interface Command {
    void execute();

    void undo();
}