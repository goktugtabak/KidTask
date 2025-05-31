package stringsorts;

import java.util.Arrays;

public class SuffixArray {

    private final String text;
    private final int length;
    private final int[] index; // sıralanmış suffix başlangıç indeksleri

    // Suffix sınıfı
    private static class Suffix implements Comparable<Suffix> {
        private final String text;
        private final int offset;

        public Suffix(String text, int offset) {
            this.text = text;
            this.offset = offset;
        }

        public int length() {
            return text.length() - offset;
        }

        public char charAt(int i) {
            return text.charAt(offset + i);
        }

        @Override
        public int compareTo(Suffix that) {
            int minLen = Math.min(this.length(), that.length());
            for (int i = 0; i < minLen; i++) {
                if (this.charAt(i) != that.charAt(i)) {
                    return this.charAt(i) - that.charAt(i);
                }
            }
            return this.length() - that.length();
        }

        @Override
        public String toString() {
            return text.substring(offset);
        }
    }

    // Suffix array constructor
    public SuffixArray(String text) {
        this.text = text;
        this.length = text.length();

        // 1. Tüm suffix nesnelerini oluştur
        Suffix[] suffixes = new Suffix[length];
        for (int i = 0; i < length; i++) {
            suffixes[i] = new Suffix(text, i);
        }

        // 2. Leksikografik olarak sırala
        Arrays.sort(suffixes);

        // 3. Sıralanmış index'leri kaydet
        index = new int[length];
        for (int i = 0; i < length; i++) {
            index[i] = suffixes[i].offset;
        }
    }

    // Sıralı index'leri döndür
    public int[] getSuffixArray() {
        return index;
    }

    // Recursive method
    public static int substringSearch(String pattern, String text, int[] suffixArray, int low, int high) {
        if (low > high) return -1;  // not found

        int mid = low + (high - low) / 2;
        int suffixStart = suffixArray[mid];
        String suffix = text.substring(suffixStart);

        if (suffix.startsWith(pattern)) {
            return suffixStart; // Match found
        } else if (suffix.compareTo(pattern) < 0) {
            return substringSearch(pattern, text, suffixArray, mid + 1, high); // go right
        } else {
            return substringSearch(pattern, text, suffixArray, low, mid - 1); // go left
        }
    }

    public static String findLongestRepeatedSubstring(String s) {
        int n = s.length();
        String[] suffixes = new String[n];

        // 1. Adım: Suffixleri oluştur
        for (int i = 0; i < n; i++) {
            suffixes[i] = s.substring(i);
        }

        // 2. Adım: Suffixleri alfabetik sıraya koy
        Arrays.sort(suffixes);

        // 3. Adım: En uzun LCP’yi bul
        String longest = "";
        for (int i = 0; i < n - 1; i++) {
            String lcp = commonPrefix(suffixes[i], suffixes[i + 1]);
            if (lcp.length() > longest.length()) {
                longest = lcp;
            }
        }

        return longest;
    }

    // Yardımcı fonksiyon: İki string’in ortak prefix’ini bul
    private static String commonPrefix(String a, String b) {
        int len = Math.min(a.length(), b.length());
        int i = 0;
        while (i < len && a.charAt(i) == b.charAt(i)) {
            i++;
        }
        return a.substring(0, i);
    }

    public static void main(String[] args) {
        String input = "banana";
        SuffixArray sa = new SuffixArray(input);
        int[] saIndex = sa.getSuffixArray();

        System.out.println("Suffix Array:");
        for (int idx : saIndex) {
            System.out.printf("%d: %s\n", idx, input.substring(idx));
        }
    }
}
