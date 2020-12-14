/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.isarthur.netbeans.editor.textselector;

import com.github.isarthur.netbeans.editor.textselector.exception.UnsupportedTokenException;
import com.github.isarthur.netbeans.editor.textselector.selection.ExpressionSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.BlockSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.EnclosedExpressionSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.IdentifierSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.IfStmtSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.LiteralSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.ModifierSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.Selection;
import com.github.isarthur.netbeans.editor.textselector.selection.SemicolonSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.SeparatorTokenSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.TryStmtSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.StatementSelection;
import com.github.isarthur.netbeans.editor.textselector.selection.BlockStatementSelection;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;

/**
 *
 * @author Arthur Sadykov
 */
public class SelectionFactory {

    private SelectionFactory() {
    }

    public static Selection create(JTextComponent editor, TokenSequence<?> ts, int selectionStart, int selectionEnd,
            Direction direction, CompilationController controller) throws UnsupportedTokenException {
        TokenId id = ts.token().id();
        if (id == JavaTokenId.AMP
                || id == JavaTokenId.AMPAMP
                || id == JavaTokenId.AMPEQ
                || id == JavaTokenId.BANG
                || id == JavaTokenId.BANGEQ
                || id == JavaTokenId.BAR
                || id == JavaTokenId.BARBAR
                || id == JavaTokenId.BAREQ
                || id == JavaTokenId.BOOLEAN
                || id == JavaTokenId.BYTE
                || id == JavaTokenId.CARET
                || id == JavaTokenId.CARETEQ
                || id == JavaTokenId.CHAR
                || id == JavaTokenId.CLASS
                || id == JavaTokenId.DOUBLE
                || id == JavaTokenId.ENUM
                || id == JavaTokenId.EQ
                || id == JavaTokenId.EQEQ
                || id == JavaTokenId.EXTENDS
                || id == JavaTokenId.FLOAT
                || id == JavaTokenId.GT
                || id == JavaTokenId.GTEQ
                || id == JavaTokenId.GTGT
                || id == JavaTokenId.GTGTEQ
                || id == JavaTokenId.GTGTGT
                || id == JavaTokenId.GTGTGTEQ
                || id == JavaTokenId.IMPLEMENTS
                || id == JavaTokenId.INT
                || id == JavaTokenId.INTERFACE
                || id == JavaTokenId.LONG
                || id == JavaTokenId.LT
                || id == JavaTokenId.LTEQ
                || id == JavaTokenId.LTLT
                || id == JavaTokenId.LTLTEQ
                || id == JavaTokenId.MINUS
                || id == JavaTokenId.MINUSEQ
                || id == JavaTokenId.MINUSMINUS
                || id == JavaTokenId.PERCENT
                || id == JavaTokenId.PERCENTEQ
                || id == JavaTokenId.PLUS
                || id == JavaTokenId.PLUSEQ
                || id == JavaTokenId.PLUSPLUS
                || id == JavaTokenId.SHORT
                || id == JavaTokenId.SLASH
                || id == JavaTokenId.SLASHEQ
                || id == JavaTokenId.STAR
                || id == JavaTokenId.STAREQ
                || id == JavaTokenId.THROWS
                || id == JavaTokenId.TILDE
                || id == JavaTokenId.VOID) {
            return new ExpressionSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.COMMA
                || id == JavaTokenId.DOT
                || id == JavaTokenId.WHITESPACE) {
            return new SeparatorTokenSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.LBRACE
                || id == JavaTokenId.RBRACE) {
            return new BlockSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.IDENTIFIER) {
            return new IdentifierSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.LPAREN
                || id == JavaTokenId.RPAREN) {
            return new EnclosedExpressionSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.SEMICOLON) {
            return new SemicolonSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.STRING_LITERAL
                || id == JavaTokenId.INT_LITERAL
                || id == JavaTokenId.LONG_LITERAL
                || id == JavaTokenId.DOUBLE_LITERAL
                || id == JavaTokenId.FLOAT_LITERAL
                || id == JavaTokenId.CHAR_LITERAL
                || id == JavaTokenId.FALSE
                || id == JavaTokenId.TRUE
                || id == JavaTokenId.NULL) {
            return new LiteralSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.ABSTRACT
                || id == JavaTokenId.DEFAULT
                || id == JavaTokenId.FINAL
                || id == JavaTokenId.NATIVE
                || id == JavaTokenId.PRIVATE
                || id == JavaTokenId.PROTECTED
                || id == JavaTokenId.PUBLIC
                || id == JavaTokenId.STATIC
                || id == JavaTokenId.STRICTFP
                || id == JavaTokenId.SYNCHRONIZED
                || id == JavaTokenId.TRANSIENT
                || id == JavaTokenId.TRANSITIVE
                || id == JavaTokenId.VOLATILE) {
            return new ModifierSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.IF
                || id == JavaTokenId.ELSE) {
            return new IfStmtSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.TRY
                || id == JavaTokenId.CATCH
                || id == JavaTokenId.FINALLY) {
            return new TryStmtSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.ASSERT
                || id == JavaTokenId.IMPORT
                || id == JavaTokenId.PACKAGE
                || id == JavaTokenId.RETURN
                || id == JavaTokenId.THROW) {
            return new StatementSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else if (id == JavaTokenId.DO
                || id == JavaTokenId.WHILE
                || id == JavaTokenId.FOR
                || id == JavaTokenId.SWITCH) {
            return new BlockStatementSelection(editor, ts, selectionStart, selectionEnd, direction, controller);
        } else {
            throw new UnsupportedTokenException(
                    "TokenSelectionFactory.create: unsupported token '" + ts.token().text() + "'."); //NOI18N
        }
    }
}
