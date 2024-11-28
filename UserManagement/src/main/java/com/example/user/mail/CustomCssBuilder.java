package com.example.user.mail;

import ch.qos.logback.classic.html.DefaultCssBuilder;
import ch.qos.logback.core.CoreConstants;

import static ch.qos.logback.core.CoreConstants.LINE_SEPARATOR;

public class CustomCssBuilder extends DefaultCssBuilder {

    @Override
    public void addCss(StringBuilder sbuf) {
        sbuf.append("<style  type=\"text/css\">");
        sbuf.append(LINE_SEPARATOR);
        sbuf.append("table { margin-left: 2em; margin-right: 2em; border-left: 2px solid #AAA; width: 100%; table-layout: fixed; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("TR.even { background: #FFFFFF; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("TR.odd { background: #FF7373; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("TD { padding-right: 1ex; padding-left: 1ex; border-right: 2px solid #AAA; word-wrap: break-word; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("TD.Time, TD.Date { text-align: center; font-family: courier, monospace; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("TD.Thread { text-align: center; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("TD.Level { text-align: center; font-weight: bold; color: #FF4040; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("TD.Logger { text-align: center; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("TR.header { background: #40a0FF; color: #FFF; font-weight: bold; font-size: larger; border: 1px #AAA; }");
        sbuf.append(CoreConstants.LINE_SEPARATOR);

        sbuf.append("TD.Exception { background: #FF4040; font-family: courier, monospace; }");
        sbuf.append(LINE_SEPARATOR);

        sbuf.append("</style>");
        sbuf.append(LINE_SEPARATOR);
    }
}
