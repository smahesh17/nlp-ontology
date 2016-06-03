package org.research.load.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Consumer;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ContentExtractor {

	public static void main(String[] args) {
		File file = null;
		try {

			file = new File("C:/E/tech/nlp-ontology/requirement/ACCO-Requirements-v1.0.doc");
			Consumer<String[]> consumer = (fileData) -> {
				for (int i = 0; i < fileData.length; i++) {

					if (fileData[i] != null) {
						System.out.println();
						System.out.println("----------- START ------");
						System.out.println(fileData[i]);
						System.out.println("----------- END ------");
					}
				}

			};

			new ContentExtractor().extractText(file, consumer);

		} catch (Exception exep) {
			exep.printStackTrace();
		}
	}

	public void extractText(File file, Consumer<String[]> consumer) throws IOException, FileNotFoundException {
		try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
			HWPFDocument document = new HWPFDocument(fis);
			WordExtractor extractor = new WordExtractor(document);
			String[] fileData = extractor.getParagraphText();
			consumer.accept(fileData);
		}
	}
}