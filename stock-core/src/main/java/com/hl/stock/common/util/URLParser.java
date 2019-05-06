package com.hl.stock.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class URLParser {
    protected static final byte TYPE_URL = 1;
    protected static final byte TYPE_QUERY_STRING = 2;
    public Map<String, String> parsedParams;
    protected byte type;
    protected String url;
    protected String baseUrl;
    protected String queryString;
    protected String label;
    protected String charset = CharsetConstant.UTF8;
    protected boolean compiled = false;
    protected URLDecoder urld = new URLDecoder();

    public static URLParser fromURL(String url) {
        URLParser parser = new URLParser();

        parser.type = 1;
        parser.url = url;

        String[] split = url.split("\\?", 2);
        parser.baseUrl = split[0];
        parser.queryString = (split.length > 1 ? split[1] : "");

        String[] split2 = url.split("#", 2);
        parser.label = (split2.length > 1 ? split2[1] : null);

        return parser;
    }

    public static URLParser fromQueryString(String queryString) {
        URLParser parser = new URLParser();

        parser.type = 2;
        parser.queryString = queryString;

        return parser;
    }

    public URLParser useCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public URLParser compile() throws UnsupportedEncodingException {
        if (this.compiled) {
            return this;
        }
        String paramString = this.queryString.split("#")[0];
        String[] params = paramString.split("&");

        this.parsedParams = new HashMap<String, String>(params.length);
        for (String p : params) {
            String[] kv = p.split("=");
            if (kv.length == 2) {
                this.parsedParams.put(kv[0], URLDecoder.decode(kv[1], this.charset));
            }
        }
        this.compiled = true;

        return this;
    }

    public String getParameter(String name) {
        if (this.compiled) {
            return this.parsedParams.get(name);
        }
        String paramString = this.queryString.split("#")[0];
        Matcher match = Pattern.compile("(^|&)" + name + "=([^&]*)").matcher(paramString);
        match.lookingAt();

        return match.group(2);
    }

    public URLParser setParameter(String name, String value) throws UnsupportedEncodingException {
        if (!this.compiled) {
            compile();
        }
        this.parsedParams.put(name, value);

        return this;
    }
}