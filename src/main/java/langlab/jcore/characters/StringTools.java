package langlab.jcore.characters;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

public final class StringTools {

    private StringTools() {}

    public static int countLatinVowelGroups (String word) {
        boolean lastCharWasConsonant=true;
        int numVowelGroups=0;
        
        for(int i = 0; i < word.length(); ) {
             int cp = word.codePointAt(i);
             if (CharacterTools.isLatinVowel(cp)) {
                 if (lastCharWasConsonant) numVowelGroups++;
                 lastCharWasConsonant=false;
             }
             else {
                 lastCharWasConsonant=true;
             }
            i += Character.charCount(cp);
         }
        
        if (numVowelGroups<0) numVowelGroups=0;
         
        return numVowelGroups;
    }

    public static int countLatinVowelGroupsWithoutFinal (String word) {
        boolean lastCharWasConsonant=true;
        int numVowelGroups=0;
        
        for(int i = 0; i < word.length(); ) {
            int cp = word.codePointAt(i);
            if (CharacterTools.isLatinVowel(cp)) {
                if (lastCharWasConsonant) numVowelGroups++;
                lastCharWasConsonant=false;
            }
            else {
                 lastCharWasConsonant=true;
            }
            i += Character.charCount(cp);
        }

        if (! lastCharWasConsonant) {
            numVowelGroups--;
        }

        if (numVowelGroups<0) numVowelGroups=0;
         
        return numVowelGroups;
    }
    
    public static String removeDiacritics(String s) {
        return Normalizer.normalize(s, Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .replace("ł","l")
            .replace("Ł","L");

    }

    public static boolean containsWhitespaceOnly(String s) {
        boolean res=true;
    
        for(int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (!Character.isWhitespace(cp)) {
                res=false;
                break;
            }
            i += Character.charCount(cp);
        }

        if (s.length()==0) { res=false; } 

        return res;
    }

    public static boolean containsLettersOrDigitsOnly(String s) {
        boolean res=true;

        for(int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (!Character.isLetterOrDigit(cp)) {
                res=false;
                break;
            }
            i += Character.charCount(cp);
        }

        if (s.length()==0) { res=false; }

        return res;
    }

    public static boolean containsWhitespace(String s) {
        boolean res=false;

        for(int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (Character.isWhitespace(cp)) {
                res=true;
                break;
            }
            i += Character.charCount(cp);
        }

        return res;
    }

    public static boolean containsPunctOnly(String s) {
        boolean res=true;

        for(int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (!CharacterTools.isPunctuation(cp)) {
                res=false;
                break;
            }
           
            i += Character.charCount(cp);
        }

        if (s.length()==0) { res=false; } 
    
        return res;
    }

    public static boolean containsPunct(String s) {
        boolean res=false;

        for(int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (CharacterTools.isPunctuation(cp)) {
                res=true;
                break;
            }
            i += Character.charCount(cp);
        }

        return res;
    }

    public static boolean containsNonBmp(String s) {
        boolean res=false;

        for(int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (!Character.isBmpCodePoint(cp)) {
                res=true;
                break;
            }
            i += Character.charCount(cp);
        }

        return res;
    }

    public static String removeNonBmp(String s) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (Character.isBmpCodePoint(cp)) {
                sb.appendCodePoint(cp);
            }
            i += Character.charCount(cp);
        }
        return sb.toString();
    }

    public static String removeBmp(String s) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (!Character.isBmpCodePoint(cp)) {
                sb.appendCodePoint(cp);
            }
            i += Character.charCount(cp);
        }

        return sb.toString();
    }

    public static int countCharactersBreakIterator(String s, Locale loc) {
        // Based on SO
        // http://stackoverflow.com/questions/6828076/how-to-correctly-compute-the-length-of-a-string-in-java

        java.text.BreakIterator charIterator =
            java.text.BreakIterator.getCharacterInstance(loc);

        charIterator.setText(s);

        int res = 0;

        while(charIterator.next() != java.text.BreakIterator.DONE) {
            res++;
        }
        return res;
    }

    public static int countCharactersBreakIterator(String s, String lang) {
        Locale loc=new Locale(lang);
        return countCharactersBreakIterator(s,loc);
    }

    public static int countCharactersICUBreakIterator(String s, Locale loc) {
        // Based on SO
        // http://stackoverflow.com/questions/6828076/how-to-correctly-compute-the-length-of-a-string-in-java

        com.ibm.icu.text.BreakIterator charIterator =
            com.ibm.icu.text.BreakIterator.getCharacterInstance(loc);

        charIterator.setText(s);

        int res = 0;

        while(charIterator.next() != com.ibm.icu.text.BreakIterator.DONE) {
            res++;
        }

        return res;
    }

    public static int countCharactersICUBreakIterator(String s, String lang) {
        Locale loc=new Locale(lang);
        return countCharactersICUBreakIterator(s,loc);
    }
}
