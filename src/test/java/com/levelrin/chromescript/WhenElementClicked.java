package com.levelrin.chromescript;

import com.levelrin.antlr.generated.MainGrammarLexer;
import com.levelrin.antlr.generated.MainGrammarParser;
import java.util.ArrayList;
import java.util.Collections;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class WhenElementClicked {

    @Test
    void whenElementClicked() {
        final String code = """
            When the button is clicked, do the following: {
              Print `Clicked!` on the console.
            }
            """;
        final CharStream charStream = CharStreams.fromString(code);
        final MainGrammarLexer lexer = new MainGrammarLexer(charStream);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final MainGrammarParser parser = new MainGrammarParser(tokens);
        final ParseTree tree = parser.file();
        final MainGrammarListener listener = new MainGrammarListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);
        Assertions.assertEquals("button.addEventListener('click', () => {console.log(`Clicked!`);});", listener.popupFile().toString());
    }

}
