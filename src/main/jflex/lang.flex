package edu.appstate.cs.analysis;

import beaver.Symbol;
import beaver.Scanner;
import java.math.BigInteger;

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
String         = [\"] [^\"]* [\"]
Identifier     = [:jletter:] [:jletterdigit:]*
%%

{WhiteSpace}+    { /* ignore */ }
<YYINITIAL> {
	{Integer}    { return newToken(Terminals.INTEGER, new BigInteger(yytext())); }
	{String}     { return newToken(Terminals.STRING, new String(yytext())); }
	"else-if"    { return newToken(Terminals.ELSEIF); }
	"return"     { return newToken(Terminals.RETURN); }
	"while"      { return newToken(Terminals.WHILE); }
	"False"      { return newToken(Terminals.FALSE); }
	"else"       { return newToken(Terminals.ELSE); }
	"then"       { return newToken(Terminals.THEN); }
	"True"       { return newToken(Terminals.TRUE); }
	"for"        { return newToken(Terminals.FOR); }
	"not"        { return newToken(Terminals.NOT); }
	"or"		 { return newToken(Terminals.OR); }
	"in"         { return newToken(Terminals.IN); }
	"if"         { return newToken(Terminals.IF); }
	"=="         { return newToken(Terminals.EQUAL); }
	"="          { return newToken(Terminals.ASSIGN); }
	";"          { return newToken(Terminals.SEMI); }
	"{"          { return newToken(Terminals.LCURLY); }
	"}"          { return newToken(Terminals.RCURLY); }
	","          { return newToken(Terminals.COMMA); }
	"["          { return newToken(Terminals.LBRACKET); }
	"]"          { return newToken(Terminals.RBRACKET); }
	"("          { return newToken(Terminals.LPAREN); }
	")"          { return newToken(Terminals.RPAREN); }
	"+"          { return newToken(Terminals.PLUS); }
	"*"          { return newToken(Terminals.MULT); }
	"/"          { return newToken(Terminals.DIV); }
	"!="         { return newToken(Terminals.NOTEQL); }
	"var"        { return newToken(Terminals.VAR); } // DeclStmt
	{Identifier} { return newToken(Terminals.IDENTIFIER, new String(yytext())); }
}

[^]|\n             { throw new Scanner.Exception("unexpected character '" + yytext() + "'"); }
