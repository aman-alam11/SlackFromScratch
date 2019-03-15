package edu.northeastern.ccs.im.server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.business.logic.JsonMessageHandlerFactory;

import static junit.framework.TestCase.assertTrue;


public class PrattleTest {

    @Mock
    JsonMessageHandlerFactory messageHandlerFactory;

    private static boolean isServerRunning;

    @BeforeClass
    public static void startServer() {
        isServerRunning = true;
        Runnable server = new Runnable() {

            @Override
            public void run() {
                Prattle.main(new String[0]);
            }
        };
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    @Test
    public void test() {
        assertTrue(isServerRunning);
    }


    @AfterClass
    public static void tearDown() {
        Prattle.stopServer();
    }
}
