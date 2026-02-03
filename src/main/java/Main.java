

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Search Engine started");
        String folderPath = "resources/files1";
        File folder = new File(folderPath);

    
            if (!folder.exists()) {
                System.out.println("Resource 'resources/files1' not found");
                return;
            }

            if (!folder.exists()) { // Eλεγχος Υπαρξης 
                System.out.println("Folder does not exist");
                return;
            }

            if (!folder.isDirectory()) {
                System.out.println("Path is not a directory");
                return;
            }

            File[] files = folder.listFiles();

            if (files == null) {
                System.out.println("Could not read files");
                return;
            }

            Map<String, String> fileContents = new HashMap<>();
            Map<String, Set<String>> InvertedIndex = new HashMap<>();

            for (File file : files) {

                if (file.isFile() && file.getName().endsWith(".txt")) {

                    try {
                        String content = Files.readString(file.toPath());
                        fileContents.put(file.getName(), content);

                    } catch (IOException e) {
                        System.out.println("Error reading file: " + file.getName());
                    }

                }
            }

            for (Map.Entry<String, String> entry : fileContents.entrySet()) {
                String fileName = entry.getKey();
                String content = entry.getValue();

                // 1. lowercase
                content = content.toLowerCase();

                // 2. αφαίρεση σημείων στίξης
                content = content.replaceAll("[^a-z0-9\\s]", " ");

                // 3. split σε λέξεις
                String[] words = content.split("\\s+");

                for (String word : words) {

                    if (word.isEmpty()) {
                        continue;
                    }

                    // 4.  Αρχικοποιηση και ενημέρωση inverted index
                    if (!InvertedIndex.containsKey(word)) {
                        InvertedIndex.put(word, new HashSet<>());
                    }

                    InvertedIndex.get(word).add(fileName);
                }

            }

            // Menu loop
            boolean running = true;
            while (running) {
                System.out.println("\n========== Search Engine Menu ==========");
                System.out.println("1. Search single word");
                System.out.println("2. Search AND (both words)");
                System.out.println("3. Search OR (either word)");
                System.out.println("4. Exit");
                System.out.print("Enter your choice (1-4): ");
                
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
                    case "1":
                        System.out.print("Enter a word to search: ");
                        String searchWord = scanner.nextLine();
                        searchWord(InvertedIndex, searchWord);
                        break;
                    
                    case "2":
                        System.out.print("Enter first word: ");
                        String word1 = scanner.nextLine();
                        System.out.print("Enter second word: ");
                        String word2 = scanner.nextLine();
                        searchAND(InvertedIndex, word1, word2);
                        break;
                    
                    case "3":
                        System.out.print("Enter first word: ");
                        word1 = scanner.nextLine();
                        System.out.print("Enter second word: ");
                        word2 = scanner.nextLine();
                        searchOr(InvertedIndex, word1, word2);
                        break;
                    
                    case "4":
                        System.out.println("Exiting Search Engine...");
                        running = false;
                        break;
                    
                    default:
                        System.out.println("Invalid choice! Please enter 1-4.");
                }
            }
            scanner.close();
        }

    

    public static void searchWord(Map<String, Set<String>> InvertedIndex, String word) {
        {

            // Convert word to lowercase for consistent searching
            word = word.toLowerCase();

            Set<String> files = InvertedIndex.get(word);

            if (InvertedIndex.containsKey(word)) {
                System.out.println("The word " + word + " found in file(s): ");
                for (String file : files) {
                    System.out.println(file);
                }
            } else {
                System.out.println("Word " + word + " not found");
            }

        }
    }

    public static void searchAND(Map<String, Set<String>> InvertedIndex, String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        // Αν κανενα αρχειο δεν περιεχει καμια απο τις δυο λεξεις
        if (!InvertedIndex.containsKey(word1) || !InvertedIndex.containsKey(word2)) {
            System.out.println("No files contain both words.");
            return;
        }

        Set<String> result = new HashSet<>(InvertedIndex.get(word1));

        result.retainAll(InvertedIndex.get(word2));

        if (result.isEmpty()) {
            System.out.println("No files contain both words.");
            return;
        }

        System.out.println("Files containing BOTH \"" + word1 + "\" AND \"" + word2 + "\":");
        for (String file : result) {
            System.out.println("- " + file);
        }

    }

    public static void searchOr(
            Map<String, Set<String>> invertedIndex,
            String word1,
            String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        Set<String> result = new HashSet<>();

        if (invertedIndex.containsKey(word1)) {
            result.addAll(invertedIndex.get(word1));
        }

        if (invertedIndex.containsKey(word2)) {
            result.addAll(invertedIndex.get(word2));
        }

        if (result.isEmpty()) {
            System.out.println("No files contain either word.");
            return;
        }

        System.out.println("Files containing \"" + word1 + "\" OR \"" + word2 + "\":");
        for (String file : result) {
            System.out.println("- " + file);
        }
    }

}


