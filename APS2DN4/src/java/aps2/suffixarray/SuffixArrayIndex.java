package aps2.suffixarray;

import org.junit.Test;

import java.util.*;

public class SuffixArrayIndex {
    private String text; // input string
    private int[] SA;    // suffix array
    private String[] Suffix;
    private int[] tempMergArr;


    public int[] getSuffixArray() {
        return SA;
    }

    SuffixArrayIndex(String text) {
        this.text = text;
        this.SA = new int[text.length()];
        Suffix = new String[text.length()];

        construct();
    }

    /**
     * Constructs the suffix array corresponding to the text in expected
     * O(n log n) suffix comparisons.
     */
    private void construct() {
        //Integer[] tmp = new Integer[text.length()];
        //String suff = "";
        for (int i = 0; i < SA.length; i++) {
            //suff = text.charAt(i) + suff;
            this.SA[i] = i;
            //tmp[i] = i;
        }

        //SA = new int[] {45,23,11,89,77,98,4,28,65,43, -5, 45,
        //4, 28, 647, 8525};
        mergeSort(SA);
        /*Arrays.sort(tmp, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (suffixSuffixCompare(SA[o1], SA[o2]))
                    return -1;
                return 0;
            }
        });
        for (int i = 0; i < tmp.length; i++) {
            this.SA[i] = tmp[i];
        }*/
    }


    public void mergeSort(int[] arr){
        this.SA = arr;
        tempMergArr = new int[arr.length];
        doMergeSort(0, arr.length-1);
    }

    public void doMergeSort(int l, int h){
        if (l < h){
            int m = l + (h - l) / 2;
            doMergeSort(l, m);
            doMergeSort(m+1, h);
            mergeParts(l, m, h);
        }
    }

    public void mergeParts(int l, int m, int h){
        for (int i = l; i <= h ; i++) {
            tempMergArr[i] = SA[i];
        }
        int i = l;
        int j = m+1;
        int k = l;
        while (i <= m && j <= h){
            if (suffixSuffixCompare(tempMergArr[i], tempMergArr[j])){
                SA[k] = tempMergArr[i];
                i++;
            }else{
                SA[k] = tempMergArr[j];
                j++;
            }
            k++;
        }
        while (i <= m){
            SA[k] = tempMergArr[i];
            k++;
            i++;
        }
        while (j <= h){
            SA[k] = tempMergArr[j];
            j++;
            k++;
        }
    }

    /**
     * Returns True, if the suffix at pos1 is lexicographically before
     * the suffix at pos2.
     *
     * @param int pos1
     * @param int pos2
     * @return boolean
     */
    public boolean suffixSuffixCompare(int pos1, int pos2) {
        while (pos1 < text.length() && pos2 < text.length()){
            if (text.charAt(pos1) > text.charAt(pos2))
                return false;
            else if (text.charAt(pos1) < text.charAt(pos2))
                return true;
            pos1++;
            pos2++;
        }
        return false;
    }


    /**
     * Return True, if the query string is lexicographically lesser or
     * equal to the suffix string at pos1.
     *
     * @param String query The query string
     * @param int    pos2 Position of the suffix
     * @return boolean
     */
    public boolean stringSuffixCompare(String query, int pos2) {
        int i = 0;
        while (i < query.length() && pos2 < text.length()){
            if (query.charAt(i) < text.charAt(pos2))
                return false;
            else if (query.charAt(i) > text.charAt(pos2))
                return true;
            i++;
            pos2++;
        }
        return false;
    }

    /**
     * Returns the positions of the given substring in the text using binary
     * search. The empty query is located at all positions in the text.
     *
     * @param String query The query substring
     * @return A set of positions where the query is located in the text
     */
    public Set<Integer> locate(String query) {
        if (!text.contains(query)) return new HashSet<>();
        int l = 0;
        int r = SA.length-1;
        Set<Integer> tmp = new HashSet<>();
        while (l <= r) {
            int  mid = l + (r - l) / 2;
            if (stringSuffixCompare(query, SA[mid])) {
                l = mid + 1;
            } else
                r = mid - 1;
        }

        for (int i = l; i >= 0; i--){
            if (longestCommonPrefixLen(SA[l], SA[i]) >= query.length())
                tmp.add(SA[i]);
            else
                break;
        }
        for (int i = l; i < SA.length; i++){
            if (longestCommonPrefixLen(SA[l], SA[i]) >= query.length())
                tmp.add(SA[i]);
            else
                break;
        }
        return tmp;
    }


    /**
     * Returns the longest substring in the text which repeats at least 2 times
     * by examining the suffix array.
     *
     * @return The longest repeated substring in the text
     */
    public String longestRepeatedSubstring() {
        int max = 0;
        int tmp1 = 0;
        for (int i = 0; i < SA.length - 1; i++) {
            int lcp = longestCommonPrefixLen(SA[i], SA[i+1]);
            if (lcp > max){
                max = lcp;
                tmp1 = SA[i];
            }
        }
        String t = "";
        int tmp2 = tmp1;
        while (tmp1 < tmp2+max){
            t += text.charAt(tmp1);
            tmp1++;
        }
        return t;
    }

    /**
     * Calculates the length of the longest common prefix of two suffixes.
     *
     * @param int pos1 Position of the first suffix in the text
     * @param int pos2 Position of the second suffix in the text
     * @return The number of characters in the common prefix of the first and the second suffix
     */
    public int longestCommonPrefixLen(int pos1, int pos2) {
        String s1 = text.substring(pos1);
        String s2 = text.substring(pos2);
        int counter = 0;
        if (s1.length() < s2.length()){
            for (int i = 0; i < s1.length(); i++) {
                if (s1.charAt(i) == s2.charAt(i))
                    counter++;
                else break;
            }
        }else{
            for (int i = 0; i < s2.length(); i++) {
                if (s1.charAt(i) == s2.charAt(i))
                    counter++;
                else break;
            }
        }
        return counter;
    }
}

