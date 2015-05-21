package fortytwo.language;

public final class Resources {
	/* Redundant elements */
	public static final String A = "a";
	public static final String AN = "an";
	public static final String THE = "the";
	/* Conjunctions */
	public static final String CALLED = "called";
	public static final String WHICH = "which";
	public static final String OF = "of";
	public static final String TO = "to";
	public static final String ALL = "all";
	public static final String AND = "and";
	public static final String THAT = "that";
	public static final String TAKES = "takes";
	public static final String OUTPUT = "output";
	public static final String OUTPUTS = "outputs";
	public static final String FOLLOWING = "following";
	public static final String PERIOD = ".";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	/* Names */
	public static final String DECL_FUNCTION = "function";
	public static final String VALUE = "value";
	public static final String TYPE = "type";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String ADDITION_SIGN = "+";
	public static final String SUBTRACTION_SIGN = "-";
	public static final String FLOORDIV_SIGN = "//";
	public static final String MULTIPLICATION_SIGN = "*";
	public static final String MOD_SIGN = "%";
	public static final String DIV_SIGN = "/";
	/* Naming Conventions */
	public static final String VARIABLE_START = "_";
	public static final String OPEN_PAREN = "(";
	public static final String CLOSE_PAREN = ")";
	/* Statements */
	public static final String RUN = "Run";
	public static final String DEFINE = "Define";
	public static final String SET = "Set";
	public static final String EXIT = "Exit";
	/* Control Flow */
	public static final String IF = "If";
	public static final String OTHERWISE = "Otherwise";
	public static final String WHILE = "While";
	public static final String DO = "Do";
	public static final String THATS = "That's";
	/* Regex */
	public static final String OP_FIND = "(?<op>(//)|\\+|-|\\*|/|%)";
	public static final String OP_REPLACE = " ${op} ";
	public static final String PUNCT_FIND = "(?<punct>\\.|:|,)(?!\\d)";
	public static final String PUNCT_REPLACE = " ${punct} ";
	public static final String WHITESPACE = "\\s+";
	public static final String SPACE = " ";
	private Resources() {}
}