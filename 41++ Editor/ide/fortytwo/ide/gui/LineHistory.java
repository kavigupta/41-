package fortytwo.ide.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.commons.lang3.StringEscapeUtils;

import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.errors.Error42;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralString;

public class LineHistory extends JScrollPane {
	private JPanel entry;
	private int index = 0;
	private ArrayList<String> history = new ArrayList<String>();
	public LineHistory() {
		super();
		entry = new JPanel();
		entry.setBackground(Color.BLACK);
		entry.setOpaque(true);
		entry.setLayout(new GridBagLayout());
		setBackground(Color.BLACK);
		setOpaque(true);
		this.setViewportView(entry);
	}
	public void put(JTextPane textBox, boolean setForeground) {
		if (setForeground) {
			textBox.setForeground(Color.WHITE);
		}
		String text = textBox.getText();
		textBox.setContentType("text/html");
		textBox.setText(text);
		// comp.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		textBox.setBackground(Color.BLACK);
		textBox.setOpaque(true);
		textBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		textBox.setEditable(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.gridwidth = 10;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridy = index;
		index++;
		entry.add(textBox, gbc);
	}
	public void displayCommand(String cmd) {
		System.out.println("Command " + cmd);
		cmd = process(cmd);
		JTextPane command = new JTextPane();
		command.setText(">> " + cmd);
		put(command, true);
	}
	public static String process(String cmd) {
		int i = cmd.length() - 1;
		while (cmd.charAt(i) == '\r' || cmd.charAt(i) == '\n')
			i--;
		System.out.println(cmd + "\t" + i);
		System.out.println(cmd.substring(0, i + 1));
		return StringEscapeUtils.escapeHtml3(cmd.substring(0, i + 1))
				.replaceAll("\n", "<br>&nbsp;&nbsp;&nbsp;");
	}
	public void displayOutput(LiteralExpression literalValue) {
		String output = process(literalValue.toSourceCode());
		String store = literalValue.toSourceCode();
		if (literalValue instanceof LiteralString) {
			store = "'" + Tokenizer.escape(store.substring(1, store.length() - 1))
					+ "'";
		}
		history.add(store);
		JTextPane result = new JTextPane();
		System.out.println("Lit: "
				+ literalValue.toSourceCode().replace("\n", "\\n"));
		result.setText("&nbsp;= " + output);
		System.out.println(result.getText());
		result.setForeground(Color.YELLOW);
		put(result, false);
	}
	public void displayln(String line) {
		JTextPane result = new JTextPane();
		result.setText("   " + line);
		put(result, true);
	}
	public void displayerr(Error42 err) {
		StringBuffer error = new StringBuffer();
		error.append("<html>&nbsp;&nbsp;&nbsp;<font color=#ff0000>");
		boolean openTilde = true;
		String msg = process(err.msg);
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == '~') {
				if (openTilde)
					error.append("</font><font color=#00ff00>");
				else error.append("</font><font color=#ff0000>");
				openTilde = !openTilde;
			} else if (msg.charAt(i) == '{') {
				error.append("</font><font color=#0000ff>");
			} else if (msg.charAt(i) == '}') {
				error.append("</font><font color=#00ff00>");
			} else {
				error.append(msg.charAt(i));
			}
		}
		error.append("</font></html>");
		JTextPane pane = new JTextPane();
		pane.setText(error.toString());
		put(pane, false);
	}
	public int nCommands() {
		return history.size();
	}
	public void set(int pointer, String text) {
		history.set(pointer, text);
	}
	public String get(int pointer) {
		return history.get(pointer);
	}
}