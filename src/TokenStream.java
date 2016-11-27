// TokenStream.java

// Implementation of the Scanner for JAY

// This code DOES NOT implement a scanner for JAY. You have to complete
// the code and also make sure it implements a scanner for JAY - not something
// else.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream { // Step 1

	// CHECK THE WHOLE CODE
	// Comment slashes need to be discerned from just a slash 

	private boolean isEof = false;

	private char nextChar = ' '; // next character in input stream

	private BufferedReader input;
   
	// This function was added to make the main.
	public boolean isEoFile() {
		return isEof;
	}

	// Pass a filename for the program text as a source for the TokenStream.
	public TokenStream(String fileName) { //Constructor 
		try {
			input = new BufferedReader(new FileReader(fileName)); //Creating a BR from file
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			// System.exit(1); // Removed to allow ScannerDemo to continue
			// running after the input file is not found.
			isEof = true;
		}
	}

	public Token nextToken() { // Return next token type and value.
		Token t = new Token();
		t.setType("Other"); //Not defined by Grammar Lexical Errors 
		t.setValue("");
		// First check for whitespace and bypass it.
		skipWhiteSpace();

		// Then check for a comment, and bypass it
		// but remember that / is also a division operator.
		while (nextChar == '/') {
			// Changed if to while to avoid the 2nd line being printed when
			// there
			// are two comment lines in a row.
			nextChar = readChar();
			if (nextChar == '/') { // If / is followed by another /
				// skip rest of line - it's a comment.
				// TO BE COMPLETED  !!!DONE!!!
				// look for <cr>, <lf>, <ff>
				while (!isEndOfLine(nextChar)) {
					nextChar = readChar();
				}
				skipWhiteSpace();
					
			} else {
				// A slash followed by a backslash is an AND operator (/\). !!!WRONG IT'S &&!!!
				// 92 is \, the number is used since \ causes an error.
				if (nextChar == 92) { //Possible Easter Egg 
					t.setValue("/" + nextChar);
					nextChar = readChar();
				} else
					// A slash followed by anything else must be an operator.
					t.setValue("/");
				t.setType("Operator");
				return t;
			} // END if/else
		} // End while (nextChar == '/')

		// Then check for an operator; recover 2-character operators
		// as well as 1-character ones.
		if (isOperator(nextChar)) {
			t.setType("Operator");
			//t.setValue(t.getValue() + nextChar);
			switch (nextChar) {
			//case '<' & '=':
			case '<':
			case '>':
			case '=':
			case '!':
			case '|':
			case '&':
			
				// look for <=, >=, !=, ==
				//To Complete !!!DONE!!!
				while (isOperator(nextChar)) {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
				}
				if (isEndOfToken(nextChar)) 
					return t;
				//return t;
			case '+':
			case '-':
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
				return t;
			case 92: // look for the OR operator, \/ !!!Possible Easter Egg!!!
				nextChar = readChar();  //!!!Possible Easter Egg!!!
				// TO BE COMPLETED  !!!Possible Easter Egg!!!
				return t;
			default: // all other operators
				nextChar = readChar();
				return t;
			}
		}

		// Then check for a separator.
		if (isSeparator(nextChar)) {
			t.setType("Separator");
			t.setValue(t.getValue() + nextChar);
			nextChar = readChar();
			// TO BE COMPLETED
			return t;
		}

		// Then check for an identifier, keyword, or literal.
		if (isLetter(nextChar)) {
			// get an identifier
			t.setType("Identifier");
			while ((isLetter(nextChar) || isDigit(nextChar))) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			// NOT WORKING 
			// now see if this is a keyword
			//System.out.println(t.getValue());
			if (isKeyword(t.getValue()))
				t.setType("Keyword");

//			System.out.println(t.toString());

			
			if (isEndOfToken(nextChar)) // If token is valid, returns.
				return t;
		}

		if (isDigit(nextChar)) { // check for integers
			t.setType("Literal"); //Easter Egg CHANGED FROM Integer-Literal 
			while (isDigit(nextChar)) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			// An Integer-Literal is to be only followed by a space,
			// an operator, or a separator.
			if (isEndOfToken(nextChar)) // If token is valid, returns.
				return t;
		}

		if (isEof)
			return t;
		
		// NOT WORKING 
		// Makes sure that the whole unknown token (Type: Other) is printed.
		while (!isEndOfToken(nextChar) && nextChar != 7) {
			if (nextChar == '!') {
				nextChar = readChar();
				if (nextChar == '=') { // looks for = after !
					nextChar = 7; // means next token is !=
					break;
				} else
					t.setValue(t.getValue() + "!");
			} else {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
		}

		if (nextChar == 7) { // 7??? thats an audible bell 
			if (t.getValue().equals("")) { // Looks for a !=
				t.setType("Operator"); // operator. If token is
				t.setValue("!="); // empty, sets != as token,
				nextChar = readChar();
			}

		} else
			t.setType("Other"); // otherwise, unknown token.

		return t;
	} //Function Ends public Token nextToken()

	private char readChar() {
		int i = 0;
		if (isEof) //false 
			return (char) 0;
		System.out.flush();
		try {
			i = input.read();
		} catch (IOException e) {
			System.exit(-1);
		}
		if (i == -1) {
			isEof = true;
			return (char) 0;
		}
		return (char) i;
	}

	private boolean isKeyword(String s) {
		// TO BE COMPLETED !!!DONE!!!
		return (s.equalsIgnoreCase("boolean") || s.equalsIgnoreCase("main") || s.equalsIgnoreCase("void") || s.equalsIgnoreCase("while") || s.equalsIgnoreCase("int") || s.equalsIgnoreCase("if") || s.equalsIgnoreCase("else"));
	}

	private boolean isWhiteSpace(char c) {
		return (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f');
	}

	private boolean isEndOfLine(char c) {
		return (c == '\r' || c == '\n' || c == '\f');
	}

	private boolean isEndOfToken(char c) { // Is the value a separate token?
		return (isWhiteSpace(nextChar) || isOperator(nextChar)
				|| isSeparator(nextChar) || isEof);
	}

	private void skipWhiteSpace() {
		// check for whitespace, and bypass it
		while (!isEof && isWhiteSpace(nextChar)) {
			nextChar = readChar();
		}
	}

	private boolean isSeparator(char c) {
		return (c == '(' || c == ')' || c == '{' || c == '}' || c == ';' || c == ',');
	}

	private boolean isOperator(char c) {
		// TO BE COMPLETED !!!DONE!!!
		return (c == '=' || c == '+' || c == '-' || c == '*' || c == '>' || c == '<' || c == '!' || c == '&' || c == '|' || c == 92);
	}

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}

	private static boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	public boolean isEndofFile() { //Maybe unnecessary 
		return isEof;
	}
	
	public static void main (String[]args) {
		//System.out.println(isDigit((char) '1'));
		TokenStream t = new TokenStream("/Users/goldberg/Desktop/ParserScanner/src/prog2.jay"); 
		while (!t.isEoFile()) {
			Token to = t.nextToken();
			System.out.println(to.toString());
		}
	}
} //END TokenStream

