package fortytwo.language.identifier;

import java.util.Arrays;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.classification.ExpressionType;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.Field;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.SyntaxErrors;
import fortytwo.vm.expressions.Expression;

public class VariableIdentifier implements ParsedExpression {
	public static final VariableIdentifier VALUE = new VariableIdentifier(
			new Token(Resources.VALUE, Context.synthetic()));
	public final Token name;
	public static VariableIdentifier getInstance(Token token) {
		if (token.token.equals(Resources.VALUE)) return VALUE;
		if (!Language.isValidVariableIdentifier(token.token))
			SyntaxErrors.invalidExpression(ExpressionType.VARIABLE,
					Arrays.asList(token));
		return new VariableIdentifier(token);
	}
	private VariableIdentifier(Token name) {
		this.name = name;
	}
	@Override
	public SentenceType type() {
		return SentenceType.PURE_EXPRESSION;
	}
	@Override
	public Context context() {
		return name.context;
	}
	@Override
	public int hashCode() {
		return name.token.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		VariableIdentifier other = (VariableIdentifier) obj;
		return this.name.token.equals(other.name.token);
	}
	@Override
	public String toString() {
		return name.token;
	}
	@Override
	public String toSourceCode() {
		return name.token;
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public Expression contextualize(StaticEnvironment env) {
		return new Field(this, env.typeOf(this));
	}
}
