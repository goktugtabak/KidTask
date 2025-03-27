import domain.*;
import manager.*;
import file.FileHandler;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Basit domain nesneleri
        Child child = new Child("C001", "Ahmet", "Child");
        Parent parent = new Parent("P001", "Mehmet", "Parent");
        Teacher teacher = new Teacher("T001", "Ayse", "Teacher");

        // Manager nesneleri
        TaskManager taskManager = new TaskManager();
        WishManager wishManager = new WishManager();

        // FileHandler (txt dosyalarından okuma/yazma ve komut işleme)
        FileHandler fileHandler = new FileHandler(taskManager, wishManager, child, parent, teacher);

        // tasks.txt ve wishes.txt okunarak başlangıç verilerini yüklüyoruz
        fileHandler.readTasks("tasks.txt");
        fileHandler.readWishes("wishes.txt");

        // Ardından commands.txt içindeki komutları işliyoruz
        fileHandler.processCommands("commands.txt");

        // Son olarak kaydetmek istersek:
        fileHandler.writeTasks("tasks_out.txt");
        fileHandler.writeWishes("wishes_out.txt");

        System.out.println("\n=== FINAL STATUS ===");
        System.out.println(child.printStatus());
        System.out.println("Child's tasks: ");
        child.viewTasks();
        System.out.println("\nChild's wishes:");
        for (Wish w : child.listAllWishes()) {
            System.out.println("Wish " + w.getWishID() + " - " + w.getTitle() + " - Status: " + w.getWishStatus());
        }
    }
}
