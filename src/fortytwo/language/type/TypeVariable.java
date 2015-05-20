package fortytwo.language.type;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.TypeVariableRoster;

public class TypeVariable implements GenericType {
	public final VariableIdentifier name;
	public TypeVariable(VariableIdentifier name) {
		this.name = name;
	}
	@Override
	public Kind kind() {
		return Kind.VARIABLE;
	}
	@Override
	public TypeVariableRoster match(ConcreteType toMatch) {
		TypeVariableRoster roster = new TypeVariableRoster();
		roster.assign(this, toMatch);
		return roster;
	}
	@Override
	public ConcreteType resolve(TypeVariableRoster roster) {
		ConcreteType type = roster.referenceTo(this);
		if (type == null) throw new RuntimeException(/* LOWPRI-E */);
		return type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TypeVariable other = (TypeVariable) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
}
