package langlab.jcore.detectors;

import java.lang.System;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.mozilla.universalchardet.UniversalDetector;
import org.mozilla.universalchardet.Constants;

public final class EncodDetectorTools {

    private static final String UNKNOWN="UNKNOWN";

    public static Set<String> getEncodAvailUnichardet() 
        throws IllegalAccessException
    {
     
        HashSet<String> res = new HashSet();

        for(Field f : Constants.class.getDeclaredFields() ) {
            int   m=f.getModifiers();

            if ( Modifier.isPublic(m) && 
                 Modifier.isFinal(m) &&
                 Modifier.isStatic(m) &&
                 (f.getType().getName() == "java.lang.String")
                ) {
                res.add(f.get(Constants.class).toString());
               }
            }

        return res;
    }

    public static String detectEncodUnichardet(String fName) 
        throws java.io.IOException {

        byte[] buf = new byte[4096];

        java.io.FileInputStream fis = new java.io.FileInputStream(fName);

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
}
