package fortytwo.vm.environment;

import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.expressions.LiteralExpression;

public class TypeEnvironment {
	private final Optional<TypeEnvironment> container;
	public final StructureRoster structs;
	public final FunctionSignatureRoster funcs;
	private final VariableRoster<LiteralExpression> globalVariables;
	private final VariableTypeRoster types;
	public static TypeEnvironment getDefault() {
		final StructureRoster structs = StdLib42.DEF_STRUCT;
		final FunctionSignatureRoster funcs = new FunctionSignatureRoster();
		StdLib42.defaultSignatures(funcs);
		final VariableRoster<LiteralExpression> globalVariables = new VariableRoster<>();
		final VariableTypeRoster types = new VariableTypeRoster();
		return new TypeEnvironment(structs, funcs, globalVariables, types);
	}
	public static TypeEnvironment getChild(TypeEnvironment environment) {
		return new TypeEnvironment(environment);
	}
	private TypeEnvironment(TypeEnvironment env) {
		this.structs = env.structs;
		this.funcs = new FunctionSignatureRoster();
		this.globalVariables = new VariableRoster<>();
		this.types = new VariableTypeRoster();
		this.container = Optional.of(env);
	}
	private TypeEnvironment(StructureRoster structureRoster,
			FunctionSignatureRoster sigRost,
			VariableRoster<LiteralExpression> global,
			VariableTypeRoster types) {
		this.structs = structureRoster;
		this.funcs = sigRost;
		this.globalVariables = global;
		this.types = types;
		this.container = Optional.empty();
	}
	public void addGlobalVariable(VariableIdentifier name,
			LiteralExpression express) {
		this.globalVariables.assign(name, express);
		addType(name, express.resolveType());
	}
	public void addType(VariableIdentifier variableIdentifier,
			ConcreteType concreteType) {
		types.add(variableIdentifier, concreteType);
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		final ConcreteType type = types.typeOf(name);
		if (type != null) return type;
		if (!container.isPresent()) DNEErrors.variableDNE(name);
		return container.get().typeOf(name);
	}
	public Optional<FunctionType> referenceTo(FunctionName name,
			List<ConcreteType> types) {
		final Optional<FunctionType> sig = funcs.typeof(name, types);
		if (sig.isPresent()) return sig;
		if (!container.isPresent()) DNEErrors.functionSignatureDNE(name, types);
		return container.get().referenceTo(name, types);
	}
	public void putReference(VariableIdentifier id, FunctionType type) {
		funcs.putReference(id, type);
	}
	public LiteralExpression referenceTo(VariableIdentifier name) {
		final LiteralExpression expr = globalVariables.referenceTo(name);
		if (expr != null) return expr;
		if (!container.isPresent()) DNEErrors.variableDNE(name);
		return container.get().referenceTo(name);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (funcs == null ? 0 : funcs.hashCode());
		result = prime * result
				+ (globalVariables == null ? 0 : globalVariables.hashCode());
		result = prime * result + (structs == null ? 0 : structs.hashCode());
		result = prime * result + (types == null ? 0 : types.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final TypeEnvironment other = (TypeEnvironment) obj;
		if (funcs == null) {
			if (other.funcs != null) return false;
		} else if (!funcs.equals(other.funcs)) return false;
		if (globalVariables == null) {
			if (other.globalVariables != null) return false;
		} else if (!globalVariables.equals(other.globalVariables)) return false;
		if (structs == null) {
			if (other.structs != null) return false;
		} else if (!structs.equals(other.structs)) return false;
		if (types == null) {
			if (other.types != null) return false;
		} else if (!types.equals(other.types)) return false;
		return true;
	}
}