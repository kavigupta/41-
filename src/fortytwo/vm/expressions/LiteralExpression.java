package fortytwo.vm.expressions;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.vm.environment.LocalEnvironment;

public abstract class LiteralExpression implements ParsedExpression, Expression {
	@Override
	public final Expression contextualize(LocalEnvironment env) {
		return this;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		// No - op by default
	}
	@Override
	public final LiteralExpression literalValue(LocalEnvironment env) {
		return this;
	}
	@Override
	public SentenceType type() {
		return SentenceType.PURE_EXPRESSION;
	}
}
