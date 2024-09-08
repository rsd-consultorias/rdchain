```bash
# Gerar jar executável
./mvnw clean compile assembly:single

# Executar
java -jar target/rd-coin-1.0-SNAPSHOT-jar-with-dependencies.jar 5001

# Executar CLI
java -cp target/rd-coin-1.0-SNAPSHOT-jar-with-dependencies.jar br.com.rsdconsultoria.rdcoin.cli.ERPCLI

# O comando nohup permite que você execute um processo que continua rodando mesmo após você sair do terminal. O & no final do comando coloca o processo em segundo plano.
nohup java -jar target/rd-coin-1.0-SNAPSHOT-jar-with-dependencies.jar 5001 &

# Verificar processos em execução:
ps aux | grep rd-coin-1.0-SNAPSHOT-jar-with-dependencies.jar

# Verificar o arquivo de saída nohup: O comando nohup redireciona a saída padrão e de erro para um arquivo chamado nohup.out por padrão.
tail -f nohup.out
```