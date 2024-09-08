package br.com.rsdconsultoria.rdcoin.application;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class EVMInterpreter {
    private Stack<BigInteger> stack = new Stack<>();
    private Map<Integer, String> program = new HashMap<>();
    private int instructionPointer = 0;

    public void loadProgram(Map<Integer, String> program) {
        this.program = program;
    }

    public void executeNext() {
        if (instructionPointer < program.size()) {
            String instruction = program.get(instructionPointer);
            String[] parts = instruction.split(" ");
            String opcode = parts[0];
            BigInteger[] args = new BigInteger[parts.length - 1];
            for (int i = 1; i < parts.length; i++) {
                args[i - 1] = new BigInteger(parts[i]);
            }
            execute(opcode, args);
            instructionPointer++;
        }
    }

    /**
     * <pre>
     * - ADD: Remove os dois valores do topo da pilha, soma-os e empurra o resultado
     * de volta para a pilha.
     * - MUL: Remove os dois valores do topo da pilha, multiplica-os e empurra o
     * resultado de volta para a pilha.
     * - SUB: Remove os dois valores do topo da pilha, subtrai o segundo valor do
     * primeiro e empurra o resultado de volta para a pilha.
     * - DIV: Remove os dois valores do topo da pilha, divide o primeiro valor pelo
     * segundo e empurra o resultado de volta para a pilha.
     * - MOD: Remove os dois valores do topo da pilha, calcula o módulo (resto da
     * divisão) do primeiro valor pelo segundo e empurra o resultado de volta para a
     * pilha.
     * - PUSH: Empurra um valor fornecido (argumento) para o topo da pilha.
     * - POP: Remove o valor do topo da pilha.
     * - AND: Remove os dois valores do topo da pilha, realiza a operação lógica AND
     * bit a bit e empurra o resultado de volta para a pilha.
     * - OR: Remove os dois valores do topo da pilha, realiza a operação lógica OR bit
     * a bit e empurra o resultado de volta para a pilha.
     * - XOR: Remove os dois valores do topo da pilha, realiza a operação lógica XOR
     * bit a bit e empurra o resultado de volta para a pilha.
     * - NOT: Remove o valor do topo da pilha, realiza a operação lógica NOT bit a bit
     * e empurra o resultado de volta para a pilha.
     * - JUMP: Define o ponteiro de instrução para o valor fornecido (argumento).
     * - LT: Remove os dois valores do topo da pilha, compara-os e empurra 1 para a
     * pilha se o primeiro valor for menor que o segundo, caso contrário, empurra 0.
     * - GT: Remove os dois valores do topo da pilha, compara-os e empurra 1 para a
     * pilha se o primeiro valor for maior que o segundo, caso contrário, empurra 0.
     * - EQ: Remove os dois valores do topo da pilha, compara-os e empurra 1 para a
     * pilha se os valores forem iguais, caso contrário, empurra 0.
     * - DUP: Duplica o valor do topo da pilha.
     * - SWAP: Troca os dois valores do topo da pilha.
     * </pre>
     * 
     * @param opcode
     * @param args
     */
    public void execute(String opcode, BigInteger... args) {
        switch (opcode) {
            case "ADD":
                stack.push(stack.pop().add(stack.pop()));
                break;
            case "MUL":
                stack.push(stack.pop().multiply(stack.pop()));
                break;
            case "SUB":
                stack.push(stack.pop().subtract(stack.pop()));
                break;
            case "DIV":
                stack.push(stack.pop().divide(stack.pop()));
                break;
            case "MOD":
                stack.push(stack.pop().mod(stack.pop()));
                break;
            case "PUSH":
                stack.push(args[0]);
                break;
            case "POP":
                stack.pop();
                break;
            case "AND":
                stack.push(stack.pop().and(stack.pop()));
                break;
            case "OR":
                stack.push(stack.pop().or(stack.pop()));
                break;
            case "XOR":
                stack.push(stack.pop().xor(stack.pop()));
                break;
            case "NOT":
                stack.push(stack.pop().not());
                break;
            case "JMP":
                instructionPointer = args[0].intValue();
                break;
            case "JMPG":
                if (stack.pop().compareTo(stack.peek()) < 0) {
                    instructionPointer = args[0].intValue();
                }
                break;
            case "JMPL":
                if (stack.pop().compareTo(stack.peek()) > 0) {
                    instructionPointer = args[instructionPointer].intValue();
                }
                break;
            case "LT":
                stack.push(stack.pop().compareTo(stack.pop()) < 0 ? BigInteger.ONE : BigInteger.ZERO);
                break;
            case "GT":
                stack.push(stack.pop().compareTo(stack.pop()) > 0 ? BigInteger.ONE : BigInteger.ZERO);
                break;
            case "EQ":
                stack.push(stack.pop().equals(stack.pop()) ? BigInteger.ONE : BigInteger.ZERO);
                break;
            case "DUP":
                stack.push(stack.peek());
                break;
            case "SWAP":
                BigInteger top1 = stack.pop();
                BigInteger top2 = stack.pop();
                stack.push(top1);
                stack.push(top2);
                break;
            case "CREATE_TX":
                createTransaction(args[0].toString(), top());
                // instructionPointer++; // Skip the next argument
                break;
            default:
                throw new UnsupportedOperationException("Opcode não suportado: " + opcode);
        }
    }

    public BigInteger top() {
        return stack.peek();
    }

    private void createTransaction(String to, BigInteger value) {
        // TransactionReceipt receipt = transactionManager.sendTransaction(
        // DefaultGasProvider.GAS_PRICE,
        // DefaultGasProvider.GAS_LIMIT,
        // to.toString(),
        // "",
        // value);
        System.out.println("Transaction complete: " + value.toString() + " will be sent to " + to);
    }

    public static void main(String[] args) {
        EVMInterpreter interpreter = new EVMInterpreter();
        Map<Integer, String> program = new HashMap<>();
        Integer i = -1;

        program.put(i += 1, "PUSH 500"); // Atribui um saldo inicial
        program.put(i += 1, "PUSH 100"); // Atribui valor de taxa
        program.put(i += 1, "CREATE_TX 1"); // Cria transação de taxa enquanto o saldo for maior que 100
        program.put(i += 1, "SWAP"); // Inverte a stack de: [100, 500] para: [500, 100]
        program.put(i += 1, "SUB"); // Subtrai 100 do saldo (500 - 100)
        program.put(i += 1, "PUSH 100"); // Adiciona valor para comparar
        program.put(i += 1, "JMPG 0"); // Se saldo maior que 100, vai para instrução 0
        program.put(i += 1, "CREATE_TX 2"); // Cria transação com saldo remanescente

        interpreter.loadProgram(program);

        var executions = 0;
        while (interpreter.instructionPointer < program.size()) {
            interpreter.executeNext();
            executions += 1;

            if (executions > program.size() * 300) {
                System.out.println("TOO MANY ITERATIONS. STOP FORCED. INACCURATE VALUE.");
                break;
            }
        }

        System.out.println("Resultado: " + interpreter.top()); // Deve imprimir 30
    }
}
