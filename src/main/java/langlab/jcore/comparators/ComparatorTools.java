package langlab.jcore.comparators;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

public final class ComparatorTools {

    private ComparatorTools() {}

    public static int calcCommonPrefixLength
        (String word1,  String word2) {

        String shorterWord,longerWord;
        if (word1.length()< word2.length()) {
            shorterWord=word1;
            longerWord=word2;
        } else {
            shorterWord=word2;
            longerWord=word1;
        }

        int commonPrefixLength=0;

        for(int i = 0; i < shorterWord.length(); i++) {
            int shorterCp = shorterWord.codePointAt(i);
            int longerCp = longerWord.codePointAt(i);
            if (shorterCp==longerCp) {
                commonPrefixLength++;
            }
            else {
                break;
            }
        }

        return commonPrefixLength;
    }

}
