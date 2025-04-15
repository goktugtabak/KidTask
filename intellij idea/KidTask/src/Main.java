import domain.Child;
import domain.Parent;
import domain.Teacher;
import file.FileHandler;
import manager.TaskManager;
import manager.WishManager;

public class Main {
    public static void main(String[] args) {
        Child child = new Child("C001", "Ahmet", "Child");
        Parent parent = new Parent("P001", "Mehmet", "Parent");
        Teacher teacher = new Teacher("T001", "Ayse", "Teacher");

        // 2) Manager nesneleri
        TaskManager taskManager = new TaskManager();
        WishManager wishManager = new WishManager();

        // 3) FileHandler (dosyalardan okuma & komutları işleme)
        FileHandler fileHandler = new FileHandler(taskManager, wishManager, child, parent, teacher);

        String tasksFile   = "C:\\Users\\nedim göktuğ tabak\\Desktop\\Software\\intellij idea\\KidTask\\src\\file\\tasks.txt";
        String wishesFile  = "C:\\Users\\nedim göktuğ tabak\\Desktop\\Software\\intellij idea\\KidTask\\src\\file\\wishes.txt";
        String commandsFile= "C:\\Users\\nedim göktuğ tabak\\Desktop\\Software\\intellij idea\\KidTask\\src\\file\\commands.txt";

        // 4) tasks.txt ve wishes.txt okunarak başlangıç verileri yüklüyoruz
        fileHandler.readTasks(tasksFile);
        fileHandler.readWishes(wishesFile);

        // 5) commands.txt içindeki komutları işliyoruz
        fileHandler.processCommands(commandsFile);

        // 6) Son durumda çocuğun durumunu ekrana basalım (program konsoldan input almayacak)
//        System.out.println("\n=== FINAL STATUS ===");
//        System.out.println(child.printStatus());
//        System.out.println("Child's tasks: ");
//        child.viewTasks();

//        System.out.println("\nChild's wishes:");
//        // Dilerseniz WishManager içindeki listAllWishes metodunu da kullanabilirsiniz
//        child.listAllWishes().forEach(w ->
//                System.out.println("Wish " + w.getWishID() + " - " + w.getTitle() +
//                        " - Status: " + w.getWishStatus())
//        );
    }
}

