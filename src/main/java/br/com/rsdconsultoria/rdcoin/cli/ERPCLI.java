package br.com.rsdconsultoria.rdcoin.cli;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "erp", mixinStandardHelpOptions = true, version = "RD Coin CLI 0.1",
        description = "")
public class ERPCLI implements Callable<Integer> {

    private static List<String> clientes = new ArrayList<>();
    private static List<String> produtos = new ArrayList<>();
    private static List<String> pedidos = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ERPCLI erpcli = new ERPCLI();
        CommandLine cmd = new CommandLine(erpcli);

        Terminal terminal = TerminalBuilder.builder().system(true).build();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter("add-cliente", "list-clientes", "add-produto", "list-produtos", "add-pedido", "list-pedidos", "exit"))
                .build();

        System.out.println("Bem-vindo ao RD Coin CLI! Digite 'help' para ver os comandos disponíveis.");

        while (true) {
            cmd.clearExecutionResults();
            String input = reader.readLine("> ");
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Saindo do RD Coin CLI.");
                break;
            }
            cmd.execute(input.split(" "));
        }
    }

    @Override
    public Integer call() {
        return 0;
    }

    @Command(name = "add-cliente", description = "Adiciona um novo cliente")
    void addCliente(@Parameters(paramLabel = "<nome>", description = "Nome do cliente") String nome) {
        clientes.add(nome);
        System.out.println("Cliente adicionado: " + nome);
    }

    @Command(name = "list-clientes", description = "Lista todos os clientes")
    void listClientes() {
        System.out.println("Clientes:");
        for (String cliente : clientes) {
            System.out.println(" - " + cliente);
        }
    }

    @Command(name = "add-produto", description = "Adiciona um novo produto")
    void addProduto(@Parameters(paramLabel = "<nome>", description = "Nome do produto") String nome) {
        produtos.add(nome);
        System.out.println("Produto adicionado: " + nome);
    }

    @Command(name = "list-produtos", description = "Lista todos os produtos")
    void listProdutos() {
        System.out.println("Produtos:");
        for (String produto : produtos) {
            System.out.println(" - " + produto);
        }
    }

    @Command(name = "add-pedido", description = "Adiciona um novo pedido")
    void addPedido(@Parameters(paramLabel = "<detalhes>", description = "Detalhes do pedido") String detalhes) {
        pedidos.add(detalhes);
        System.out.println("Pedido adicionado: " + detalhes);
    }

    @Command(name = "list-pedidos", description = "Lista todos os pedidos")
    void listPedidos() {
        System.out.println("Pedidos:");
        for (String pedido : pedidos) {
            System.out.println(" - " + pedido);
        }
    }
}
