import java.time.LocalDateTime;

public class Task {
    private int taskID;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int points;
    private TaskStatus status;
    private User assignedBy;
    private int rating;

    public Task(int taskID, String title, String description, LocalDateTime deadline, int points, User assignedBy) {
        this.taskID = taskID;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.points = points;
        this.status = TaskStatus.PENDING;
        this.assignedBy = assignedBy;
    }

    public void markAsDone() {
        this.status = TaskStatus.DONE;
    }

    public void approve(int rating) {
        this.rating = rating;
        this.status = TaskStatus.APPROVED;
    }

    public int calculateAwardedPoints(int rating) {
        return (int) (points * (rating / 5.0));
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTitle() {
        return title;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(User assignedBy) {
        this.assignedBy = assignedBy;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
