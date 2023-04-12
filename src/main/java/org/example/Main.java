package org.example;


import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queue_A = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue_B = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue_C = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        int countStrings = 10_000;
        int countLetters = 100_000;


        Thread thrdGenerateText = new Thread(() -> {
            for (int r = 0; r < countStrings; r++) {
                String s = generateText("abc", countLetters);
                try {
                    queue_A.put(s);
                    queue_B.put(s);
                    queue_C.put(s);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        thrdGenerateText.start();

        Thread a = new Thread(() -> {
            char letter = 'a';
            int maxA = maxLetterInRoute(queue_A, letter);
            System.out.println("Максимальное количество буквы" + " \"" + letter + "\": " + maxA);
        });
        a.start();

        Thread b = new Thread(() -> {
            char letter = 'b';
            int maxB = maxLetterInRoute(queue_B, letter);
            System.out.println("Максимальное количество буквы" + " \"" + letter + "\": " + maxB);
        });
        b.start();

        Thread c = new Thread(() -> {
            char letter = 'c';
            int maxC = maxLetterInRoute(queue_C, letter);
            System.out.println("Максимальное количество буквы" + " \"" + letter + "\": " + maxC);
        });
        c.start();

        a.join();
        b.join();
        c.join();

    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static int maxLetterInRoute(BlockingQueue<String> queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < 10000; i++) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted");
            return -1;
        }
        return max;
    }
}
