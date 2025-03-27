package file;

import domain.*;
import manager.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileHandler {
    private TaskManager taskManager;
    private WishManager wishManager;
    private Child child;
    private Parent parent;
    private Teacher teacher;

    // Basit tarih-saat biçimi.
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FileHandler(TaskManager taskManager, WishManager wishManager, Child child,
                       Parent parent, Teacher teacher) {
        this.taskManager = taskManager;
        this.wishManager = wishManager;
        this.child = child;
        this.parent = parent;
        this.teacher = teacher;
    }

    // ------------------------------------------------------------
    //  TASKS.TXT OKUMA/YAZMA
    // ------------------------------------------------------------
    public void readTasks(String filePath) {
        System.out.println("Reading tasks from file: " + filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Beklenen format:
                // 101 "Math Homework" "Solve pages 10 to 20" 2025-03-01 15:00 10
                // veya
                // 102 "School Picnic Preparation" "Göksu Park" "2025-03-05 10:00-12:00" 10
                parseTaskLine(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading tasks: " + e.getMessage());
        }
    }

    public void writeTasks(String filePath) {
        // Opsiyonel: TaskManager içerisindeki tüm task'ları yazabiliriz.
        System.out.println("Writing tasks to file: " + filePath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Task t : taskManager.listTasks("ALL")) {
                // startTime ve endTime var mı yok mu bakarız.
                // Demo amaçlı basit yazım.
                String line;
                if (t.getStatus() != null) {
                    // Basit bir format, isterseniz tam da toString()'i kullanabilirsiniz.
                    line = t.getTaskID() + " \"" + t.getTitle() + "\" \"" + "DESCRIPTION_PLACEHOLDER\" "
                            + (t.getStatus() == TaskStatus.APPROVED ? "APPROVED" : "NOT_APPROVED");
                } else {
                    line = t.getTaskID() + " \"" + t.getTitle() + "\" (Status Unknown)";
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing tasks: " + e.getMessage());
        }
    }

    private void parseTaskLine(String line) {
        // Örnek bir parse metodu.
        // Satırın yapısına göre tokenize ediyoruz.
        // Mantık: ID, Başlık, Açıklama, eğer start-end time varsa? vs.
        try {
            // Split etmeden önce tırnak içini korumak için basit yaklaşımlar:
            // 1) Regex kullanarak gruplar halinde çekmek
            // 2) Manuel parse
            // Burada basit bir yaklaşımla string içindeki tırnakları parse edeceğiz.

            // Örneğin:
            // 101 "Math Homework" "Solve pages 10 to 20" 2025-03-01 15:00 10
            // => parçalara ayırıyoruz.
            // ID = 101
            // Title = Math Homework
            // Description = Solve pages 10 to 20
            // Deadline date/time = 2025-03-01 15:00
            // Points = 10

            // Basit bir regex approach:
            // - ilk rakam ID
            // - Son rakam Points
            // - ortada tırnak içinde 2 string, vs.

            String regex = "(\\d+)\\s+\"([^\"]+)\"\\s+\"([^\"]+)\"\\s+(.*)";
            // Bu, en azından ID, Title, Description, ve kalan kısım (deadline/points) çekmeye yarar.
            // Sonra kalan kısımda start-end var mı bakarız.

            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                int taskID = Integer.parseInt(matcher.group(1));
                String title = matcher.group(2);
                String description = matcher.group(3);
                String remainder = matcher.group(4).trim();

                // remainder örneğin: 2025-03-01 15:00 10
                // veya "2025-03-05 10:00-12:00" 10
                if (remainder.contains("\"")) {
                    // Demek ki start-end var. remainder = "2025-03-05 10:00-12:00" 10
                    // Yine tırnak ayıklıyoruz:
                    int firstQuote = remainder.indexOf('"');
                    int lastQuote = remainder.lastIndexOf('"');
                    String dateRange = remainder.substring(firstQuote + 1, lastQuote);
                    // dateRange = 2025-03-05 10:00-12:00
                    String[] rangeParts = dateRange.split(" ");
                    // rangeParts[0] = 2025-03-05
                    // rangeParts[1] = 10:00-12:00
                    String[] times = rangeParts[1].split("-");
                    // times[0] = 10:00
                    // times[1] = 12:00
                    String dateStr = rangeParts[0];

                    String afterQuote = remainder.substring(lastQuote + 1).trim();
                    int points = Integer.parseInt(afterQuote);

                    LocalDateTime start = LocalDateTime.parse(dateStr + " " + times[0], dtf);
                    LocalDateTime end = LocalDateTime.parse(dateStr + " " + times[1], dtf);

                    // Varsayalım ki teacher atıyor (demo)
                    Task task = new Task(taskID, title, description, start, end, points, teacher);
                    taskManager.addTask(task);
                    // Ayrıca çocuğa da ekliyoruz.
                    child.getTaskList().add(task);

                } else {
                    // remainder: 2025-03-01 15:00 10
                    String[] arr = remainder.split("\\s+");
                    String datePart = arr[0];
                    String timePart = arr[1];
                    String pointsPart = arr[2];
                    LocalDateTime deadline = LocalDateTime.parse(datePart + " " + timePart, dtf);
                    int points = Integer.parseInt(pointsPart);

                    // Varsayalım ki teacher atıyor (demo)
                    Task task = new Task(taskID, title, description, deadline, points, teacher);
                    taskManager.addTask(task);
                    child.getTaskList().add(task);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing task line: " + line + " => " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    //  WISHES.TXT OKUMA/YAZMA
    // ------------------------------------------------------------
    public void readWishes(String filePath) {
        System.out.println("Reading wishes from file: " + filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseWishLine(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading wishes: " + e.getMessage());
        }
    }

    public void writeWishes(String filePath) {
        System.out.println("Writing wishes to file: " + filePath);
        // Opsiyonel: WishManager içerisindeki wishes'ları yazabilirsiniz.
        // Örnek olarak bir dummy implementasyon
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Bu manager'da ekli wishes yoksa, child'ların istekleri üzerinden de gidebilirsiniz.
            // Ya da WishManager'a getter ekleyip oradan çekebilirsiniz.
            bw.write("// Wishes output - Demo");
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing wishes: " + e.getMessage());
        }
    }

    private void parseWishLine(String line) {
        // Beklenen format:
        // W102 "Lego Set" "Price:150TL, Brand:LEGO" PRODUCT
        // veya
        // W103 "Go to the Cinema" "Price:100TL" ACTIVITY "2025-03-07 14:00-16:00"
        try {
            // Kısmen tasks'a benzer parse, ID + "title" + "desc" + type (+ activity time)
            String regex = "(W\\d+)\\s+\"([^\"]+)\"\\s+\"([^\"]+)\"\\s+(.*)";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                String wishIDStr = matcher.group(1); // Örnek: W102
                int wishID = Integer.parseInt(wishIDStr.substring(1)); // 102
                String title = matcher.group(2);
                String desc = matcher.group(3);
                String remainder = matcher.group(4).trim();

                // remainder: PRODUCT
                // veya ACTIVITY "2025-03-07 14:00-16:00"
                if (remainder.startsWith("PRODUCT")) {
                    Wish wish = new Wish(wishID, title, desc, WishType.PRODUCT);
                    wishManager.addWish(wish);
                    child.addWish(wish);
                } else if (remainder.startsWith("ACTIVITY")) {
                    // remainder => ACTIVITY "2025-03-07 14:00-16:00"
                    if (remainder.contains("\"")) {
                        int firstQuote = remainder.indexOf('"');
                        int lastQuote = remainder.lastIndexOf('"');
                        String dateTimeRange = remainder.substring(firstQuote + 1, lastQuote);
                        // Örnek: 2025-03-07 14:00-16:00
                        String[] dtParts = dateTimeRange.split(" ");
                        String datePart = dtParts[0]; // 2025-03-07
                        String[] hourRange = dtParts[1].split("-");
                        String startHour = hourRange[0]; // 14:00

                        // Basitçe start'ı alıyoruz, end'i tam kullanmıyoruz veya isterseniz ayırabilirsiniz.
                        LocalDateTime dt = LocalDateTime.parse(datePart + " " + startHour, dtf);

                        Wish wish = new Wish(wishID, title, desc, WishType.ACTIVITY, dt);
                        wishManager.addWish(wish);
                        child.addWish(wish);
                    } else {
                        // Sadece "ACTIVITY" yazıyorsa
                        Wish wish = new Wish(wishID, title, desc, WishType.ACTIVITY);
                        wishManager.addWish(wish);
                        child.addWish(wish);
                    }
                } else {
                    System.err.println("Unknown Wish type: " + remainder);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing wish line: " + line + " => " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    //  COMMANDS.TXT OKUMA & PARSE
    // ------------------------------------------------------------
    public void processCommands(String filePath) {
        System.out.println("Processing commands from file: " + filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseCommand(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading commands file: " + e.getMessage());
        }
    }

    public void parseCommand(String command) {
        System.out.println("\n[COMMAND] " + command);
        // Komutları boşluk bazlı ayırarak basit bir switch yazalım:
        String[] tokens = tokenizeCommand(command);

        if (tokens.length == 0) return;
        String cmd = tokens[0];

        switch (cmd) {
            case "ADD_TASK1":
                // Örnek kullanım:
                // ADD_TASK1 T 101 "Math Homework" "Solve pages 10 to 20" 2025-03-01 15:00 10
                handleAddTask1(tokens);
                break;
            case "ADD_TASK2":
                // Örnek kullanım:
                // ADD_TASK2 F 102 "School Picnic" "Park" 2025-03-05 10:00 2025-03-05 12:00 10
                handleAddTask2(tokens);
                break;
            case "LIST_ALL_TASKS":
                if (tokens.length > 1) {
                    String filter = tokens[1];
                    List<Task> filtered = taskManager.listTasks(filter);
                    for (Task t : filtered) {
                        System.out.println(t.getTaskID() + " " + t.getTitle() + " (" + t.getStatus() + ")");
                    }
                } else {
                    // Filtre yok
                    List<Task> all = taskManager.listTasks("ALL");
                    for (Task t : all) {
                        System.out.println(t.getTaskID() + " " + t.getTitle() + " (" + t.getStatus() + ")");
                    }
                }
                break;
            case "TASK_DONE":
                // TASK_DONE 101
                if (tokens.length >= 2) {
                    int taskId = Integer.parseInt(tokens[1]);
                    child.markTaskAsDone(taskId);
                }
                break;
            case "TASK_CHECKED":
                // TASK_CHECKED 101 5
                if (tokens.length >= 3) {
                    int taskId = Integer.parseInt(tokens[1]);
                    int rating = Integer.parseInt(tokens[2]);
                    taskManager.approveTaskAndRate(taskId, rating, child);
                }
                break;
            case "ADD_WISH1":
                // ADD_WISH1 W102 "Lego Set" "Price:150TL, Brand:LEGO"
                handleAddWish1(tokens);
                break;
            case "ADD_WISH2":
                // ADD_WISH2 W103 "Go to the Cinema" "Price:100TL" 2025-03-07 14:00 2025-03-07 16:00
                handleAddWish2(tokens);
                break;
            case "WISH_CHECKED":
                // WISH_CHECKED W102 APPROVED 3
                // WISH_CHECKED W103 REJECTED
                handleWishChecked(tokens);
                break;
            case "ADD_BUDGET_COIN":
                // ADD_BUDGET_COIN 50
                if (tokens.length >= 2) {
                    int amount = Integer.parseInt(tokens[1]);
                    parent.addBudgetPoints(child, amount);
                }
                break;
            case "PRINT_BUDGET":
                child.printBudget();
                break;
            case "PRINT_STATUS":
                System.out.println(child.printStatus());
                break;
            default:
                System.out.println("[WARNING] Unknown command: " + cmd);
                break;
        }
    }

    private String[] tokenizeCommand(String cmdLine) {
        // Tırnak içindeki ifadeleri korumak üzere bir parser daha yazmak gerekir.
        // Basit yaklaşımla: Komut isimlerini vs. alırken, tırnakları bulup patlatacağız.
        // Detaylı, güvenilir parsing için regex veya özel mantık gerekir.
        // Bu örnekte, minimal bir tokenizer sunuyoruz.

        // Yararlanabileceğimiz bir regex:
        // "[^"]*"|[^\\s]+ => Tırnak içini bir grup, tırnak dışını başka grup alır.
        List<String> list = new ArrayList<>();
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("[^\\s\"]+|\"[^\"]*\"");
        java.util.regex.Matcher m = p.matcher(cmdLine);
        while (m.find()) {
            list.add(m.group().replace("\"", "")); // Tırnakları sıyırıyoruz.
        }
        return list.toArray(new String[0]);
    }

    // ------------------------------------------------------------
    //  Komut İşleme Yardımcı Metodları
    // ------------------------------------------------------------
    private void handleAddTask1(String[] tokens) {
        // ADD_TASK1 T 101 "Math Homework" "Solve pages 10 to 20" 2025-03-01 15:00 10
        //  0        1 2   3                4                          5         6      7
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

        User assignedBy = who.equals("T") ? teacher : parent;
        Task task = new Task(taskID, title, desc, deadline, points, assignedBy);
        taskManager.addTask(task);
        child.getTaskList().add(task);
    }

    private void handleAddTask2(String[] tokens) {
        // ADD_TASK2 F 102 "School Picnic" "Göksu Park" 2025-03-05 10:00 2025-03-05 12:00 10
        //  0        1 2   3                4             5           6      7           8      9
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

        User assignedBy = who.equals("T") ? teacher : parent;
        Task task = new Task(taskID, title, desc, start, end, points, assignedBy);
        taskManager.addTask(task);
        child.getTaskList().add(task);
    }

    private void handleAddWish1(String[] tokens) {
        // ADD_WISH1 W102 "Lego Set" "Price:150TL, Brand:LEGO"
        // 0         1     2          3
        if (tokens.length < 4) {
            System.err.println("ADD_WISH1 command format error.");
            return;
        }
        String wishIDstr = tokens[1];
        int wishID = Integer.parseInt(wishIDstr.substring(1)); // W102 -> 102
        String title = tokens[2];
        String desc = tokens[3];

        Wish wish = new Wish(wishID, title, desc, WishType.PRODUCT);
        wishManager.addWish(wish);
        child.addWish(wish);
    }

    private void handleAddWish2(String[] tokens) {
        // ADD_WISH2 W103 "Go to the Cinema" "Price:100TL" 2025-03-07 14:00 2025-03-07 16:00
        if (tokens.length < 5) {
            System.err.println("ADD_WISH2 command format error.");
            return;
        }
        // 0         1     2                   3              4           5      6           7      8
        // Sadece 4 parametre varsa: isActivity ama saat yok
        // Daha fazla varsa: date/time var

        String wishIDstr = tokens[1];
        int wishID = Integer.parseInt(wishIDstr.substring(1));
        String title = tokens[2];
        String desc = tokens[3];

        if (tokens.length == 4) {
            // Sadece activity, datetime yok
            Wish w = new Wish(wishID, title, desc, WishType.ACTIVITY);
            wishManager.addWish(w);
            child.addWish(w);
        } else if (tokens.length >= 8) {
            // 2025-03-07 14:00 2025-03-07 16:00
            String startDate = tokens[4];
            String startTime = tokens[5];
            // end date/time
            String endDate = tokens[6];
            String endTime = tokens[7];
            // Bu örnekte Wish sadece single dateTime tutuyor, isterseniz startTime/endTime ayrıştırıp ek mantık yazabilirsiniz.
            // Biz sadece start date/time koyacağız.
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

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public WishManager getWishManager() {
        return wishManager;
    }

    public void setWishManager(WishManager wishManager) {
        this.wishManager = wishManager;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public DateTimeFormatter getDtf() {
        return dtf;
    }

    public void setDtf(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }
}
