package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.language.field.GenericField;
import fortytwo.language.type.GenericStructureType;

public class GenericStructureSignature {
	public final GenericStructureType type;
	public final List<GenericField> fields;
	public GenericStructureSignature(GenericStructureType type,
			List<GenericField> fields) {
		this.type = type;
		this.fields = fields;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GenericStructureSignature other = (GenericStructureSignature) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (type == null) {
			if (other.type != null) return false;
		} else if (!type.equals(other.type)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "GenericStructure [type=" + type + ", fields=" + fields + "]";
	}
}