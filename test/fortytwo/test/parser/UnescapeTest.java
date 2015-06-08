package fortytwo.test.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fortytwo.compiler.parser.Tokenizer;

/**
 * Complete test of the unescaper. This should be viewed as a specification.
 */
public class UnescapeTest {
	@Test
	public void standard() {
		assertUE("\r\n\b\t\f", "\\r\\n\\b\\t\\f");
		assertUE("a\rb\nc\bd\te\ff", "a\\rb\\nc\\bd\\te\\ff");
	}
	@Test
	public void standardInvalid() {
		assertUE("\r\n\f\t\\", "\\r\\n\\f\\t\\");
		assertUE("\r\n\f\t\\ f", "\\r\\n\\f\\t\\ f");
		assertUE("\\ r\n\b\t\f", "\\ r\\n\\b\\t\\f");
	}
	@Test
	public void multiple() {
		assertUE("'\\'", "'\\\\'");
	}
	@Test
	public void unicode() {
		assertUE("\u0045\u0123\u12319", "\\u0045\\u0123\\u12319");
	}
	@Test
	public void unicodeInvalid() {
		assertUE("\\uASDF", "\\uASDF");
		assertUE("\\u121$", "\\u121$");
	}
	@Test
	public void complexTestCase() {
		assertUE("\t\t\t\tArrays.asList(\"If\", \"4.0\", \"+\", \"4.0\", \"=\", \"2\", \":\",",
				"\\t\\t\\t\\tArrays.asList(\"If\", \"4.0\", \"+\", \"4.0\", \"=\", \"2\", \":\",");
	}
	public static void assertUE(String unescaped, String escaped) {
		assertEquals(unescaped, Tokenizer.unescape(escaped));
	}
}
