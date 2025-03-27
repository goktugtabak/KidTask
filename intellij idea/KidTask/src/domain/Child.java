package domain;

import java.util.ArrayList;
import java.util.List;

public class Child extends User {
    private int points;
    private int level;
    private double averageRating;
    private int completedTasks;
    private List<Task> taskList;
    private List<Wish> wishList;

    public Child(String userID, String userName, String userRole) {
        super(userID, userName, userRole);
        this.points = 0;
        this.level = 1;
        this.averageRating = 0.0;
        this.completedTasks = 0;
        this.taskList = new ArrayList<>();
        this.wishList = new ArrayList<>();
    }

    public void viewTasks() {
        for (Task t : taskList) {
            System.out.println("Task ID: " + t.getTaskID() + ", Title: " + t.getTitle()
                    + ", Status: " + t.getStatus());
        }
    }

    public void markTaskAsDone(int taskID) {
        for (Task t : taskList) {
            if (t.getTaskID() == taskID) {
                t.markAsComplete();
                System.out.println("Child " + getUserName() + " marked task " + taskID + " as done.");
                break;
            }
        }
    }

    public void addWish(Wish wish) {
        wishList.add(wish);
        System.out.println("Child " + getUserName() + " added wish: " + wish.getTitle());
    }

    public List<Wish> listAllWishes() {
        return wishList;
    }

    public int calculateLevel() {
        // Örneğin, ortalama rating 1-5 aralığında olabilir,
        // basit bir örnek: level = (int)(averageRating) vs.
        // Daha farklı bir hesaplama da yapılabilir.
        // Şimdilik sadece integer cast ile kullanalım (örnek).
        return (int) Math.max(1, averageRating);
    }

    public List<Task> listTasks(String filterType) {
        // "D" (Daily) ya da "W" (Weekly) için basit örnek.
        // Şu an filtre mantığı tam değil, istenirse takvime göre süzülür.
        // Burada sadece tam liste dönelim (demo).
        return taskList;
    }

    public int printBudget() {
        System.out.println("Current points (budget) for " + getUserName() + ": " + points);
        return points;
    }

    public String printStatus() {
        return "Child " + getUserName() + " - Level: " + level
                + ", Points: " + points + ", Average Rating: " + averageRating;
    }

    public void updateAverageRating(int newRating) {
        // Basit ortalama hesaplama (toplam rating / completedTasks).
        completedTasks++;
        double total = averageRating * (completedTasks - 1) + newRating;
        this.averageRating = total / completedTasks;
        this.level = calculateLevel();
        System.out.println("New average rating for " + getUserName() + ": " + this.averageRating);
    }

    public void addPoints(int pointsEarned) {
        this.points += pointsEarned;
        System.out.println("Added " + pointsEarned + " points to " + getUserName()
                + ". Total: " + this.points);
    }

    // Getter & Setter
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public int getCompletedTasks() { return completedTasks; }
    public List<Task> getTaskList() { return taskList; }
    public List<Wish> getWishList() { return wishList; }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setWishList(List<Wish> wishList) {
        this.wishList = wishList;
    }
}
