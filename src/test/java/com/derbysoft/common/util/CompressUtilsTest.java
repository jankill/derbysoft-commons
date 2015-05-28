package com.derbysoft.common.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class CompressUtilsTest {

    public static final String SHORT_MESSAGE = "Hello 这是中文";
    public static final String LONG_MESSAGE = "<com.derbysoft.synchronizer.remote.dto.DeleteKeysResponse>\n" +
            "  <taskId>I7PUNDYRN64</taskId>\n" +
            "  <elapsedTimes>\n" +
            "    <com.derbysoft.dswitch.dto.common.KeyValue>\n" +
            "      <key>Synchronizer</key>\n" +
            "      <value>2</value>\n" +
            "    </com.derbysoft.dswitch.dto.common.KeyValue>\n" +
            "    <com.derbysoft.dswitch.dto.common.KeyValue>\n" +
            "      <key>Expedia-DS</key>\n" +
            "      <value>2</value>\n" +
            "    </com.derbysoft.dswitch.dto.common.KeyValue>\n" +
            "  </elapsedTimes>\n" +
            "  <deleteKeysRS>\n" +
            "    <keyTokens>\n" +
            "      <com.derbysoft.synchronizer.dto.KeyTokenDTO>\n" +
            "        <key>DSC|ALL|EC1|2281350</key>\n" +
            "        <tokens>\n" +
            "          <string>wjlcq</string>\n" +
            "          <string>wjlhv</string>\n" +
            "          <string>wjlle</string>\n" +
            "        </tokens>\n" +
            "      </com.derbysoft.synchronizer.dto.KeyTokenDTO>\n" +
            "    </keyTokens>\n" +
            "  </deleteKeysRS>\n" +
            "</com.derbysoft.synchronizer.remote.dto.DeleteKeysResponse>";

    @Test
    public void withoutZipIfMessageShorterThanThreshold() {
        String zip = CompressUtils.smartZip(SHORT_MESSAGE);
        Assert.assertEquals(SHORT_MESSAGE, zip);
        Assert.assertEquals(SHORT_MESSAGE, CompressUtils.smartUnzip(zip));
    }

    @Test
    public void withoutZipIfMessageOverThanThreshold() {
        String zip = CompressUtils.smartZip(LONG_MESSAGE);
        Assert.assertTrue(zip.length() < LONG_MESSAGE.length());
        Assert.assertTrue(StringUtils.startsWith(zip, CompressUtils.ZIP_FLAG));
        Assert.assertEquals(LONG_MESSAGE, CompressUtils.smartUnzip(zip));
    }
}
