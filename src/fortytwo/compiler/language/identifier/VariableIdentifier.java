package fortytwo.compiler.language.identifier;

import fortytwo.compiler.language.Language;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class VariableIdentifier implements ParsedExpression {
	public final String name;
	public static VariableIdentifier getInstance(String name) {
		if (!Language.isValidVariableIdentifier(name))
			throw new RuntimeException(/* LOWPRI-E */);
		return new VariableIdentifier(name);
	}
	private VariableIdentifier(String name) {
		this.name = name;
	}
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
