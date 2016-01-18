package com.derbysoft.common.util;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class CCSEscapeUtils {
    private static final String[][] CCS_ESCAPE = {
            {"&", "&amp;"},
            {"\r", "&cr;"},
            {"\n", "&lf;"},
            {":", "&cln;"},
            {"|", "&vb;"},
    };

    private static final String[][] CCS_UNESCAPE = EntityArrays.invert(CCS_ESCAPE);


    private static final CharSequenceTranslator ESCAPE_CCS =
            new AggregateTranslator(
                    new LookupTranslator(CCS_ESCAPE())
            );

    private static final CharSequenceTranslator UNESCAPE_CCS =
            new AggregateTranslator(
                    new LookupTranslator(CCS_UNESCAPE())
            );

    private static String[][] CCS_ESCAPE() {
        return CCS_ESCAPE.clone();
    }

    private static String[][] CCS_UNESCAPE() {
        return CCS_UNESCAPE.clone();
    }

    public static String escape(String input) {
        return ESCAPE_CCS.translate(input);
    }

    public static String unescape(String input) {
        return UNESCAPE_CCS.translate(input);
    }
}
