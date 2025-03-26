public class Teacher extends User {

    public Teacher(String userID, String name,String userRole) {
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

    public void addBudgetCoin(Child child, int amount) {
        child.addPoints(amount);
    }

}
