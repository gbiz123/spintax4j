package spintax4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContentSpinner implements ContentGenerator {

	private static Logger logger = LogManager.getLogger();

	private final Random RANDOM = new Random();

	private List<String> generatedContent = new ArrayList<>();
	private String spintaxContent;
	private boolean allowDuplicates;

	public ContentSpinner(String spintaxContent, boolean allowDuplicates) {
		this.spintaxContent = spintaxContent;
		this.allowDuplicates = allowDuplicates;
	}

	@Override
	public String generate() {
		validateSpintax(spintaxContent);
		String spun = spin(spintaxContent);
		if (!allowDuplicates && generatedContent.contains(spun)) {
			logger.debug(
					"Not allowing duplicates and generated content already contains %s. Respinning".formatted(spun));
			spun = spin(spintaxContent);
		}
		generatedContent.add(spun);
		logger.debug("Spun content: " + spun);
		return spun;
	}

	private String spin(String spintaxContent) {
		if (!hasSpintax(spintaxContent))
			return spintaxContent;
		for (String group : extractInnermostGroups(spintaxContent))
			spintaxContent = spintaxContent.replace(group, choose(group));
		return spin(spintaxContent);
	}

	private boolean hasSpintax(String content) {
		if (content.matches(".*\\{.*\\}.*"))
			return true;
		logger.debug("No spintax detected in string: " + content);
		return false;
	}

	/**
	 * Extract spintax groups where no further groups are nested inside.
	 * If spintax is nested, extract the deepest nested group
	 *
	 * e.g. "{hello {world|woop}|Hi everybody}" -> "{world|woop}"
	 */
	private List<String> extractInnermostGroups(String content) {
		List<String> groups = new ArrayList<>();
		Pattern p = Pattern.compile("\\{[^\\{\\}]*?\\}");
		Matcher m = p.matcher(content);
		while (m.find())
			groups.add(m.group());
		logger.debug("Found innermost groups: " + String.join(", ", groups));
		return groups;
	}

	/**
	 * Randomly choose a single spintax value from an individual spintax chunk
	 *
	 * e.g. "{spin|tax}" -> "spin"
	 */
	private String choose(String content) {
		String result;
		content = content.replaceAll("[\\{\\}]", "");
		String[] choices = content.split("\\|");
		if (choices.length == 1)
			result = choices[0];
		else
			result = choices[RANDOM.nextInt(0, choices.length)];
		logger.debug("Chose %s from %s".formatted(result, content));
		return result;
	}

	private void validateSpintax(String spintaxContent) {
		if (!stringHasEvenNumberOfBrackets(spintaxContent))
			throw new IllegalArgumentException(
					"String %s is not valid spintax due to mismatched brackets.".formatted(spintaxContent));
	}

	private boolean stringHasEvenNumberOfBrackets(String str) {
		long bracketCount = str.chars().filter(ch -> ch == '{' || ch == '}').count();
		if (bracketCount % 2 == 0)
			return true;
		return false;
	}

	public List<String> getGeneratedContent() {
		return generatedContent;
	}

	public String getSpintaxContent() {
		return spintaxContent;
	}

	public void setSpintaxContent(String spintaxContent) {
		this.spintaxContent = spintaxContent;
	}

	public boolean isAllowDuplicates() {
		return allowDuplicates;
	}

	public void setAllowDuplicates(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}

}
