package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.constructions.ParsedVariableRoster;
import fortytwo.language.SourceCode;
import fortytwo.language.field.Field;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Definition;
import fortytwo.vm.statements.Statement;

public class ParsedDefinition implements ParsedStatement {
	public final Field name;
	public final ParsedVariableRoster fields;
	public ParsedDefinition(Field name, ParsedVariableRoster fields) {
		this.name = name;
		this.fields = fields;
	}
	@Override
	public Statement contextualize(StaticEnvironment environment) {
		environment.types.add(name.name, name.type);
		return new Definition(name, fields.contextualize(environment),
				environment.structs);
	}
	@Override
	public SentenceType type() {
		return SentenceType.DEFINITION;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedDefinition other = (ParsedDefinition) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
}
