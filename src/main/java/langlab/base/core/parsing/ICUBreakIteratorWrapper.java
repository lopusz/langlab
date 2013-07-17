package langlab.base.core.parsers;

import java.util.Iterator;
import com.ibm.icu.text.BreakIterator;
import java.util.Locale;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

public class ICUBreakIteratorWrapper implements Iterator<String> {

    String s;
    BreakIterator bi;
    int beg,end;
    boolean hasNextCalled;

    public boolean hasNext() {  
        if (!hasNextCalled) {
            String res;
            do { 
                beg=end;
                end=bi.next();
                if (end!=BreakIterator.DONE) {
                    res=s.substring(beg,end).trim();
                } else {
                    res="done";
                }
            } while ( res.length()==0 );
            hasNextCalled=true;
        }

        return (end!=BreakIterator.DONE);
    }

    public String next() throws NoSuchElementException {
        if (hasNext()) {
            hasNextCalled=false;
            return s.substring(beg,end).trim();
        }
        else {
            throw new NoSuchElementException();
        }
    }

    public void remove() throws UnsupportedOperationException { 
        throw  new UnsupportedOperationException();
    }
    

    private ICUBreakIteratorWrapper(BreakIterator abi,String as) {
        bi=abi;
        s=as;
        bi.setText(s);
        end=bi.first();
        beg=end;
        hasNextCalled=false;
    }

    public static ICUBreakIteratorWrapper 
    getSentenceIterator(String as, Locale loc) {
        return 
          new ICUBreakIteratorWrapper(BreakIterator.getSentenceInstance(loc),as);
    }

    public static ICUBreakIteratorWrapper 
    getSentenceIterator(String as, String lang) {
        Locale loc=new Locale(lang);
        return 
          new ICUBreakIteratorWrapper(BreakIterator.getSentenceInstance(loc),as);
    }

    public static ICUBreakIteratorWrapper 
    getWordIterator(String as, Locale loc) {
        return 
          new ICUBreakIteratorWrapper(BreakIterator.getWordInstance(loc),as);
    }

    public static ICUBreakIteratorWrapper 
    getWordIterator(String as, String lang) {
        Locale loc=new Locale(lang);
        return 
          new ICUBreakIteratorWrapper(BreakIterator.getWordInstance(loc),as);
    }
}
