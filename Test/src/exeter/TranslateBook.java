package exeter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TranslateBook {

	public static void main(String[] args) throws IOException {
		long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long start = System.currentTimeMillis();

		// Reading dictionary and load to Map
		Map<String, String> dictMap = new LinkedHashMap<String, String>();
		Scanner sc = null;
		try {
			File file = new File("D:\\french_dictionary.csv");
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String str = sc.nextLine().trim();
				dictMap.put(str.substring(0, str.indexOf(",")).toLowerCase(), str.substring(str.indexOf(",") + 1, str.length()));
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}

		// Find frequency of the english words
		Map<String, Integer> freqMap = new LinkedHashMap<String, Integer>();
		FileWriter translatedText = null;
		try {
			File bookNeedToTranslate = new File("D:\\t8.shakespeare.txt");
			translatedText = new FileWriter("D:\\t8.shakespeare.translated.txt");
			sc = new Scanner(bookNeedToTranslate);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] arr1 = line.split(" ");
				for (String s : arr1) {
					s = stripSpclChars(s.trim().toLowerCase());
					if (dictMap.containsKey(s)) {
						if (freqMap.containsKey(s)) {
							freqMap.put(s, freqMap.get(s) + 1);
						} else {
							freqMap.put(s, 1);
						}
						line = line.replaceAll("\\b(?i)" + s + "\\b", dictMap.get(s));
					}
				}
				translatedText.write(line + "\n");
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
			if (translatedText != null) {
				translatedText.close();
			}
		}

		// Creating CSV with words frequency
		FileWriter writer = null;
		try {
			writer = new FileWriter("D:\\new.csv");
			for (Map.Entry<String, String> r : dictMap.entrySet()) {
				String origWord = r.getKey();
				String frenchWord = r.getValue();
				Integer freq = 0;
				if (freqMap.containsKey(origWord)) {
					freq = freqMap.get(origWord);

				}
				String csv = origWord + "," + frenchWord + "," + freq;
				writer.write(csv + "\n");
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

		System.out.println(System.currentTimeMillis() - start);
		long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println((afterUsedMem - beforeUsedMem) / (1024 * 1024));

		// create file with time and memory
		try {
			writer = new FileWriter("D:\\perfomance.txt");
			long timeTaken = System.currentTimeMillis() - start;
			writer.write("Time to process: 0 minutes " + (TimeUnit.MILLISECONDS.toSeconds(timeTaken)) + " seconds\n"
					+ "Memory used: " + (afterUsedMem - beforeUsedMem) / (1024 * 1024) + " MB");
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	static String stripSpclChars(String s) {
		int index;
		for (index = 0; index < s.length(); index++) {
			if (Character.isLetterOrDigit(s.charAt(index))) {
				break;
			}
		}
		s = s.substring(index);
		for (index = s.length() - 1; index >= 0; index--) {
			if (Character.isLetterOrDigit(s.charAt(index))) {
				break;
			}
		}
		return s.substring(0, index + 1);
	}
}
