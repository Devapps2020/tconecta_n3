package com.blm.qiubopay.printers;

//N3_FLAG_COMMENT

import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.nexgo.oaf.apiv3.device.printer.FontEntity;

//N3_FLAG_COMMENT

public class FormattedLine {
    //N3_FLAG_COMMENT

    private FontEntity fontType;
    private AlignEnum alignLine;

    //N3_FLAG_COMMENT

    private int fontSize;
    private PrinterLineType lineType;
    private String text;

    private AlignText alignText;

    //N3_FLAG_COMMENT

    public FormattedLine(int fontSize, FontEntity fontType, AlignEnum alignLine, PrinterLineType lineType, String text){
        this.fontSize   = fontSize;
        this.fontType   = fontType;
        this.alignLine  = alignLine;
        this.lineType   = lineType;
        this.text       = text;
        this.alignText  = null;
    }

    public FormattedLine(int fontSize, FontEntity fontType, AlignEnum alignLine, PrinterLineType lineType, String text, String leftText, String rightText){
        this.fontSize   = fontSize;
        this.fontType   = fontType;
        this.alignLine  = alignLine;
        this.lineType   = lineType;
        this.text       = text;
        this.alignText  = new AlignText(leftText, rightText);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public FontEntity getFontType() {
        return fontType;
    }

    public void setFontType(FontEntity fontType) {
        this.fontType = fontType;
    }

    public AlignEnum getAlignLine() {
        return alignLine;
    }

    public void setAlignLine(AlignEnum alignLine) {
        this.alignLine = alignLine;
    }

    public PrinterLineType getLineType() {
        return lineType;
    }

    public void setLineType(PrinterLineType lineType) {
        this.lineType = lineType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AlignText getAlignText() {
        return alignText;
    }

    public void setAlignText(AlignText alignText) {
        this.alignText = alignText;
    }

    //N3_FLAG_COMMENT

    public class AlignText{
        private String leftText;
        private String rightText;

        public AlignText(String l, String r){
            setLeftText(l);
            setRightText(r);
        }

        public String getLeftText() {
            return leftText;
        }

        public void setLeftText(String leftText) {
            this.leftText = leftText;
        }

        public String getRightText() {
            return rightText;
        }

        public void setRightText(String rightText) {
            this.rightText = rightText;
        }
    }

}
