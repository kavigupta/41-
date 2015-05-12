package fortytwo.compiler.language.identifier;

import fortytwo.compiler.language.Language;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class TypeIdentifier implements ParsedExpression {
	public final String name;
	public static TypeIdentifier getInstance(String name) {
		if (!Language.isValidTypeIdentifier(name))
			throw new RuntimeException(/* LOWPRI-E */);
		return new TypeIdentifier(name);
	}
	private TypeIdentifier(String name) {
		this.name = name;
	}
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
