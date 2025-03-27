package domain;

public class Parent extends User {
    public Parent(String userID, String userName, String userRole) {
        super(userID, userName, userRole);
    }

    public void addTask(Task task) {
        System.out.println("Parent " + getUserName() + " is adding a task: " + task.getTitle());
    }

    public void approveTaskAndRate(Task task, int rating, Child child) {
        if (task.getStatus() == TaskStatus.COMPLETED) {
            task.approveTask(rating);
            // Çocuğun rating'ini güncelle
            child.updateAverageRating(rating);
            // Çocuğa puan ekle
            int awardedPoints = task.calculateAwardedPoints(rating);
            child.addPoints(awardedPoints);
        } else {
            System.out.println("Task " + task.getTaskID() + " is not completed yet.");
        }
    }

    public void approveOrRejectWish(Wish wish, boolean isApproved, int requiredLevel) {
        if (isApproved) {
            wish.approveWish(requiredLevel);
        } else {
            wish.rejectWish();
        }
    }

    public void addBudgetPoints(Child child, int amount) {
        System.out.println("Parent " + getUserName() + " adding " + amount + " points to " + child.getUserName());
        child.addPoints(amount);
    }

}
