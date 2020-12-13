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
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.lexer.TokenSequence;

/**
 *
 * @author Arthur Sadykov
 */
public class ModifierSelection extends Selection {

    public ModifierSelection(JTextComponent editor, TokenSequence<?> ts, int selectionStart, int selectionEnd,
            Direction direction, CompilationController controller) {
        super(editor, ts, selectionStart, selectionEnd, direction, controller);
    }

    @Override
    public void select() {
        long startPosition = tokenSequence.offset();
        long endPosition = tokenSequence.offset() + tokenSequence.token().length();
        select((int) startPosition, (int) endPosition);
    }
}
