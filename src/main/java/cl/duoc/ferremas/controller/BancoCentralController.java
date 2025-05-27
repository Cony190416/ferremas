package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.service.BancoCentralService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banco-central")
public class BancoCentralController {
    @GetMapping("/dolar")
public ResponseEntity<String> obtenerValorDolar() {
    try {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String url = "https://si3.bcentral.cl/SieteRestWS/SieteRestWS.ashx"
                   + "?user=guest&pass=guest&firstdate=" + fecha
                   + "&lastdate=" + fecha
                   + "&timeseries=F073.TCO.PRE.Z.D&function=GetSeries";

        System.out.println("Conectando a: " + url);

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();
        System.out.println("Código de respuesta: " + responseCode);

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }

            in.close();
            System.out.println("Respuesta de la API:\n" + response.toString());

            return ResponseEntity.ok(response.toString());
        } else {
            System.out.println("Error al conectarse. Código: " + responseCode);
            return ResponseEntity.status(responseCode).body("No se pudo obtener el valor del dólar.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Error al obtener el valor del dólar: " + e.getMessage());
    }
}

    @Autowired
    private BancoCentralService bancoCentralService;

    @GetMapping("/dolar")
    public String obtenerDolar() {
        return bancoCentralService.obtenerValorDolar();
    }
}
