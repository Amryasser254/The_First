package Man;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.media.opengl.GLCanvas;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Anim extends JFrame {

    private Animator animator;
    private GLCanvas glcanvas;

    public static void main(String[] args) {
        new Anim();
    }

    public Anim() {
        AnimListener listener = new AnimGLEventListener5();

        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        animator = new FPSAnimator(glcanvas, 120);
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!animator.isAnimating()) {
                    animator.start(); // بدء الرسوم المتحركة
                    startButton.setText("Stop Game");
                } else {
                    animator.stop(); // إيقاف الرسوم المتحركة
                    startButton.setText("Start Game");
                }
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(startButton);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        setTitle("Anim Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}
