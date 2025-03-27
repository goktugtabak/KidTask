package domain;

import java.time.LocalDateTime;

public class Task {
    private int taskID;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private int points;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private User assignedBy;
    private TaskStatus status;
    private int rating;

    public Task(int taskID, String title, String description, LocalDateTime deadline, int points, User assignedBy) {
        this.taskID = taskID;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.points = points;
        this.assignedBy = assignedBy;
        this.status = TaskStatus.PENDING;
        this.rating = 0;
    }

    // Constructor for tasks that include a start/end time
    public Task(int taskID, String title, String description, LocalDateTime startTime,
                LocalDateTime endTime, int points, User assignedBy) {
        this.taskID = taskID;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.points = points;
        this.assignedBy = assignedBy;
        this.status = TaskStatus.PENDING;
        this.rating = 0;
    }

    public void markAsComplete() {
        this.status = TaskStatus.COMPLETED;
    }

    public void approveTask(int rating) {
        this.status = TaskStatus.APPROVED;
        this.rating = rating;
    }

    public int calculateAwardedPoints(int rating) {
        // İstenirse rating'e göre farklı hesaplama yapılabilir.
        // Şimdilik puanı doğrudan döndürüyoruz.
        return points;
    }

    // Getter ve Setter'lar
    public int getTaskID() {
        return taskID;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getRating() {
        return rating;
    }

    public void setAssignedBy(User assignedBy) {
        this.assignedBy = assignedBy;
    }

    public User getAssignedBy() {
        return assignedBy;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
