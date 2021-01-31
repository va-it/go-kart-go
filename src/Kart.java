import javax.swing.*;
import java.io.File;
import java.util.Locale;

public class Kart {
    private int speed;
    private int xPosition;
    private int yPosition;
    private String colour;
    private String image;
    private int imageIndex;
    private ImageIcon[] images = new ImageIcon[NUMBER_OF_IMAGES];
    public static final int NUMBER_OF_IMAGES = 16;

    public Kart(String colour) {
        this.speed = 0;
        this.colour = colour;
        this.image = "kart" + this.colour.toUpperCase(Locale.ROOT);

        // load images into an array
        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            this.images[i] = new ImageIcon(
                    getClass().getResource("images" + File.separator + this.image + i + ".png")
            );
        }
        // initial position of the car is pointing left
        this.imageIndex = 12;

    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public ImageIcon getImageAtCurrentIndex() {
        return this.images[this.imageIndex];
    }

    public void setNextXPosition() {
        // multiple cases as shown in docs:
        // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
        switch (this.getImageIndex()) {
            case 1:
            case 7:
                this.setxPosition(this.getxPosition() + 1 * ((this.getSpeed() * 10) / 100)); // + 10%
                break;
            case 2:
            case 6:
                this.setxPosition(this.getxPosition() + 1 * ((this.getSpeed() * 20) / 100)); // + 20%
                break;
            case 3:
            case 4:
            case 5:
                this.setxPosition(this.getxPosition() + 1 * ((this.getSpeed() * 30) / 100)); // + 30%
                break;
            case 9:
            case 15:
                this.setxPosition(this.getxPosition() - 1 * ((this.getSpeed() * 10) / 100)); // - 10%
                break;
            case 10:
            case 14:
                this.setxPosition(this.getxPosition() - 1 * ((this.getSpeed() * 20) / 100)); // - 20%
                break;
            case 11:
            case 12:
            case 13:
                this.setxPosition(this.getxPosition() - 1 * ((this.getSpeed() * 30) / 100)); // - 30%
                break;
            default:
                // 0 and 8, no change in position
                break;
        }
    }

    public void setNextYPosition() {
        switch (this.getImageIndex()) {
            case 0: case 1: case 15:
                this.setyPosition(this.getyPosition() - 1 * ((this.getSpeed()*30)/100)); // -30%
                break;
            case 2: case 14:
                this.setyPosition(this.getyPosition() - 1 * ((this.getSpeed()*20)/100)); // - 20%
                break;
            case 3: case 13:
                this.setyPosition(this.getyPosition() - 1 * ((this.getSpeed()*10)/100)); // - 10%
                break;
            case 5: case 11:
                this.setyPosition(this.getyPosition() + 1 * ((this.getSpeed()*10)/100)); // + 10%
                break;
            case 6: case 10:
                this.setyPosition(this.getyPosition() + 1 * ((this.getSpeed()*20)/100)); // + 20%
                break;
            case 7: case 8: case 9:
                this.setyPosition(this.getyPosition() + 1 * ((this.getSpeed()*30)/100)); // + 30%
                break;
            default:
                //4 and 12, no change in position
                break;
        }
    }

    public void accelerate() {
        this.setSpeed(this.getSpeed() + 10);
    }

    public void decelerate() {
        this.setSpeed(this.getSpeed() - 10);

    }

    public void turnLeft() {
        if (this.getSpeed() != 0) {
            // only turn car when it's moving
            if (this.getImageIndex() == 0) {
                // if the index is 0 then jump to the last image in the array (can't go negative index)
                this.setImageIndex(Kart.NUMBER_OF_IMAGES - 1);
            } else {
                this.setImageIndex(this.getImageIndex() - 1);
            }
        }
    }

    public void turnRight() {
        if (this.getSpeed() != 0) {
            // % NUMBER_OF_IMAGES ensures that the first image is prepared when we reach the end of the array
            this.setImageIndex((this.getImageIndex() + 1) % Kart.NUMBER_OF_IMAGES);
        }
    }
}
