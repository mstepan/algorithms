package com.max.algs.pdf;

import java.util.Iterator;

/**
 * Iterator over words inside PDF file.
 */
public class PdfWordIterator implements Iterator<String> {

    //        Map<String, Integer> wordsFreq = new HashMap<>();
//
//        File file = new File("/Users/mstepan/Desktop/War_and_Peace_NT.pdf");
//
//        try (PDDocument document = PDDocument.load(file)) {
//            PDFTextStripper pdfStripper = new PDFTextStripper();
//            String text = pdfStripper.getText(document);
//
//            String[] words = text.split("\\s+");
//
//            for (String singleWord : words) {
//                String word = singleWord.trim().toLowerCase().replaceAll("[,.?’‘*;:!]", "");
//
//                if ("".equals(word) || isNumber(word)) {
//                    continue;
//                }
//
//                wordsFreq.compute(word, (key, value) -> value == null ? 1 : value + 1);
//            }
//
//            for (Map.Entry<String, Integer> entry : wordsFreq.entrySet()) {
//                System.out.println(entry);
//            }
//
//            System.out.println(wordsFreq.size());
//
//        }


    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public String next() {
        return null;
    }
}
