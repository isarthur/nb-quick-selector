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

    public void testAbstractKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public abstract class Test {\n"
                + "\n"
                + "        public abstract void test();\n"
                + "    }");
        setCaretPosition(53);
        actionPerformed();
        assertEquals("abstract", getSelectedText());
    }

    public void testAbstractKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public abstract class Test {\n"
                + "\n"
                + "        public abstract void test();\n"
                + "    }");
        setCaretPosition(45);
        actionPerformed();
        assertEquals("abstract", getSelectedText());
    }

    public void testDefaultKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public interface Test {\n"
                + "\n"
                + "        default void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(40);
        actionPerformed();
        assertEquals("default", getSelectedText());
    }

    public void testDefaultKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public interface Test {\n"
                + "\n"
                + "        default void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(33);
        actionPerformed();
        assertEquals("default", getSelectedText());
    }

    public void testFinalKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public final void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(41);
        actionPerformed();
        assertEquals("final", getSelectedText());
    }

    public void testFinalKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public final void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(36);
        actionPerformed();
        assertEquals("final", getSelectedText());
    }

    public void testNativeKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public native int square(int i);\n"
                + "    }");
        setCaretPosition(42);
        actionPerformed();
        assertEquals("native", getSelectedText());
    }

    public void testNativeKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public native int square(int i);\n"
                + "    }");
        setCaretPosition(36);
        actionPerformed();
        assertEquals("native", getSelectedText());
    }

    public void testPrivateKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        private void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(36);
        actionPerformed();
        assertEquals("private", getSelectedText());
    }

    public void testPrivateKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        private void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(29);
        actionPerformed();
        assertEquals("private", getSelectedText());
    }

    public void testProtectedKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        protected void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(38);
        actionPerformed();
        assertEquals("protected", getSelectedText());
    }

    public void testProtectedKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        protected void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(29);
        actionPerformed();
        assertEquals("protected", getSelectedText());
    }

    public void testPublicKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(35);
        actionPerformed();
        assertEquals("public", getSelectedText());
    }

    public void testPublicKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(29);
        actionPerformed();
        assertEquals("public", getSelectedText());
    }

    public void testStaticKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public static void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(42);
        actionPerformed();
        assertEquals("static", getSelectedText());
    }

    public void testStaticKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public static void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(36);
        actionPerformed();
        assertEquals("static", getSelectedText());
    }

    public void testStrictfpKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public strictfp void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(44);
        actionPerformed();
        assertEquals("strictfp", getSelectedText());
    }

    public void testStrictfpKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public strictfp void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(36);
        actionPerformed();
        assertEquals("strictfp", getSelectedText());
    }

    public void testSynchronizedKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public synchronized void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(48);
        actionPerformed();
        assertEquals("synchronized", getSelectedText());
    }

    public void testSynchronizedKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public synchronized void test() {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(36);
        actionPerformed();
        assertEquals("synchronized", getSelectedText());
    }

    public void testTransientKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        private transient String dontSaveMe;\n"
                + "    }");
        setCaretPosition(46);
        actionPerformed();
        assertEquals("transient", getSelectedText());
    }

    public void testTransientKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        private transient String dontSaveMe;\n"
                + "    }");
        setCaretPosition(37);
        actionPerformed();
        assertEquals("transient", getSelectedText());
    }

    public void testVolatileKeywordSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        static volatile boolean ready;\n"
                + "    }");
        setCaretPosition(44);
        actionPerformed();
        assertEquals("volatile", getSelectedText());
    }

    public void testVolatileKeywordSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        static volatile boolean ready;\n"
                + "    }");
        setCaretPosition(36);
        actionPerformed();
        assertEquals("volatile", getSelectedText());
    }

    public void testIntegerLiteralSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                count = 1024;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(104);
        actionPerformed();
        assertEquals("1024", getSelectedText());
    }

    public void testIntegerLiteralSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                count = 1024;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(100);
        actionPerformed();
        assertEquals("1024", getSelectedText());
    }

    public void testCharLiteralSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                ch = 'a';\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(100);
        actionPerformed();
        assertEquals("'a'", getSelectedText());
    }

    public void testCharLiteralSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                ch = 'a';\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(97);
        actionPerformed();
        assertEquals("'a'", getSelectedText());
    }

    public void testDoubleLiteralSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                count = 1024.5;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(106);
        actionPerformed();
        assertEquals("1024.5", getSelectedText());
    }

    public void testDoubleLiteralSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                count = 1024.5;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(100);
        actionPerformed();
        assertEquals("1024.5", getSelectedText());
    }

    public void testFloatLiteralSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                count = 1024.5F;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(107);
        actionPerformed();
        assertEquals("1024.5F", getSelectedText());
    }

    public void testFloatLiteralSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                count = 1024.5F;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(100);
        actionPerformed();
        assertEquals("1024.5F", getSelectedText());
    }

    public void testLongLiteralSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                count = 1024L;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(105);
        actionPerformed();
        assertEquals("1024L", getSelectedText());
    }

    public void testLongLiteralSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                count = 1024L;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(100);
        actionPerformed();
        assertEquals("1024L", getSelectedText());
    }

    public void testFalseSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                valid = false;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(105);
        actionPerformed();
        assertEquals("false", getSelectedText());
    }

    public void testFalseSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                valid = false;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(100);
        actionPerformed();
        assertEquals("false", getSelectedText());
    }

    public void testTrueSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                valid = true;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(104);
        actionPerformed();
        assertEquals("true", getSelectedText());
    }

    public void testTrueSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                valid = true;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(100);
        actionPerformed();
        assertEquals("true", getSelectedText());
    }

    public void testNullSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                file = null;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(103);
        actionPerformed();
        assertEquals("null", getSelectedText());
    }

    public void testNullSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s >= t) {\n"
                + "                file = null;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(99);
        actionPerformed();
        assertEquals("null", getSelectedText());
    }

    public void testWhenPreviousTokenIsStringLiteralThenSelectItIncludingQuotes()
            throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public static void test() {\n"
                + "            repository.create(\"string literal\");\n"
                + "        }\n"
                + "    }");
        setCaretPosition(103);
        actionPerformed();
        assertEquals("\"string literal\"", getSelectedText());
    }

    public void testWhenPreviousTokenIsStringLiteralThenCaretShouldBeOnTheLeftSideOfLiteral()
            throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public static void test() {\n"
                + "            repository.create(\"string literal\");\n"
                + "        }\n"
                + "    }");
        setCaretPosition(103);
        actionPerformed();
        assertEquals(87, getCaretPosition());
    }

    public void testWhenNextTokenIsStringLiteralThenSelectItIncludingQuotes()
            throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public static void test() {\n"
                + "            repository.create(\"string literal\");\n"
                + "        }\n"
                + "    }");
        setCaretPosition(87);
        actionPerformed();
        assertEquals("\"string literal\"", getSelectedText());
    }

    public void testWhenNextTokenIsStringLiteralThenCaretShouldBeOnTheRightSideOfLiteral()
            throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public static void test() {\n"
                + "            repository.create(\"string literal\");\n"
                + "        }\n"
                + "    }");
        setCaretPosition(87);
        actionPerformed();
        assertEquals(103, getCaretPosition());
    }

    public void testWhenCurrentTokenIsStringLiteralThenSelectItExcludingQuotes()
            throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public static void test() {\n"
                + "            repository.create(\"string literal\");\n"
                + "        }\n"
                + "    }");
        setCaretPosition(90);
        actionPerformed();
        assertEquals("string literal", getSelectedText());
    }

    public void testWhenCurrentTokenIsStringLiteralThenCaretShouldBeBeforeClosingQuote()
            throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public static void test() {\n"
                + "            repository.create(\"string literal\");\n"
                + "        }\n"
                + "    }");
        setCaretPosition(90);
        actionPerformed();
        assertEquals(102, getCaretPosition());
    }

    public void testVariableDeclarationSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            Document document = editor.getDocument();\n"
                + "        }\n"
                + "    }");
        setCaretPosition(81);
        actionPerformed();
        assertEquals("Document document = editor.getDocument();", getSelectedText());
    }

    public void testVariableDeclarationSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            Document document = editor.getDocument();\n"
                + "        }\n"
                + "    }");
        setCaretPosition(80);
        actionPerformed();
        assertEquals("Document document = editor.getDocument();", getSelectedText());
    }

    public void testAssignmentSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            document = otherDocument;\n"
                + "        }\n"
                + "    }");
        setCaretPosition(72);
        actionPerformed();
        assertEquals("document = otherDocument", getSelectedText());
    }

    public void testAssignmentSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            document = otherDocument;\n"
                + "        }\n"
                + "    }");
        setCaretPosition(71);
        actionPerformed();
        assertEquals("document = otherDocument", getSelectedText());
    }

    public void testBlockSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "    public void test() {\n"
                + "    }\n"
                + "}");
        setCaretPosition(50);
        actionPerformed();
        assertEquals(
                "{\n"
                + "    }",
                getSelectedText());
    }

    public void testBlockSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "    public void test() {\n"
                + "    }\n"
                + "}");
        setCaretPosition(43);
        actionPerformed();
        assertEquals(
                "{\n"
                + "    }",
                getSelectedText());
    }

    public void testIfBlockSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s != t) {\n"
                + "                t = s;\n"
                + "            } else {\n"
                + "                s = t;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(64);
        actionPerformed();
        assertEquals(
                "if (s != t) {\n"
                + "                t = s;\n"
                + "            }",
                getSelectedText());
    }

    public void testIfBlockSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s != t) {\n"
                + "                t = s;\n"
                + "            } else {\n"
                + "                s = t;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(62);
        actionPerformed();
        assertEquals(
                "if (s != t) {\n"
                + "                t = s;\n"
                + "            }",
                getSelectedText());
    }

    public void testElseBlockSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s != t) {\n"
                + "                t = s;\n"
                + "            } else {\n"
                + "                s = t;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(117);
        actionPerformed();
        assertEquals(
                "else {\n"
                + "                s = t;\n"
                + "            }",
                getSelectedText());
    }

    public void testElseBlockSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s != t) {\n"
                + "                t = s;\n"
                + "            } else {\n"
                + "                s = t;\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(113);
        actionPerformed();
        assertEquals(
                "else {\n"
                + "                s = t;\n"
                + "            }",
                getSelectedText());
    }

    public void testElseIfBlockSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "    public void test() {\n"
                + "        if (s != t) {\n"
                + "            t = s;\n"
                + "        } else if (s > t) {\n"
                + "            t++;\n"
                + "        } else if (s < t) {\n"
                + "            t = s + s;\n"
                + "        } else {\n"
                + "            s = t;\n"
                + "        }\n"
                + "    }\n"
                + "}");
        setCaretPosition(100);
        actionPerformed();
        assertEquals(
                "else if (s > t) {\n"
                + "            t++;\n"
                + "        }",
                getSelectedText());
    }

    public void testElseIfBlockSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "    public void test() {\n"
                + "        if (s != t) {\n"
                + "            t = s;\n"
                + "        } else if (s > t) {\n"
                + "            t++;\n"
                + "        } else if (s < t) {\n"
                + "            t = s + s;\n"
                + "        } else {\n"
                + "            s = t;\n"
                + "        }\n"
                + "    }\n"
                + "}");
        setCaretPosition(96);
        actionPerformed();
        assertEquals(
                "else if (s > t) {\n"
                + "            t++;\n"
                + "        }",
                getSelectedText());
    }

    public void testRightParenthesisSelectionOfIfStatementInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s != t) {\n"
                + "                t = s;\n"
                + "            }n"
                + "        }\n"
                + "    }");
        setCaretPosition(73);
        actionPerformed();
        assertEquals("(s != t)", getSelectedText());
    }

    public void testLeftParenthesisSelectionOfIfStatementInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            if (s != t) {\n"
                + "                t = s;\n"
                + "            }n"
                + "        }\n"
                + "    }");
        setCaretPosition(65);
        actionPerformed();
        assertEquals("(s != t)", getSelectedText());
    }

    public void testRightParenthesisSelectionOfMethodInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test(String name, int count) {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(69);
        actionPerformed();
        assertEquals("(String name, int count)", getSelectedText());
    }

    public void testLeftParenthesisSelectionOfMethodInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test(String name, int count) {\n"
                + "        }\n"
                + "    }");
        setCaretPosition(41);
        actionPerformed();
        actionPerformed();
        assertEquals("test(String name, int count)", getSelectedText());
    }

    public void testRightParenthesisSelectionOfMethodInvocationInBackwardDirection()
            throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            test(name, count);\n"
                + "        }\n"
                + "    }");
        setCaretPosition(79);
        actionPerformed();
        assertEquals("(name, count)", getSelectedText());
    }

    public void testLeftParenthesisSelectionOfMethodInvocationInForwardDirection()
            throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            test(name, count);\n"
                + "        }\n"
                + "    }");
        setCaretPosition(62);
        actionPerformed();
        actionPerformed();
        assertEquals("test(name, count)", getSelectedText());
    }

    public void testWhileStatementSelectionInBackwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            while (true) {\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(67);
        actionPerformed();
        assertEquals(
                "while (true) {\n"
                + "            }",
                getSelectedText());
    }

    public void testWhileStatementSelectionInForwardDirection() throws BadLocationException, IOException {
        setText(
                "public class Test {\n"
                + "\n"
                + "        public void test() {\n"
                + "            while (true) {\n"
                + "            }\n"
                + "        }\n"
                + "    }");
        setCaretPosition(62);
        actionPerformed();
        assertEquals(
                "while (true) {\n"
                + "            }",
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
