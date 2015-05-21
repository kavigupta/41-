package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.parsed.constructions.ParsedVariableRoster;
import fortytwo.compiler.parsed.declaration.FunctionReturn;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.compiler.parsed.sentences.Sentence.SentenceType;
import fortytwo.compiler.parsed.statements.*;
import fortytwo.language.Language;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;

public class StatementParser {
	public static Sentence parseCompleteStatement(
			List<List<String>> currentPhrases) {
		if (currentPhrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList());
		ParsedExpression condition;
		switch (currentPhrases.get(0).get(0)) {
			case "While":
				currentPhrases.get(0).remove(0);
				condition = ExpressionParser.parseExpression(currentPhrases
						.get(0));
				List<ParsedStatement> statements = new ArrayList<>();
				for (int i = 1; i < currentPhrases.size(); i++) {
					Sentence s = parseStatement(currentPhrases.get(i));
					if (!(s instanceof ParsedStatement))
						throw new RuntimeException(/*
											 * LOWPRI-E Declarations
											 * are not allowed in
											 * while statements.
											 */);
					statements.add((ParsedStatement) s);
				}
				return new ParsedWhileLoop(condition,
						new ParsedStatementSeries(statements));
			case "If":
				return parseIfStatement(currentPhrases);
		}
		if (currentPhrases.size() == 1)
			return parseStatement(currentPhrases.get(0));
		List<ParsedStatement> statements = new ArrayList<>();
		for (int i = 0; i < currentPhrases.size(); i++) {
			Sentence s = parseStatement(currentPhrases.get(i));
			if (!(s instanceof ParsedStatement))
				throw new RuntimeException(/*
									 * Compound statements do not allow
									 * declarations
									 */);
			statements.add((ParsedStatement) s);
		}
		return new ParsedStatementSeries(statements);
	}
	private static ParsedIfElse parseIfStatement(
			List<List<String>> currentPhrases) {
		currentPhrases.get(0).remove(0);
		ParsedExpression condition = ExpressionParser
				.parseExpression(currentPhrases.get(0));
		List<List<String>> ifso = new ArrayList<>();
		int i = 1;
		int ifnest = 1;
		for (; i < currentPhrases.size(); i++) {
			System.out.println("IFNest" + ifnest + "\t"
					+ currentPhrases.get(i));
			if (currentPhrases.get(i).get(0).equals("If"))
				ifnest++;
			else if (currentPhrases.get(i).get(0).equals("otherwise")) {
				ifnest--;
				if (ifnest == 0) break;
			}
			ifso.add(currentPhrases.get(i));
		}
		List<List<String>> ifelse = new ArrayList<>();
		for (i++; i < currentPhrases.size(); i++) {
			ifelse.add(currentPhrases.get(i));
		}
		Sentence ifsoS = parseCompleteStatement(ifso);
		Sentence ifelseS = parseCompleteStatement(ifelse);
		if (!(ifsoS instanceof ParsedStatement)
				|| !(ifelseS instanceof ParsedStatement))
			throw new RuntimeException(/* LOWPRI-E */);
		return ParsedIfElse.getInstance(condition, ParsedStatementSeries
				.getInstance((ParsedStatement) ifsoS),
				ParsedStatementSeries
						.getInstance((ParsedStatement) ifelseS));
	}
	private static Sentence parseStatement(List<String> line) {
		line = new ArrayList<>(line);
		line.remove(line.size() - 1);
		switch (line.get(0)) {
			case "Run":
				line.remove(0);
				ParsedExpression e = ExpressionParser.parseExpression(line);
				if (e.type() == SentenceType.PURE_EXPRESSION)
					throw new RuntimeException(/* LOWPRI-E */);
				return e;
			case "Define":
				return parseDefinition(line);
			case "Set":
				return parseAssignment(line);
			case "Exit":
				return parseReturn(line);
			default:
				return parseVoidFunctionCall(line);
		}
	}
	private static FunctionReturn parseReturn(List<String> line) {
		/* Exit the function( and output <output>)?. */
		if (!line.get(1).equals("the") || !line.get(2).equals("function"))
			throw new RuntimeException(/* LOWPRI-E */);
		if (line.size() == 3) return new FunctionReturn(null);
		if (!line.get(3).equals("and") || !line.get(4).equals("output"))
			throw new RuntimeException(/* LOWPRI-E */);
		line.subList(0, 5).clear();
		return new FunctionReturn(ExpressionParser.parseExpression(line));
	}
	private static ParsedStatement parseVoidFunctionCall(List<String> list) {
		ParsedFunctionCall function = ConstructionParser
				.composeFunction(list);
		if (function.name.function.size() == 1
				&& function.name.function.get(0) instanceof FunctionArgument)
			throw new RuntimeException(/* LOWPRI-E non-void function call */);
		return function;
	}
	private static ParsedAssignment parseAssignment(List<String> line) {
		/*
		 * Set the <field> of <name> to <value>.
		 */
		if (!line.get(1).equals("the") || !line.get(3).equals("of")
				|| !line.get(5).equals("to"))
			throw new RuntimeException(/* LOWPRI-E */);
		String field = line.get(2);
		VariableIdentifier name = VariableIdentifier.getInstance(line.get(4));
		line.subList(0, 6).clear();
		ParsedExpression expr = ExpressionParser.parseExpression(line);
		return field.equals("value") ? new ParsedRedefinition(name, expr)
				: new ParsedFieldAssignment(name,
						VariableIdentifier.getInstance(field), expr);
	}
	private static Sentence parseDefinition(List<String> line) {
		System.out.println("Line:" + line);
		/*
		 * Define a[n] <type> called <name>( with a <field1> of <value1>, a
		 * <field2> of <value2>, ...)?.
		 */
		if (!Language.isArticle(line.get(1)) || !line.get(3).equals("called"))
			throw new RuntimeException(/* LOWPRI-E */);
		String type = Language.deparenthesize(line.get(2));
		if (type.equals("function"))
			return ConstructionParser.parseFunctionDefinition(line);
		if (type.equals("type"))
			return ConstructionParser.parseStructDefinition(line);
		String name = line.get(4);
		ParsedVariableRoster fields = new ParsedVariableRoster();
		for (int i = 5; i < line.size(); i++) {
			if (!line.get(i).equals("of")) continue;
			String field = line.get(i - 1);
			ArrayList<String> tokens = new ArrayList<>();
			i++;
			while (i < line.size() && !line.get(i).equals(",")
					&& !line.get(i).equals("and")) {
				tokens.add(line.get(i));
				i++;
			}
			fields.add(VariableIdentifier.getInstance(field),
					ExpressionParser.parseExpression(tokens));
		}
		System.out.println(fields.pairs);
		GenericType genericType = ExpressionParser.parseType(type);
		if (!(genericType instanceof ConcreteType))
			throw new RuntimeException(/* LOWPRI-E */);
		return new ParsedDefinition(new Field(
				VariableIdentifier.getInstance(name),
				(ConcreteType) genericType), fields);
	}
}
