package langlab.base.core.characters;

public class  CharacterTools {
    static final int [] LATIN_VOWELS = 
                        { 'a', 'e', 'i', 'o', 'u', 'y', 
                          'A', 'E', 'I', 'O', 'U', 'Y' };

    public static boolean isLatinVowel(int cp) {
        boolean res=false;

        for(int v :LATIN_VOWELS) {
            if (v==cp) {
                res=true;
                break;
            }
        }
        return res;
    }
}
