import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MyKeyListener implements KeyListener {

    MainWindowPanel myPanel;

    public MyKeyListener(MainWindowPanel panel) {

        myPanel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int player = 2; // blue
        switch( keyChar ) {
            case 'w':
                myPanel.speedPlus(player);
                break;
            case 's':
                myPanel.speedMinus(player);
                break;
            case 'a':
                myPanel.turnLeft(player);
                break;
            case 'd':
                myPanel.turnRight(player);
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int player = 1; // red
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                myPanel.speedPlus(player);
                break;
            case KeyEvent.VK_DOWN:
                myPanel.speedMinus(player);
                break;
            case KeyEvent.VK_LEFT:
                myPanel.turnLeft(player);
                break;
            case KeyEvent.VK_RIGHT:
                myPanel.turnRight(player);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}
