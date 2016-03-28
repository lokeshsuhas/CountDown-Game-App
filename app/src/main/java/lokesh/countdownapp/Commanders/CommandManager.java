package lokesh.countdownapp.Commanders;

import java.util.Stack;

/**
 * Created by Lokesh on 26-03-2016.
 */
public class CommandManager {
    private Stack<Command> lastCommands;

    public CommandManager() {
        lastCommands = new Stack<>();
    }

    /**
     * Execute the command and pushes the command to the stack
     * @param c
     */
    public void executeCommand(Command c) {
        c.execute();
        lastCommands.push(c);
    }

    /**
     * Clears the stack
     */
    public void clear() {
        lastCommands.clear();
    }

    /**
     * Check can undo
     * @return
     */
    public boolean canUndo() {
        return !lastCommands.isEmpty();
    }

    /**
     * Takes the top command and does the undo
     */
    public void undo() {
        Command cmd = lastCommands.pop();
        cmd.undo();
    }
}