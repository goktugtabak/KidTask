package domain;

import java.time.LocalDateTime;

public class Wish {
    private int wishID;
    private String title;
    private String description;
    private WishType type;
    private int requiredLevel;
    private boolean approved;
    private LocalDateTime dateTime;   // ACTIVITY istekleri için
    private WishStatus wishStatus;

    public Wish(int wishID, String title, String description, WishType type) {
        this.wishID = wishID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.wishStatus = WishStatus.PENDING;
        this.approved = false;
        this.requiredLevel = 0;
    }

    public Wish(int wishID, String title, String description, WishType type, LocalDateTime dateTime) {
        this(wishID, title, description, type);
        this.dateTime = dateTime;
    }

    public void rejectWish() {
        this.wishStatus = WishStatus.REJECTED;
    }

    public void approveWish(int requiredLevel) {
        this.requiredLevel = requiredLevel;
        this.wishStatus = WishStatus.APPROVED;
        this.approved = true;
    }

    public int parsePrice() {
        // "Price:150TL"  -> 150
        try {
            int idx = description.toLowerCase().indexOf("price:");
            if (idx < 0) return 0;
            String digits = description.substring(idx + 6)
                    .replaceAll("[^0-9]", "");
            return digits.isEmpty() ? 0 : Integer.parseInt(digits);
        } catch (Exception e) { return 0; }
    }


    public boolean isActivity() {
        return this.type == WishType.ACTIVITY;
    }

    // Getter & Setter
    public int getWishID() {
        return wishID;
    }

    public String getTitle() {
        return title;
    }

    public WishStatus getWishStatus() {
        return wishStatus;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setWishID(int wishID) {
        this.wishID = wishID;
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

    public WishType getType() {
        return type;
    }

    public void setType(WishType type) {
        this.type = type;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setWishStatus(WishStatus wishStatus) {
        this.wishStatus = wishStatus;
    }
}
