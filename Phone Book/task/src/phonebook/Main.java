package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String DIR_PATH = "D:\\7-Learn\\Java\\HyperSkill\\Phone Book\\Phone Book\\assest\\directory.txt";
    private static final String FIND_PATH = "D:\\7-Learn\\Java\\HyperSkill\\Phone Book\\Phone Book\\assest\\find.txt";
    private static final String SORT_PATH = "D:\\7-Learn\\Java\\HyperSkill\\Phone Book\\Phone Book\\assest\\sort.txt";

    private static final ArrayList<Contact> dirContacts = new ArrayList<>();
    private static final HashSet<String> findNames = new HashSet<>();
    private static ArrayList<Contact> sortedContacts = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        applyLinearSearch();
        System.out.println();
        applyJumpSearch();
    }

    private static void readFiles() {
        File dirFile = new File(DIR_PATH);
        File findFile = new File(FIND_PATH);
//        File sortedFile = new File(SORT_PATH);

        try (Scanner dirScanner = new Scanner(dirFile);
             Scanner findScanner = new Scanner(findFile);
                /*Scanner sortScanner = new Scanner(sortedFile)*/) {
            while (dirScanner.hasNext()) {
                String contact = dirScanner.nextLine();
                int number = Integer.parseInt(contact.substring(0, contact.indexOf(" ")));
                dirContacts.add(new Contact(contact.substring(contact.indexOf(" ") + 1), number));
            }
            while (findScanner.hasNext()) {
                findNames.add(findScanner.nextLine());
            }

//            while (sortScanner.hasNext()) {
//                String contact = sortScanner.nextLine();
//                int number = Integer.parseInt(contact.substring(0, contact.indexOf(" ")));
//                sortedContacts.add(new Contact(contact.substring(contact.indexOf(" ") + 1), number));
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void applyLinearSearch() {
        System.out.println("Start searching (linear search)...");
        readFiles();
        var t1 = Instant.now();

        int found = 0;
        var names = dirContacts.stream().map(Contact::getName).collect(Collectors.toList());
        for (var contact : findNames) {
            if (names.contains(contact)) {
                found++;
            }
        }
        var t2 = Instant.now();
        var duration = Duration.between(t1, t2);
        var minutes = duration.toMinutesPart();
        var seconds = duration.toSecondsPart();
        var millis = duration.toMillisPart();

        System.out.println("Found " + found + " / " + findNames.size() + " entries. Time taken: " + minutes + " min. " + seconds + " sec. " + millis + " ms.");
    }

    private static void applyJumpSearch() throws IOException {
        System.out.println("Start searching (bubble sort + jump search)...");

        var tSort1 = Instant.now();
        sort();
        var tSort2 = Instant.now();
        var sortDuration = Duration.between(tSort1, tSort2);
        var t1 = Instant.now();
        var sortedNames = sortedContacts.stream()
                .map(Contact::getName)
                .collect(Collectors.toList());

        int found = 0;
        for (var name : findNames) {
            if (jumpSearch(sortedNames, name)) {
                found++;
            }
        }

        var t2 = Instant.now();
        var searchDuration = Duration.between(t1, t2);

        var fullDuration = Duration.between(tSort1, t2);
        System.out.println("Found " + found + " / " + findNames.size() + " entries. Time taken: "
                + fullDuration.toMinutesPart() + " min. "
                + fullDuration.toSecondsPart() + " sec. "
                + fullDuration.toMillisPart() + " ms.");

        System.out.println("Sorting time:  "
                + sortDuration.toMinutesPart() + " min. "
                + sortDuration.toSecondsPart() + " sec. "
                + sortDuration.toMillisPart() + " ms.");

        System.out.println("Searching time:  "
                + searchDuration.toMinutesPart() + " min. "
                + searchDuration.toSecondsPart() + " sec. "
                + searchDuration.toMillisPart() + " ms.");
    }

    private static boolean jumpSearch(List<String> sortedNames, String name) {
        int blockSize = (int) Math.floor(Math.sqrt(sortedNames.size()));

        int currentLastIndex = blockSize - 1;

        // Jump to next block as long as target element is > currentLastIndex
        // and the array end has not been reached
        while (currentLastIndex < sortedNames.size() && name.compareToIgnoreCase(sortedNames.get(currentLastIndex)) > 0) {
            currentLastIndex += blockSize;
        }

        // Find accurate position of target element using Linear Search
        for (int currentSearchIndex = currentLastIndex - blockSize + 1;
             currentSearchIndex <= currentLastIndex && currentSearchIndex < sortedNames.size(); currentSearchIndex++) {
            if (name.equals(sortedNames.get(currentSearchIndex))) {
                return true;
            }
        }
        // Target element not found. Return negative integer as element position.
        return false;
    }

    private static void sort() throws IOException {
        sortedContacts = dirContacts.stream()
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .collect(Collectors.toCollection(ArrayList::new));

        try (FileWriter writer = new FileWriter(SORT_PATH)) {
            int i = 0;
            for (var contact : sortedContacts) {
                if (i == sortedContacts.size() - 1) {
                    writer.write(contact.getPhoneNumber() + " " + contact.getName());
                } else {
                    writer.write(contact.getPhoneNumber() + " " + contact.getName() + "\n");
                }
                i++;
            }
        }
    }
}