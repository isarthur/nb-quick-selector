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
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.TreeUtilities;
import org.netbeans.api.lexer.TokenSequence;

/**
 *
 * @author Arthur Sadykov
 */
public class IdentifierSelection extends Selection {

    public IdentifierSelection(JTextComponent editor, TokenSequence<?> ts, int selectionStart, int selectionEnd,
            Direction direction, CompilationController controller) {
        super(editor, ts, selectionStart, selectionEnd, direction, controller);
    }

    @Override
    public void select() {
        TreeUtilities treeUtilities = controller.getTreeUtilities();
        TreePath path = treeUtilities.pathFor(tokenSequence.offset() + 1);
        if (path == null) {
            return;
        }
        Tree tree = path.getLeaf();
        Trees trees = controller.getTrees();
        SourcePositions sourcePositions = trees.getSourcePositions();
        CompilationUnitTree compilationUnitTree = controller.getCompilationUnit();
        long startPosition;
        long endPosition;
        switch (tree.getKind()) {
            case BREAK:
                BreakTree breakTree = (BreakTree) tree;
                int[] breakNameSpan = treeUtilities.findNameSpan(breakTree);
                if (breakNameSpan[0] == tokenSequence.offset()) {
                    startPosition = breakNameSpan[0];
                    endPosition = breakNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            case CLASS:
                ClassTree classTree = (ClassTree) tree;
                int[] classNameSpan = treeUtilities.findNameSpan(classTree);
                if (classNameSpan[0] == tokenSequence.offset()) {
                    startPosition = classNameSpan[0];
                    endPosition = classNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            case CONTINUE:
                ContinueTree continueTree = (ContinueTree) tree;
                int[] continueNameSpan = treeUtilities.findNameSpan(continueTree);
                if (continueNameSpan[0] == tokenSequence.offset()) {
                    startPosition = continueNameSpan[0];
                    endPosition = continueNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            case LABELED_STATEMENT:
                LabeledStatementTree labeledStatementTree = (LabeledStatementTree) tree;
                int[] labeledStatementNameSpan = treeUtilities.findNameSpan(labeledStatementTree);
                if (labeledStatementNameSpan[0] == tokenSequence.offset()) {
                    startPosition = labeledStatementNameSpan[0];
                    endPosition = labeledStatementNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            case MEMBER_REFERENCE:
                MemberReferenceTree memberReferenceTree = (MemberReferenceTree) tree;
                int[] memberReferenceNameSpan = treeUtilities.findNameSpan(memberReferenceTree);
                if (memberReferenceNameSpan[0] == tokenSequence.offset()) {
                    startPosition = memberReferenceNameSpan[0];
                    endPosition = memberReferenceNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            case MEMBER_SELECT:
                MemberSelectTree memberSelectTree = (MemberSelectTree) tree;
                int[] memberSelectNameSpan = treeUtilities.findNameSpan(memberSelectTree);
                if (memberSelectNameSpan[0] == tokenSequence.offset()) {
                    startPosition = memberSelectNameSpan[0];
                    endPosition = memberSelectNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            case METHOD:
                MethodTree methodTree = (MethodTree) tree;
                int[] methodTreeNameSpan = treeUtilities.findNameSpan(methodTree);
                if (methodTreeNameSpan[0] == tokenSequence.offset()) {
                    startPosition = methodTreeNameSpan[0];
                    endPosition = methodTreeNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            case TYPE_PARAMETER:
                TypeParameterTree typeParameterTree = (TypeParameterTree) tree;
                int[] typeParameterNameNameSpan = treeUtilities.findNameSpan(typeParameterTree);
                if (typeParameterNameNameSpan[0] == tokenSequence.offset()) {
                    startPosition = typeParameterNameNameSpan[0];
                    endPosition = typeParameterNameNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            case VARIABLE:
                VariableTree variableTree = (VariableTree) tree;
                int[] variableNameSpan = treeUtilities.findNameSpan(variableTree);
                if (variableNameSpan[0] == tokenSequence.offset()) {
                    startPosition = variableNameSpan[0];
                    endPosition = variableNameSpan[1];
                } else {
                    startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                    endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
                }
                break;
            default:
                startPosition = sourcePositions.getStartPosition(compilationUnitTree, tree);
                endPosition = sourcePositions.getEndPosition(compilationUnitTree, tree);
        }
        select((int) startPosition, (int) endPosition);
    }
}
