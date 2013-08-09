package langlab.jcore.detectors;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;

import org.mozilla.universalchardet.UniversalDetector;
import org.mozilla.universalchardet.Constants;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import langlab.jcore.characters.StringTools;

public final class EncodDetectorTools {

    private EncodDetectorTools() {}

    public static final String UNKNOWN="UNKNOWN";

    public static List<String> getEncodAvailUnichardet() 
        throws IllegalAccessException
    {
     
        LinkedList<String> res = new LinkedList<String>();

        for(Field f : Constants.class.getDeclaredFields() ) {
            int   m=f.getModifiers();

            if ( Modifier.isPublic(m) && 
                 Modifier.isFinal(m) &&
                 Modifier.isStatic(m) &&
                 (f.getType().getName() == "java.lang.String")
                ) {
                  String  enc=f.get(Constants.class).toString();
                  res.add(enc.toUpperCase());
               }
            }

        return res;
    }

    public static String detectEncodUnichardet(String fName) 
        throws java.io.IOException {

        byte[] buf = new byte[4096];

        FileInputStream fis = new FileInputStream(fName);

        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }

        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        if (encoding == null) {
            encoding=UNKNOWN; 
        }

        // Probably not necessary, since we are not going to reuse `detector`
        detector.reset();

        return encoding;
    }

    public static String detectLangICU(String s) 
        throws UnsupportedEncodingException
    {
         CharsetDetector detector;
         CharsetMatch match;
         detector = new CharsetDetector();

         // For some queer reason it runs in an acceptable manner only on
         // text with removed accents.

         String sPlain=StringTools.removeDiacritics(s);         
         detector.setText(sPlain.getBytes("UTF-8"));
         detector.setDeclaredEncoding("UTF-8");
         match = detector.detect();
         String res=match.getLanguage();
         if (res==null) res=UNKNOWN;
         return res;
    }

    private static CharsetMatch getMatchForDetectEncodICU(String fName) 
        throws java.io.IOException {
         CharsetDetector detector;
         CharsetMatch match;
         FileInputStream fis = new FileInputStream(fName);
         BufferedInputStream bfis= new BufferedInputStream(fis);
         detector = new CharsetDetector();
         
         detector.setText(bfis);
         match = detector.detect();
         return match;     
    }


    public static String detectEncodICU(String fName) 
        throws java.io.IOException {
        CharsetMatch match=getMatchForDetectEncodICU(fName);
        String res;
        if (match != null) {
            res=match.getName().toUpperCase();
        }
        else {
            res=UNKNOWN;
        }
        return res;
    }

    public static Map<String,Double> detectEncodProbICU(String fName) 
        throws java.io.IOException {
        CharsetMatch match=getMatchForDetectEncodICU(fName);
        Map<String,Double> res=new HashMap<String,Double>();

        if (match != null) {
            String s=match.getName().toUpperCase();
            double prob=match.getConfidence()/100.0;
            res.put(s,prob);
        }
        else {
            res.put(UNKNOWN,1.0);
        }
        return res;
    }

    public static Map<String,Double> detectAllEncodProbICU(String fName)
        throws java.io.IOException {
         CharsetDetector detector;
         CharsetMatch[] matches;
         FileInputStream fis = new FileInputStream(fName);
         BufferedInputStream bfis= new BufferedInputStream(fis);
         detector = new CharsetDetector();
         
         detector.setText(bfis);

         Map<String,Double> res=new HashMap<String,Double>();

         matches = detector.detectAll();
         for(CharsetMatch match:matches) {
            
             if (match != null) {
                 String s=match.getName().toUpperCase();
                 double prob=match.getConfidence()/100.0;
                 res.put(s,prob);
             }
             else {
                 res.put(UNKNOWN,1.0);
             }
         }
         return res;
    }
}
