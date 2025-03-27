package manager;

import domain.Task;
import domain.TaskStatus;
import domain.User;
import domain.Child;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("[TaskManager] Added task: " + task.getTitle() + " (ID " + task.getTaskID() + ")");
    }

    public List<Task> listTasks(String filterType) {
        System.out.println("[TaskManager] Listing tasks. Filter: " + filterType);
        // "D" (daily), "W" (weekly) vs. gibi filtre mantığı eklenebilir.
        // Burada basitçe tüm task'ları döndürüyoruz.
        return tasks;
    }

    public void approveTaskAndRate(int taskId, int rating, Child child) {
        for (Task task : tasks) {
            if (task.getTaskID() == taskId) {
                if (task.getStatus() == TaskStatus.COMPLETED) {
                    task.approveTask(rating);
                    System.out.println("[TaskManager] Task " + taskId + " approved with rating " + rating);

                    // Child puan & rating güncelle
                    child.updateAverageRating(rating);
                    child.addPoints(task.calculateAwardedPoints(rating));
                } else {
                    System.out.println("[TaskManager] Task " + taskId + " is not marked as completed yet.");
                }
                return;
            }
        }
        System.out.println("[TaskManager] Task " + taskId + " not found.");
    }
}
