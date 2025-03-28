import domain.*;
import manager.*;
import file.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1) Domain nesnelerini oluştur
        Child child = new Child("C001", "Ahmet", "Child");
        Parent parent = new Parent("P001", "Mehmet", "Parent");
        Teacher teacher = new Teacher("T001", "Ayse", "Teacher");

        // 2) Manager nesneleri
        TaskManager taskManager = new TaskManager();
        WishManager wishManager = new WishManager();

        // 3) Komutları yönetecek bir sınıf
        //    (ister FileHandler içindeki parseCommand metodlarını kullanır,
        //     isterseniz CommandHandler gibi ayrı bir sınıfa da koyabilirsiniz)
        CommandHandler cmdHandler = new CommandHandler(taskManager, wishManager, child, parent, teacher);

        // 4) Program başlarken commands.txt içindeki komutları uygula
        //    (örneğin projenin kök dizininde "commands.txt" olduğunu varsayıyoruz)
        String commandsFilePath = "C:\\Users\\nedim göktuğ tabak\\Desktop\\Software\\intellij idea\\KidTask\\src\\file\\commands.txt";
        cmdHandler.processCommandsFromFile(commandsFilePath);

        // 5) Artık kullanıcının konsoldan komut girmesini bekle.
        //    Kullanıcı "EXIT" yazana kadar parse edip devam et.
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\nBir komut giriniz (EXIT ile çıkış): ");
            String inputLine = scanner.nextLine();
            if (inputLine.equalsIgnoreCase("EXIT")) {
                System.out.println("Program sonlandırılıyor...");
                break;
            }
            cmdHandler.parseCommand(inputLine);
        }

        // Son durumda çocuğun durumunu ekrana basalım
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
