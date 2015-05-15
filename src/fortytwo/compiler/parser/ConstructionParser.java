package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.Language;
import fortytwo.compiler.language.declaration.Declaration;
import fortytwo.compiler.language.declaration.FunctionDefinition;
import fortytwo.compiler.language.declaration.StructureDeclaration;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.FunctionName;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.compiler.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.compiler.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.compiler.language.identifier.functioncomponent.FunctionToken;
import fortytwo.compiler.language.statements.ParsedFunctionCall;
import fortytwo.vm.constructions.Field;
import fortytwo.vm.constructions.GenericStructure;

public class ConstructionParser {
	public static ParsedFunctionCall composeFunction(List<String> list) {
		Pair<FunctionName, List<ParsedExpression>> fsig = parseFunctionSignature(list);
		return ParsedFunctionCall.getInstance(fsig.key, fsig.value);
	}
	public static Declaration parseStructDefinition(List<String> line) {
		/*
		 * Define a structure called <structure> of <typevar1>, <typevar2>,
		 * and <typevar3> ; which contains a[n] <type> called <field> , a[n]
		 * <type> called <field>, ...
		 */
		if (!line.get(1).equals("a") || !line.get(3).equals("called"))
			throw new RuntimeException(/* LOWPRI-E */);
		int i = 4;
		ArrayList<String> structExpression = new ArrayList<>();
		for (; i < line.size() && !line.get(i).equals(";")
				&& !line.get(i).equals("of"); i++) {
			structExpression.add(line.get(i));
		}
		List<TypeIdentifier> typeVariables = new ArrayList<>();
		if (line.get(i).equals("of")) {
			i++;
			for (; i < line.size() && !line.get(i).equals(";"); i++) {
				if (Language.isValidVariableIdentifier(line.get(i))) {
					typeVariables.add(TypeIdentifier.getInstance(line
							.get(i)));
				}
			}
		}
		ArrayList<Field> fields = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).equals("called")) continue;
			fields.add(new Field(VariableIdentifier.getInstance(line
					.get(i + 1)), TypeIdentifier.getInstance(line
					.get(i - 1))));
		}
		return new StructureDeclaration(new GenericStructure(
				structExpression, typeVariables, fields));
	}
	public static FunctionDefinition parseFunctionDefinition(List<String> line) {
		/*
		 * Define a function called <function expression> that takes a[n]
		 * <type1> called <field1>, a[n] <type2> called <field2>, and a[n]
		 * <type3> called <field3>( and outputs a <return type>)?.
		 */
		if (!line.get(1).equals("a") || !line.get(2).equals("function")
				|| !line.get(3).equals("called"))
			throw new RuntimeException(/* LOWPRI-E */);
		int i = 4;
		ArrayList<String> funcExpress = new ArrayList<>();
		for (; i < line.size() && !line.get(i).equals("that"); i++) {
			funcExpress.add(line.get(i));
		}
		i++;
		if (!line.get(i).equals("takes")) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
		ArrayList<TypeIdentifier> types = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).equals("called")) continue;
			types.add(TypeIdentifier.getInstance(line.get(i + 1)));
		}
		int outputloc = line.indexOf("outputs");
		Pair<FunctionName, List<ParsedExpression>> sig = parseFunctionSignature(funcExpress);
		List<VariableIdentifier> variables = sig.value
				.stream()
				.map(x -> {
					if (!(x instanceof VariableIdentifier))
						throw new RuntimeException(/* LOWPRI-E */);
					return (VariableIdentifier) x;
				}).collect(Collectors.toList());
		if (outputloc < 0)
			return new FunctionDefinition(sig.key, variables, types, null);
		if (!Language.isArticle(line.get(outputloc + 1)))
			throw new RuntimeException(/* LOWPRI-E */);
		line.subList(0, outputloc + 2).clear();
		return new FunctionDefinition(sig.key, variables, types,
				ExpressionParser.parseExpression(line));
	}
	private static Pair<FunctionName, List<ParsedExpression>> parseFunctionSignature(
			List<String> list) {
		List<FunctionComponent> function = new ArrayList<>();
		List<String> currentExpression = new ArrayList<>();
		List<ParsedExpression> arguments = new ArrayList<>();
		for (String token : list) {
			if (Language.isExpression(token)) {
				currentExpression.add(token);
			} else {
				if (currentExpression.size() != 0) {
					ParsedExpression argument = ExpressionParser
							.parsePureExpression(currentExpression);
					arguments.add(argument);
					function.add(FunctionArgument.INSTANCE);
					currentExpression.clear();
				}
				function.add(new FunctionToken(token));
			}
		}
		if (currentExpression.size() != 0) {
			ParsedExpression argument = ExpressionParser
					.parsePureExpression(currentExpression);
			arguments.add(argument);
			function.add(FunctionArgument.INSTANCE);
			currentExpression.clear();
		}
		return Pair
				.getInstance(FunctionName.getInstance(function), arguments);
	}
}