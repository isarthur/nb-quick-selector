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
package com.github.isarthur.netbeans.editor.textselector.selection;

import com.github.isarthur.netbeans.editor.textselector.Direction;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.document.LineDocument;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.lexer.TokenSequence;

/**
 *
 * @author Arthur Sadykov
 */
public abstract class Selection {

    protected JTextComponent editor;
    protected LineDocument document;
    protected TokenSequence<?> tokenSequence;
    protected int selectionStart;
    protected int selectionEnd;
    protected Direction direction;
    protected final CompilationController controller;

    protected Selection(JTextComponent editor, TokenSequence<?> ts, int selectionStart, int selectionEnd,
            Direction direction, CompilationController controller) {
        this.editor = editor;
        document = (LineDocument) editor.getDocument();
        editor.setDocument(document);
        this.tokenSequence = ts;
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
        this.direction = direction;
        this.controller = controller;
    }

    public void select() {
    }

    protected void select(int startPosition, int endPosition) {
        int start;
        int end;
        if (isTextSelected()) {
            if (isBackwardDirection()) {
                start = startPosition;
                end = selectionEnd;
            } else {
                start = selectionStart;
                end = endPosition;
            }
        } else {
            start = startPosition;
            end = endPosition;
        }
        if (direction == Direction.BACKWARD) {
            selectBackward(end, start);
            selectionStart = end;
            selectionEnd = start;
        } else {
            selectForward(start, end);
            selectionStart = start;
            selectionEnd = end;
        }
    }

    protected boolean isTextSelected() {
        return selectionStart != selectionEnd;
    }

    protected boolean isBackwardDirection() {
        return direction == Direction.BACKWARD;
    }

    protected void selectBackward(int right, int left) {
        editor.select(left, right);
        editor.setCaretPosition(right);
        editor.moveCaretPosition(left);
    }

    protected void selectForward(int left, int right) {
        editor.select(left, right);
        editor.setCaretPosition(left);
        editor.moveCaretPosition(right);
    }
}
