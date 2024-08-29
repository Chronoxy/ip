package patrick.tasklist;

import patrick.parser.Parser;
import patrick.storage.Storage;
import patrick.ui.Ui;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code TaskList} class manages a list of tasks, allowing operations such as adding, deleting,
 * marking as done or undone, and retrieving tasks from storage.
 */
public class TaskList {
    ArrayList<Task> taskList;

    /**
     * Constructs a {@code TaskList} with the specified list of tasks.
     *
     * @param taskList the list of tasks to initialize the {@code TaskList} with.
     * @throws Parser.PatrickException if the task list is empty.
     */
    public TaskList(ArrayList<Task> taskList) throws Parser.PatrickException {
        if (!taskList.isEmpty()) {
            this.taskList = taskList;
        } else {
            throw new Parser.PatrickException("Your Task List is empty!\n");
        }
    }

    /**
     * Constructs an empty {@code TaskList}.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Deletes a task from the task list based on the user input.
     *
     * @param input the user input containing the index of the task to be deleted.
     * @throws Parser.PatrickException if the input is invalid or the index is out of bounds.
     */
    public static void delete(String input) throws Parser.PatrickException {
        String taskNo = input.replace("delete", "").trim();
        try {
            Integer.parseInt(taskNo);
        } catch (NumberFormatException e) {
            throw new Parser.PatrickException("Delete Task Details must be an integer");
        }
        int num = Integer.parseInt(taskNo);
        if (taskNo.isEmpty()) {
            throw new Parser.PatrickException("Delete Task Details cannot be empty!!");
        } else if (num > Storage.getList().size()) {
            throw new Parser.PatrickException("Input task index is invalid. Please try again!!");
        } else {
            Ui.showDeleteItemMsg(num);
            Storage.deleteItem(num);
            try {
                Storage.writeToFile();
            } catch (IOException e) {
                System.out.println("There is an error: " + e.getMessage());
            }
        }
    }

    /**
     * Marks a task as done based on the user input.
     *
     * @param input the user input containing the index of the task to be marked as done.
     * @throws Parser.PatrickException if the input is invalid, the index is out of bounds, or the task is already marked as done.
     */
    public static void mark(String input) throws Parser.PatrickException {
        String taskNo = input.replace("mark", "").trim();
        if (taskNo.isEmpty()) {
            throw new Parser.PatrickException("Task Number cannot be empty!!");
        } else {
            try {
                Integer.parseInt(taskNo);
            } catch (NumberFormatException e) {
                throw new Parser.PatrickException("Mark Task Details must be an integer");
            }
            int num = Integer.parseInt(taskNo);
            if (num > Storage.getList().size()) {
                throw new Parser.PatrickException("Invalid Task Number!!");
            } else {
                Task curr = (Task) Storage.getList().get(num - 1);
                if (curr.isDone) {
                    throw new Parser.PatrickException("You cannot mark a completed task!!");
                } else {
                    curr.markAsDone();
                    Ui.showMarkUnmarkMsg("Nice! I've marked this task as done:\n  ", curr.toString() + "\n");
                    try {
                        Storage.writeToFile();
                    } catch (IOException e) {
                        System.out.println("There is an error: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Marks a task as not done based on the user input.
     *
     * @param input the user input containing the index of the task to be marked as not done.
     * @throws Parser.PatrickException if the input is invalid, the index is out of bounds, or the task is already marked as not done.
     */
    public static void unmark(String input) throws Parser.PatrickException {
        String taskNo = input.replace("unmark", "").trim();
        if (taskNo.isEmpty()) {
            throw new Parser.PatrickException("Task Number cannot be empty!!");
        } else {
            try {
                Integer.parseInt(taskNo);
            } catch (NumberFormatException e) {
                throw new Parser.PatrickException("Unmark Task Details must be an integer");
            }
            int num = Integer.parseInt(taskNo);

            if (num > Storage.getList().size()) {
                throw new Parser.PatrickException("Invalid Task Number!!");
            } else {
                Task curr = (Task) Storage.getList().get(num - 1);
                if (!curr.isDone) {
                    throw new Parser.PatrickException("You cannot unmark an incomplete task!!");
                } else {
                    curr.markAsUndone();
                    Ui.showMarkUnmarkMsg("Nice! I've marked this task as not done yet:\n  ", curr.toString() + "\n");
                    try {
                        Storage.writeToFile();
                    } catch (IOException e) {
                        System.out.println("There is an error: " + e.getMessage());
                    }
                }
            }
        }
    }

    public static void findTask(String input) throws Parser.PatrickException {
        int count = 0;
        String keyword = input.replace("find", "").trim();
        if (keyword.isEmpty()) {
            throw new Parser.PatrickException("Find keyword cannot be empty!");
        } else {
            for (int i = 0; i < Storage.getList().size(); i++) {
                if (Storage.getList().get(i).toString().contains(keyword)) {
                    if (count == 0) {
                        Ui.printLine();
                        Ui.showMsg("Here are the matching tasks in your list:\n");
                    }
                    count++;
                    Ui.showMsg(count + " " + Storage.getList().get(i).toString() + "\n");
                }
            }
            if (count == 0) {
                Ui.showErrorMsg("There are no matching tasks in your list!");
            }
        }
    }
}
