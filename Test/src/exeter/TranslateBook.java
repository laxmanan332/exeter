package exeter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class TranslateBook {

	public static void main(String[] args) throws IOException {
		long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long start = System.currentTimeMillis();
		
		//Reading dictionary and load to Map
		Map<String, String> dictMap = new LinkedHashMap<String, String>();
		Scanner sc = null;
		try {
			File file = new File("D:\\french_dictionary.csv");
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String str = sc.nextLine().trim();
				dictMap.put(str.substring(0, str.indexOf(",")), str.substring(str.indexOf(",") + 1, str.length()));
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		
		//Find frequency of the english words
		Map<String, Integer> freqMap = new LinkedHashMap<String, Integer>();
		try {
			File bookNeedToTranslate = new File("D:\\t8.shakespeare.txt");
			sc = new Scanner(bookNeedToTranslate);
			while (sc.hasNextLine()) {
				String str1 = sc.nextLine().trim();
				String[] arr1 = str1.split(" ");
				for (String s : arr1) {
					s = s.trim();
					if (dictMap.containsKey(s)) {
						if (freqMap.containsKey(s)) {
							freqMap.put(s, freqMap.get(s) + 1);
						} else {
							freqMap.put(s, 1);
						}
					}
				}
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		
		//Creating CSV with words frequency
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
	}
}
