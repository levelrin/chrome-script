package com.levelrin.chromescript;

import com.levelrin.antlr.generated.MainGrammarBaseListener;
import com.levelrin.antlr.generated.MainGrammarParser;
import java.util.List;

public final class MainGrammarListener extends MainGrammarBaseListener {

    private final List<StringBuilder> scripts;

    private StringBuilder currentFile;

    public MainGrammarListener(final List<StringBuilder> scripts) {
        this.scripts = scripts;
    }

    @Override
    public void enterFile(final MainGrammarParser.FileContext context) {
        this.currentFile = this.scripts.get(0);
    }

    @Override
    public void enterPrint(final MainGrammarParser.PrintContext context) {
        this.currentFile.append("console.log(")
            .append(context.STRING().getText())
            .append(");");
    }

    @Override
    public void enterElementById(final MainGrammarParser.ElementByIdContext context) {
        this.currentFile.append("const ")
            .append(context.NAME().getText())
            .append(" = document.getElementById(")
            .append(context.STRING())
            .append(");");
    }

    @Override
    public void enterWhenElementClicked(final MainGrammarParser.WhenElementClickedContext context) {
        this.currentFile.append(context.NAME().getText())
            .append(".addEventListener('click', () => {");
    }

    @Override
    public void exitWhenElementClicked(MainGrammarParser.WhenElementClickedContext context) {
        this.currentFile.append("});");
    }

}
