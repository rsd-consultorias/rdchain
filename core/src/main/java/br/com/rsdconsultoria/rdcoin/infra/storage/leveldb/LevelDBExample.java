package br.com.rsdconsultoria.rdcoin.infra.storage.leveldb;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

public class LevelDBExample {
    public static void main(String[] args) {
        Options options = new Options();
        options.createIfMissing(true);

        DBFactory factory = Iq80DBFactory.factory;
        DB db = null;

        try {
            db = factory.open(new File("/Users/rafaeldias/repositories/rdchain/rdchain/core/leveldb"), options);

            try {
                // Escrevendo dados
                db.put("chave1".getBytes(), "valor1".getBytes());
            
                // Lendo dados
                byte[] valor = db.get("chave1".getBytes());
                System.out.println(new String(valor));
            
                // Deletando dados
                db.delete("chave1".getBytes());
            
            } catch (Exception e) {
                e.printStackTrace();
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
