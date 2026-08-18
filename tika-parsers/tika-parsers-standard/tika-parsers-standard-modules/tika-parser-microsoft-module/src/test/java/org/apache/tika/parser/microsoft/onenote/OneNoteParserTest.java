/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.parser.microsoft.onenote;


import static org.apache.tika.parser.microsoft.onenote.OneNoteParser.ONE_NOTE_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.xml.sax.ContentHandler;

import org.apache.tika.TikaTest;
import org.apache.tika.extractor.EmbeddedDocumentExtractor;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.ToTextContentHandler;

public class OneNoteParserTest extends TikaTest {

    //test recursive parser wrapper for image files

    /**
     * This is the sample document that is automatically created from onenote 2013.
     */
    @Test
    public void testOneNote2013Doc1() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("testOneNote1.one", metadata);
        assertNoJunk(txt);

        List<String> authors = Arrays.asList(metadata.getValues(TikaCoreProperties.CREATOR));
        assertContains("Olya Veselova\u0000", authors);
        assertContains("Microsoft\u0000", authors);
        assertContains("Scott\u0000", authors);
        assertContains("Scott H. W. Snyder\u0000", authors);

        List<String> mostRecentAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "mostRecentAuthors"));
        assertContains("Microsoft\u0000", mostRecentAuthors);

        List<String> originalAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "originalAuthors"));
        assertContains("Microsoft\u0000", originalAuthors);

        assertEquals(Instant.ofEpochSecond(1336059427),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "creationTimestamp"))));
        assertEquals(Instant.ofEpochMilli(1383613114000L),
                Instant.ofEpochMilli(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "lastModifiedTimestamp"))));
        assertEquals(Instant.ofEpochSecond(1446572147),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(TikaCoreProperties.MODIFIED))));
    }

    @Test
    public void testOneNote2013Doc2() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("testOneNote2.one", metadata);
        assertContains("wow this is neat", txt);
        assertContains("neat info about totally killin it bro", txt);
        assertContains("Section1TextArea1", txt);
        assertContains("Section1HeaderTitle", txt);
        assertContains("Section1TextArea2", txt);
        assertNoJunk(txt);

        List<String> authors = Arrays.asList(metadata.getValues(TikaCoreProperties.CREATOR));
        assertContains("Olya Veselova\u0000", authors);
        assertContains("Microsoft\u0000", authors);
        assertContains("Scott\u0000", authors);
        assertContains("Scott H. W. Snyder\u0000", authors);
        assertContains("ndipiazza\u0000", authors);

        List<String> mostRecentAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "mostRecentAuthors"));
        assertContains("ndipiazza\u0000", mostRecentAuthors);
        assertContains("Microsoft\u0000", mostRecentAuthors);

        List<String> originalAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "originalAuthors"));
        assertContains("Microsoft\u0000", originalAuthors);
        assertContains("ndipiazza\u0000", mostRecentAuthors);

        assertEquals(Instant.ofEpochSecond(1336059427),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "creationTimestamp"))));
        assertEquals(Instant.ofEpochMilli(1574426629000L),
                Instant.ofEpochMilli(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "lastModifiedTimestamp"))));
        assertEquals(Instant.ofEpochSecond(1574426628),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(TikaCoreProperties.MODIFIED))));
    }

    @Test
    public void testOneNote2013Doc3() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("testOneNote3.one", metadata);
        assertContains("awesome information about sports or some crap like that.", txt);
        assertContains("Quit doing horrible things to me. Dang you. ", txt);
        assertContains("Section2TextArea1", txt);
        assertContains("Section2HeaderTitle", txt);
        assertContains("Section2TextArea2", txt);
        assertNoJunk(txt);

        List<String> authors = Arrays.asList(metadata.getValues(TikaCoreProperties.CREATOR));
        assertNotContained("Olya Veselova\u0000", authors);
        assertNotContained("Microsoft\u0000", authors);
        assertNotContained("Scott\u0000", authors);
        assertNotContained("Scott H. W. Snyder\u0000", authors);
        assertContains("ndipiazza\u0000", authors);

        List<String> mostRecentAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "mostRecentAuthors"));
        assertContains("ndipiazza\u0000", mostRecentAuthors);
        assertNotContained("Microsoft\u0000", mostRecentAuthors);

        List<String> originalAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "originalAuthors"));
        assertNotContained("Microsoft\u0000", originalAuthors);
        assertContains("ndipiazza\u0000", mostRecentAuthors);

        assertEquals(Instant.ofEpochSecond(1574426349),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "creationTimestamp"))));
        assertEquals(Instant.ofEpochMilli(1574426623000L),
                Instant.ofEpochMilli(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "lastModifiedTimestamp"))));
        assertEquals(Instant.ofEpochSecond(1574426624),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(TikaCoreProperties.MODIFIED))));
    }

    @Test
    public void testOneNote2013Doc4() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("testOneNote4.one", metadata);

        assertContains("way too much information about poptarts to handle.", txt);
        assertContains("Section3TextArea1", txt);
        assertContains("Section3HeaderTitle", txt);
        assertContains("Section3TextArea2", txt);
        assertNoJunk(txt);

        List<String> authors = Arrays.asList(metadata.getValues(TikaCoreProperties.CREATOR));
        assertNotContained("Olya Veselova\u0000", authors);
        assertNotContained("Microsoft\u0000", authors);
        assertNotContained("Scott\u0000", authors);
        assertNotContained("Scott H. W. Snyder\u0000", authors);
        assertContains("ndipiazza\u0000", authors);

        List<String> mostRecentAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "mostRecentAuthors"));
        assertContains("ndipiazza\u0000", mostRecentAuthors);
        assertNotContained("Microsoft\u0000", mostRecentAuthors);

        List<String> originalAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "originalAuthors"));
        assertNotContained("Microsoft\u0000", originalAuthors);
        assertContains("ndipiazza\u0000", mostRecentAuthors);

        assertEquals(Instant.ofEpochSecond(1574426385),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "creationTimestamp"))));
        assertEquals(Instant.ofEpochMilli(1574426548000L),
                Instant.ofEpochMilli(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "lastModifiedTimestamp"))));
        assertEquals(Instant.ofEpochSecond(1574426547),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(TikaCoreProperties.MODIFIED))));
    }

    @Test
    public void testOneNote2016() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("testOneNote2016.one", metadata);

        assertContains("So good", txt);
        assertContains("This is one note 2016", txt);
        assertNoJunk(txt);

        List<String> authors = Arrays.asList(metadata.getValues(TikaCoreProperties.CREATOR));
        assertContains("nicholas dipiazza\u0000", authors);

        List<String> mostRecentAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "mostRecentAuthors"));
        assertContains("nicholas dipiazza\u0000", mostRecentAuthors);

        List<String> originalAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "originalAuthors"));
        assertContains("nicholas dipiazza\u0000", originalAuthors);

        assertEquals(Instant.ofEpochSecond(1576107472),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "creationTimestamp"))));
        assertEquals(Instant.ofEpochMilli(1576107481000L),
                Instant.ofEpochMilli(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "lastModifiedTimestamp"))));
        assertEquals(Instant.ofEpochSecond(1576107480),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(TikaCoreProperties.MODIFIED))));
    }

    @Test
    public void testOneNote2007OrEarlier() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("testOneNote2007OrEarlier.one", metadata);

        // utf-16 LE text
        assertContains(
                "One note is the application.  The notebooks are the files within the application" +
                        ".  " +
                        "Each notebook can have an unlimited amount of sections and pages.  To " +
                        "create a new notebook, go to file, new, computer, " +
                        "and name it.  It will go to my documents, oneNote Notebooks folder.  The" +
                        " notebook doesn't close and you don't have to save.  " +
                        "If it closes, you can go back to it and it will open at the same place " +
                        "you left off.  If you are offline and the notebook is " +
                        "being stored on a sharepoint site, you can work on it and it will sync " +
                        "when you go back online.", txt);
        // ascii text
        assertContains("Correlation between Outlook and OneNote", txt);
    }

    @Test
    public void testOneNoteEmbeddedWordDoc() throws Exception {
        List<Metadata> metadataList = getRecursiveMetadata("testOneNoteEmbeddedWordDoc.one");

        assertTrue(metadataList.stream().anyMatch(
                ml -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(
                        ml.get("Content-Type"))));
    }

    @Test
    public void testOneNoteEmbeddedImage() throws Exception {
        List<byte[]> embedded = new ArrayList<>();
        List<String> embeddedTypes = new ArrayList<>();
        ParseContext context = new ParseContext();
        context.set(EmbeddedDocumentExtractor.class, new EmbeddedDocumentExtractor() {
            @Override
            public boolean shouldParseEmbedded(Metadata metadata) {
                return true;
            }

            @Override
            public void parseEmbedded(InputStream stream, ContentHandler handler,
                                      Metadata metadata, boolean outputHtml) throws IOException {
                embedded.add(stream.readAllBytes());
                embeddedTypes.add(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_TYPE));
            }
        });
        try (InputStream tis = getResourceAsStream("/test-documents/testOneNoteEmbeddedImage.one")) {
            new OneNoteParser().parse(tis, new ToTextContentHandler(), new Metadata(), context);
        }

        assertFalse(embedded.isEmpty());
        assertTrue(embedded.stream().anyMatch(bytes -> bytes.length > 1000),
                () -> "embedded sizes: " + embedded.stream().map(bytes -> bytes.length).collect(Collectors.toList()));
        assertTrue(embeddedTypes.contains("INLINE"));
    }

    /**
     * A real FSSHTTPB section containing a primary image and a derived web rendition. This
     * covers the full OneNoteParser/ParseContext embedded-document path, page ordering, and
     * the deleted storage-index mapping emitted by newer OneNote accounts.
     */
    @Test
    public void testOneNoteFsshttpbImageAndPageOrder() throws Exception {
        List<byte[]> embedded = new ArrayList<>();
        ParseContext context = new ParseContext();
        context.set(EmbeddedDocumentExtractor.class, new EmbeddedDocumentExtractor() {
            @Override
            public boolean shouldParseEmbedded(Metadata metadata) {
                return true;
            }

            @Override
            public void parseEmbedded(InputStream stream, ContentHandler handler,
                                      Metadata metadata, boolean outputHtml) throws IOException {
                embedded.add(stream.readAllBytes());
            }
        });
        Metadata metadata = new Metadata();
        ToTextContentHandler handler = new ToTextContentHandler();
        try (InputStream tis = getResourceAsStream(
                "/test-documents/testOneNoteFsshttpbImage.one")) {
            new OneNoteParser().parse(tis, handler, metadata, context);
        }

        String text = handler.toString();
        assertTrue(text.indexOf("Page 1") < text.indexOf("Pictures or something"));
        assertTrue(text.indexOf("Pictures or something") < text.indexOf("Very wide"));
        assertEquals(1, StringUtils.countMatches(text, "Pictures or something"));
        assertEquals(1, embedded.size());
        assertTrue(embedded.get(0).length > 500_000);
        assertEquals((byte) 0x89, embedded.get(0)[0]);
        assertEquals((byte) 0x50, embedded.get(0)[1]);
        assertEquals((byte) 0x4e, embedded.get(0)[2]);
        assertEquals((byte) 0x47, embedded.get(0)[3]);
    }

    @Test
    public void testOneNoteFsshttpbTableAndListStructure() throws Exception {
        String xml = getXML("testOneNoteFsshttpbStructure.one", new OneNoteParser(),
                new ParseContext()).xml;

        assertContains("<table>", xml);
        assertContains("<tr>", xml);
        assertContains("<td>", xml);
        assertContains("Table cell 1a", xml);
        assertContains("<ul>", xml);
        assertContains("<li>", xml);
        assertContains("Indented bullet", xml);
    }

    /**
     * Test a document pulled from Office 365 which stores the MS-ONESTORE document using the MS-FSSHTTPB
     * protocol.
     */
    @Test
    public void testOneNoteDocumentFromOffice365_1() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("testOneNoteFromOffice365.one", metadata);

        // only the authors of the current content count - authors that only appear in
        // older page version snapshots are not reported
        assertEquals("Du Chang", metadata.get(TikaCoreProperties.CREATOR));
        assertEquals(1, metadata.getValues(ONE_NOTE_PREFIX + "mostRecentAuthors").length);

        assertEquals(Instant.ofEpochSecond(1636621406),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "creationTimestamp"))));
        assertEquals(Instant.ofEpochMilli(1636621448000L),
                Instant.ofEpochMilli(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "lastModifiedTimestamp"))));
        assertEquals(Instant.ofEpochSecond(1636621448),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(TikaCoreProperties.MODIFIED))));
        assertContains("Section1Page1Content", txt);
        assertContains("Section1Page2Content", txt);
        assertTrue(txt.indexOf("Section1Page1Content") <
                txt.indexOf("Section1Page2Content"));
        assertEquals(1, StringUtils.countMatches(txt, "Section1Page2Content"));
    }

    /**
     * Test a document pulled from Office 365 which stores the MS-ONESTORE document using the MS-FSSHTTPB
     * protocol.
     */
    @Test
    public void testOneNoteDocumentFromOffice365_2() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("testOneNoteFromOffice365-2.one", metadata);

        assertEquals("Robert Lucarini", metadata.get(TikaCoreProperties.CREATOR));
        List<String> mostRecentAuthors = Arrays.asList(metadata.getValues(ONE_NOTE_PREFIX + "mostRecentAuthors"));
        assertContains(
                "Robert Lucarini",
                mostRecentAuthors);

        assertEquals(Instant.ofEpochSecond(1591712300),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "creationTimestamp"))));
        assertEquals(Instant.ofEpochMilli(1623597638000L),
                Instant.ofEpochMilli(Long.parseLong(metadata.get(ONE_NOTE_PREFIX + "lastModifiedTimestamp"))));
        assertEquals(Instant.ofEpochSecond(1623597587),
                Instant.ofEpochSecond(Long.parseLong(metadata.get(TikaCoreProperties.MODIFIED))));

        assertContains("Section1Page1Content", txt);
        assertContains("Section1Page2Content", txt);
        assertTrue(txt.indexOf("Section1Page1Content") <
                txt.indexOf("Section1Page2Content"));
        assertEquals(1, StringUtils.countMatches(txt, "Section1Page2Content"));
    }

    private void assertNoJunk(String txt) {
        //Should not include font names in the text
        assertNotContained("Calibri", txt);
        //Should not include UTF-16 property values that are garbage
        assertNotContained("\u5902", txt);
        assertNotContained("\u83F2", txt);
        assertNotContained("\u432F", txt);
        assertNotContained("\u01E1", txt);
    }

    /**
     * TIKA-3970 - test duplicate text.
     */
    @Test
    public void testDupeText() throws Exception {
        Metadata metadata = new Metadata();
        String txt = getText("test-tika-3970-dupetext.one", metadata);

        assertEquals(1, StringUtils.countMatches(txt, "Sunday morning"));
    }

    /**
     * TIKA-4303 - test extract Chinese
     */
    @Test
    public void testExtractChinese() throws Exception {
        Metadata metadata = new Metadata();
        XMLResult xml = getXML("test-tika-4303-Chinese-notes.one", metadata);
        assertContains("<p>中文标题</p>", xml.xml);
    }
}
