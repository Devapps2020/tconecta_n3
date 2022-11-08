package com.blm.qiubopay.helpers;

import com.nexgo.oaf.apiv3.device.printer.DotMatrixFontEnum;
import com.nexgo.oaf.apiv3.device.printer.FontEntity;

public class N3Constants {

    //PRINTER
    public static final int FONT_SIZE_SMALL   = 20;
    public static final int FONT_SIZE_NORMAL  = 24;
    public static final int FONT_SIZE_BIG     = 24;

    //N3_FLAG_COMMENT

    public static FontEntity fontSmall    = new FontEntity(DotMatrixFontEnum.CH_SONG_20X20, DotMatrixFontEnum.ASC_SONG_8X16);
    public static FontEntity fontNormal   = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24, DotMatrixFontEnum.ASC_SONG_12X24);
    public static FontEntity fontBold     = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24, DotMatrixFontEnum.ASC_SONG_BOLD_16X24);
    public static FontEntity fontBig      = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24, DotMatrixFontEnum.ASC_SONG_12X24, false, true);

}
