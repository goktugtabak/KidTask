package file;

import domain.*;
import manager.TaskManager;
import manager.WishManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a single command line and executes it (ADD_TASK1, WISH_CHECKED, etc.)
 */
public class CommandHandler {
    private TaskManager taskManager;
    private WishManager wishManager;
    private Child child;
    private Parent parent;
    private Teacher teacher;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CommandHandler(TaskManager taskManager, WishManager wishManager,
                          Child child, Parent parent, Teacher teacher) {
        this.taskManager = taskManager;
        this.wishManager = wishManager;
        this.child = child;
        this.parent = parent;
        this.teacher = teacher;
    }

    public void parseCommand(String rawLine) {

    /* -----------------------------------------------------------------
       1)  Satırın "//" ile başlayan kısmı yorumdur; komut dışında bırakılır
       ----------------------------------------------------------------- */
        String commandLine = rawLine.split("//")[0].trim();
        if (commandLine.isEmpty()) return;      // Satır tamamen yorumsa / boşsa çık.

    /* -----------------------------------------------------------------
       2)  Log çıktısı – kullanıcıya ham satırı göstermek istiyoruz
       ----------------------------------------------------------------- */
        System.out.println("\n[COMMAND] " + rawLine);

    /* -----------------------------------------------------------------
       3)  Tokenizasyon  (tırnak içini koruyan regex ile)
       ----------------------------------------------------------------- */
        String[] tokens = tokenizeCommand(commandLine);
        if (tokens.length == 0) return;

    /* -----------------------------------------------------------------
       4)  Komuta göre işlem
       ----------------------------------------------------------------- */
        String cmd = tokens[0];

        switch (cmd) {
            case "ADD_TASK1":
                handleAddTask1(tokens);
                break;

            case "ADD_TASK2":
                handleAddTask2(tokens);
                break;

            case "LIST_ALL_TASKS":
                handleListAllTasks(tokens);
                break;

            case "LIST_ALL_WISHES":
                handleListAllWishes();
                break;

            case "TASK_DONE":
                handleTaskDone(tokens);
                break;

            case "TASK_CHECKED":
                handleTaskChecked(tokens);
                break;

            case "ADD_WISH1":
                handleAddWish1(tokens);
                break;

            case "ADD_WISH2":
                handleAddWish2(tokens);
                break;

            case "WISH_CHECKED":
                handleWishChecked(tokens);
                break;

            case "ADD_BUDGET_COIN":
                handleAddBudgetCoin(tokens);
                break;

            case "PRINT_BUDGET":
                child.printBudget();
                break;

            case "PRINT_STATUS":
                System.out.println(child.printStatus());
                break;

            default:
                System.out.println("[WARNING] Unknown command: " + cmd);
        }
    }

    private String[] tokenizeCommand(String cmdLine) {
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("[^\\s\"]+|\"[^\"]*\"");
        Matcher m = p.matcher(cmdLine);
        while (m.find()) {
            String token = m.group().replace("\"", "");
            list.add(token);
        }
        return list.toArray(new String[0]);
    }

    // ... handleAddTask1, handleAddTask2, handleListAllTasks vs.
    // kısaltmak adına direkt paylaşıyorum:

    private void handleAddTask1(String[] tokens) {
        if (tokens.length < 8) {
            System.err.println("ADD_TASK1 command format error.");
            return;
        }
        String who = tokens[1]; // T veya F
        int taskID = Integer.parseInt(tokens[2]);
        String title = tokens[3];
        String desc = tokens[4];
        String dateStr = tokens[5];
        String timeStr = tokens[6];
        int points = Integer.parseInt(tokens[7]);

        LocalDateTime deadline = LocalDateTime.parse(dateStr + " " + timeStr, dtf);
        User assignedBy = who.equalsIgnoreCase("T") ? teacher : parent;

        Task task = new Task(taskID, title, desc, deadline, points, assignedBy);
        taskManager.addTask(task);
        child.getTaskList().add(task);
    }

    private void handleAddTask2(String[] tokens) {
        if (tokens.length < 10) {
            System.err.println("ADD_TASK2 command format error.");
            return;
        }
        String who = tokens[1];
        int taskID = Integer.parseInt(tokens[2]);
        String title = tokens[3];
        String desc = tokens[4];
        String startDate = tokens[5];
        String startTime = tokens[6];
        String endDate = tokens[7];
        String endTime = tokens[8];
        int points = Integer.parseInt(tokens[9]);

        LocalDateTime start = LocalDateTime.parse(startDate + " " + startTime, dtf);
        LocalDateTime end = LocalDateTime.parse(endDate + " " + endTime, dtf);

        User assignedBy = who.equalsIgnoreCase("T") ? teacher : parent;
        Task task = new Task(taskID, title, desc, start, end, points, assignedBy);
        taskManager.addTask(task);
        child.getTaskList().add(task);
    }

    private void handleListAllTasks(String[] tokens) {
        if (tokens.length > 1) {
            String filter = tokens[1];
            var filtered = taskManager.listTasks(filter);
            for (Task t : filtered) {
                System.out.println(t.getTaskID() + " " + t.getTitle() + " (" + t.getStatus() + ")");
            }
        } else {
            var all = taskManager.listTasks("ALL");
            for (Task t : all) {
                System.out.println(t.getTaskID() + " " + t.getTitle() + " (" + t.getStatus() + ")");
            }
        }
    }

    private void handleListAllWishes() {
        var wlist = wishManager.listAllWishes(child);
        for (Wish w : wlist) {
            System.out.println("Wish " + w.getWishID() + " " + w.getTitle()
                    + " (" + w.getWishStatus() + "), requiredLevel=" + w.getRequiredLevel());
        }
    }

    private void handleTaskDone(String[] tokens) {
        if (tokens.length >= 2) {
            int taskId = Integer.parseInt(tokens[1]);
            child.markTaskAsDone(taskId);
        } else {
            System.err.println("TASK_DONE <taskID>");
        }
    }

    private void handleTaskChecked(String[] tokens) {
        if (tokens.length >= 3) {
            int taskId = Integer.parseInt(tokens[1]);
            int rating = Integer.parseInt(tokens[2]);
            taskManager.approveTaskAndRate(taskId, rating, child);
        } else {
            System.err.println("TASK_CHECKED <taskID> <rating>");
        }
    }

    private void handleAddWish1(String[] tokens) {
        if (tokens.length < 4) {
            System.err.println("ADD_WISH1 command format error.");
            return;
        }
        String wishIDstr = tokens[1];
        int wishID = Integer.parseInt(wishIDstr.substring(1));
        String title = tokens[2];
        String desc = tokens[3];

        Wish wish = new Wish(wishID, title, desc, WishType.PRODUCT);
        wishManager.addWish(wish);
        child.addWish(wish);
    }

    private void handleAddWish2(String[] tokens) {
        if (tokens.length < 4) {
            System.err.println("ADD_WISH2 command format error.");
            return;
        }
        String wishIDstr = tokens[1];
        int wishID = Integer.parseInt(wishIDstr.substring(1));
        String title = tokens[2];
        String desc = tokens[3];

        if (tokens.length == 4) {
            Wish w = new Wish(wishID, title, desc, WishType.ACTIVITY);
            wishManager.addWish(w);
            child.addWish(w);
        } else if (tokens.length >= 8) {
            String startDate = tokens[4];
            String startTime = tokens[5];
            String endDate = tokens[6];
            String endTime = tokens[7];

            LocalDateTime dateTime = LocalDateTime.parse(startDate + " " + startTime, dtf);
            Wish w = new Wish(wishID, title, desc, WishType.ACTIVITY, dateTime);
            wishManager.addWish(w);
            child.addWish(w);
        }
    }

    private void handleWishChecked(String[] tokens) {
        // WISH_CHECKED W102 APPROVED 3
        // WISH_CHECKED W103 REJECTED
        if (tokens.length < 3) {
            System.err.println("WISH_CHECKED format error.");
            return;
        }
        String wishIDstr = tokens[1];
        int wishID = Integer.parseInt(wishIDstr.substring(1));
        String status = tokens[2];

        boolean isApproved = status.equalsIgnoreCase("APPROVED");
        int requiredLevel = 0;
        if (isApproved && tokens.length >= 4) {
            requiredLevel = Integer.parseInt(tokens[3]);
        }
        wishManager.approveOrRejectedWish(wishID, isApproved, requiredLevel, child);
    }

    private void handleAddBudgetCoin(String[] tokens) {
        // ADD_BUDGET_COIN [T/F] <amount>
        // gibi.
        if (tokens.length == 2) {
            // eski kullanım
            int amount = Integer.parseInt(tokens[1]);
            parent.addBudgetPoints(child, amount);
        } else if (tokens.length >= 3) {
            String who = tokens[1];
            int amount = Integer.parseInt(tokens[2]);
            if (who.equalsIgnoreCase("T")) {
                teacher.addBudgetPoints(child, amount);
            } else {
                parent.addBudgetPoints(child, amount);
            }
        } else {
            System.err.println("ADD_BUDGET_COIN format error.");
        }
    }
}
