package fortytwo.compiler.parsed.expressions;

import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.Expression;

public interface ParsedExpression extends ParsedStatement {
	@Override
	public Expression contextualize(StaticEnvironment env);
}
