import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TranslateBook {

	public static void main(String[] args) throws IOException {
		System.gc();
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
				dictMap.put(str.substring(0, str.indexOf(",")).toLowerCase(),
						new String(str.substring(str.indexOf(",") + 1, str.length()).getBytes(), "UTF-8"));
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
			translatedText = new FileWriter("out/t8.shakespeare.translated.txt");
			sc = new Scanner(bookNeedToTranslate);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] arr = line.split(" ");
				for (String s : arr) {
					s = stripSpclChars(s.trim());
					String lowerCase = s.toLowerCase();
					if (dictMap.containsKey(lowerCase)) {
						if (freqMap.containsKey(lowerCase)) {
							freqMap.put(lowerCase, freqMap.get(lowerCase) + 1);
						} else {
							freqMap.put(lowerCase, 1);
						}
						// Case handling
						String frenchWord = dictMap.get(lowerCase);

						if (s.equals(s.toUpperCase())) {
							frenchWord = frenchWord.toUpperCase();
						} else if (s.equals(s.toLowerCase())) {
							frenchWord = frenchWord.toLowerCase();
						} else if (Character.isUpperCase(s.charAt(0))) {
							frenchWord = Character.toUpperCase(frenchWord.charAt(0)) + frenchWord.substring(1);
						}
						line = line.replaceAll("\\b(?i)" + lowerCase + "\\b", frenchWord);
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
			writer = new FileWriter("out/frequency.csv");
			for (Map.Entry<String, String> itr : dictMap.entrySet()) {
				String originalWord = itr.getKey();
				String frenchWord = itr.getValue();
				Integer freq = 0;
				if (freqMap.containsKey(originalWord)) {
					freq = freqMap.get(originalWord);

				}
				String csv = originalWord + "," + frenchWord + "," + freq;
				writer.write(csv + "\n");
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

		long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		// create a performance file with time and memory
		try {
			writer = new FileWriter("out/perfomance.txt");
			long timeTaken = System.currentTimeMillis() - start;
			writer.write("Time to process: 0 minutes " + (TimeUnit.MILLISECONDS.toSeconds(timeTaken)) + " seconds\n"
					+ "Memory used: " + (afterUsedMem - beforeUsedMem) / (1024 * 1024) + " MB");
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		System.out.println("Completed.");
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
