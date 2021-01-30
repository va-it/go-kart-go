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
        switch( keyChar ) {
            case 'w':
                System.out.println("BLUE UP");
                myPanel.speedPlusBlue();
                break;
            case 's':
                System.out.println("BLUE DOWN");
                myPanel.speedMinusBlue();
                break;
            case 'a':
                System.out.println("BLUE LEFT");
                break;
            case 'd':
                System.out.println("BLUE RIGHT");
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                myPanel.speedPlusRed();
                break;
            case KeyEvent.VK_DOWN:
                myPanel.speedMinusRed();
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("RED LEFT");
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("RED RIGHT");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}
