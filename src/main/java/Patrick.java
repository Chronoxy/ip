import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Patrick {
    static String horizontalLine = "____________________________________________________________\n";
    static String greetingMsg = horizontalLine + "Hello! I'm Patrick, Spongebob bestie\nHow can I help you?\n" + horizontalLine;
    static String exitMsg = horizontalLine + "Bye. Hope to see you again soon!\n" + horizontalLine;
    static String taskMsg = "Got it. I've added this task:\n";
    static String numTaskMsg1 = "Now you have ";
    static String numTaskMsg2 = " tasks in the list.\n";
    static String input;
    static ArrayList<Task> list = new ArrayList<>();
    static Task task;
    public enum Type {
        LIST, BYE, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, ERROR
    }
    static Type inputType;
    static File file;
    static String FILE_PATH = "data/tasks.txt";

    public static void main(String[] args) {
        Scanner inputMsg = new Scanner(System.in);
        System.out.println(greetingMsg);

        do {
            input = inputMsg.nextLine();
            CheckType(input);
            switch (inputType) {
                case LIST:
                    loadFile(FILE_PATH);
                    break;

                case BYE:
                    System.out.println(exitMsg);
                    break;

                case MARK:
                    try {
                        Mark(input);
                        break;
                    } catch (PatrickException e) {
                        System.out.print(horizontalLine + e.toString() + "\n" + horizontalLine);
                    }

                case UNMARK:
                    try {
                        Unmark(input);
                        break;
                    } catch (PatrickException e) {
                        System.out.println(horizontalLine + e.toString() + "\n" + horizontalLine);
                    }
    
                case TODO:
                    try {
                        ToDoTask(input);
                        break;
                    } catch (PatrickException e) {
                        System.out.print(horizontalLine + e.toString() + "\n" + horizontalLine);
                    }

                case DEADLINE:
                    try {
                        DeadlineTask(input);
                        break;
                    } catch (PatrickException e) {
                        System.out.print(horizontalLine + e.toString() + "\n" + horizontalLine);
                    }

                case EVENT:
                    try {
                        EventTask(input);
                        break;
                    } catch (PatrickException e) {
                        System.out.print(horizontalLine + e.toString() + "\n" + horizontalLine);
                    }

                case DELETE:
                    try {
                        Delete(input);
                        break;
                    } catch (PatrickException e) {
                        System.out.print(horizontalLine + e.toString() + "\n" + horizontalLine);
                    }

                default:
                    System.out.println(horizontalLine + "What are you trying to say man. Re-enter your command \n" + horizontalLine);
                    break;
            }
        } while (!inputType.equals(Type.BYE));
    }

    private static void ToDoTask(String input) throws PatrickException {
        String taskDescription = input.replace("todo", "");
        if (taskDescription.isEmpty()) {
            throw new PatrickException("Description of a todo cannot be empty!!");
        } else {
            task = new ToDo(taskDescription);
            list.add(task);
            System.out.println(horizontalLine + taskMsg + task.toString() + "\n" + numTaskMsg1
                    + list.size() + numTaskMsg2 + horizontalLine);
        }
    }

    private static void DeadlineTask(String input) throws PatrickException {
        String newInput = input.replace("deadline", "");
        if (newInput.isEmpty()) {
            throw new PatrickException("Deadline Task Details cannot be empty!!");
        }
        else if (!newInput.contains("/by")) {
            throw new PatrickException("You are missing a '/by' in ur details!!");
        }
        else {
            String taskDescription = newInput.substring(0, newInput.indexOf("/") - 1);
            if (taskDescription.isEmpty()) {
                throw new PatrickException("Deadline Task Description cannot be empty!!");
            }
            else {
                String deadline = newInput.substring(newInput.indexOf("/by")).replace("/by", "");
                if (deadline.isEmpty()) {
                    throw new PatrickException("Deadline Task deadline cannot be empty!!");
                }
                else {
                    task = new Deadline(taskDescription, deadline);
                    list.add(task);
                    System.out.println(horizontalLine + taskMsg + task.toString() + "\n" + numTaskMsg1
                            + list.size() + numTaskMsg2 + horizontalLine);
                }
            }
        }
    }

    private static void EventTask(String input) throws PatrickException {
        String newInput = input.replace("event", "");
        if (newInput.isEmpty()) {
            throw new PatrickException("Event Task Details cannot be empty!!");
        } else if (!newInput.contains("/from")) {
            throw new PatrickException("You are missing a '/from' in your details!!");
        } else if (!newInput.contains("/to")) {
            throw new PatrickException("You are missing a 'to' in your details!!");
        } else {
            String taskDescription = newInput.substring(0, newInput.indexOf("/from") - 1);
            if (taskDescription.isEmpty()) {
                throw new PatrickException("Event Task Description cannot be empty!!");
            } else {
                String from = newInput.substring(newInput.indexOf("/from"), newInput.indexOf("/to") - 1).replace("/from", "");
                String to = newInput.substring(newInput.indexOf("/to")).replace("/to", "");
                if (from.isEmpty()) {
                    throw new PatrickException("You are missing 'from' information from your details!!");
                } else if (to.isEmpty()) {
                    throw new PatrickException("You are missing 'to' information from your details!!");
                } else {
                    task = new Event(taskDescription, from, to);
                    list.add(task);
                    System.out.println(horizontalLine + taskMsg + task.toString() + "\n" + numTaskMsg1
                            + list.size() + numTaskMsg2 + horizontalLine);
                }
            }
        }
    }

    private static void Mark(String input) throws PatrickException {
        String taskNo = input.replace("mark", "").trim();
        if (taskNo.isEmpty()) {
            throw new PatrickException("Task Number cannot be empty!!");
        } else {
            try {
                Integer.parseInt(taskNo);
            } catch (NumberFormatException e) {
                throw new PatrickException("Mark Task Details must be an integer");
            }
            int num = Integer.parseInt(taskNo);
            if (num > list.size()) {
                throw new PatrickException("Invalid Task Number!!");
            } else {
                Task curr = (Task) list.get(num - 1);
                if (curr.isDone) {
                    throw new PatrickException("You cannot mark a completed task!!");
                } else {
                    curr.markAsDone();
                    System.out.println(horizontalLine + "Nice! I've marked this task as done:\n  "
                            + curr.toString() + "\n" + horizontalLine);
                }
            }
        }
    }

    private static void Unmark(String input) throws PatrickException {
        String taskNo = input.replace("unmark", "").trim();
        if (taskNo.isEmpty()) {
            throw new PatrickException("Task Number cannot be empty!!");
        } else {
            try {
                Integer.parseInt(taskNo);
            } catch (NumberFormatException e) {
                throw new PatrickException("Unmark Task Details must be an integer");
            }
            int num = Integer.parseInt(taskNo);

            if (num > list.size()) {
                throw new PatrickException("Invalid Task Number!!");
            } else {
                Task curr = (Task) list.get(num - 1);
                if (!curr.isDone) {
                    throw new PatrickException("You cannot unmark an incomplete task!!");
                } else {
                    curr.markAsUndone();
                    System.out.println(horizontalLine + "Nice! I've marked this task as as not done yet:\n  "
                            + curr.toString() + "\n" + horizontalLine);
                }
            }
        }
    }

    private static void Delete(String input) throws PatrickException {
        String taskNo = input.replace("delete", "").trim();
        try {
            Integer.parseInt(taskNo);
        } catch (NumberFormatException e) {
            throw new PatrickException("Delete Task Details must be an integer");
        }
        int num = Integer.parseInt(taskNo);
        if (taskNo.isEmpty()) {
            throw new PatrickException("Delete Task Details cannot be empty!!");
        }
        else if (num > list.size()){
            throw new PatrickException("Input task index is invalid. Please try again!!");
        }
        else {
            System.out.println(horizontalLine + "Noted. I've removed this task:\n"
                + list.get(num - 1).toString() + "\n" + numTaskMsg1 + (list.size() - 1) + numTaskMsg2);
            list.remove(num - 1);
        }
    }

    private static void CheckType(String input) {
        if (input.startsWith("list"))
            inputType = Type.LIST;
        else if (input.startsWith("bye"))
            inputType = Type.BYE;
        else if (input.startsWith("mark"))
            inputType = Type.MARK;
        else if (input.startsWith("unmark"))
            inputType = Type.UNMARK;
        else if (input.startsWith("todo"))
            inputType = Type.TODO;
        else if (input.startsWith("deadline"))
            inputType = Type.DEADLINE;
        else if (input.startsWith("event"))
            inputType = Type.EVENT;
        else if (input.startsWith("delete"))
            inputType = Type.DELETE;
        else
            inputType = Type.ERROR;
    }

    public static class Task {
        protected String description;
        protected boolean isDone;

        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        public String getStatusIcon() {
            return (isDone ? "X" : " ");
        }

        public void markAsDone() {
            this.isDone = true;
        }

        public void markAsUndone() {
            this.isDone = false;
        }

        @Override
        public String toString() {
            return "[" + getStatusIcon() + "]" + this.description;
        }

    }

    public static class Deadline extends Task {
        protected String by;

        public Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        public String toString() {
            return "[D]" + super.toString() + " (by:" + this.by + ")";
        }
    }

    public static class ToDo extends Task {
        public ToDo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }
    }

    public static class Event extends Task {
        protected String from, to;

        public Event(String description, String from, String to) {
            super(description);
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "[E]" + super.toString() + " (from:" + this.from + " to:" + this.to + ")";
        }
    }

    public static class PatrickException extends Exception {
        String str;
        public PatrickException(String str) {
            super(str);
        }
    }

    private static void printFileContents(String filepath) throws FileNotFoundException {
        file = new File(filepath);
        Scanner s = new Scanner(file);
        while (s.hasNext()) {
            System.out.println(s.nextLine());
        }
    }

    private static void loadFile(String filePath) {
        System.out.println(horizontalLine + "Here are the tasks in your list:");
        try {
            printFileContents(filePath);
        } catch (FileNotFoundException e) {
            System.out.println(horizontalLine + e.toString() + "\n" + horizontalLine);
        }
        System.out.println(horizontalLine);
    }
}
