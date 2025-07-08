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
Integer        = [:digit:] [:digit:]*
%%

{WhiteSpace}+    { /* ignore */ }
<YYINITIAL> {
	{ Integer }  { return newToken(Terminals.INTEGER, new Integer(yytext())); }
	"else-if"    { return newToken(Terminals.ELSEIF); }
	"return"     { return newToken(Terminals.RETURN); }
    "hello"      { return newToken(Terminals.HELLO); }
    "world"      { return newToken(Terminals.WORLD); }
	"while"      { return newToken(Terminals.WHILE); }
	"else"       { return newToken(Terminals.ELSE); }
	"then"       { return newToken(Terminals.THEN); }
	"for"        { return newToken(Terminals.FOR); }
	"in"         { return newToken(Terminals.IN); }
	"if"         { return newToken(Terminals.IF); }
	"="          { return newToken(Terminals.ASSIGN); }
	";"          { return newToken(Terminals.SEMI); }
	"{"          { return newToken(Terminals.LCURLY); }
	"}"          { return newToken(Terminals.RCURLY); }
	","          { return newToken(Terminals.COMMA); }
	"["          { return newToken(Terminals.LBRACKET); }
	"]"          { return newToken(Terminals.RBRACKET); }
	"("          { return newToken(Terminals.LPAREN); }
	")"          { return newToken(Terminals.RPAREN); }
}

[^]|\n             { throw new Scanner.Exception("unexpected character '" + yytext() + "'"); }