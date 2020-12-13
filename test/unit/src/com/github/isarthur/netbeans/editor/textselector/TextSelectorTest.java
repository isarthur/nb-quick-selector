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

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import junit.framework.Test;
import org.netbeans.api.java.lexer.JavaTokenId;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.lexer.Language;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.editor.NbEditorKit;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author: Arthur Sadykov
 */
public class TextSelectorTest extends NbTestCase {

    private static final String JAVA_CLUSTER = "java";
    private static final String IDE_CLUSTER = "ide";
    private static final String EXTIDE_CLUSTER = "extide";
    private static final String TEST_FILE = "Test.java";
    private static final String JAVA_MIME_TYPE = "text/x-java";
    private static final String MIME_TYPE = "mimeType";
    private JEditorPane editor;
    private Document document;
    private TextSelector selector;
    private FileObject testFile;

    public TextSelectorTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return NbModuleSuite.createConfiguration(TextSelectorTest.class)
                .clusters(EXTIDE_CLUSTER)
                .clusters(IDE_CLUSTER)
                .clusters(JAVA_CLUSTER)
                .gui(false)
                .suite();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        clearWorkDir();
        testFile = FileUtil.toFileObject(getWorkDir()).createData(TEST_FILE);
        EditorKit kit = new NbEditorKit();
        editor = new JEditorPane();
        editor.setEditorKit(kit);
        document = editor.getDocument();
        document.putProperty(Document.StreamDescriptionProperty, testFile);
        document.putProperty(MIME_TYPE, JAVA_MIME_TYPE);
        document.putProperty(Language.class, JavaTokenId.language());
        document.putProperty(JavaSource.class, new WeakReference<>(JavaSource.forFileObject(testFile)));
        selector = new TextSelector();
    }

    @Override
    protected boolean runInEQ() {
        return true;
    }

    public void testWhenPreviousTokenIsSemicolonThenSelectStatementInBackwardDirection()
            throws BadLocationException, IOException {
        setText(
                "class Test {\n"
                + "\n"
                + "        void test() {\n"
                + "            repository.create();\n"
                + "            boolean found = records.find(id);\n"
                + "            found = false;\n"
                + "            int count;\n"
                + "            ++count;\n"
                + "            count -= 1;\n"
                + "            boolean valid = count == 0 ? false : found();\n"
                + "            throw new IllegalArgumentException();\n"
                + "            assert true : \"\";\n"
                + "            while (true) {\n"
                + "                break;\n"
                + "                continue;\n"
                + "            }\n"
                + "            List<Object> arrayList = new ArrayList<>();\n"
                + "            return;\n"
                + "        }\n"
                + "    }");
        setCaretPosition(68);
        actionPerformed();
        assertEquals("repository.create();", getSelectedText());
        setCaretPosition(114);
        actionPerformed();
        assertEquals("boolean found = records.find(id);", getSelectedText());
        setCaretPosition(141);
        actionPerformed();
        assertEquals("found = false;", getSelectedText());
        setCaretPosition(164);
        actionPerformed();
        assertEquals("int count;", getSelectedText());
        setCaretPosition(185);
        actionPerformed();
        assertEquals("++count;", getSelectedText());
        setCaretPosition(209);
        actionPerformed();
        assertEquals("count -= 1;", getSelectedText());
        setCaretPosition(267);
        actionPerformed();
        assertEquals("boolean valid = count == 0 ? false : found();", getSelectedText());
        setCaretPosition(317);
        actionPerformed();
        assertEquals("throw new IllegalArgumentException();", getSelectedText());
        setCaretPosition(347);
        actionPerformed();
        assertEquals("assert true : \"\";", getSelectedText());
        setCaretPosition(397);
        actionPerformed();
        assertEquals("break;", getSelectedText());
        setCaretPosition(423);
        actionPerformed();
        assertEquals("continue;", getSelectedText());
        setCaretPosition(493);
        actionPerformed();
        assertEquals("List<Object> arrayList = new ArrayList<>();", getSelectedText());
        setCaretPosition(513);
        actionPerformed();
        assertEquals("return;", getSelectedText());
    }

    public void testWhenPreviousTokenIsSemicolonThenCaretPositionShouldBeAtBeginningOfStatement()
            throws BadLocationException, IOException {
        setText(
                "class Test {\n"
                + "\n"
                + "        void test() {\n"
                + "            repository.create();\n"
                + "            boolean found = records.find(id);\n"
                + "            found = false;\n"
                + "            int count;\n"
                + "            ++count;\n"
                + "            count -= 1;\n"
                + "            boolean valid = count == 0 ? false : found();\n"
                + "            throw new IllegalArgumentException();\n"
                + "            assert true : \"\";\n"
                + "            while (true) {\n"
                + "                break;\n"
                + "                continue;\n"
                + "            }\n"
                + "            List<Object> arrayList = new ArrayList<>();\n"
                + "            return;\n"
                + "        }\n"
                + "    }");
        setCaretPosition(68);
        actionPerformed();
        assertEquals(48, getCaretPosition());
        setCaretPosition(114);
        actionPerformed();
        assertEquals(81, getCaretPosition());
        setCaretPosition(141);
        actionPerformed();
        assertEquals(127, getCaretPosition());
        setCaretPosition(164);
        actionPerformed();
        assertEquals(154, getCaretPosition());
        setCaretPosition(185);
        actionPerformed();
        assertEquals(177, getCaretPosition());
        setCaretPosition(209);
        actionPerformed();
        assertEquals(198, getCaretPosition());
        setCaretPosition(267);
        actionPerformed();
        assertEquals(222, getCaretPosition());
        setCaretPosition(317);
        actionPerformed();
        assertEquals(280, getCaretPosition());
        setCaretPosition(347);
        actionPerformed();
        assertEquals(330, getCaretPosition());
        setCaretPosition(397);
        actionPerformed();
        assertEquals(391, getCaretPosition());
        setCaretPosition(423);
        actionPerformed();
        assertEquals(414, getCaretPosition());
        setCaretPosition(493);
        actionPerformed();
        assertEquals(450, getCaretPosition());
        setCaretPosition(513);
        actionPerformed();
        assertEquals(506, getCaretPosition());
    }

    public void testWhenPreviousTokenIsSemicolonAndTextIsSelectedThenExtendSelectionToBiginningOfStatement()
            throws BadLocationException, IOException {
        setText(
                "class Test {\n"
                + "\n"
                + "        void test() {\n"
                + "            repository.create();\n"
                + "            boolean found = records.find(id);\n"
                + "            found = false;\n"
                + "            int count;\n"
                + "            ++count;\n"
                + "            count -= 1;\n"
                + "            boolean valid = count == 0 ? false : found();\n"
                + "            throw new IllegalArgumentException();\n"
                + "            assert true : \"\";\n"
                + "            while (true) {\n"
                + "                break;\n"
                + "                continue;\n"
                + "            }\n"
                + "            List<Object> arrayList = new ArrayList<>();\n"
                + "            return;\n"
                + "        }\n"
                + "    }");
        setCaretPosition(513);
        actionPerformed();
        assertEquals("return;", getSelectedText());
        actionPerformed();
        assertEquals(""
                + "List<Object> arrayList = new ArrayList<>();\n"
                + "            return;",
                getSelectedText());
    }

    private void setText(String text) throws BadLocationException, IOException {
        document.insertString(0, text, null);
        try ( OutputStream out = testFile.getOutputStream();  Writer writer = new OutputStreamWriter(out)) {
            writer.append(text);
        }
    }

    private void setCaretPosition(int position) {
        editor.setCaretPosition(position);
    }

    private void actionPerformed() {
        selector.actionPerformed(new ActionEvent(editor, ActionEvent.ACTION_PERFORMED, ""), editor);
    }

    private String getSelectedText() {
        return editor.getSelectedText();
    }

    private int getCaretPosition() {
        return editor.getCaretPosition();
    }
}
