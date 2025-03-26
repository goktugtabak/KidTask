import java.util.*;

public class WishManager {
    private List<Wish> allWishes;

    public WishManager() {
        this.allWishes = new ArrayList<>();
    }

    public void addWish(Child child, Wish wish) {
        child.getWishList().add(wish);
        allWishes.add(wish);
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

    public List<Wish> listAllWishes(Child child) {
        return child.getWishList();
    }
}
