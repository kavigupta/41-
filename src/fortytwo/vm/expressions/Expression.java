package fortytwo.vm.expressions;

import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public interface Expression extends Statement {
	public LiteralExpression literalValue(LocalEnvironment environment);
	public ConcreteType resolveType();
}
