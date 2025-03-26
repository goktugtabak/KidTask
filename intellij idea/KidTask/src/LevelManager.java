public class LevelManager {
    public int calculateLevel(float averageRating) {
        if (averageRating >= 80) return 4;
        else if (averageRating >= 60) return 3;
        else if (averageRating >= 40) return 2;
        else return 1;
    }

    public void updateChildLevel(Child child) {
        int newLevel = calculateLevel(child.getAverageRating());
        child.setLevel(newLevel);
    }
}
