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
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import java.util.EnumSet;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.TreeUtilities;
import org.netbeans.api.lexer.TokenSequence;

/**
 *
 * @author Arthur Sadykov
 */
public class EnclosedExpressionSelection extends Selection {

    public EnclosedExpressionSelection(JTextComponent editor, TokenSequence<?> tokenSequence, int selectionStart,
            int selectionEnd, Direction direction, CompilationController controller) {
        super(editor, tokenSequence, selectionStart, selectionEnd, direction, controller);
    }

    @Override
    public void select() {
        TreeUtilities treeUtilities = controller.getTreeUtilities();
        TreePath currentPath;
        if (tokenSequence.token().id() == JavaTokenId.LPAREN) {
            currentPath = treeUtilities.pathFor(tokenSequence.offset() + 1);
        } else {
            currentPath = treeUtilities.pathFor(tokenSequence.offset());
        }
        if (currentPath == null) {
            return;
        }
        TreePath enclosedExpressionPath = treeUtilities.getPathElementOfKind(
                EnumSet.of(Tree.Kind.METHOD, Tree.Kind.METHOD_INVOCATION, Tree.Kind.PARENTHESIZED), currentPath);
        if (enclosedExpressionPath == null) {
            return;
        }
        Trees trees = controller.getTrees();
        SourcePositions sourcePositions = trees.getSourcePositions();
        CompilationUnitTree compilationUnitTree = controller.getCompilationUnit();
        long startPosition;
        long endPosition;
        Tree enclosedExpressionTree = enclosedExpressionPath.getLeaf();
        switch (enclosedExpressionTree.getKind()) {
            case METHOD:
                MethodTree methodTree = (MethodTree) enclosedExpressionTree;
                int[] methodParameterSpan = treeUtilities.findMethodParameterSpan(methodTree);
                if (methodParameterSpan[0] == tokenSequence.offset() || methodParameterSpan[1] == tokenSequence.offset()) {
                    startPosition = methodParameterSpan[0];
                    endPosition = methodParameterSpan[1] + 1;
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, currentPath.getLeaf());
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, currentPath.getLeaf());
                }
                break;
            case METHOD_INVOCATION:
                MethodInvocationTree methodInvocationTree = (MethodInvocationTree) enclosedExpressionTree;
                ExpressionTree methodSelect = methodInvocationTree.getMethodSelect();
                long methodSelectEndPosition = sourcePositions.getEndPosition(compilationUnitTree, methodSelect);
                tokenSequence.move((int) methodSelectEndPosition);
                while (tokenSequence.moveNext() && tokenSequence.token().id() == JavaTokenId.WHITESPACE) {
                }
                if (tokenSequence.token().id() == JavaTokenId.LPAREN) {
                    startPosition = tokenSequence.offset();
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, methodInvocationTree);
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, enclosedExpressionTree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, enclosedExpressionTree);
                }
                break;
            default:
                startPosition = sourcePositions.getStartPosition(compilationUnitTree, enclosedExpressionTree);
                endPosition = sourcePositions.getEndPosition(compilationUnitTree, enclosedExpressionTree);
        }
        select((int) startPosition, (int) endPosition);
    }

    @Override
    protected void select(int startPosition, int endPosition) {
        if (direction == Direction.BACKWARD) {
            if (isTextSelected()) {
                if (selectionEnd < endPosition) {
                    selectBackward(endPosition, startPosition);
                    selectionStart = endPosition;
                } else {
                    selectBackward(selectionEnd, startPosition);
                    selectionStart = selectionEnd;
                }
                selectionEnd = startPosition;
            } else {
                selectBackward(endPosition, startPosition);
                selectionStart = endPosition;
                selectionEnd = startPosition;
            }
        } else {
            if (isTextSelected()) {
                if (selectionStart < startPosition) {
                    selectForward(selectionStart, endPosition);
                    selectionEnd = endPosition;
                } else {
                    selectForward(startPosition, endPosition);
                    selectionStart = startPosition;
                    selectionEnd = endPosition;
                }
            } else {
                selectForward(startPosition, endPosition);
                selectionStart = startPosition;
                selectionEnd = endPosition;
            }
        }
    }
}
