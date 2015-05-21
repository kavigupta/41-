package fortytwo.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.constructions.ParsedVariableRoster;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionReturn;
import fortytwo.compiler.parsed.declaration.StructureDeclaration;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.statements.*;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.vm.expressions.*;

public class SourceCode {
	public static String display(FunctionDefinition def) {
		String fieldList = displayFieldList(def.parameterTypes,
				def.parameterVariables);
		return "Define a function called "
				+ displayFunctionSignature(def.name, def.parameterVariables)
				+ (fieldList.length() == 0 ? ""
						: (" that takes " + fieldList))
				+ displayOutputType(def.outputType, fieldList.length() != 0);
	}
	public static String display(FunctionReturn functionReturn) {
		return "Exit the function" + displayReturn(functionReturn.output);
	}
	public static String display(StructureDeclaration structureDeclaration) {
		String fields = displayFieldList(structureDeclaration.structure.fields);
		if (fields.length() != 0) {
			fields = " which contains " + fields;
		}
		return "Define a type called "
				+ structureDeclaration.structure.type.toSourceCode()
				+ fields;
	}
	public static String display(ParsedDefinition parsedDefinition) {
		return "Define "
				+ Language.articleized(parsedDefinition.name.type
						.toSourceCode())
				+ " called "
				+ parsedDefinition.name.name.name
				+ (parsedDefinition.fields.pairs.size() == 0 ? ""
						: (" with " + displayFieldList(parsedDefinition.fields)));
	}
	public static String display(ParsedFieldAssignment parsedFieldAssignment) {
		return "Set the " + parsedFieldAssignment.field.name + " of "
				+ parsedFieldAssignment.name.name + " to "
				+ parsedFieldAssignment.value.toSourceCode();
	}
	public static String display(ParsedFunctionCall parsedFunctionCall) {
		String func = displayFunctionSignature(parsedFunctionCall.name,
				parsedFunctionCall.arguments).trim();
		if (func.length() == 0) return "";
		if (Character.isUpperCase(func.charAt(0))
				&& Character.isAlphabetic(func.charAt(0))) return func;
		return "(" + func + ")";
	}
	public static String display(ParsedIfElse parsedIfElse) {
		System.out.println("Condition: "
				+ parsedIfElse.condition.toSourceCode());
		return "If "
				+ parsedIfElse.condition.toSourceCode()
				+ ": "
				+ parsedIfElse.ifso.toSourceCode()
				+ (parsedIfElse.ifelse.statements.size() == 0 ? ""
						: ("; otherwise: " + parsedIfElse.ifelse
								.toSourceCode()));
	}
	public static String display(ParsedRedefinition parsedRedefinition) {
		return "Set the value of " + parsedRedefinition.name.toSourceCode()
				+ " to " + parsedRedefinition.expr.toSourceCode();
	}
	public static String display(ParsedStatementSeries parsedStatementSeries) {
		return parsedStatementSeries.statements.stream()
				.map(x -> x.toSourceCode())
				.reduce("", (a, b) -> a + "; " + b).substring(2);
	}
	public static String display(ParsedWhileLoop parsedWhileLoop) {
		return "While " + parsedWhileLoop.condition.toSourceCode() + ": "
				+ parsedWhileLoop.statement.toSourceCode();
	}
	public static String display(LiteralArray literalArray) {
		StringBuffer sbuff = new StringBuffer("[");
		for (int i = 0; i < literalArray.length(); i++) {
			sbuff.append(literalArray.get(i + 1)).append(", ");
		}
		return sbuff.delete(sbuff.length() - 2, sbuff.length()).append("]")
				.toString();
	}
	public static String display(LiteralBool literalBool) {
		return Boolean.toString(literalBool.contents);
	}
	public static String display(LiteralNumber literalNumber) {
		return literalNumber.contents.toPlainString();
	}
	public static String display(LiteralObject literalObject) {
		StringBuffer sbuff = new StringBuffer("{").append(
				literalObject.struct.type.toSourceCode()).append(":");
		for (Entry<VariableIdentifier, LiteralExpression> e : literalObject.fields.pairs
				.entrySet()) {
			sbuff.append(e.getKey().toSourceCode()).append(" <- ")
					.append(e.getValue().toSourceCode()).append(",");
		}
		return sbuff.append("}").toString();
	}
	public static String display(LiteralString literalString) {
		return "'" + literalString.contents + "'";
	}
	public static String display(StructureType st) {
		if (st.types.size() == 0) return parenthesizedName(st.name);
		StringBuffer buff = new StringBuffer("(");
		for (String s : st.name) {
			buff.append(s).append(" ");
		}
		List<String> types = st.types.stream().map(x -> x.toSourceCode())
				.collect(Collectors.toList());
		System.out.println("types: " + types);
		return buff.append("of ").append(displayList(types)).append(")")
				.toString();
	}
	public static String display(GenericStructureType type) {
		StringBuffer name = new StringBuffer();
		for (String s : type.name)
			name.append(s).append(' ');
		String list = displayList(type.typeVariables.stream()
				.map(x -> x.toSourceCode()).collect(Collectors.toList()));
		return name.toString().trim()
				+ (list.length() == 0 ? "" : (" of " + list));
	}
	private static String parenthesizedName(List<String> name) {
		if (name.size() == 0) return "()";
		if (name.size() == 1) return name.get(0);
		StringBuffer buff = new StringBuffer("(");
		for (String s : name) {
			buff.append(s).append(" ");
		}
		return buff.substring(0, buff.length() - 1) + ")";
	}
	private static String displayFieldList(ParsedVariableRoster fields) {
		List<String> items = new ArrayList<>();
		for (Entry<VariableIdentifier, ParsedExpression> e : fields.pairs
				.entrySet()) {
			items.add(Language.articleized(e.getKey().toSourceCode()
					+ " of " + e.getValue().toSourceCode()));
		}
		System.out.println(items);
		return displayList(items);
	}
	private static String displayReturn(ParsedExpression output) {
		if (output == null) return "";
		return " and output " + output.toSourceCode();
	}
	private static String displayOutputType(ConcreteType outputType,
			boolean hasFields) {
		if (outputType == PrimitiveType.VOID) return "";
		return " " + (hasFields ? "and" : "that") + " outputs "
				+ Language.articleized(outputType.toSourceCode());
	}
	private static String displayFieldList(List<GenericType> parameterTypes,
			List<VariableIdentifier> parameterVariables) {
		List<String> items = new ArrayList<>();
		for (int i = 0; i < parameterTypes.size(); i++) {
			items.add(displayField(parameterTypes.get(i),
					parameterVariables.get(i)));
		}
		return displayList(items);
	}
	private static String displayField(GenericType genericType,
			VariableIdentifier variableIdentifier) {
		return Language.articleized(genericType.toSourceCode() + " called "
				+ variableIdentifier.toSourceCode());
	}
	private static String displayField(GenericField field) {
		return displayField(field.type, field.name);
	}
	private static String displayList(List<String> items) {
		System.out.println("List to Display: " + items);
		switch (items.size()) {
			case 0:
				return "";
			case 1:
				return items.get(0);
			case 2:
				return items.get(0) + " and " + items.get(1);
		}
		String s = "";
		for (int i = 0; i < items.size() - 1; i++) {
			s += items.get(i) + ", ";
		}
		System.out.println(s + "and " + items.get(items.size() - 1));
		return s + "and " + items.get(items.size() - 1);
	}
	private static String displayFunctionSignature(FunctionName name,
			List<? extends ParsedConstruct> parameterVariables) {
		String s = "";
		int i = 0;
		for (FunctionComponent tok : name.function) {
			if (tok instanceof FunctionToken) {
				s += ((FunctionToken) tok).token + " ";
			} else {
				s += parameterVariables.get(i).toSourceCode() + " ";
				i++;
			}
		}
		return s.trim();
	}
	private static String displayFieldList(List<GenericField> fields) {
		return displayList(fields.stream().map(f -> displayField(f))
				.collect(Collectors.toList()));
	}
}