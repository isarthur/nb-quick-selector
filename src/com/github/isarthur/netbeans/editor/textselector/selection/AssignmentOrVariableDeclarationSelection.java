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
import com.sun.source.tree.Tree;
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
public class AssignmentOrVariableDeclarationSelection extends Selection {

    public AssignmentOrVariableDeclarationSelection(JTextComponent editor, TokenSequence<?> tokenSequnce,
            int selectionStart, int selectionEnd, Direction direction, CompilationController controller) {
        super(editor, tokenSequnce, selectionStart, selectionEnd, direction, controller);
    }

    @Override
    public void select() {
        TreeUtilities treeUtilities = controller.getTreeUtilities();
        TreePath primitiveTypePath = treeUtilities.pathFor(tokenSequence.offset() + 1);
        if (primitiveTypePath == null) {
            return;
        }
        Tree primitiveTypeTree = primitiveTypePath.getLeaf();
        Trees trees = controller.getTrees();
        SourcePositions sourcePositions = trees.getSourcePositions();
        CompilationUnitTree compilationUnitTree = controller.getCompilationUnit();
        long startPosition = sourcePositions.getStartPosition(compilationUnitTree, primitiveTypeTree);
        long endPosition = sourcePositions.getEndPosition(compilationUnitTree, primitiveTypeTree);
        select((int) startPosition, (int) endPosition);
    }
}
