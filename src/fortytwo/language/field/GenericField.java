package fortytwo.language.field;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;

public class GenericField {
	public final VariableIdentifier name;
	public final GenericType type;
	public GenericField(VariableIdentifier name, GenericType type) {
		this.name = name;
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GenericField other = (GenericField) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (type == null) {
			if (other.type != null) return false;
		} else if (!type.equals(other.type)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "GenericField [name=" + name + ", type=" + type + "]";
	}
}
