package fortytwo.language.type;

import java.util.Optional;

import fortytwo.compiler.parsed.ParsedConstruct;
import fortytwo.vm.environment.TypeVariableRoster;

public interface GenericType extends ParsedConstruct {
	public Kind kind();
	public Optional<TypeVariableRoster> match(ConcreteType toMatch);
	public ConcreteType resolve(TypeVariableRoster roster);
	@Override
	public String toSourceCode();
	@Override
	public default boolean isSimple() {
		return true;
	}
}
