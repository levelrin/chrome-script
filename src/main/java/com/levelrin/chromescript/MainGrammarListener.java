package com.levelrin.chromescript;

import com.levelrin.antlr.generated.MainGrammarBaseListener;
import com.levelrin.antlr.generated.MainGrammarParser;

public final class MainGrammarListener extends MainGrammarBaseListener {

    private final StringBuilder js;

    public MainGrammarListener(final StringBuilder js) {
        this.js = js;
    }

    @Override
    public void enterPrint(final MainGrammarParser.PrintContext context) {
        this.js.append("console.log(")
            .append(context.STRING().getText())
            .append(");");
    }

    @Override
    public void enterElementById(final MainGrammarParser.ElementByIdContext context) {
        this.js.append("const ")
            .append(context.NAME().getText())
            .append(" = document.getElementById(")
            .append(context.STRING())
            .append(");");
    }

    @Override
    public void enterWhenElementClicked(final MainGrammarParser.WhenElementClickedContext context) {
        this.js.append(context.NAME().getText())
            .append(".addEventListener('click', () => {");
    }

    @Override
    public void exitWhenElementClicked(MainGrammarParser.WhenElementClickedContext context) {
        this.js.append("});");
    }

}
