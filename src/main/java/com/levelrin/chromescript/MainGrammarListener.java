package com.levelrin.chromescript;

import com.levelrin.antlr.generated.MainGrammarBaseListener;
import com.levelrin.antlr.generated.MainGrammarParser;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class MainGrammarListener extends MainGrammarBaseListener {

    private final StringBuilder backgroundFile = new StringBuilder();

    private final StringBuilder popupFile = new StringBuilder();

    private final List<StringBuilder> scriptFiles = new ArrayList<>();

    private final Deque<StringBuilder> fileStack = new ArrayDeque<>();

    private StringBuilder currentFile;

    @Override
    public void enterPopupLogic(final MainGrammarParser.PopupLogicContext context) {
        this.currentFile = this.popupFile;
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

    @Override
    public void enterOpenNewTab(final MainGrammarParser.OpenNewTabContext context) {
        final int scriptIndex = this.scriptFiles.size();
        this.backgroundFile.append(
            String.format(
                """
                chrome.runtime.onMessage.addListener((message, __, ___) => {
                    if (message.about === `newTab%d`) {
                        chrome.tabs.create({url: message.url}).then((tab) => {
                            const listener = (tabId, changeInfo) => {
                                if (tabId === tab.id && changeInfo.status === `complete`) {
                                    chrome.tabs.onUpdated.removeListener(listener);
                                    chrome.scripting.executeScript({
                                        target: {tabId: tab.id},
                                        files: [`scripts/%d.js`]
                                    });
                                }
                            };
                            chrome.tabs.onUpdated.addListener(listener);
                        });
                    }
                });
                """,
                scriptIndex,
                scriptIndex
            )
        );
        this.currentFile.append("chrome.runtime.sendMessage({about: `newTab")
            .append(scriptIndex)
            .append("`, url: ")
            .append(context.STRING())
            .append(", script: `")
            .append(this.scriptFiles.size())
            .append("`});");
        this.fileStack.push(this.currentFile);
        final StringBuilder scriptFile = new StringBuilder();
        this.scriptFiles.add(scriptFile);
        this.currentFile = scriptFile;
    }

    @Override
    public void exitOpenNewTab(final MainGrammarParser.OpenNewTabContext context) {
        this.currentFile = this.fileStack.pop();
    }

    public StringBuilder backgroundFile() {
        return this.backgroundFile;
    }

    public StringBuilder popupFile() {
        return this.popupFile;
    }

    public List<StringBuilder> scriptFiles() {
        return this.scriptFiles;
    }

}
