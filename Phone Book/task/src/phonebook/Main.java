package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String DIR_PATH = "D:\\7-Learn\\Java\\HyperSkill\\Phone Book\\Phone Book\\assest\\directory.txt";
    private static final String FIND_PATH = "D:\\7-Learn\\Java\\HyperSkill\\Phone Book\\Phone Book\\assest\\find.txt";

    private static final ArrayList<Contact> dirContacts = new ArrayList<>();
    private static final HashSet<String> findContacts = new HashSet<>();

    public static void main(String[] args) {
        readFiles();
        applyLinearSearch();
        System.out.println();
        applyJumpSearch();
    }

    private static void readFiles() {
        File dirFile = new File(DIR_PATH);
        File findFile = new File(FIND_PATH);


        try (Scanner dirScanner = new Scanner(dirFile); Scanner findScanner = new Scanner(findFile)) {
            while (dirScanner.hasNext()) {
                String contact = dirScanner.nextLine();
                int number = Integer.parseInt(contact.substring(0, contact.indexOf(" ")));
                dirContacts.add(new Contact(contact.substring(contact.indexOf(" ") + 1), number));
            }
            while (findScanner.hasNext()) {
                findContacts.add(findScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void applyLinearSearch() {
        System.out.println("Start searching (linear search)...");

        var t1 = Instant.now();

        int found = 0;
        var names = dirContacts.stream().map(Contact::getName).collect(Collectors.toList());
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

        System.out.println("Found " + found + " / " + findContacts.size()
                + " entries. Time taken: " + minutes + " min. " + seconds
                + " sec. " + millis + " ms.");
    }

    private static void applyJumpSearch() {
        System.out.println("Start searching (bubble sort + jump search)...");

        var tSort1 = Instant.now();

        var names = dirContacts.stream()
                .map(Contact::getName)
                .sorted()
                .collect(Collectors.toList());
        var tSort2 = Instant.now();
        var sortDuration = Duration.between(tSort1, tSort2);

        var t1 = Instant.now();

        int found = 0;
        for (var contact : findContacts) {
            if (names.contains(contact)) {
                found++;
            }
        }


        var t2 = Instant.now();
        var searchDuration = Duration.between(t1, t2);

        var fullDuration = Duration.between(tSort1, t2);
        System.out.println("Found " + found + " / " + findContacts.size()
                + " entries. Time taken: " + fullDuration.toMinutesPart() + " min. "
                + fullDuration.toSecondsPart() + " sec. "
                + fullDuration.toMillisPart() + " ms.");

        System.out.println("Sorting time:  "
                + searchDuration.toMinutesPart() + " min. " +
                searchDuration.toSecondsPart() + " sec. "
                + searchDuration.toMillisPart() + " ms.");

        System.out.println("Searching time:  "
                + sortDuration.toMinutesPart() + " min. " +
                sortDuration.toSecondsPart() + " sec. "
                + sortDuration.toMillisPart() + " ms.");
    }
}
