package com.blm.qiubopay.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blm.qiubopay.R;

import mx.devapps.utils.components.HActivity;

public class CalcularActivity extends HActivity implements View.OnClickListener {

    protected TextView display;
    protected TextView display2;

    protected String inputString;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_calculator);

        this.display = this.findViewById(R.id.display);
        this.display2 = this.findViewById(R.id.display2);
        final Button button9 = this.findViewById(R.id.button9);
        final Button button8 = this.findViewById(R.id.button8);
        final Button button7 = this.findViewById(R.id.button7);
        final Button button6 = this.findViewById(R.id.button6);
        final Button button5 = this.findViewById(R.id.button5);
        final Button button4 = this.findViewById(R.id.button4);
        final Button button3 = this.findViewById(R.id.button3);
        final Button button2 = this.findViewById(R.id.button2);
        final Button button1 = this.findViewById(R.id.button1);
        final Button button0 = this.findViewById(R.id.button0);
        final Button button_divide = this.findViewById(R.id.button_divide);
        final Button button_multiply = this.findViewById(R.id.button_multiply);
        final Button button_plus = this.findViewById(R.id.button_plus);
        final Button button_substract = this.findViewById(R.id.button_substract);
        final Button button_dot = this.findViewById(R.id.button_ce);
        final Button button_equals = this.findViewById(R.id.button_equals);

        button9.setOnClickListener(this);
        button8.setOnClickListener(this);
        button7.setOnClickListener(this);
        button6.setOnClickListener(this);
        button5.setOnClickListener(this);
        button4.setOnClickListener(this);
        button3.setOnClickListener(this);
        button2.setOnClickListener(this);
        button1.setOnClickListener(this);
        button0.setOnClickListener(this);
        button_divide.setOnClickListener(this);
        button_multiply.setOnClickListener(this);
        button_plus.setOnClickListener(this);
        button_substract.setOnClickListener(this);
        button_dot.setOnClickListener(this);
        button_equals.setOnClickListener(this);

        inputString = "";

        ImageView btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().finish();
            }
        });
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.button9:
                this.updateDisplay2("9");
                inputString += "9";
                break;
            case R.id.button8:
                this.updateDisplay2("8");
                inputString += "8";
                break;
            case R.id.button7:
                this.updateDisplay2("7");
                inputString += "7";
                break;
            case R.id.button6:
                this.updateDisplay2("6");
                inputString += "6";
                break;
            case R.id.button5:
                this.updateDisplay2("5");
                inputString += "5";
                break;
            case R.id.button4:
                this.updateDisplay2("4");
                inputString += "4";
                break;
            case R.id.button3:
                this.updateDisplay2("3");
                inputString += "3";
                break;
            case R.id.button2:
                this.updateDisplay2("2");
                inputString += "2";
                break;
            case R.id.button1:
                this.updateDisplay2("1");
                inputString += "1";
                break;
            case R.id.button0:
                this.updateDisplay2("0");
                inputString += "0";
                break;
            case R.id.button_divide:
                this.updateDisplay2(" / ");
                inputString += " / ";
                break;
            case R.id.button_multiply:
                this.updateDisplay2(" * ");
                inputString += " * ";
                break;
            case R.id.button_plus:
                this.updateDisplay2(" + ");
                inputString += " + ";
                break;
            case R.id.button_substract:
                this.updateDisplay2(" - ");
                inputString += " - ";
                break;
            case R.id.button_ce:
                this.display.setText("");
                this.display2.setText("");
                inputString = "";
                break;
            case R.id.button_equals:
                display2.setText("");
                prepareInput(inputString);
                inputString = "";
                break;
        }
    }

    private void updateDisplay(String value) {
        this.display.setText(value);
    }

    private void updateDisplay2(final String value) {
        this.display2.append(value);
    }

    private void prepareInput(String input) {
        if(input.startsWith(" ")) {
            String firstChar = String.valueOf(input.charAt(1));
            if(firstChar.contains("/")|| firstChar.contains("*")) {
                updateDisplay("Error");
                return;
            } else {
                if(firstChar.contains("+")) {
                    input = input.replace(" + ", "");
                }
                if(firstChar.contains("-")) {
                    input = input.replace(" - ", "-");
                }
            }
        }
        calculate(input);
    }

    private void calculate(String input) {
        String[] arr = input.split(" ");
        if(arr.length > 1) {
            for(int i=0; i<arr.length; i++) {
                int temp = 0;
                try {
                    if(arr[i].contains("/")) {
                        int op1 = Integer.parseInt(arr[i-1]);
                        int op2 = Integer.parseInt(arr[i+1]);
                        if(op2 == 0) {
                            System.out.println("Not allowed");
                            updateDisplay("Error");
                            input = "";
                            return;
                        }
                        temp = op1 / op2;
                        arr[i-1] = " ";
                        arr[i] = String.valueOf(temp);
                        arr[i+1] = " ";
                    }
                    if(arr[i].contains("*")) {
                        int op1 = Integer.parseInt(arr[i-1]);
                        int op2 = Integer.parseInt(arr[i+1]);
                        temp = op1 * op2;
                        arr[i-1] = " ";
                        arr[i] = String.valueOf(temp);
                        arr[i+1] = " ";
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            for(int i=0; i<arr.length; i++) {
                int temp = 0;
                try {
                    if(arr[i].contains("+")) {
                        int op1 = Integer.parseInt(arr[i-1]);
                        int op2 = Integer.parseInt(arr[i+1]);
                        temp = op1 + op2;
                        arr[i-1] = " ";
                        arr[i] = String.valueOf(temp);
                        arr[i+1] = " ";
                    }
                    if(arr[i].contains("-")) {
                        int op1 = Integer.parseInt(arr[i-1]);
                        int op2 = Integer.parseInt(arr[i+1]);
                        temp = op1 - op2;
                        arr[i-1] = " ";
                        arr[i] = String.valueOf(temp);
                        arr[i+1] = " ";
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            inputString = "";
            for(String s : arr) {
                if(s != " ") {
                    inputString += s;
                    inputString += " ";
                }
            }
            calculate(inputString);
        } else {
            updateDisplay(arr[0]);
        }
    }


}
