package manager;

import domain.Child;
import domain.Task;
import domain.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        boolean exists = tasks.stream().anyMatch(t -> t.getTaskID() == task.getTaskID());
        if (exists) {
            System.out.println("[ERROR] A task with ID " + task.getTaskID() + " already exists!");
            return;
        }
        tasks.add(task);
        System.out.println("[TaskManager] Added task: " + task.getTitle() + " (ID " + task.getTaskID() + ")");
    }

    public List<Task> listTasks(String filterType) {
        System.out.println("[TaskManager] Listing tasks. Filter: " + filterType);
        LocalDate now = LocalDate.now();
        List<Task> result = new ArrayList<>();

        switch (filterType) {
            case "D": // daily
                for (Task t : tasks) {
                    if (t.getDeadline() != null && t.getDeadline().toLocalDate().isEqual(now)) {
                        result.add(t);
                    }
                }
                return result;
            case "W": // weekly
                LocalDate oneWeek = now.plusDays(7);
                for (Task t : tasks) {
                    if (t.getDeadline() != null) {
                        LocalDate d = t.getDeadline().toLocalDate();
                        if (!d.isBefore(now) && !d.isAfter(oneWeek)) {
                            result.add(t);
                        }
                    }
                }
                return result;
            default:
                return tasks;
        }
    }

    public void approveTaskAndRate(int taskId, int rating, Child child) {
        if (rating < 1 || rating > 5) {
            System.out.println("[ERROR] Rating must be between 1 and 5.");
            return;
        }
        for (Task task : tasks) {
            if (task.getTaskID() == taskId) {
                if (task.getStatus() == TaskStatus.COMPLETED) {
                    task.approveTask(rating);
                    System.out.println("[TaskManager] Task " + taskId + " approved with rating " + rating);
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
