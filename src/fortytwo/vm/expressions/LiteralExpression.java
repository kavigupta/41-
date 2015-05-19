package fortytwo.vm.expressions;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;

public abstract class LiteralExpression implements ParsedExpression, Expression {
	@Override
	public final Expression contextualize(StaticEnvironment env) {
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
	@Override
	public final boolean typeCheck() {
		return true;
	}
	@Override
	public abstract ConcreteType resolveType();
}
