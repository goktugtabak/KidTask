package file;

import domain.*;
import manager.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // commands.txt içindeki satırları okur ve parseCommand'a gönderir
    public void processCommandsFromFile(String filePath) {
        System.out.println("Reading commands from file: " + filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    parseCommand(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading commands file: " + e.getMessage());
        }
    }

    // Girilen bir komutu (satırı) tokenize edip ilgili işlemi yapar
    public void parseCommand(String commandLine) {
        System.out.println("\n[COMMAND] " + commandLine);
        // Tokenize edelim (tırnak içi korunsun)
        String[] tokens = tokenizeCommand(commandLine);
        if (tokens.length == 0) return;

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

    // Tırnak içini koruyarak tokenleyecek basit regex
    private String[] tokenizeCommand(String cmdLine) {
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("[^\\s\"]+|\"[^\"]*\"");
        Matcher m = p.matcher(cmdLine);
        while (m.find()) {
            // Bulduğu grupta tırnak varsa kaldırıyoruz
            String token = m.group().replace("\"", "");
            list.add(token);
        }
        return list.toArray(new String[0]);
    }

    // -----------------------------
    // Aşağıdaki handle metotları
    // parseCommand içinde switch-case ile çağrılıyor.
    // Mevcut FileHandler’daki mantığı buraya kopyalayabilirsiniz.
    // -----------------------------

    private void handleAddTask1(String[] tokens) {
        // Örn: ADD_TASK1 T 101 "Math Homework" "Solve pages 10 to 20" 2025-03-01 15:00 10
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
        // Örn: ADD_TASK2 F 102 "School Picnic" "Göksu Park" 2025-03-05 10:00 2025-03-05 12:00 10
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
            List<Task> filtered = taskManager.listTasks(filter);
            for (Task t : filtered) {
                System.out.println(t.getTaskID() + " " + t.getTitle() + " (" + t.getStatus() + ")");
            }
        } else {
            List<Task> all = taskManager.listTasks("ALL");
            for (Task t : all) {
                System.out.println(t.getTaskID() + " " + t.getTitle() + " (" + t.getStatus() + ")");
            }
        }
    }

    private void handleTaskDone(String[] tokens) {
        // TASK_DONE 101
        if (tokens.length >= 2) {
            int taskId = Integer.parseInt(tokens[1]);
            child.markTaskAsDone(taskId);
        }
    }

    private void handleTaskChecked(String[] tokens) {
        // TASK_CHECKED 101 5
        if (tokens.length >= 3) {
            int taskId = Integer.parseInt(tokens[1]);
            int rating = Integer.parseInt(tokens[2]);
            taskManager.approveTaskAndRate(taskId, rating, child);
        }
    }

    private void handleAddWish1(String[] tokens) {
        // ADD_WISH1 W102 "Lego Set" "Price:150TL, Brand:LEGO"
        if (tokens.length < 4) {
            System.err.println("ADD_WISH1 command format error.");
            return;
        }
        String wishIDstr = tokens[1];
        int wishID = Integer.parseInt(wishIDstr.substring(1)); // 'W102' -> '102'
        String title = tokens[2];
        String desc = tokens[3];

        Wish wish = new Wish(wishID, title, desc, WishType.PRODUCT);
        wishManager.addWish(wish);
        child.addWish(wish);
    }

    private void handleAddWish2(String[] tokens) {
        // ADD_WISH2 W103 "Go to the Cinema" "Price:100TL" 2025-03-07 14:00 2025-03-07 16:00
        if (tokens.length < 4) {
            System.err.println("ADD_WISH2 command format error.");
            return;
        }
        String wishIDstr = tokens[1];
        int wishID = Integer.parseInt(wishIDstr.substring(1));
        String title = tokens[2];
        String desc = tokens[3];

        if (tokens.length == 4) {
            // sadece type: ACTIVITY, tarih yok
            Wish w = new Wish(wishID, title, desc, WishType.ACTIVITY);
            wishManager.addWish(w);
            child.addWish(w);
        } else if (tokens.length >= 8) {
            // 2025-03-07 14:00 2025-03-07 16:00
            String startDate = tokens[4];
            String startTime = tokens[5];
            String endDate = tokens[6];
            String endTime = tokens[7];
            // Wish nesnesi tek bir LocalDateTime tutuyor (örnek),
            // isterseniz start/end ikisini de ekleyebilirsiniz.
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
            System.err.println("WISH_CHECKED command format error.");
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
        wishManager.approveOrRejectedWish(wishID, isApproved, requiredLevel);
    }

    private void handleAddBudgetCoin(String[] tokens) {
        // ADD_BUDGET_COIN 50
        if (tokens.length >= 2) {
            int amount = Integer.parseInt(tokens[1]);
            parent.addBudgetPoints(child, amount);
        }
    }
}
