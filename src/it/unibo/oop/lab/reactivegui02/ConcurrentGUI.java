package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * This is a first example on how to realize a reactive GUI.
 */
    public final class ConcurrentGUI extends JFrame {

        private static final long serialVersionUID = 1L;
        private static final double WIDTH_PERC = 0.2;
        private static final double HEIGHT_PERC = 0.1;
        private final JLabel display = new JLabel();
        private final JButton up = new JButton("up");
        private final JButton down = new JButton("down");
        private final JButton stop = new JButton("stop");
        /**
         * Builds a new CGUI.
         */
        public ConcurrentGUI() {
            super();
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            final JPanel panel = new JPanel();
            panel.add(display);
            panel.add(stop);
            panel.add(up);
            panel.add(down);
            this.getContentPane().add(panel);
            this.setVisible(true);
        final Agent agent = new Agent();
        new Thread(agent).start();
        /*
         * Register a listener that stops it
         */
        stop.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Agent should be final
                agent.stopCounting();
            }
        });
        up.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Agent should be final
                agent.upCounting();
            }
        });

        down.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Agent should be final
                agent.downCounting();
            }
        });

    }
    private class Agent implements Runnable{
        private volatile boolean stop;
        private volatile boolean up=true;
        private volatile int count;
        public void run() {
            while(!this.stop) {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            // This will happen in the EDT: since i'm reading counter it needs to be volatile.
                            ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.count));
                        }
                    });
                    if(up==true) {
                        this.count++;
                        Thread.sleep(100);
                    }
                        else {
                            this.count--;
                            Thread.sleep(100);
                        }
                }
                catch (InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
            }
            }
        }
                public void stopCounting() {
                    this.stop=true;
                }
                public void upCounting() {
                    this.up=true;
                }
                public void downCounting() {
                    this.up=false;
                }
       }


}
