package org.research.load.doc;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFTextParser {

	
	// Extract text from PDF Document
	public String pdftoText(String fileName) {
	
		String parsedText = null;
		PDFTextStripper pdfStripper = null;
		PDDocument pdDoc = null;
		File file = new File(fileName);
		if (!file.isFile()) {
			System.err.println("File " + fileName + " does not exist.");
			return null;
		}
		try (FileInputStream fileInputStream = new FileInputStream(file);
				RandomAccessBufferedFileInputStream randomAccessBufferedFileInputStream = new RandomAccessBufferedFileInputStream(
						fileInputStream);) {

			PDFParser parser = new PDFParser(randomAccessBufferedFileInputStream);
			parser.parse();
			try (COSDocument cosDoc = parser.getDocument();) {
				pdfStripper = new PDFTextStripper();
				pdDoc = new PDDocument(cosDoc);
				parsedText = pdfStripper.getText(pdDoc);
			}
		} catch (Exception e) {
			System.err.println("An exception occured in parsing the PDF Document." + e.getMessage());
		}
		return parsedText;
	}

	public static void main(String args[]) {
		System.out.println(new PDFTextParser().pdftoText("C:/E/tech/nlp-ontology/requirement/SRS_4.pdf"));
	}

}