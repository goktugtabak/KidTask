package file;

import domain.*;
import manager.TaskManager;
import manager.WishManager;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHandler {
    private TaskManager taskManager;
    private WishManager wishManager;
    private Child child;
    private Parent parent;
    private Teacher teacher;

    // Basit tarih-saat biçimi
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Command parser (komutları parse eder)
    private CommandHandler commandHandler;

    public FileHandler(TaskManager taskManager, WishManager wishManager,
                       Child child, Parent parent, Teacher teacher) {
        this.taskManager = taskManager;
        this.wishManager = wishManager;
        this.child = child;
        this.parent = parent;
        this.teacher = teacher;

        // Komut parser
        this.commandHandler = new CommandHandler(taskManager, wishManager, child, parent, teacher);
    }

    // ------------------------------------------------------------
    //  TASKS.TXT OKUMA
    // ------------------------------------------------------------
    public void readTasks(String filePath) {
        System.out.println("Reading tasks from file: " + filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    parseTaskLine(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading tasks: " + e.getMessage());
        }
    }

    private void parseTaskLine(String line) {
        // Beklenen formatlar:
        // 1) 101 "Math Homework" "Solve pages 10 to 20" 2025-03-01 15:00 10
        // 2) 102 "School Picnic" "Göksu Park" "2025-03-05 10:00-12:00" 10
        // ID => int
        // title => "..."
        // desc => "..."
        // ya deadline & time => 2025-03-01 15:00
        // ya da "2025-03-05 10:00-12:00"
        // en sonda points

        try {
            // Regex ile ID, Title, Desc ve kalan kısım
            String regex = "(\\d+)\\s+\"([^\"]+)\"\\s+\"([^\"]+)\"\\s+(.*)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                int taskID = Integer.parseInt(matcher.group(1));
                String title = matcher.group(2);
                String description = matcher.group(3);
                String remainder = matcher.group(4).trim();

                // remainder tipine göre ayırt edelim
                // Örn: remainder = 2025-03-01 15:00 10  (deadline tipi)
                // veya remainder = "2025-03-05 10:00-12:00" 10  (start-end tipi)
                if (remainder.startsWith("\"")) {
                    // start-end time var
                    // remainder => "2025-03-05 10:00-12:00" 10
                    int lastQ = remainder.lastIndexOf('"');
                    String dateRange = remainder.substring(1, lastQ); // 2025-03-05 10:00-12:00
                    String afterQuote = remainder.substring(lastQ + 1).trim(); // 10

                    int points = Integer.parseInt(afterQuote);

                    String[] arr = dateRange.split(" ");
                    // arr[0] = 2025-03-05
                    // arr[1] = 10:00-12:00
                    String datePart = arr[0];
                    String[] times = arr[1].split("-");
                    String startTime = times[0];
                    String endTime = times[1];

                    LocalDateTime start = LocalDateTime.parse(datePart + " " + startTime, dtf);
                    LocalDateTime end = LocalDateTime.parse(datePart + " " + endTime, dtf);

                    Task task = new Task(taskID, title, description, start, end, points, teacher); // teacher atanıyor (örnek)
                    taskManager.addTask(task);
                    child.getTaskList().add(task);
                } else {
                    // remainder => 2025-03-01 15:00 10
                    String[] arr = remainder.split("\\s+");
                    // arr[0] = 2025-03-01
                    // arr[1] = 15:00
                    // arr[2] = 10
                    String datePart = arr[0];
                    String timePart = arr[1];
                    int points = Integer.parseInt(arr[2]);

                    LocalDateTime deadline = LocalDateTime.parse(datePart + " " + timePart, dtf);

                    Task task = new Task(taskID, title, description, deadline, points, parent); // parent atanıyor (örnek)
                    taskManager.addTask(task);
                    child.getTaskList().add(task);
                }
            } else {
                System.err.println("Could not parse task line: " + line);
            }
        } catch (Exception e) {
            System.err.println("Error parsing task line: " + line + " => " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    //  WISHES.TXT OKUMA
    // ------------------------------------------------------------
    public void readWishes(String filePath) {
        System.out.println("Reading wishes from file: " + filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    parseWishLine(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading wishes: " + e.getMessage());
        }
    }

    private void parseWishLine(String line) {
        // Beklenen formatlar:
        // W201 "Lego Set" "Price:150TL" PRODUCT
        // W202 "Go to the Cinema" "Price:100TL" ACTIVITY "2025-03-07 14:00-16:00"
        try {
            String regex = "(W\\d+)\\s+\"([^\"]+)\"\\s+\"([^\"]+)\"\\s+(.*)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                String wishIDStr = matcher.group(1); // W201
                int wishID = Integer.parseInt(wishIDStr.substring(1)); // 201
                String title = matcher.group(2);
                String desc = matcher.group(3);
                String remainder = matcher.group(4).trim(); // PRODUCT veya ACTIVITY "2025-03-07 14:00-16:00"

                if (remainder.startsWith("PRODUCT")) {
                    Wish wish = new Wish(wishID, title, desc, WishType.PRODUCT);
                    wishManager.addWish(wish);
                    child.addWish(wish);
                } else if (remainder.startsWith("ACTIVITY")) {
                    // varsa tarih-saat var
                    if (remainder.contains("\"")) {
                        // remainder => ACTIVITY "2025-03-07 14:00-16:00"
                        int firstQ = remainder.indexOf('"');
                        int lastQ  = remainder.lastIndexOf('"');
                        String dateTimeRange = remainder.substring(firstQ+1, lastQ); // 2025-03-07 14:00-16:00

                        String[] arr = dateTimeRange.split(" ");
                        String datePart = arr[0];   // 2025-03-07
                        String[] times = arr[1].split("-");
                        String startT = times[0];  // 14:00
                        // endT = times[1];  // 16:00 (kullanmak isterseniz Wish'e ekleyebilirsiniz)

                        LocalDateTime dt = LocalDateTime.parse(datePart + " " + startT, dtf);

                        Wish wish = new Wish(wishID, title, desc, WishType.ACTIVITY, dt);
                        wishManager.addWish(wish);
                        child.addWish(wish);
                    } else {
                        // Sadece "ACTIVITY"
                        Wish wish = new Wish(wishID, title, desc, WishType.ACTIVITY);
                        wishManager.addWish(wish);
                        child.addWish(wish);
                    }
                } else {
                    System.err.println("Unknown wish type: " + remainder);
                }
            } else {
                System.err.println("Could not parse wish line: " + line);
            }
        } catch (Exception e) {
            System.err.println("Error parsing wish line: " + line + " => " + e.getMessage());
        }
    }

    public void appendWishToFile(Wish wish, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (wish.getType() == WishType.PRODUCT) {
                writer.write(String.format("W%d \"%s\" \"%s\" PRODUCT\n",
                        wish.getWishID(),
                        wish.getTitle(),
                        wish.getDescription()));
            } else {
                if (wish.getDateTime() != null) {
                    String date = wish.getDateTime().toLocalDate().toString();
                    String startTime = wish.getDateTime().toLocalTime().toString();
                    String endTime = wish.getDateTime().plusHours(2).toLocalTime().toString(); // tahmini
                    writer.write(String.format("W%d \"%s\" \"%s\" ACTIVITY \"%s %s-%s\"\n",
                            wish.getWishID(),
                            wish.getTitle(),
                            wish.getDescription(),
                            date,
                            startTime,
                            endTime));
                } else {
                    writer.write(String.format("W%d \"%s\" \"%s\" ACTIVITY\n",
                            wish.getWishID(),
                            wish.getTitle(),
                            wish.getDescription()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error appending wish to file: " + e.getMessage());
        }
    }


    public void writeWishes(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Wish wish : wishManager.listAllWishes(child)) {
                if (wish.getType() == WishType.PRODUCT) {
                    writer.write(String.format("W%d \"%s\" \"%s\" PRODUCT\n",
                            wish.getWishID(),
                            wish.getTitle(),
                            wish.getDescription()));
                } else {
                    if (wish.getDateTime() != null) {
                        String date = wish.getDateTime().toLocalDate().toString();
                        String startTime = wish.getDateTime().toLocalTime().toString();
                        String endTime = wish.getDateTime().plusHours(2).toLocalTime().toString(); // tahmini
                        writer.write(String.format("W%d \"%s\" \"%s\" ACTIVITY \"%s %s-%s\"\n",
                                wish.getWishID(),
                                wish.getTitle(),
                                wish.getDescription(),
                                date,
                                startTime,
                                endTime));
                    } else {
                        writer.write(String.format("W%d \"%s\" \"%s\" ACTIVITY\n",
                                wish.getWishID(),
                                wish.getTitle(),
                                wish.getDescription()));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing wishes.txt: " + e.getMessage());
        }
    }


    public void writeTasks(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : taskManager.listTasks("ALL")) {
                if (task.getStartTime() != null && task.getEndTime() != null) {
                    // Zaman aralıklı görev (ADD_TASK2)
                    writer.write(String.format("%d \"%s\" \"%s\" \"%s %s-%s\" %d\n",
                            task.getTaskID(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getStartTime().toLocalDate(),
                            task.getStartTime().toLocalTime(),
                            task.getEndTime().toLocalTime(),
                            task.getPoints()));
                } else {
                    // Deadline'lı görev (ADD_TASK1)
                    writer.write(String.format("%d \"%s\" \"%s\" %s %s %d\n",
                            task.getTaskID(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getDeadline().toLocalDate(),
                            task.getDeadline().toLocalTime(),
                            task.getPoints()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing tasks.txt: " + e.getMessage());
        }
    }


    // ------------------------------------------------------------
    //  COMMANDS.TXT OKUMA & PARSE
    // ------------------------------------------------------------
    public void processCommands(String filePath) {
        System.out.println("\nProcessing commands from file: " + filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    commandHandler.parseCommand(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading commands file: " + e.getMessage());
        }
    }

}
