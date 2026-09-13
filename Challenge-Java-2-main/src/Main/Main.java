package Main;
import buissiness.App;
import model.Points;
import model.Ticket;
import model.Usuario;
import buissiness.CO2Emission;
import java.util.Scanner;
/**
 *
 * @author Nicolas Martins, Nicolas Frazao, Vinicius Soares, Henrique Soares, Caua Bordini
 * @version 1.2
 * @since 1.0
 *
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CO2Emission gas = new CO2Emission();
        Points points = new Points();
        Ticket ticket = new Ticket();
        Usuario usuario2 = new Usuario();
        System.out.println("========== CADASTRO DE USUÁRIO ==========");
        System.out.println("\nDigite seu nome: ");
        usuario2.setName(sc.nextLine());
        System.out.println("Digite seu nome de usuário: ");
        usuario2.setUserName(sc.nextLine());
        System.out.println("Digite sua senha: ");
        usuario2.setPassword(sc.nextLine());
        System.out.println("Digite seu CPF (apenas números): ");
        usuario2.setCpf(sc.nextLine());
        System.out.println("\nCADASTRADO COM SUCESSO!");
        System.out.println("========== PONTUAÇÃO E CONVERSÃO ==========");
        System.out.println("Digite seu tempo assistido: ");
        usuario2.setViewMinutes(sc.nextInt());
        sc.nextLine();
        App app = new App(usuario2, points, ticket);
        System.out.println("Seu total de pontos: " + app.conversionViewsForPoints());
        System.out.println("Suas passagens: " + app.conversionPointsToTickets());
        System.out.println("Digite a distância percorrida:");
        double km = sc.nextDouble();
        System.out.println("Digite quantidade de passageiros do ônibus:");
        int passengers = sc.nextInt();
        double saved = gas.calculateSavings(km, passengers);
        System.out.println("CO2 economizado: " + saved + " g");
    }
}