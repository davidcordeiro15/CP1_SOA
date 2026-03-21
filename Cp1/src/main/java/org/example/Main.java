package org.example;
import javax.xml.ws.Endpoint;
import org.example.service.ClienteService;




//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        String url = "http://localhost:8090/cliente";

        Endpoint.publish(url, new ClienteService());

        System.out.println("Serviço SOAP rodando em: " + url);

    }
}