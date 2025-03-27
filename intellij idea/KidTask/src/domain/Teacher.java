package domain;

public class Teacher extends User {
    public Teacher(String userID, String userName, String userRole) {
        super(userID, userName, userRole);
    }

    public void addTask(Task task) {
        System.out.println("Teacher " + getUserName() + " is adding a task: " + task.getTitle());
    }

    public void approveTaskAndRate(Task task, int rating, Child child) {
        if (isTaskCompleted(task)) {
            task.approveTask(rating);
            child.updateAverageRating(rating);
            int awardedPoints = task.calculateAwardedPoints(rating);
            child.addPoints(awardedPoints);
        } else {
            System.out.println("Task " + task.getTaskID() + " is not completed yet.");
        }
    }

    private boolean isTaskCompleted(Task task) {
        return task.getStatus() == TaskStatus.COMPLETED;
    }

    public void addBudgetPoints(Child child, int amount) {
        System.out.println("Teacher " + getUserName() + " adding " + amount + " points to " + child.getUserName());
        child.addPoints(amount);
    }
}
