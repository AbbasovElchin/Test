import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ReaderFileServiceImpl {

    private static final File file = new File("Directory.txt");
    private static final File resultFilePath = new File("SortedDirectory.txt");

    public static void main(String[] args) {

        try (
                var br = new BufferedReader(new FileReader(file));
                var resultOutputStream = new FileOutputStream(resultFilePath)
        ) {

            Map<String, FileOutputStream> outputFileHandlers = new HashMap<>();
            Map<String, String> filePaths = new TreeMap<>();

            var line = br.readLine();

            while (null != line && line.length() > 0) {

                var newLine = line + System.lineSeparator();
                var character = line.substring(0, 1);
                var filePath = character + ".txt";

                FileOutputStream outputStream = outputFileHandlers.get(character);

                if (outputStream == null) {

                    filePaths.put(character, filePath);
                    outputStream = new FileOutputStream(filePath);
                    outputFileHandlers.put(character, outputStream);
                }

                outputStream.write(newLine.getBytes());
                line = br.readLine();
            }

            for (var entry : outputFileHandlers.entrySet()) {
                entry.getValue().close();
            }

            for (var keyCharacter : filePaths.keySet()) {
                var inputPath = filePaths.get(keyCharacter);
                var buffReader = new BufferedReader(new FileReader(inputPath));
                Map<String, String> fileContent = new TreeMap<>();

                while (buffReader.ready()) {
                    var inputLine = buffReader.readLine();
                    var delimiterIndex = inputLine.indexOf(", -");
                    var key = inputLine.substring(0, delimiterIndex);
                    var value = inputLine.substring(delimiterIndex + ", -".length());
                    fileContent.put(key, value);
                }

                for (var keyValue : fileContent.keySet()) {
                    var newLine = keyValue + ", -" + fileContent.get(keyValue) + System.lineSeparator();
                    resultOutputStream.write(newLine.getBytes());
                }
            }

            for (var filePath : filePaths.values()) {
                File file = new File(filePath);
                if (!file.delete()) {
                    throw new RuntimeException("File is not deleted!");
                }

            }

        } catch (Exception e) {
            throw new RuntimeException("Something wrong!");
        }
    }

}