package stringsorts;

public class MSDRadixSort {

    public static int R = 256;

    /*
    Tüm string'leri ilk karakterlerine göre grupla.

    Her grup için, o grubu tekrar kendi içinde ikinci karaktere göre sırala (recursive).

    Bu işlem stringlerin sonuna kadar devam eder (ya da karakter biter).*/

    public static void sort(String[] a) {
        String[] aux = new String[a.length];
        sort(a, aux, 0, a.length - 1, 0);
    }
    private static void sort(String[] a, String[] aux, int lo, int hi, int d) {
        if (hi <= lo) return;
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++)
            count[charAt(a[i], d) + 2]++;
        for (int r = 0; r < R+1; r++)
            count[r+1] += count[r];
        for (int i = lo; i <= hi; i++)
            aux[count[charAt(a[i], d) + 1]++] = a[i];
        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];
        for (int r = 0; r < R; r++)
            sort(a, aux, lo + count[r], lo + count[r+1] - 1, d+1);
    }

    private static int charAt(String s, int d) {
        return d < s.length() ? s.charAt(d) : -1;
    }

}
