package fortytwo.library.standard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.constructions.FunctionSynthetic;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;

public class FunctionLogicalOperator extends FunctionSynthetic {
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		final boolean[] array = new boolean[arguments.size()];
		for (int k = 0; k < arguments.size(); k++)
			array[k] = ((LiteralBool) arguments.get(k)).contents;
		return LiteralBool.getInstance(op.apply(array), Context.SYNTHETIC);
	}
	public static final FunctionLogicalOperator AND = new FunctionLogicalOperator(
			x -> x[0] && x[1], "", "and", "");
	public static final FunctionLogicalOperator OR = new FunctionLogicalOperator(
			x -> x[0] || x[1], "", "or", "");
	public static final FunctionLogicalOperator NOT = new FunctionLogicalOperator(
			x -> !x[0], "not", "");
	public final FunctionName name;
	public final Function<boolean[], Boolean> op;
	private FunctionLogicalOperator(Function<boolean[], Boolean> op,
			String... name) {
		this.name = FunctionName.getInstance(name);
		this.op = op;
	}
	@Override
	public GenericType outputType() {
		return new PrimitiveType(PrimitiveTypeWOC.BOOL, Context.SYNTHETIC);
	}
	@Override
	public FunctionSignature signature() {
		final ArrayList<GenericType> args = new ArrayList<>();
		for (final FunctionComponent fc : name.function)
			if (fc instanceof FunctionArgument)
				args.add(new PrimitiveType(PrimitiveTypeWOC.BOOL,
						Context.SYNTHETIC));
		return FunctionSignature.getInstance(name, args,
				new PrimitiveType(PrimitiveTypeWOC.BOOL, Context.SYNTHETIC));
	}
}
