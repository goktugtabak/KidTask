package manager;

import domain.Child;
import domain.Wish;
import domain.WishStatus;
import domain.WishType;

import java.util.ArrayList;
import java.util.List;

public class WishManager {
    private List<Wish> wishes;

    public WishManager() {
        wishes = new ArrayList<>();
    }

    public void addWish(Wish wish) {
        boolean exists = wishes.stream().anyMatch(w -> w.getWishID() == wish.getWishID());
        if (exists) {
            System.out.println("[ERROR] A wish with ID " + wish.getWishID() + " already exists!");
            return;
        }
        wishes.add(wish);
        System.out.println("[WishManager] Added wish: " + wish.getTitle() + " (ID " + wish.getWishID() + ")");
    }

    public List<Wish> listAllWishes(Child child) {
        System.out.println("[WishManager] Listing all wishes for child: " + child.getUserName());
        return child.getWishList();
    }

    public void approveOrRejectedWish(int wishId,
                                      boolean isApproved,
                                      int requiredLevel,
                                      Child child) {

        Wish wish = wishes.stream()
                .filter(w -> w.getWishID() == wishId)
                .findFirst().orElse(null);

        if (wish == null) {
            System.out.println("[WishManager] Wish " + wishId + " not found.");
            return;
        }

        if (!isApproved) {                          // -------- REJECT --------
            wish.rejectWish();
            wishes.remove(wish);
            child.getWishList().remove(wish);
            System.out.println("[WishManager] Wish " + wishId +
                    " rejected and removed.");
            return;
        }

        // ---------- APPROVE ----------
        if (child.getLevel() < requiredLevel) {
            System.out.println("[WishManager] Child level (" + child.getLevel()
                    + ") < requiredLevel (" + requiredLevel + "). Approval denied.");
            return;
        }

        int price = wish.parsePrice();              // TL tutarını çek
        if (price > 0 && child.getPoints() < price) {
            System.out.println("[WishManager] Insufficient budget for wish "
                    + wishId + " (price " + price + ").");
            return;
        }

        wish.approveWish(requiredLevel);
        child.addPoints(-price);                    // ----- PUAN DÜŞ -----
        System.out.println("[WishManager] Wish " + wishId +
                " approved (price " + price + " TL, level ≥ " + requiredLevel + ").");
    }

}
