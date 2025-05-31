package stringsorts;

public class KeyIndexedCounting {

    public static int R = 256;

    public static void sort(String[] a){
        int N = a.length;
        String[] aux = new String[N]; // Yardımcı dizi
        int[] count = new int[R+1];

        // 1. Frekans sayımı
        for (int i = 0; i < N; i++) {
            int c = a[i].charAt(0); // İlk karakterin ASCII değeri
            count[c + 1]++; // Frekansı bir sonraki hücreye yaz
        }

        // 2. Kümülatif toplam (index hesaplama)
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        // 3. Dağıtım: her karakteri doğru pozisyona yerleştir
        for (int i = 0; i < N; i++) {
            int c = a[i].charAt(0);
            aux[count[c]++] = a[i];
        }

        // 4. Geri kopyalama: aux[] → a[]
        for (int i = 0; i < N; i++) {
            a[i] = aux[i];
        }

    }
}
