package fortytwo.language.identifier;

import java.util.List;

import fortytwo.language.identifier.functioncomponent.FunctionComponent;

public class FunctionName {
	public final List<FunctionComponent> function;
	public static FunctionName getInstance(List<FunctionComponent> function) {
		return new FunctionName(function);
	}
	private FunctionName(List<FunctionComponent> function) {
		this.function = function;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionName other = (FunctionName) obj;
		if (function == null) {
			if (other.function != null) return false;
		} else if (!function.equals(other.function)) return false;
		return true;
	}
}