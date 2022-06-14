package com.lw.rocksdb;

import org.rocksdb.Checkpoint;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

public class RocksDBDemo {

    private static byte[] KEY = "key".getBytes();
    private static String ROCKSDB_DIR = "F:\\datagen\\rocksdb";
    private static String ROCKSDB_CHECKPOINT_DIR = "F:\\datagen\\ck";

    public static void main(String[] args) throws RocksDBException {

        RocksDB db = RocksDB.open(ROCKSDB_DIR);
        db.compactRange();
        // db.put(KEY, "value".getBytes());
        System.out.println(new String(db.get(KEY)));
        Checkpoint checkpoint = Checkpoint.create(db);
        checkpoint.createCheckpoint(ROCKSDB_CHECKPOINT_DIR);
    }
}
