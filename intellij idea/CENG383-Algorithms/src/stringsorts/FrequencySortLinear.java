package stringsorts;

import java.util.*;

public class FrequencySortLinear {

    public static void main(String[] args) {
        int[] arr = {2, 5, 2, 8, 5, 6, 8, 8};

        // 1. Frekansları say
        int[] freq = new int[101];  // çünkü değerler 1..100 arasında

        for (int num : arr) {
            freq[num]++;
        }

        // 2. Bucket dizisi oluştur (her frekans için liste)
        List<Integer>[] buckets = new ArrayList[arr.length + 1];  // max frekans = n
        for (int i = 0; i <= arr.length; i++) {
            buckets[i] = new ArrayList<>();
        }

        // 3. Sayıları frekanslarına göre bucket’lara koy
        for (int i = 1; i <= 100; i++) {
            if (freq[i] > 0) {
                int f = freq[i];          // frekans
                buckets[f].add(i);        // frekansa uygun bucket’a koy
            }
        }

        // 4. En yüksek frekanstan başlayarak sonuç dizisini oluştur
        List<Integer> result = new ArrayList<>();
        for (int f = arr.length; f >= 1; f--) {
            for (int val : buckets[f]) {
                for (int i = 0; i < f; i++) {
                    result.add(val);  // frekans sayısı kadar ekle
                }
            }
        }

        // 5. Sonucu yazdır
        System.out.println("Sorted by frequency:");
        System.out.println(result);
    }
}
