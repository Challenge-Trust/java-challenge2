package br.com.fiap.soulup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SoulUpApplication {

    public static void main(String[] args) {
        // Servidores na nuvem rodam em UTC: as datas gravadas seguem o horário de Brasília
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        SpringApplication.run(SoulUpApplication.class, args);
    }
}
