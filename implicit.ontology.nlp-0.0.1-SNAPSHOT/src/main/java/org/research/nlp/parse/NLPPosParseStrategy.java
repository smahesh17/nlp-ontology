package org.research.nlp.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.function.Consumer;

import org.research.nlp.Result;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class NLPPosParseStrategy implements INLPParseStrategy {

	public Result getParseResult(String content) {
		Result result  = null;
		
		return result;
	}
	
	
	public void tokenize(String content,Consumer<String> consumer) throws InvalidFormatException, IOException {

		String name = "en-token.bin";

		InputStream is = getResourceStream(name);
	 
		TokenizerModel model = new TokenizerModel(is);
	 
		Tokenizer tokenizer = new TokenizerME(model);
	 
		String tokens[] = tokenizer.tokenize(content);
	 
		for (String a : tokens){
			System.out.println(a);
			consumer.accept(a);
		}
	 
		is.close();
	}


	private InputStream getResourceStream(String name) throws FileNotFoundException {
		String name2 = getPathSuffix();
		return getClass().getResourceAsStream(name2+name);
	}


	private String getPathSuffix() {
		String name2 = "nlp"+File.separator;
		return name2;
	}

	
	public void POSTag(String input, Consumer<POSSample> consumer) throws IOException {

//		portability is the problem here - FIX THIS
		
		String pathname = "en-pos-maxent.bin";
		POSModel model = new POSModelLoader().load(getModelFile(pathname));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);
	 
//		String input = "Hi. How are you? This is Mike.";
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new StringReader(input));
	 
		perfMon.start();
		String line;
		while ((line = lineStream.read()) != null) {
	 
			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
					.tokenize(line);
			String[] tags = tagger.tag(whitespaceTokenizerLine);
	 
			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
//			System.out.println(sample.toString());
			consumer.accept(sample);
	 
			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();
	}

	private File getModelFile(String pathname) {
		String location = System.getProperty("resources", "C:/test");
		return new File(location+File.separator+ getPathSuffix() +pathname);
	}
	
}
