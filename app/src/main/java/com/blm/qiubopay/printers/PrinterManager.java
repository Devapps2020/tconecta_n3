package com.blm.qiubopay.printers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.nexgo.oaf.apiv3.device.printer.OnPrintListener;
import com.nexgo.oaf.apiv3.device.printer.Printer;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.utils.Globals;

import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

//N3_FLAG_COMMENT

public class PrinterManager extends AppCompatActivity {
    //N3_FLAG_COMMENT

    private DeviceEngine deviceEngine;
    private Printer printer;

    //N3_FLAG_COMMENT

    private Context context;


    public PrinterManager(Context context){
        this.context = context;

        //N3_FLAG_COMMENT

        deviceEngine = APIProxy.getDeviceEngine(context);
        printer = deviceEngine.getPrinter();
        printer.setTypeface(Typeface.DEFAULT);

        //N3_FLAG_COMMENT
    }

    public void printTicket(List<String> lines){
        //N3_FLAG_COMMENT

        printer.initPrinter();
        printer.setTypeface(Typeface.DEFAULT);
        printer.setLetterSpacing(5);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        if(null != lines && lines.size()>0)
        {

            for(int i=0; i<lines.size(); i++){
                printer.appendPrnStr(lines.get(i), Globals.FONT_SIZE_NORMAL, AlignEnum.LEFT, false);
            }
            printer.startPrint(true, new OnPrintListener() {
                @Override
                public void onPrintResult(final int retCode) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(PrinterManager.this, retCode + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });//(true, null);
        }

        //N3_FLAG_COMMENT
    }

    public void printFormattedTicket1(List<FormattedLine> lines){
        //N3_FLAG_COMMENT

        printer.initPrinter();
        printer.setTypeface(Typeface.DEFAULT);
        printer.setLetterSpacing(5);
        FormattedLine line;
        Bitmap bitmap;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        if(null != lines && lines.size()>0)
        {
            for(int i=0; i<lines.size(); i++){
                line = lines.get(i);
                if(line.getLineType() == PrinterLineType.TEXT){
                    printer.appendPrnStr(line.getText(), line.getFontSize(), line.getAlignLine(), false);
                } else if(line.getLineType() == PrinterLineType.IMAGE){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_app_tconecta);
                    printer.appendImage(bitmap, AlignEnum.CENTER);//line.getAlignLine());
                }
            }

            printer.startPrint(true, new OnPrintListener() {
                @Override
                public void onPrintResult(final int retCode) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Toast.makeText(PrinterManager.this, retCode + "", Toast.LENGTH_SHORT).show();
                            //try {
                            //    Thread.sleep(3000);
                            //} catch (InterruptedException e) {
                            //    e.printStackTrace();
                            //}
                        }
                    });
                }
            });//(true, null);
        }

        //N3_FLAG_COMMENT
    }

    public void printFormattedTicket(final List<FormattedLine> lines, final HActivity context){
        //N3_FLAG_COMMENT

        printer.initPrinter();
        printer.setTypeface(Typeface.DEFAULT);
        printer.setLetterSpacing(5);
        FormattedLine line;
        Bitmap bitmap;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        if(null != lines && lines.size()>0)
        {
            for(int i=0; i<lines.size(); i++){
                line = lines.get(i);
                if(line.getLineType() == PrinterLineType.TEXT){
                    if(line.getAlignText() != null)
                        printer.appendPrnStr(line.getAlignText().getLeftText(), line.getAlignText().getRightText(), line.getFontSize(), line.getFontType()==Globals.fontBold ? true : false);
                    else
                        printer.appendPrnStr(line.getText(), line.getFontSize(), line.getAlignLine(), line.getFontType()== Globals.fontBold ? true : false);
                } else if(line.getLineType() == PrinterLineType.IMAGE){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_app_tconecta);
                    printer.appendImage(bitmap, AlignEnum.CENTER);//line.getAlignLine());
                }
            }

            printer.startPrint(true, new OnPrintListener() {
                @Override
                public void onPrintResult(final int retCode) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*if(retCode == 0){
                                printerManager.cutPaper();
                            }
                            else*/
                            if(retCode == -1005){
                                //printTicket();
                                //Toast.makeText(context, retCode + "", Toast.LENGTH_SHORT).show();
                                context.alert("Se terminó el papel, por favor cambie el rollo y presione el botón aceptar.", new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        printFormattedTicket(lines, context);
                                    }
                                }, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "No imprimir";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        //N3_FLAG_COMMENT
    }

    public void printFormattedTicket(final List<FormattedLine> lines, final MenuActivity context){
        //N3_FLAG_COMMENT

        printer.initPrinter();
        printer.setTypeface(Typeface.DEFAULT);
        printer.setLetterSpacing(5);
        FormattedLine line;
        Bitmap bitmap;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        if(null != lines && lines.size()>0)
        {
            for(int i=0; i<lines.size(); i++){
                line = lines.get(i);
                if(line.getLineType() == PrinterLineType.TEXT){
                    if(line.getAlignText() != null)
                        printer.appendPrnStr(line.getAlignText().getLeftText(), line.getAlignText().getRightText(), line.getFontSize(), line.getFontType()==Globals.fontBold ? true : false);
                    else
                        printer.appendPrnStr(line.getText(), line.getFontSize(), line.getAlignLine(), line.getFontType()== Globals.fontBold ? true : false);
                } else if(line.getLineType() == PrinterLineType.IMAGE){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_app_tconecta);
                    printer.appendImage(bitmap, AlignEnum.CENTER);//line.getAlignLine());
                }
            }

            printer.startPrint(true, new OnPrintListener() {
                @Override
                public void onPrintResult(final int retCode) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*if(retCode == 0){
                                printerManager.cutPaper();
                            }
                            else*/
                            if(retCode == -1005){
                                //printTicket();
                                //Toast.makeText(context, retCode + "", Toast.LENGTH_SHORT).show();
                                context.alert("Se terminó el papel, por favor cambie el rollo y presione el botón aceptar.", new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        printFormattedTicket(lines, context);

                                    }
                                }, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "No imprimir";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });

                            }
                        }
                    });
                }
            });
        }

        //N3_FLAG_COMMENT
    }

    public void printFormattedTicket(final IFunction function, final List<FormattedLine> lines, final MenuActivity context){
        //N3_FLAG_COMMENT

        printer.initPrinter();
        printer.setTypeface(Typeface.DEFAULT);
        printer.setLetterSpacing(5);
        FormattedLine line;
        Bitmap bitmap;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        if(null != lines && lines.size()>0)
        {
            for(int i=0; i<lines.size(); i++){
                line = lines.get(i);
                if(line.getLineType() == PrinterLineType.TEXT){

                    if(line.getAlignText() != null)
                        printer.appendPrnStr(line.getAlignText().getLeftText(), line.getAlignText().getRightText(), line.getFontSize(), line.getFontType()==Globals.fontBold ? true : false);
                    else
                        printer.appendPrnStr(line.getText(), line.getFontSize(), line.getAlignLine(), line.getFontType()== Globals.fontBold ? true : false);
                } else if(line.getLineType() == PrinterLineType.IMAGE){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_app_tconecta);
                    printer.appendImage(bitmap, AlignEnum.CENTER);//line.getAlignLine());
                }
            }

            printer.startPrint(true, new OnPrintListener() {
                @Override
                public void onPrintResult(final int retCode) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(retCode == 0){
                                //printerManager.cutPaper();
                                if(function != null){
                                    function.execute();
                                }
                            }
                            else
                            if(retCode == -1005){
                                //printTicket();
                                Toast.makeText(context, retCode + "", Toast.LENGTH_SHORT).show();
                                context.alert("Se terminó el papel, por favor cambie el rollo y presione el botón aceptar.", new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        printFormattedTicket(function, lines, context);
                                    }
                                }, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "No imprimir";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });

                            }
                        }
                    });
                }
            });
        }

        //N3_FLAG_COMMENT
    }

    public void printFormattedTicket(final IFunction function, final List<FormattedLine> lines, final HActivity context){
        //N3_FLAG_COMMENT

        printer.initPrinter();
        printer.setTypeface(Typeface.DEFAULT);
        printer.setLetterSpacing(5);
        FormattedLine line;
        Bitmap bitmap;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        if(null != lines && lines.size()>0)
        {
            for(int i=0; i<lines.size(); i++){
                line = lines.get(i);
                if(line.getLineType() == PrinterLineType.TEXT){

                    if(line.getAlignText() != null)
                        printer.appendPrnStr(line.getAlignText().getLeftText(), line.getAlignText().getRightText(), line.getFontSize(), line.getFontType()==Globals.fontBold ? true : false);
                    else
                        printer.appendPrnStr(line.getText(), line.getFontSize(), line.getAlignLine(), line.getFontType()== Globals.fontBold ? true : false);
                } else if(line.getLineType() == PrinterLineType.IMAGE){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_app_tconecta);
                    printer.appendImage(bitmap, AlignEnum.CENTER);//line.getAlignLine());
                }
            }

            printer.startPrint(true, new OnPrintListener() {
                @Override
                public void onPrintResult(final int retCode) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(retCode == 0){
                                //printerManager.cutPaper();
                                if(function != null){
                                    function.execute();
                                }
                            }
                            else
                            if(retCode == -1005){
                                //printTicket();
                                Toast.makeText(context, retCode + "", Toast.LENGTH_SHORT).show();

                                context.alert("Se terminó el papel, por favor cambie el rollo y presione el botón aceptar.", new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }
                                    @Override
                                    public void onClick() {
                                        printFormattedTicket(function, lines, context);
                                    }
                                }, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "No imprimir";
                                    }

                                    @Override
                                    public void onClick() {

                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        //N3_FLAG_COMMENT
    }

    public void cutPaper(){
        printer.cutPaper();
    }

    public void printFormattedTicket(List<FormattedLine> lines, OnPrintListener listener){
        //N3_FLAG_COMMENT

        printer.initPrinter();
        printer.setTypeface(Typeface.DEFAULT);
        printer.setLetterSpacing(5);
        FormattedLine line;
        Bitmap bitmap;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        if(null != lines && lines.size()>0)
        {
            for(int i=0; i<lines.size(); i++){
                line = lines.get(i);
                if(line.getLineType() == PrinterLineType.TEXT){
                    printer.appendPrnStr(line.getText(), line.getFontSize(), line.getAlignLine(), false);
                } else if(line.getLineType() == PrinterLineType.IMAGE){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_app_tconecta);
                    printer.appendImage(bitmap, AlignEnum.CENTER);//line.getAlignLine());
                }
            }

            printer.startPrint(true, listener);
        }

        //N3_FLAG_COMMENT
    }
}
