package umn.ac.id.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    TextView input, results;
    String operation, value1, value2;
    Double angka1, angka2, hasil;

    boolean titik, stateError, lastResult, minus=false, clickoperator=false, back=false;
    private int[] numberBtns = {R.id.btnnol, R.id.btnsatu, R.id.btndua, R.id.btntiga, R.id.btnempat,
            R.id.btnlima, R.id.btnenam, R.id.btntujuh, R.id.btndelapan, R.id.btnsembilan};
    private int[] operatorBtns= {R.id.btntambah, R.id.btnkurang, R.id.btnkali, R.id.btnbagi};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (TextView) findViewById(R.id.input);
        results = (TextView) findViewById(R.id.result);

        inputNumberOnClickListener();
        inputOperatorOnClickListener();
    }

    private void inputNumberOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;

                String s = input.getText().toString();
                char[] arr = s.toCharArray();

                if (stateError) {
                    input.setText(button.getText());
                    stateError = false;
                }
                else{
                    if(input.getText().toString().equals("") && !results.getText().toString().equals("")) {
                        Toast noangka= Toast.makeText(getApplicationContext(), "Input must be an " +
                                        "operator",
                                Toast.LENGTH_SHORT);
                        noangka.show();
                    }
                    else if(button.getText().equals('0')){
                        Toast nonol= Toast.makeText(getApplicationContext(), "Zero can't be a " +
                                        "first number",
                                Toast.LENGTH_SHORT);
                        nonol.show();
                    }
                    else
                        input.append(button.getText());
                }
                lastResult = true;
            }
        };
        for (int id : numberBtns) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void inputOperatorOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!results.getText().toString().equals("") && !clickoperator) {
                    input.setText(results.getText());
                    clickoperator = true;
                    lastResult = true;
                }
                if (lastResult && !stateError) {
                    Button button = (Button) v;
                    if(input.getText().toString().length() >= 16 ) {
                        Toast maxangka = Toast.makeText(getApplicationContext(), "Operation too long",
                                Toast.LENGTH_SHORT);
                        maxangka.show();
                    } else {
                        input.append(button.getText());
                        lastResult = false;
                        titik = false;    // Reset the DOT flag
                        minus = false;
                    }
                }
            }
        };
        for (int id : operatorBtns) {
            findViewById(id).setOnClickListener(listener);
        }

        // Decimal point
        findViewById(R.id.btnkoma).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = input.getText().toString();
                char[] arr = s.toCharArray();
                if (!titik) {
                    if (input.getText().toString().equals("")) {
                        input.setText("0.");
                        titik = true;
                    } else if (arr[s.length() - 1] == ')'){
                        Toast komakurung = Toast.makeText(getApplicationContext(), "Next input must be an operator",
                                Toast.LENGTH_SHORT);
                        komakurung.show();
                    }
                    //biar koma cuma bisa 1 buah doang
                    else if (lastResult && !stateError && !titik) {
                        input.append(".");
                        lastResult = false;
                        titik = true;
                    }
                    else {
                        Toast koma = Toast.makeText(getApplicationContext(), "Commas must be " +
                                        "preceded by a number",
                                Toast.LENGTH_SHORT);
                        koma.show();
                    }
                }
            }
        });

        findViewById(R.id.btnminus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = input.getText().toString();
                char[] arr = s.toCharArray();

                if(input.getText().toString().length()+3 > 16) {
                    Toast maxinput = Toast.makeText(getApplicationContext(), "Operation too long",
                            Toast.LENGTH_SHORT);
                    maxinput.show();
                }
                else if(input.getText().toString().length() !=0) {
                    int index = 0;
                    if (input.getText().toString().contains("x") || input.getText().toString().contains("/") || input.getText().toString().contains("+") || input.getText().toString().contains("-")){
                        for (int i = s.length() - 1; i >= 0; i--) {
                            //buat detect sblm arr[index] dikasi -
                            if (arr[s.length() - 1] != '+' && arr[s.length() - 1] != '-' && arr[s.length() - 1] != '/' && arr[s.length() - 1] != 'x') {
                                if (arr[i] == '+' || arr[i] == 'x' || arr[i] == '-' || arr[i] == '/') {
                                    index = i;
                                    input.setText(s.substring(0, index + 1) + "(-" + s.substring(index + 1,
                                            s.length()) + ")");
                                    break;
                                }
                            } else {
                                Toast nominus = Toast.makeText(getApplicationContext(),
                                        "Input a number first", Toast.LENGTH_SHORT);
                                nominus.show();
                                break;
                            }
                        }
                    } else{
                        input.setText("(-" + s + ")");
                        minus = true;
                    }
                }else {
                    Toast posmin = Toast.makeText(getApplicationContext(), "Positif or Negatif " +
                            "notation must be preceded by a number", Toast.LENGTH_SHORT);
                    posmin.show();
                }
            }

        });

        // Clear button (CE)
        findViewById(R.id.btnCE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
                results.setText("");
                lastResult = false;
                stateError = false;
                titik = false;
            }
        });

        //Button C
        findViewById(R.id.btnC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText(value1);
                value2 = null;
                operation = null;
                titik = false;
                minus = false;
                clickoperator= false;
            }
        });

        //Back Button
        findViewById(R.id.btnback).setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           if(!input.getText().toString().equals("")){
                                                               int length = input.getText().length();
                                                               String s = input.getText().toString();
                                                               if (s.charAt(length - 1) == '.') {
                                                                   titik = false;
                                                                   input.setText(input.getText().subSequence(0, input.getText().length() - 1));
                                                               } else {
                                                                   input.setText(input.getText().subSequence(0, input.getText().length() - 1));
                                                               }
                                                           }
                                                           else if(input.getText().toString().equals("")) {
                                                               Toast backnull =
                                                                       Toast.makeText(getApplicationContext(), "Nothing can be deleted",
                                                                       Toast.LENGTH_SHORT);
                                                               backnull.show();
                                                           }
                                                           else if (input.getText().toString().equals(
                                                                   "") && !results.getText().toString().equals("") && !back) {
                                                               input.setText(results.getText());
                                                           }
                                                           else{
                                                               return;
                                                           }
                                                       }

                                                   });
        // Equal button
        findViewById(R.id.btnsamadengan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result();
            }
        });
    }

    private void result() {
        if (lastResult && !stateError) {
            String txt = input.getText().toString();
            txt = txt.replaceAll("x", "*"); //ubah x jd * karena komp kenalnya *

            Expression expression = new ExpressionBuilder(txt).build();
            try {
                double result = expression.evaluate();
                int resultTemp = (int)result;

                //buat nentuin double atau int untuk ditampilin di output
                if(resultTemp == result)
                    results.setText(Integer.toString((int) result ));
                else {
//                     String hasil = String.format("%f", result);
                    results.setText(Double.toString(result));
//                                        results.setText(hasil);

                }
            } catch (ArithmeticException ex) {
                Toast dividedbyzero = Toast.makeText(getApplicationContext(), "Can't Divided by " +
                                "Zero",
                        Toast.LENGTH_SHORT);
                dividedbyzero.show();
                stateError = true;
                lastResult= false;
            }
        }
        else if(input.getText().toString().equals("") && results.getText().toString().equals("")){
            Toast noinput = Toast.makeText(getApplicationContext(), "Press number to start " +
                            "counting",
                    Toast.LENGTH_SHORT);
            noinput.show();
        }
        else{
            Toast error = Toast.makeText(getApplicationContext(), "Operation is not complete",
                    Toast.LENGTH_SHORT);
            error.show();
        }
    }
}
