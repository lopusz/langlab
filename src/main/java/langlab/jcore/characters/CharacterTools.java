package langlab.jcore.characters;

public final class  CharacterTools {

    private CharacterTools() {}

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

    public static boolean isPunctuation(int cp) {
        int t=Character.getType(cp);
        boolean res=false;
        if ( (t==Character.CONNECTOR_PUNCTUATION)     ||
             (t==Character.DASH_PUNCTUATION)          ||
             (t==Character.END_PUNCTUATION)           ||
             (t==Character.FINAL_QUOTE_PUNCTUATION)   ||
             (t==Character.INITIAL_QUOTE_PUNCTUATION) ||
             (t==Character.OTHER_PUNCTUATION)         ||
             (t==Character.START_PUNCTUATION)) {
            res=true;
        }
        return res;
    }
}
