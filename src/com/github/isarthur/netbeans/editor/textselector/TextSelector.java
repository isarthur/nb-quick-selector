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
import com.github.isarthur.netbeans.editor.textselector.settings.Settings;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "nb.editor.ui.actions.ExtendSelection"
)
@ActionRegistration(
        displayName = "#CTL_ExtendSelection"
)
@ActionReferences({
    @ActionReference(path = "Shortcuts", name = "D-W")
})
@Messages("CTL_ExtendSelection=Extend Selection")
public class TextSelector extends BaseAction {

    private static final long serialVersionUID = -5804809213732564866L;
    private static final String MIME_TYPE = "mimeType"; //NOI18N
    private static final String JAVA_MIME_TYPE = "text/x-java"; //NOI18N
    private int selectionEnd;
    private int selectionStart;
    private Direction direction = Direction.BACKWARD;
    private int caretPosition;

    @Override
    public void actionPerformed(ActionEvent event, JTextComponent editor) {
        if (!mimeTypeOfEditorDocumentIsJava(editor)) {
            return;
        }
        selectionStart = editor.getSelectionStart();
        selectionEnd = editor.getSelectionEnd();
        caretPosition = editor.getCaretPosition();
        Document document = editor.getDocument();
        JavaSource javaSource = JavaSource.forDocument(document);
        if (javaSource == null) {
            throw new IllegalStateException("The Java source is not associated to document!"); //NOI18N
        }
        try {
            javaSource.runUserActionTask(controller -> {
                controller.toPhase(JavaSource.Phase.PARSED);
                TokenHierarchy<?> tokenHierarchy = controller.getTokenHierarchy();
                TokenSequence<?> tokenSequence = tokenHierarchy.tokenSequence();
                tokenSequence.move(caretPosition);
                if (!isTextSelected()) {
                    if (tokenSequence.movePrevious()) {
                        TokenId id = tokenSequence.token().id();
                        if (id == JavaTokenId.WHITESPACE || id == JavaTokenId.LPAREN || id == JavaTokenId.DOT) {
                            direction = Direction.FORWARD;
                            resetTokenSequence(tokenSequence);
                        } else {
                            direction = Direction.BACKWARD;
                            resetTokenSequence(tokenSequence);
                        }
                    } else {
                        direction = Direction.FORWARD;
                        resetTokenSequence(tokenSequence);
                    }
                } else {
                    if (direction == Direction.BACKWARD) {
                        tokenSequence.movePrevious();
                    } else {
                        tokenSequence.moveNext();
                    }
                }
                try {
                    SelectionFactory.create(editor, tokenSequence, selectionStart, selectionEnd, direction, controller).select();
                    if (Settings.copyToClipboard()) {
                        editor.copy();
                    }
                } catch (UnsupportedTokenException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }, true);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean mimeTypeOfEditorDocumentIsJava(JTextComponent editor) {
        return getMimeTypeOfEditorDocument(editor).map(mt -> mt.equals(JAVA_MIME_TYPE)).orElse(false);
    }

    private Optional<String> getMimeTypeOfEditorDocument(JTextComponent editor) {
        return Optional.ofNullable((String) editor.getDocument().getProperty(MIME_TYPE));
    }

    private void resetTokenSequence(TokenSequence<?> ts) {
        ts.move(caretPosition);
        if (direction == Direction.BACKWARD) {
            ts.movePrevious();
        } else {
            ts.moveNext();
        }
    }

    private boolean isTextSelected() {
        return selectionStart != selectionEnd;
    }
}
