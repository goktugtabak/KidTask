import java.util.*;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Child child, Task task) {
        child.getTaskList().add(task);
        tasks.add(task);
    }

    public List<Task> listTasks(Child child, String filterType) {
        return child.getTaskList(); // Filtrelenmiş görev listesi dönebilir
    }

    public void approveTask(Child child, int taskID, int rating) {
        for (Task task : child.getTaskList()) {
            if (task.getTaskID() == taskID) {
                task.approve(rating);
                int earnedPoints = task.calculateAwardedPoints(rating);
                child.addPoints(earnedPoints);
                child.updateAverageRating(rating);
                break;
            }
        }
    }
}
