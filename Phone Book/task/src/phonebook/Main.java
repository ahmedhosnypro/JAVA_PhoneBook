package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    private static final String DIR_PATH = "D:\\7-Learn\\Java\\HyperSkill\\Phone Book\\Phone Book\\assest\\directory.txt";
    private static final String FIND_PATH = "D:\\7-Learn\\Java\\HyperSkill\\Phone Book\\Phone Book\\assest\\find.txt";

    public static void main(String[] args) {
        File dirFile = new File(DIR_PATH);
        File findFile = new File(FIND_PATH);

        HashMap<String, Integer> dirContacts = new HashMap<>();
        HashSet<String> findContacts = new HashSet<>();

        System.out.println("Start searching...");
        var t1 = Instant.now();

        try (Scanner dirScanner = new Scanner(dirFile); Scanner findScanner = new Scanner(findFile)) {
            while (dirScanner.hasNext()) {
                String contact = dirScanner.nextLine();
                int number = Integer.parseInt(contact.substring(0, contact.indexOf(" ")));
                dirContacts.put(contact.substring(contact.indexOf(" ") + 1), number);
            }
            while (findScanner.hasNext()) {
                findContacts.add(findScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int found = 0;
        var names = dirContacts.keySet();
        for (var contact : findContacts) {
            if (names.contains(contact)) {
                found++;
            }
        }
        var t2 = Instant.now();
        var duration = Duration.between(t1, t2);
        var minutes = duration.toMinutesPart();
        var seconds = duration.toSecondsPart();
        var millis = duration.toMillisPart();

        System.out.println("Found " + found + " / " + findContacts.size() + " entries. Time taken: " + minutes + " min. " + seconds + " sec. " + millis + " ms.");
    }
}
