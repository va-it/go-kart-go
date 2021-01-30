import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MyKeyListener implements KeyListener {

    public MyKeyListener() { }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        switch( keyChar ) {
            case 'w':
                System.out.println("BLUE UP");
                break;
            case 's':
                System.out.println("BLUE DOWN");
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
                System.out.println("RED UP");
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("RED DOWN");
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
