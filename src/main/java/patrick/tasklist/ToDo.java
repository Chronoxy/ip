package patrick.tasklist;

import java.io.IOException;

import patrick.parser.Parser;
import patrick.storage.Storage;
import patrick.ui.Ui;

/**
 * Represents a todo task in the task list.
 * A todo task only contains a description, without any specific time or deadline.
 */
public class ToDo extends Task {

    /**
     * Constructs a {@code ToDo} task with the specified description.
     *
     * @param description the description of the todo task.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task, including its status and description.
     *
     * @return a string representation of the todo task in the format "T | {status icon} | {description}".
     */
    @Override
    public String toString() {
        return "T | " + super.toString();
    }

    /**
     * Processes the user input for adding a new todo task.
     * Validates the input and adds the task to the task list if it is valid.
     *
     * @param input the user input containing the task description.
     * @return a response message indicating the result of the operation.
     * @throws Parser.PatrickException if the description of the todo is empty.
     */
    public static String toDoTask(String input) throws Parser.PatrickException {
        String response = null;
        String taskDescription = input.replace("todo ", "").trim();
        if (taskDescription.isEmpty()) {
            throw new Parser.PatrickException("Description of a todo cannot be empty!!");
        } else {
            Task task = new ToDo(taskDescription);
            Storage.addList(task);
            response = Ui.showUserMsg(task.toString());
            try {
                if (!Storage.getList().isEmpty()) {
                    Storage.appendToFile("\n");
                }
                Storage.appendToFile(task.toString());
            } catch (IOException e) {
                response = Ui.showErrorMsg("There is an error: " + e.getMessage());
            }
        }
        return response;
    }
}
