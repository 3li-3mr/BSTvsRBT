package ds.trees;

public class MergeSort {
    private static int[] helper(int[] array, int l, int r){
        if(l == r) {
            int[] merged = new int[1];
            merged[0] = array[l];
            return merged;
        }

        int mid = (l+r) / 2;
        int[] left = helper(array, l, mid);
        int[] right = helper(array, mid + 1, r);

        int[] merged = new int[left.length + right.length];
        int left_index = 0, right_index = 0, i = 0;
        while(left_index < left.length && right_index < right.length){
            if(left[left_index] <= right[right_index]){
                merged[i++] = left[left_index++];
            }
            else{
                merged[i++] = right[right_index++];
            }
        }

        while(left_index < left.length){
            merged[i++] = left[left_index++];
        }

        while(right_index < right.length){
            merged[i++] = right[right_index++];
        }

        for(int j = 0; j < merged.length; j++){
            array[l + j] = merged[j];
        }

        return merged;
    }

    public static int[] sort(int[] array) {
        return helper(array, 0, array.length - 1);
    }
}