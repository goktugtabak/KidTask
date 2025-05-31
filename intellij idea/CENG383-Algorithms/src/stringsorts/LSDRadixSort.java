package stringsorts;

public class LSDRadixSort {

    public static int R = 256;


    public static void lsdSort(String[] a, int W) {
        int N = a.length;
        String[] aux = new String[N];

        // LSD: En sağdaki karakterden (W-1) sola doğru
        //Sort keye göre (en sağdan en sola) key indexed count kullanarak kelimeleri her adımda sıralar.
        for (int d = W - 1; d >= 0; d--) {
            int[] count = new int[R + 1];  // Frekans sayımı için

            // 1. Frekans sayımı
            for (int i = 0; i < N; i++) {
                int c = a[i].charAt(d);
                count[c + 1]++;
            }

            // 2. Kümülatif toplam
            for (int r = 0; r < R; r++) {
                count[r + 1] += count[r];
            }

            // 3. Dağıtım
            for (int i = 0; i < N; i++) {
                int c = a[i].charAt(d);
                aux[count[c]++] = a[i];
            }

            // 4. Geri kopyalama
            for (int i = 0; i < N; i++) {
                a[i] = aux[i];
            }
        }
    }
}
