package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProgressBar extends JFrame {

    private JProgressBar progressBar;
    private JButton startButton;

    public ProgressBar() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Progress Bar Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Cria uma barra de progresso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true); // Mostra o progresso como uma string
        progressBar.setValue(0);

        // Cria um botão para iniciar a simulação
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false); // Desativa o botão enquanto a simulação está em andamento
                simulateProgress(); // Inicia a simulação de progresso
            }
        });

        // Adiciona a barra de progresso e o botão à janela
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        add(panel);
    }

    // Simula o progresso de uma tarefa
    private void simulateProgress() {
        // Cria uma nova thread para simular o progresso
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    final int progress = i;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            progressBar.setValue(progress); // Atualiza o valor da barra de progresso
                        }
                    });
                    try {
                        // Simula um atraso de 100ms entre cada atualização
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                // Reativa o botão quando a simulação estiver concluída
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        startButton.setEnabled(true);
                    }
                });
            }
        });
        thread.start(); // Inicia a thread
    }
}

