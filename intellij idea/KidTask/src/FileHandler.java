import java.io.*;
import java.util.*;

public class FileHandler {

    public List<String> readCommands(String filePath) throws IOException {
        List<String> commands = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            commands.add(line.trim());
        }
        reader.close();
        return commands;
    }

    public void writeLog(String filePath, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
        writer.write(content + "\n");
        writer.close();
    }

    public void processCommand(String command, Child child, Parent parent, Teacher teacher) {
        // örnek: ADD_TASK1, TASK_DONE, PRINT_STATUS
        // komutlara göre yönlendir
    }
}
