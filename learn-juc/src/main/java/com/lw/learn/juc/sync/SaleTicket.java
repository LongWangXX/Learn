package com.lw.learn.juc.sync;

/**
 * 三个售票員
 * 一起卖30张票
 */
public class SaleTicket {
    public static void main(String[] args) {
        SaleTicketC saleTicketC = new SaleTicketC();
        for (int i = 0; i < 3; i++)
            new Thread(
                    () -> {
                        for (int i1 = 0; i1 < 30; i1++) saleTicketC.sale();
                    })
                    .start();
    }
}

class SaleTicketC {
    private int cnt = 30;

    public synchronized void sale() {
        if (cnt <= 0) {
            System.out.println("票已售完");
            return;
        }
        System.out.println(Thread.currentThread().getName() + "买票 1,票数剩余" + (--cnt));
    }
}
