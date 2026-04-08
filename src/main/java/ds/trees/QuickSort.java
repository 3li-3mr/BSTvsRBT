package ds.trees;

public class QuickSort{
    private static int partition(int[] array, int l, int r){
        int random = l + (int)(Math.random() * (r - l + 1));
        int temp = array[l];
        array[l] = array[random];
        array[random] = temp;
        int i = l;
        for(int j = l+1; j <= r; j++){
            if(array[j] <= array[l]){
                i++;
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        temp = array[l];
        array[l] = array[i];
        array[i] = temp;
        return i;
    }

    private static void helper(int[] array, int l, int r){
        if(l >= r) return;
        int mid = partition(array, l, r);
        helper(array, l, mid - 1);
        helper(array, mid + 1, r);
    }

    public static int[] sort(int[] array) {
        helper(array, 0, array.length - 1);
        return array;
    }
}
