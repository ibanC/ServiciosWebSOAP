package com.example.dm2.servicioswebsoap;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private Button btnConvertir;
    private EditText edCantidad;
    private TextView txtResultado;
    private Spinner spinner1,spinner2;
    private String valorSpinner1;
    private String valorSpinner2;
    private SoapPrimitive resultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResultado=(TextView)findViewById(R.id.txtResultado);
        btnConvertir=(Button)findViewById(R.id.btnConvertir);
        edCantidad=(EditText)findViewById(R.id.edCantidad);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        spinner2=(Spinner)findViewById(R.id.spinner2);


        String[] datos=new String[]{"Kilometers","Yards","Centimeters","Meters","Miles"};

        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,datos);
        spinner1.setAdapter(adaptador);
        spinner2.setAdapter(adaptador);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                valorSpinner1 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                valorSpinner2 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cantidad= edCantidad.getText().toString();
                AsyncPost tarea=new AsyncPost();
                tarea.execute(cantidad);
            }
        });
    }

    private class AsyncPost extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... params) {


            try {
                String SOAP_ACTION="http://www.webserviceX.NET/ChangeLengthUnit";
                String METHOD_NAME="ChangeLengthUnit";
                String NAMESPACE="http://www.webserviceX.NET/";
                //URL url=new URL("http://www.webservicex.net/length.asmx?op=ChangeLengthUnit");
                String url="http://www.webservicex.net/length.asmx";


                SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME);
                Double num=Double.parseDouble(params[0]);
                double numValue=num.doubleValue();
               PropertyInfo primerParametro=new PropertyInfo();
                PropertyInfo segundoParametro=new PropertyInfo();
                PropertyInfo tercerParametro=new PropertyInfo();

                primerParametro.setName("LengthValue");
                primerParametro.setValue(numValue);
                segundoParametro.setName("fromLengthUnit");
                segundoParametro.setValue(valorSpinner1);
                tercerParametro.setName("toLengthUnit");
                tercerParametro.setValue(valorSpinner2);
                request.addProperty(primerParametro);
                request.addProperty(segundoParametro);
                request.addProperty(tercerParametro);
                /*request.addProperty(,numValue);
                request.addProperty("fromLengthUnit",valorSpinner1);
                request.addProperty("toLengthUnit",valorSpinner2);*/
                SoapSerializationEnvelope soapEnvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet=true;
                soapEnvelope.setOutputSoapObject(request);
                MarshalDouble md = new MarshalDouble();
                md.register(soapEnvelope);
                HttpTransportSE transport=new HttpTransportSE(url);
                transport.call(SOAP_ACTION,soapEnvelope);
                resultado=(SoapPrimitive)soapEnvelope.getResponse();

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result)
        {
            txtResultado.setText(resultado+" es el resultado");
        }
    }


}

