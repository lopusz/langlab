package langlab.base.core.parsers;

import java.util.List;
import java.util.ArrayList;
import java.io.StringReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public final class LuceneTools {

  private LuceneTools() {}

  public static List<String> 
  splitTokensWithAnalyzer(Analyzer analyzer, String string) {
    // Based on SO
    // http://stackoverflow.com/questions/6334692/how-to-use-a-lucene-analyzer-to-tokenize-a-string

    List<String> result = new ArrayList<String>();
    try {
      TokenStream stream  = 
          analyzer.tokenStream(null, new StringReader(string));
      stream.reset();
      while (stream.incrementToken()) {
        result.add(stream.getAttribute(CharTermAttribute.class).toString());
      }
    } catch (IOException e) {
      // not thrown b/c we're using a string reader...
      throw new RuntimeException(e);
    }
    return result;
  }
}
