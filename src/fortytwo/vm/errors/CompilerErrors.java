package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.StructureType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.environment.FunctionSignatureRoster;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.expressions.Expression;

public class CompilerErrors {
	public static void variableDNE(TypeVariable id) {
		// TODO 0 implement
	}
	public static void variableDNE(VariableIdentifier name) {
		// TODO Auto-generated method stub
	}
	public static void argumentTypeMismatch(FunctionSignature function,
			List<Expression> arguments, int i) {
		// TODO Auto-generated method stub
	}
	public static void structureDNE(StructureType type,
			StructureRoster structureRoster) {
		// TODO Auto-generated method stub
	}
	public static void structureDNE(GenericStructureType genericType,
			StructureRoster structureRoster) {
		// TODO Auto-generated method stub
	}
	public static void functionSignatureDNE(FunctionName name,
			List<ConcreteType> types, FunctionSignatureRoster funcs) {
		// TODO Auto-generated method stub
	}
	public static void wrongNumberOfArguments(FunctionSignature function,
			List<Expression> arguments) {
		// TODO Auto-generated method stub
	}
	public static void nonVariableInFieldAccess(
			ParsedExpression parsedExpression) {
		// TODO Auto-generated method stub
	}
}
