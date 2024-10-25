package org.agrosoft.ControlDePedidos;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.agrosoft.ControlDePedidos.GUI.MainWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class ControlDePedidosApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(1);

		Thread springBootThread = new Thread(() -> {
			SpringApplication.run(ControlDePedidosApplication.class, args);
			latch.countDown();
		});
		springBootThread.start();

		SwingUtilities.invokeLater(() -> {
            try {
				UIManager.setLookAndFeel(new FlatMacDarkLaf());
                latch.await();
				JFrame mainWindow = new MainWindow();
				mainWindow.setVisible(true);
            } catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
                System.exit(1);
            }
		});
	}

}
