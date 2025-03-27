package manager;

import domain.Wish;
import domain.Child;
import java.util.ArrayList;
import java.util.List;

public class WishManager {
    private List<Wish> wishes;

    public WishManager() {
        wishes = new ArrayList<>();
    }

    public void addWish(Wish wish) {
        wishes.add(wish);
        System.out.println("[WishManager] Added wish: " + wish.getTitle() + " (ID " + wish.getWishID() + ")");
    }

    public List<Wish> listAllWishes(Child child) {
        System.out.println("[WishManager] Listing all wishes for child: " + child.getUserName());
        return child.getWishList();
    }

    public void approveOrRejectedWish(int wishId, boolean isApproved, int requiredLevel) {
        for (Wish wish : wishes) {
            if (wish.getWishID() == wishId) {
                if (isApproved) {
                    wish.approveWish(requiredLevel);
                    System.out.println("[WishManager] Wish " + wishId + " approved. Required Level " + requiredLevel);
                } else {
                    wish.rejectWish();
                    System.out.println("[WishManager] Wish " + wishId + " rejected.");
                }
                return;
            }
        }
        System.out.println("[WishManager] Wish " + wishId + " not found in manager list.");
    }
}
