package langlab.base.characters;

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
    
    public static String removeDiacritics(String string) {
        return Normalizer.normalize(string, Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .replace("ł","l")
            .replace("Ł","L");

    }
}

