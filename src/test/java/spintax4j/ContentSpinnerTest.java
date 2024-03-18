package spintax4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ContentSpinnerTest {

	@Test
	public void testInvalidSpintaxThrowsIllegalArgument() {
		ContentSpinner spinner = new ContentSpinner("This is my { invalid | spintax", true);
		assertThrows(IllegalArgumentException.class, () -> {
			spinner.generate();
		});
	}

	@Test
	public void testSpinningWithoutSpintaxReturnsSameString() {
		ContentSpinner spinner = new ContentSpinner("This is just a string", true);
		assertEquals("This is just a string", spinner.generate());
	}

	@Test
	public void testSpinningResultsInExpectedString() {
		String spintax = "i {like java|hate ruby}";
		String expected1 = "i like java";
		String expected2 = "i hate ruby";
		ContentSpinner spinner = new ContentSpinner(spintax, true);
		String generated = spinner.generate();
		boolean wasExpected = false;
		if (generated.equals(expected1) || generated.equals(expected2))
			wasExpected = true;
		assertTrue(wasExpected);
	}

	@Test
	@Disabled
	public void testNestedSpinningResultsInExpectedString() {
		String spintax = "i {like {java|{python|snek language}}|hate ruby}";
		String expected1 = "i like java";
		String expected2 = "i like python";
		String expected3 = "i hate ruby";
		ContentSpinner spinner = new ContentSpinner(spintax, true);
		String generated = spinner.generate();
		boolean wasExpected = false;
		if (generated.equals(expected1) ||
				generated.equals(expected2) ||
				generated.equals(expected3))
			wasExpected = true;
		assertTrue(wasExpected);
	}
}
