import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MyKeyListener implements KeyListener {

    MainWindowPanel mainWindowPanel;

    public MyKeyListener(MainWindowPanel panel) {

        mainWindowPanel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w':
                mainWindowPanel.blueKart.accelerate();
                break;
            case 's':
                mainWindowPanel.blueKart.decelerate();
                break;
            case 'a':
                mainWindowPanel.blueKart.turnLeft();
                break;
            case 'd':
                mainWindowPanel.blueKart.turnRight();
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                mainWindowPanel.redKart.accelerate();
                break;
            case KeyEvent.VK_DOWN:
                mainWindowPanel.redKart.decelerate();
                break;
            case KeyEvent.VK_LEFT:
                mainWindowPanel.redKart.turnLeft();
                break;
            case KeyEvent.VK_RIGHT:
                mainWindowPanel.redKart.turnRight();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
