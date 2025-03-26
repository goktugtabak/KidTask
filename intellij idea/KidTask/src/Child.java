import java.util.*;

public class Child extends User {
    private int points;
    private int level;
    private float averageRating;
    private int completedTasks;
    private List<Task> taskList;
    private List<Wish> wishList;

    public Child(String userID, String name,String userRole) {
        super(userID, name,userRole);
        this.points = 0;
        this.level = 1;
        this.averageRating = 0;
        this.completedTasks = 0;
        this.taskList = new ArrayList<>();
        this.wishList = new ArrayList<>();
    }

    public List<Task> listTasks(String filterType) {
        // filtreye göre task döndür (daily/weekly)
        return taskList; // şimdilik direkt döndürülüyor
    }

    public void markTaskAsDone(int taskID) {
        for (Task task : taskList) {
            if (task.getTaskID() == taskID) {
                task.markAsDone();
                break;
            }
        }
    }

    public void addWish(Wish wish) {
        wishList.add(wish);
    }

    public List<Wish> listAllWishes() {
        return wishList;
    }

    public int printBudget() {
        return points;
    }

    public String printStatus() {
        return "Level: " + level + ", Average Rating: " + averageRating;
    }

    public void updateAverageRating(int newRating) {
        averageRating = ((averageRating * completedTasks) + newRating) / (completedTasks + 1);
        completedTasks++;
    }

    public void updateLevel() {
        if (averageRating >= 80) level = 4;
        else if (averageRating >= 60) level = 3;
        else if (averageRating >= 40) level = 2;
        else level = 1;
    }

    public void addPoints(int pointsEarned) {
        this.points += pointsEarned;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public List<Wish> getWishList() {
        return wishList;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

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
