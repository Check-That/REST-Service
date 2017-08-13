package de.zeppelin.checkthat.webservice.models.helper;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.controller.ReplacementLetter;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

public class UniqueStringGenerator {

	public static List<String> getSuggestions(String name) {
		// Bsp.: Cedric Dahl
		ArrayList<String> suggestions = new ArrayList<String>();
		String baseName = escapeString(name);
		baseName = "?" + baseName;

		// Exakten Namen: CedricDahl
		if (getAvailableName(baseName, ReplacementLetter.NO_WHITESPACES) != null) {
			suggestions.add(baseName.replace(" ", "")); // Exakten Namen prüfen
		}

		if (baseName.split(" ").length >= 2) {
			// Namen mit Trennzeichen Cedric.Dahl, Cedric_Dahl
			suggestions.add(checkNameVariationGroup(baseName));
			String[] split = baseName.split(" ");
			String fullName = String.join("", split);
			// C.Dahl
			suggestions.add(checkNameVariationGroup(
					split[0].substring(0, 2) + " " + fullName.substring(split[0].length(), fullName.length())));
			// Cedric.D
			suggestions.add(
					checkNameVariationGroup(fullName.substring(0, fullName.length() - split[split.length - 1].length())
							+ split[split.length - 1].substring(0, 1)));
		}

		UserRepository userRep = Application.getContext().getBean(UserRepository.class);

		// Zahlen anhängen CedricDahl37,
		baseName = baseName.replace(" ", "");
		while (suggestions.size() < 3) {
			name = baseName + randInt(1, 1000);
			System.out.println(name);
			if (name.length() >= 3 && !userRep.existsByUniqueString(name)) {
				System.out.println("check");
				suggestions.add(name);
			}
		}

		if (suggestions.get(0).equals(suggestions.get(1))) {
			suggestions.remove(0); // Doppelung von exaktem Namen und
									// replacement vermeiden
		}
		return suggestions;
	}

	private static String checkNameVariationGroup(String name) {
		String suboptimalName = null;
		List<ReplacementLetter> shuffledLetters = Arrays.asList(ReplacementLetter.values());
		Collections.shuffle(shuffledLetters);
		for (ReplacementLetter letter : shuffledLetters) {
			String tmpName = getAvailableName(name, letter);
			if (tmpName != null) {
				return tmpName;
			}
			for (int i = 0; suboptimalName == null && i < 3; i++) {
				tmpName = getAvailableName(name + randInt(0, 1000), letter);
				if (tmpName != null) {
					suboptimalName = tmpName;
					break;
				}
			}
		}
		return suboptimalName;
	}

	private static String getAvailableName(String name, ReplacementLetter replacementLetter) {
		UserRepository userRep = Application.getContext().getBean(UserRepository.class);
		name = name.replace(" ", replacementLetter.toString());
		System.out.println(name);
		if (name.length() >= 3 && !userRep.existsByUniqueString(name)) {
			System.out.println("check");
			return name;
		}
		return null;
	}

	private static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	private static String escapeString(String name) {
		String accepted = "abcdefghijklmnopqrstuvwxyz123456789._-+* ";

		name = name.trim().toLowerCase();
		char[] out = new char[name.length()];
		name = Normalizer.normalize(name, Normalizer.Form.NFD);
		int j = 0;
		for (int i = 0, n = name.length(); i < n; ++i) {
			char c = name.charAt(i);
			if (accepted.contains("" + c)) {
				out[j++] = c;
			}
		}

		// if name too short -> predefined name
		if (name.length() < 3) {
			return "Poll Master";
		}

		return new String(out);
	}
}
