/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.apps.rasterizer;

import org.apache.batik.test.*;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Validates the operation of the <tt>SVGRasterizer</tt>.
 * It validates the option setting and the manipulation
 * of source and destination sources.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @version $Id$
 */
public class SVGConverterTest extends DefaultTestSuite {
    public SVGConverterTest(){
        ///////////////////////////////////////////////////////////////////////
        // Add configuration tests
        ///////////////////////////////////////////////////////////////////////
        AbstractTest t = null;

        //
        // Test Trancoder usage
        //
        t = new TranscoderConfigTest(DestinationType.PNG,
                                     org.apache.batik.transcoder.image.PNGTranscoder.class);
        addTest(t);
        t.setId("TranscoderConfigTest.PNG");

        t = new TranscoderConfigTest(DestinationType.JPEG,
                                     org.apache.batik.transcoder.image.JPEGTranscoder.class);
        addTest(t);
        t.setId("TranscoderConfigTest.JPEG");

        t = new TranscoderConfigTest(DestinationType.TIFF,
                                     org.apache.batik.transcoder.image.TIFFTranscoder.class);
        addTest(t);
        t.setId("TranscoderConfigTest.TIFF");

        //
        // Checks that the proper hints are used
        //
        t = new HintsConfigTest(new Object[][]{ 
            {ImageTranscoder.KEY_WIDTH, new Float(40)},
            {ImageTranscoder.KEY_HEIGHT, new Float(80)},
            {ImageTranscoder.KEY_AOI, new Rectangle(40, 50, 40, 80)}}){
                protected void deltaConfigure(SVGConverter c){
                    c.setArea(new Rectangle(40, 50, 40, 80));
                }
            };
        
        addTest(t);
        t.setId("HintsConfigTest.KEY_AOI");
        
        t = new HintsConfigTest(new Object[][]{
            {JPEGTranscoder.KEY_QUALITY, new Float(.5)}}){
                protected void deltaConfigure(SVGConverter c){
                    c.setQuality(.5f);
                }
            };

        addTest(t);
        t.setId("HintsConfigTest.KEY_QUALITY");

        t = new HintsConfigTest(new Object[][]{
            {ImageTranscoder.KEY_BACKGROUND_COLOR, Color.pink}}){
                protected void deltaConfigure(SVGConverter c){
                    c.setBackgroundColor(Color.pink);
                }
            };

        addTest(t);
        t.setId("HintsConfigTest.KEY_BACKGROUND_COLOR");

        t = new HintsConfigTest(new Object[][]{
            {ImageTranscoder.KEY_HEIGHT, new Float(50)}}){
                protected void deltaConfigure(SVGConverter c){
                    c.setHeight(50);
                }
            };

        addTest(t);
        t.setId("HintsConfigTest.KEY_HEIGHT");

        t = new HintsConfigTest(new Object[][]{
            {ImageTranscoder.KEY_WIDTH, new Float(50)}}){
                protected void deltaConfigure(SVGConverter c){
                    c.setWidth(50);
                }
            };

        addTest(t);
        t.setId("HintsConfigTest.KEY_WIDTH");

        t = new HintsConfigTest(new Object[][]{
            {ImageTranscoder.KEY_MEDIA, "print"}}){
                protected void deltaConfigure(SVGConverter c){
                    c.setMediaType("print");
                }
            };

        addTest(t);
        t.setId("HintsConfigTest.KEY_MEDIA");

        t = new HintsConfigTest(new Object[][]{
            {ImageTranscoder.KEY_ALTERNATE_STYLESHEET, "myStyleSheet"}}){
                protected void deltaConfigure(SVGConverter c){
                    c.setAlternateStylesheet("myStyleSheet");
                }
            };
        addTest(t);
        t.setId("HintsConfigTest.KEY_ALTERNATE_STYLESHEET");


        //
        // Check sources
        //
        t = new SourcesConfigTest(new String[] { "samples/anne", "samples/batikFX", "samples/tests/spec/styling/smiley" }){
                protected void setSources(SVGConverter c){
                    c.setSourcesStrings(new String[] {"samples/anne.svg", "samples/batikFX.svg", "samples/tests/spec/styling/smiley.svg"});
                }
            };
            
        addTest(t);
        t.setId("SourcesConfigTest.SimpleList");

        t = new SourcesConfigTest(new String[] 
            { "samples/tests/spec/coordinates/em", 
              "samples/tests/spec/coordinates/percentagesAndUnits" }) {
                protected void setSources(SVGConverter c){
                    c.setSrcDir(new File("samples/tests/spec/coordinates"));
                }
            };
        addTest(t);
        t.setId("SourcesConfigTest.SrcDir");

        //
        // Check destination
        //
        t = new DestConfigTest(new String[] { "samples/anne.png" },
                               new String[] { "test-reports/anne.png"}){
                protected void setDestination(SVGConverter c){
                    c.setDstFile(new File("test-reports/anne.png"));
                }
            };
        addTest(t);
        t.setId("DestConfigTest.DstFile");

        t = new DestConfigTest(new String[] { "samples/anne.svg", "samples/tests/spec/styling/smiley.svg" },
                               new String[] { "test-resources/anne.png", "test-resources/smiley.png"}){
                protected void setDestination(SVGConverter c){
                    c.setDstDir(new File("test-resources"));
                }
            };
        addTest(t);
        t.setId("DestConfigTest.DstDir");

        ///////////////////////////////////////////////////////////////////////
        // Add configuration error test. These tests check that the expected
        // error gets reported for a given mis-configuration
        ///////////////////////////////////////////////////////////////////////
        t = new ConfigErrorTest(SVGConverter.ERROR_NO_SVG_FILES_IN_SRC_DIR) {
                protected void configure(SVGConverter c){
                    c.setSourcesStrings(null);
                    c.setSrcDir(new File("test-resources"));
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_NO_FILES_IN_SRC_DIR");

        t = new ConfigErrorTest(SVGConverter.ERROR_NO_SRCDIR_OR_SRCFILE_SPECIFIED){
                protected void configure(SVGConverter c){
                    c.setSourcesStrings(null);
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_NO_SRCDIR_OR_SRCFILE_SPECIFIED");

        t = new ConfigErrorTest(SVGConverter.ERROR_CANNOT_COMPUTE_DESTINATION){
                protected void configure(SVGConverter c){
                    // Do not set destination file or destination directory
                    c.setSourcesStrings(new String[]{"http://xml.apache.org/batik/dummy.svg"});
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_CANNOT_COMPUTE_DESTINATION");

        t = new ConfigErrorTest(SVGConverter.ERROR_CANNOT_ACCESS_TRANSCODER){
                protected void configure(SVGConverter c){
                    c.setDestinationType(DestinationType.PDF);
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_CANNOT_ACCESS_TRANCODER");
        
        t = new ConfigErrorTest(SVGConverter.ERROR_SOURCE_SAME_AS_DESTINATION){
                protected void configure(SVGConverter c){
                    c.setSourcesStrings(new String[]{ "samples/anne.svg" });
                    c.setDstFile(new File("samples/anne.svg"));
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_SOURCE_SAME_AS_DESTINATION");

        t = new ConfigErrorTest(SVGConverter.ERROR_CANNOT_READ_SOURCE){
                protected void configure(SVGConverter c){
                    c.setSourcesStrings(new String[]{ "test-resources/org/apache/batik/apps/rasterizer/notReadable.svg" });
                    c.setDstDir(new File("test-reports"));
                }

                public boolean proceedWithSourceTranscoding(SVGConverterSource source,
                                                            File dest){
                    // Big hack to simulate a non-readable SVG file
                    File hackedFile = new File(((SVGConverterFileSource)source).file.getPath()){
                            public boolean canRead(){
                                System.out.println("Yahoooooooo! In canRead");
                                return false;
                            }
                        };
                    ((SVGConverterFileSource)source).file = hackedFile;
                    return true;
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_CANNOT_READ_SOURCE");

        t = new ConfigErrorTest(SVGConverter.ERROR_CANNOT_OPEN_SOURCE){
                protected void configure(SVGConverter c){
                    c.setSourcesStrings(new String[]{ "test-resources/org/apache/batik/apps/rasterizer/notReadable.svg" });
                }

                public boolean proceedWithComputedTask(Transcoder transcoder,
                                                       Map hints,
                                                       Vector sources,
                                                       Vector dest){
                    System.out.println("==================> Hacked Starting to process Task <=========================");
                    SVGConverterFileSource source = (SVGConverterFileSource)sources.elementAt(0);
                    source = new SVGConverterFileSource(source.file){
                            public InputStream openStream() throws FileNotFoundException {
                                throw new FileNotFoundException("Simulated FileNotFoundException");
                            }
                        };

                    sources.setElementAt(source, 0);
                    return true;
                }

            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_CANNOT_OPEN_SOURCE");

        t = new ConfigErrorTest(SVGConverter.ERROR_OUTPUT_NOT_WRITEABLE){
                protected void configure(SVGConverter c){
                    c.setSourcesStrings(new String[]{ "samples/anne.svg" });
                    c.setDstFile(new File("test-resources/org/apache/batik/apps/rasterizer/readOnly.png"));
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_OUTPUT_NOT_WRITEABLE");
                   
        t = new ConfigErrorTest(SVGConverter.ERROR_UNABLE_TO_CREATE_OUTPUT_DIR){
                protected void configure(SVGConverter c){
                    c.setDstDir(new File("samples/anne.svg"));
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest.ERROR_UNABLE_TO_CREATE_OUTPUT_DIR");

        t = new ConfigErrorTest(SVGConverter.ERROR_WHILE_RASTERIZING_FILE){
                protected void configure(SVGConverter c){
                    c.setSourcesStrings(new String[]{ "test-resources/org/apache/batik/apps/rasterizer/invalidSVG.svg"});
                }
            };
        addTest(t);
        t.setId("ConfigErrorTest(SVGConverter.ERROR_WHILE_RASTERIZING_FILE");
        
        
    }
}

/**
 * A ConfigTest builds an SVGConverter, configures it,
 * sets itself as the SVGConverterController and checks that
 * the computed task is as expected (i.e., right set of 
 * hints).
 */
abstract class AbstractConfigTest extends AbstractTest implements SVGConverterController {
    /**
     * The 'proceedWithComputedTask' handler was not called
     */
    public static final String ERROR_NO_COMPUTED_TASK
        = "ConfigTest.error.no.computed.task";

    /**
     * The transcoderClass is not the one expected.
     */
    public static final String ERROR_UNEXPECTED_TRANSCODER_CLASS
        = "ConfigTest.error.unexpected.transcoder.class";

    public static final String ENTRY_KEY_EXPECTED_TRANSCODER_CLASS
        = "ConfigTest.entry.key.expected.transcoder.class";

    public static final String ENTRY_KEY_COMPUTED_TRANSCODER_CLASS
        = "ConfigTest.entry.key.computed.trancoder.class";

    /**
     * Error if the hints do not match
     */
    public static final String ERROR_UNEXPECTED_NUMBER_OF_HINTS
        = "ConfigTest.error.unexpected.number.of.hints";

    public static final String ENTRY_KEY_EXPECTED_NUMBER_OF_HINTS
        = "ConfigTest.entry.key.expected.number.of.hints";

    public static final String ENTRY_KEY_COMPUTED_NUMBER_OF_HINTS
        = "ConfigTest.entry.key.computed.number.of.hints";

    public static final String ENTRY_KEY_EXPECTED_HINTS
        = "ConfigTest.entry.key.expected.hints";

    public static final String ENTRY_KEY_COMPUTED_HINTS
        = "ConfigTest.entry.key.computed.hints";

    public static final String ERROR_UNEXPECTED_TRANSCODING_HINT
        = "ConfigTest.error.unexpected.transcoding.hint";

    public static final String ENTRY_KEY_EXPECTED_HINT_KEY
        = "ConfigTest.entry.key.expected.hint.key";

    public static final String ENTRY_KEY_COMPUTED_HINT_VALUE
        = "ConfigTest.entry.key.computed.hint.value";

    public static final String ENTRY_KEY_EXPECTED_HINT_VALUE
        = "ConfigTest.entry.key.expected.hint.value";

    /**
     * Error if the sources do not match
     */
    public static final String ERROR_UNEXPECTED_SOURCES_LIST
        = "ConfigTest.error.unexpected.sources.list";

    public static final String ENTRY_KEY_EXPECTED_NUMBER_OF_SOURCES
        = "ConfigTest.entry.key.expected.number.of.sources";

    public static final String ENTRY_KEY_COMPUTED_NUMBER_OF_SOURCES
        = "ConfigTest.entry.key.computed.number.of.sources";

    public static final String ENTRY_KEY_EXPECTED_SOURCE_AT_INDEX
        = "ConfigTest.entry.key.expected.source.at.index";

    public static final String ENTRY_KEY_COMPUTED_SOURCE_AT_INDEX
        = "ConfigTest.entry.key.computed.source.at.index";

    public static final String ENTRY_KEY_COMPUTED_SOURCES_LIST
        = "ConfigTest.entry.key.computed.sources.list";

    public static final String ENTRY_KEY_EXPECTED_SOURCES_LIST
        = "ConfigTest.entry.key.expected.sources.list";

    /**
     * Error if the dest do not match
     */
    public static final String ERROR_UNEXPECTED_DEST_LIST
        = "ConfigTest.error.unexpected.dest.list";

    public static final String ENTRY_KEY_EXPECTED_NUMBER_OF_DEST
        = "ConfigTest.entry.key.expected.number.of.dest";

    public static final String ENTRY_KEY_COMPUTED_NUMBER_OF_DEST
        = "ConfigTest.entry.key.computed.number.of.dest";

    public static final String ENTRY_KEY_EXPECTED_DEST_AT_INDEX
        = "ConfigTest.entry.key.expected.dest.at.index";

    public static final String ENTRY_KEY_COMPUTED_DEST_AT_INDEX
        = "ConfigTest.entry.key.computed.dest.at.index";

    public static final String ENTRY_KEY_COMPUTED_DEST_LIST
        = "ConfigTest.entry.key.computed.dest.list";

    public static final String ENTRY_KEY_EXPECTED_DEST_LIST
        = "ConfigTest.entry.key.expected.dest.list";


    /**
     * Configuration Description
     */
    static class Config {
        Class transcoderClass;
        HashMap hints;
        Vector sources, dest;
    }
            
    protected Config expectedConfig;
    protected Config computedConfig;

    protected AbstractConfigTest(){
    }

    protected void setExpectedConfig(Config expectedConfig){
        this.expectedConfig = expectedConfig;
    }

    protected abstract void configure(SVGConverter c);

    protected String makeSourceList(Vector v){
        int n = v.size();
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<n; i++){
            sb.append(v.elementAt(i).toString());
        }

        return sb.toString();
    }

    protected String makeHintsString(HashMap map){
        Iterator iter = map.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iter.hasNext()){
            Object key = iter.next();
            sb.append(key.toString());
            sb.append("(");
            sb.append(map.get(key).toString());
            sb.append(") -- ");
        }

        return sb.toString();
    }


    public TestReport runImpl() throws Exception {
        SVGConverter c = new SVGConverter(this);
        configure(c);
        c.execute();

        //
        // Now, check that the expectedConfig and the 
        // computedConfig are identical
        //
        if (computedConfig == null){
            return reportError(ERROR_NO_COMPUTED_TASK);
        }

        if (!expectedConfig.transcoderClass.equals
            (computedConfig.transcoderClass)){
            TestReport report = reportError(ERROR_UNEXPECTED_TRANSCODER_CLASS);
            report.addDescriptionEntry(ENTRY_KEY_EXPECTED_TRANSCODER_CLASS,
                                       expectedConfig.transcoderClass);
            report.addDescriptionEntry(ENTRY_KEY_COMPUTED_TRANSCODER_CLASS,
                                       computedConfig.transcoderClass);

            return report;
        }

        // Compare sources
        int en = expectedConfig.sources.size();
        int cn = computedConfig.sources.size();
        
        if (en != cn){
            TestReport report = reportError(ERROR_UNEXPECTED_SOURCES_LIST);
            report.addDescriptionEntry(ENTRY_KEY_EXPECTED_NUMBER_OF_SOURCES,
                            "" + en);
            report.addDescriptionEntry(ENTRY_KEY_COMPUTED_NUMBER_OF_SOURCES,
                            "" + cn);
            report.addDescriptionEntry(ENTRY_KEY_EXPECTED_SOURCES_LIST,
                            makeSourceList(expectedConfig.sources));

            report.addDescriptionEntry(ENTRY_KEY_COMPUTED_SOURCES_LIST,
                            makeSourceList(computedConfig.sources));

            return report;
        }

        for (int i=0; i<en; i++){
            Object es = expectedConfig.sources.elementAt(i);
            Object cs = computedConfig.sources.elementAt(i);
            if (!computedConfig.sources.contains(es)){
                TestReport report = reportError(ERROR_UNEXPECTED_SOURCES_LIST);
                report.addDescriptionEntry(ENTRY_KEY_EXPECTED_SOURCE_AT_INDEX,
                                           "[" + i + "] = -" + es + "- (" + es.getClass().getName() + ")");
                report.addDescriptionEntry(ENTRY_KEY_COMPUTED_SOURCE_AT_INDEX,
                                "[" + i + "] = -" + cs + "- (" + es.getClass().getName() + ")");
                report.addDescriptionEntry(ENTRY_KEY_EXPECTED_SOURCES_LIST,
                                makeSourceList(expectedConfig.sources));
                report.addDescriptionEntry(ENTRY_KEY_COMPUTED_SOURCES_LIST,
                                makeSourceList(computedConfig.sources));
                
                return report;
            }
        }

        // Compare dest
        en = expectedConfig.dest.size();
        cn = computedConfig.dest.size();

        if (en != cn){
            TestReport report = reportError(ERROR_UNEXPECTED_DEST_LIST);
            report.addDescriptionEntry(ENTRY_KEY_EXPECTED_NUMBER_OF_DEST,
                            "" + en);
            report.addDescriptionEntry(ENTRY_KEY_COMPUTED_NUMBER_OF_DEST,
                            "" + cn);
            report.addDescriptionEntry(ENTRY_KEY_EXPECTED_DEST_LIST,
                            makeSourceList(expectedConfig.dest));

            report.addDescriptionEntry(ENTRY_KEY_COMPUTED_DEST_LIST,
                            makeSourceList(computedConfig.dest));

            return report;
        }

        for (int i=0; i<en; i++){
            Object es = expectedConfig.dest.elementAt(i);
            Object cs = computedConfig.dest.elementAt(i);
            if (!computedConfig.dest.contains(cs)){
                TestReport report = reportError(ERROR_UNEXPECTED_DEST_LIST);
                report.addDescriptionEntry(ENTRY_KEY_EXPECTED_DEST_AT_INDEX,
                                "[" + i + "] = " + es);
                report.addDescriptionEntry(ENTRY_KEY_COMPUTED_DEST_AT_INDEX,
                                "[" + i + "] = " + cs);
                report.addDescriptionEntry(ENTRY_KEY_EXPECTED_DEST_LIST,
                                makeSourceList(expectedConfig.dest));
                report.addDescriptionEntry(ENTRY_KEY_COMPUTED_DEST_LIST,
                                makeSourceList(computedConfig.dest));
                
                return report;
            }
        }

        //
        // Finally, check the hints
        //
        en = expectedConfig.hints.size();
        cn = computedConfig.hints.size();
        
        if (en != cn){
            TestReport report = reportError(ERROR_UNEXPECTED_NUMBER_OF_HINTS);
            report.addDescriptionEntry(ENTRY_KEY_EXPECTED_NUMBER_OF_HINTS,
                                       "" + en);
            report.addDescriptionEntry(ENTRY_KEY_COMPUTED_NUMBER_OF_HINTS,
                                       "" + cn);

            report.addDescriptionEntry(ENTRY_KEY_EXPECTED_HINTS,
                                       makeHintsString(expectedConfig.hints));
            report.addDescriptionEntry(ENTRY_KEY_COMPUTED_HINTS,
                                       makeHintsString(computedConfig.hints));
            
            return report;
        }

        Iterator iter = expectedConfig.hints.keySet().iterator();
        while (iter.hasNext()){
            Object hintKey = iter.next();
            Object expectedHintValue = expectedConfig.hints.get(hintKey);
            
            Object computedHintValue = computedConfig.hints.get(hintKey);
            
            if (!expectedHintValue.equals(computedHintValue)){
                TestReport report = reportError(ERROR_UNEXPECTED_TRANSCODING_HINT);
                report.addDescriptionEntry(ENTRY_KEY_EXPECTED_HINT_KEY,
                                           hintKey.toString());
                report.addDescriptionEntry(ENTRY_KEY_EXPECTED_HINT_VALUE,
                                           expectedHintValue.toString());
                report.addDescriptionEntry(ENTRY_KEY_COMPUTED_HINT_VALUE,
                                           "" + computedHintValue);
                report.addDescriptionEntry(ENTRY_KEY_EXPECTED_HINTS,
                                           makeHintsString(expectedConfig.hints));
                report.addDescriptionEntry(ENTRY_KEY_COMPUTED_HINTS,
                                           makeHintsString(computedConfig.hints));

                return report;
            }
        }
                                            
        return reportSuccess();
    
    }

    public boolean proceedWithComputedTask(Transcoder transcoder,
                                           Map hints,
                                           Vector sources,
                                           Vector dest){
        computedConfig = new Config();
        computedConfig.transcoderClass = transcoder.getClass();
        computedConfig.sources = (Vector)sources.clone();
        computedConfig.dest = (Vector)dest.clone();
        computedConfig.hints = new HashMap(hints);
        return false; // Do not proceed with the convertion process,
        // we are only checking the config in this test.
    }

    public boolean proceedWithSourceTranscoding(SVGConverterSource source,
                                                File dest) {
        return true;
    }
        
    public boolean proceedOnSourceTranscodingFailure(SVGConverterSource source,
                                                     File dest,
                                                     String errorCode){
        return true;
    }

    public void onSourceTranscodingSuccess(SVGConverterSource source,
                                           File dest){
    }    
}

/**
 * Provides a simple string constructor which allows the user to 
 * create a given test to check that a specific transcoder class is 
 * used for a given mime type.
 */
class TranscoderConfigTest extends AbstractConfigTest {
    static final String SOURCE_FILE = "samples/anne.svg";
    static final String DEST_FILE_NAME = "samples/anne";
    static final String XML_PARSER = "org.apache.crimson.parser.XMLReaderImpl";

    protected DestinationType dstType;
    /**
     * @param dstType type of result image
     * @param expectedTranscoderClass class for the Transcoder expected to perform
     *        the convertion.
     */
    public TranscoderConfigTest(DestinationType dstType,
                                Class expectedTranscoderClass){
        this.dstType = dstType;

        Config config = new Config();
        config.transcoderClass = expectedTranscoderClass;
        
        Vector sources = new Vector();
        sources.addElement(new SVGConverterFileSource(new File(SOURCE_FILE)));
        config.sources = sources;

        Vector dest = new Vector();
        dest.addElement(new File(DEST_FILE_NAME + dstType.getExtension()));
        config.dest = dest;

        HashMap hints = new HashMap();
        hints.put(ImageTranscoder.KEY_XML_PARSER_CLASSNAME, XML_PARSER);
        config.hints = hints;
                  
        setExpectedConfig(config);
    }

    /**
     * Configures the test with the given mime type 
     */
    public void configure(SVGConverter c){
        c.setSourcesStrings(new String[] { SOURCE_FILE });
        c.setDstFile(new File(DEST_FILE_NAME + dstType.getExtension()));
        c.setDestinationType(dstType);
    }
}


/**
 * Provides a simple string array constructor which allows the user to 
 * create a test checking for a specific hint configuration.
 * The KEY_XML_PARSER_CLASSNAME  hint is always added to the list
 * passed to the test.
 */
class HintsConfigTest extends AbstractConfigTest {
    static final String SOURCE_FILE = "samples/anne.svg";
    static final String DEST_FILE_NAME = "samples/anne";
    static final String XML_PARSER = "org.apache.crimson.parser.XMLReaderImpl";
    static final Class EXPECTED_TRANSCODER_CLASS = org.apache.batik.transcoder.image.PNGTranscoder.class;
    static final DestinationType DST_TYPE = DestinationType.PNG;

    /**
     */
    public HintsConfigTest(Object[][] hintsMap){
        Config config = new Config();
        config.transcoderClass = EXPECTED_TRANSCODER_CLASS;
        
        Vector sources = new Vector();
        sources.addElement(new SVGConverterFileSource(new File(SOURCE_FILE)));
        config.sources = sources;

        Vector dest = new Vector();
        dest.addElement(new File(DEST_FILE_NAME + DST_TYPE.getExtension()));
        config.dest = dest;

        HashMap hints = new HashMap();
        hints.put(ImageTranscoder.KEY_XML_PARSER_CLASSNAME, XML_PARSER);

        //
        // Add hints from constructor argument
        //
        int n = hintsMap.length;
        for (int i=0; i<n; i++){
            hints.put(hintsMap[i][0], hintsMap[i][1]);
        }
        config.hints = hints;
                  
        setExpectedConfig(config);
    }

    /**
     * Configures the test with the given mime type 
     */
    public void configure(SVGConverter c){
        c.setSourcesStrings(new String[] { SOURCE_FILE });
        c.setDstFile(new File(DEST_FILE_NAME + DST_TYPE.getExtension()));
        c.setDestinationType(DST_TYPE);
        deltaConfigure(c);
    }

    protected void deltaConfigure(SVGConverter c){
    }
}

/**
 * Provides a simple string array constructor which allows the user to 
 * create a test checking for a specific source configuration.
 * The constructor argument takes the list of expected files and the 
 * deltaConfigure method should set the sources which is expected to 
 * produce that list of sources. The sources should be file names 
 * which ommit the ".svg" extension.
 */
class SourcesConfigTest extends AbstractConfigTest {
    static final String XML_PARSER = "org.apache.crimson.parser.XMLReaderImpl";
    static final Class EXPECTED_TRANSCODER_CLASS = org.apache.batik.transcoder.image.PNGTranscoder.class;
    static final DestinationType DST_TYPE = DestinationType.PNG;
    static final String SVG_EXTENSION = ".svg";

    /**
     */
    public SourcesConfigTest(Object[] expectedSources){
        Config config = new Config();
        config.transcoderClass = EXPECTED_TRANSCODER_CLASS;
        
        Vector sources = new Vector();
        Vector dest = new Vector();
        for (int i=0; i<expectedSources.length; i++){
            sources.addElement(new SVGConverterFileSource(new File(expectedSources[i] + SVG_EXTENSION)));
            dest.addElement(new File(expectedSources[i] + DST_TYPE.getExtension()));
        }
        config.sources = sources;
        config.dest = dest;

        HashMap hints = new HashMap();
        hints.put(ImageTranscoder.KEY_XML_PARSER_CLASSNAME, XML_PARSER);
        config.hints = hints;
                  
        setExpectedConfig(config);
    }

    /**
     * Configures the test with the given mime type 
     */
    public void configure(SVGConverter c){
        c.setDestinationType(DST_TYPE);
        setSources(c);
    }

    protected void setSources(SVGConverter c){
    }
}

/**
 * Provides a simple string array constructor which allows the user to 
 * create a test checking for a specific destination configuration.
 * The constructor argument takes the list of sources and the list of
 * expected configuration which is influenced by the 'setDestination'
 * content.
 */
class DestConfigTest extends AbstractConfigTest {
    static final String XML_PARSER = "org.apache.crimson.parser.XMLReaderImpl";
    static final Class EXPECTED_TRANSCODER_CLASS = org.apache.batik.transcoder.image.PNGTranscoder.class;
    static final DestinationType DST_TYPE = DestinationType.PNG;
    String[] sourcesStrings;
    /**
     */
    public DestConfigTest(String[] sourcesStrings,
                          String[] expectedDest){
        this.sourcesStrings = sourcesStrings;
        Config config = new Config();
        config.transcoderClass = EXPECTED_TRANSCODER_CLASS;
        
        Vector sources = new Vector();
        Vector dest = new Vector();
        for (int i=0; i<sourcesStrings.length; i++){
            sources.addElement(new SVGConverterFileSource(new File(sourcesStrings[i])));
        }

        for (int i=0; i<expectedDest.length; i++){
            dest.addElement(new File(expectedDest[i]));
        }

        config.sources = sources;
        config.dest = dest;

        HashMap hints = new HashMap();
        hints.put(ImageTranscoder.KEY_XML_PARSER_CLASSNAME, XML_PARSER);
        config.hints = hints;
                  
        setExpectedConfig(config);
    }

    /**
     * Configures the test with the given mime type 
     */
    public void configure(SVGConverter c){
        c.setDestinationType(DST_TYPE);
        c.setSourcesStrings(sourcesStrings);
        setDestination(c);
    }

    protected void setDestination(SVGConverter c){
    }
}

/**
 * This test lously checks that errors are reported as expected. It
 * checks that the error code given at construction time is reported
 * either my an exception thrown from the execute method or during the
 * processing of single files in the SVGConverterController handler.
 */
class ConfigErrorTest extends AbstractTest implements SVGConverterController{
    String errorCode;

    String foundErrorCode = null;

    public static final String ERROR_DID_NOT_GET_ERROR
        = "ConfigErrorTest.error.did.not.get.error";

    public static final String ERROR_UNEXPECTED_ERROR_CODE
        = "ConfigErrorTest.error.unexpected.error.code";

    public static final String ENTRY_KEY_EXPECTED_ERROR_CODE
        = "ConfigErrorTest.entry.key.expected.error.code";

    public static final String ENTRY_KEY_GOT_ERROR_CODE
        = "ConfigErrorTest.entry.key.got.error.code";

    public ConfigErrorTest(String expectedErrorCode){
        this.errorCode = expectedErrorCode;
    }

    public String getName(){
        return getId();
    }

    public TestReport runImpl() throws Exception {
        SVGConverter c = new SVGConverter(this);

        c.setDestinationType(DestinationType.PNG);
        c.setSourcesStrings(new String[]{ "samples/anne.svg" });

        configure(c);

        try {
            c.execute();
        } catch(SVGConverterException e){
            foundErrorCode = e.getErrorCode();
        }

        if (foundErrorCode == null){
            TestReport report = reportError(ERROR_DID_NOT_GET_ERROR);
            report.addDescriptionEntry(ENTRY_KEY_EXPECTED_ERROR_CODE,
                                       errorCode);
            return report;
        }

        if (foundErrorCode.equals(errorCode)){
            return reportSuccess();
        }

        TestReport report = reportError(ERROR_UNEXPECTED_ERROR_CODE);
        report.addDescriptionEntry(ENTRY_KEY_EXPECTED_ERROR_CODE,
                                   errorCode);
        report.addDescriptionEntry(ENTRY_KEY_GOT_ERROR_CODE,
                                   foundErrorCode);
        return report;
    }

    protected void configure(SVGConverter c){
    }

    public boolean proceedWithComputedTask(Transcoder transcoder,
                                           Map hints,
                                           Vector sources,
                                           Vector dest){
        System.out.println("==================> Starting to process Task <=========================");
        return true;
    }
    
    public boolean proceedWithSourceTranscoding(SVGConverterSource source,
                                                File dest) {
        System.out.print("Transcoding " + source + " to " + dest + " ... ");
        return true;
    }
    
    public boolean proceedOnSourceTranscodingFailure(SVGConverterSource source,
                                                     File dest,
                                                     String errorCode){
        System.out.println(" ... FAILURE");
        foundErrorCode = errorCode;
        return true;
    }

    public void onSourceTranscodingSuccess(SVGConverterSource source,
                                           File dest){
        System.out.println(" ... SUCCESS");
    }
}