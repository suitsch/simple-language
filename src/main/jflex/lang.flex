package edu.appstate.cs.analysis;

import beaver.Symbol;
import beaver.Scanner;

import edu.appstate.cs.analysis.parser.LanguageParser.Terminals;

%%

%class LanguageScanner
%extends Scanner
%public
%function nextToken
%type Symbol
%yylexthrow Scanner.Exception
%eofval{
	return newToken(Terminals.EOF, "end-of-file");
%eofval}
%unicode
%line
%column
%{
	private Symbol newToken(short id)
	{
		return new Symbol(id, yyline + 1, yycolumn + 1, yylength());
	}

	private Symbol newToken(short id, Object value)
	{
		return new Symbol(id, yyline + 1, yycolumn + 1, yylength(), value);
	}
%}
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
%%

{WhiteSpace}+    { /* ignore */ }
<YYINITIAL> {
    "hello"      { return newToken(Terminals.HELLO); }
    "world"      { return newToken(Terminals.WORLD); }
}

[^]|\n             { throw new Scanner.Exception("unexpected character '" + yytext() + "'"); }