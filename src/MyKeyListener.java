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
        int player = 2;
        switch( keyChar ) {
            case 'w':
                System.out.println("BLUE UP");
                myPanel.speedPlus(player);
                break;
            case 's':
                System.out.println("BLUE DOWN");
                myPanel.speedMinus(player);
                break;
            case 'a':
                System.out.println("BLUE LEFT");
                myPanel.turnLeft(player);
                break;
            case 'd':
                System.out.println("BLUE RIGHT");
                myPanel.turnRight(player);
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int player = 1;
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                myPanel.speedPlus(player);
                break;
            case KeyEvent.VK_DOWN:
                myPanel.speedMinus(player);
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("RED LEFT");
                myPanel.turnLeft(player);
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("RED RIGHT");
                myPanel.turnRight(player);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}
