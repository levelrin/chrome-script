package com.levelrin.plainen;

import com.levelrin.antlr.generated.MainGrammarLexer;
import com.levelrin.antlr.generated.MainGrammarParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class ElementByIdTest {

    @Test
    void elementById() {
        final String code = "The button is the element with the ID `btn`.";
        final CharStream charStream = CharStreams.fromString(code);
        final MainGrammarLexer lexer = new MainGrammarLexer(charStream);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final MainGrammarParser parser = new MainGrammarParser(tokens);
        final ParseTree tree = parser.sentences();
        final StringBuilder output = new StringBuilder();
        final MainGrammarListener listener = new MainGrammarListener(output);
        ParseTreeWalker.DEFAULT.walk(listener, tree);
        Assertions.assertEquals("const button = document.getElementById(`btn`);", output.toString());
    }

}
