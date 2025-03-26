public class Parent extends User {

    public Parent(String userID, String name,String userRole) {
        super(userID, name,userRole);
    }

    public void addTask(Child child, Task task) {
        child.getTaskList().add(task);
    }

    public void approveTaskAndRate(Child child, int taskID, int rating) {
        for (Task task : child.getTaskList()) {
            if (task.getTaskID() == taskID) {
                task.approve(rating);
                int earnedPoints = task.calculateAwardedPoints(rating);
                child.addPoints(earnedPoints);
                child.updateAverageRating(rating);
                child.updateLevel();
                break;
            }
        }
    }

    public void approveOrRejectWish(Child child, int wishID, boolean isApproved, int requiredLevel) {
        for (Wish wish : child.getWishList()) {
            if (wish.getWishID() == wishID) {
                if (isApproved) wish.approve(requiredLevel);
                else wish.reject();
                break;
            }
        }
    }

    public void addBudgetCoin(Child child, int amount) {
        child.addPoints(amount);
    }
}
