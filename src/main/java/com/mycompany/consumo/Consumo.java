/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.consumo;


import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.commons.lang3.StringEscapeUtils;



/**
 *
 * @author alans
 */
public class Consumo {

     public static void main(String[] args) throws Exception {
        // Uso del servicio TIPOCAMBIORANGOMONEDA del Banguat
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("https://www.banguat.gob.gt/variables/ws/TipoCambio.asmx")
                .header("Content-Type", "text/xml; charset=utf-8")
                .body("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                        "  <soap:Body>\n" +
                        "    <TipoCambioRangoMoneda xmlns=\"http://www.banguat.gob.gt/variables/ws/\">\n" +
                        "      <fechainit>10/05/2023</fechainit>\n" +
                        "      <fechafin>18/05/2023</fechafin>\n" +
                        "      <moneda>2</moneda>\n" +
                        "    </TipoCambioRangoMoneda>\n" +
                        "  </soap:Body>\n" +
                        "</soap:Envelope>")
                .asString();

        String escapedXml = StringEscapeUtils.unescapeHtml4(response.getBody());

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(StringEscapeUtils.unescapeHtml4(response.getBody())));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("Var");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);

            System.out.println("fecha: " + element.getElementsByTagName("fecha").item(0).getTextContent().trim());
            System.out.println("moneda: " + element.getElementsByTagName("moneda").item(0).getTextContent().trim());
            System.out.println("compra: " + element.getElementsByTagName("compra").item(0).getTextContent().trim());
            System.out.println("venta: " + element.getElementsByTagName("venta").item(0).getTextContent().trim());
            System.out.println("-----------------------");
        }

        System.out.println("Respuesta: " + response.getBody());
        System.out.println("Respuesta: " + escapedXml);

        // Uso de la API
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate rest = new RestTemplate(requestFactory);
        ResponseEntity<String> resultado = rest.getForEntity("https://api.cambio.today/v1/quotes/EUR/USD/json?quantity=1&key=7641|hLMAhC2Q8iLBtnECZ19Heg*mv3JrsxOp", String.class);
        Gson gson = new Gson();
        Moneda moneda = gson.fromJson(resultado.getBody(), Moneda.class);
        System.out.println("API: " + moneda);
    }
}