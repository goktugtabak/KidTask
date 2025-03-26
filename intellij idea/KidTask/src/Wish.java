import java.time.LocalDateTime;

public class Wish {
    private int wishID;
    private String title;
    private String description;
    private WishType type;
    private int requiredLevel;
    private boolean approved;
    private LocalDateTime dateTime;
    private WishStatus status;
    private Child requestedBy;

    public Wish(int wishID, String title, String description, WishType type, Child requestedBy) {
        this.wishID = wishID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.requestedBy = requestedBy;
        this.status = WishStatus.PENDING;
    }

    public void approve(int requiredLevel) {
        this.requiredLevel = requiredLevel;
        this.status = WishStatus.APPROVED;
    }

    public void reject() {
        this.status = WishStatus.REJECTED;
    }

    public boolean isActivity() {
        return type == WishType.ACTIVITY;
    }

    public int getWishID() {
        return wishID;
    }

    public void setWishID(int wishID) {
        this.wishID = wishID;
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

    public WishType getType() {
        return type;
    }

    public void setType(WishType type) {
        this.type = type;
    }

    public int getRequiredLevel() {
        return requiredLevel;
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

    public WishStatus getStatus() {
        return status;
    }

    public void setStatus(WishStatus status) {
        this.status = status;
    }

    public Child getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Child requestedBy) {
        this.requestedBy = requestedBy;
    }
}
