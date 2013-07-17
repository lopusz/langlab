package langlab.base.core.characters;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class StringTools {

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
}
