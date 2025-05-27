package cl.duoc.ferremas.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class BancoCentralService {

    public String obtenerValorDolar() {
        try {
            String urlStr = "https://si3.bcentral.cl/SieteRestWS/SieteRestWS.ashx?function=GetSeriesValores&param1=F073.TCO.PRE.Z.D";
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Autenticación básica
            String user = "co.mena@duocuc.cl";
            String pass = "Cony1904";
            String auth = user + ":" + pass;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);

            int responseCode = con.getResponseCode();
            System.out.println("Código de respuesta: " + responseCode);

            BufferedReader in;
            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            in.close();

            // 🔎 Mostrar respuesta cruda para inspección
            String respuestaApi = response.toString().trim();
            System.out.println("Respuesta de la API:\n" + respuestaApi);

            // 🚫 Validación simple antes de parsear XML
            if (!respuestaApi.startsWith("<?xml")) {
                return "La respuesta no es XML válida. Contenido recibido:\n" + respuestaApi;
            }

            // ✅ Parseo de XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new java.io.ByteArrayInputStream(respuestaApi.getBytes("UTF-8")));

            var valores = doc.getElementsByTagName("Obs");
            if (valores.getLength() > 0) {
                var ultimoValor = valores.item(valores.getLength() - 1);
                String valor = ultimoValor.getAttributes().getNamedItem("value").getNodeValue();
                return valor;
            } else {
                return "No se encontraron datos.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error al obtener el valor del dólar: " + e.getMessage();
        }
    }
}


