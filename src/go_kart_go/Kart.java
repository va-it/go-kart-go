package go_kart_go;

import javax.swing.*;

public class Kart {
    private int speed;
    private int xPosition;
    private int yPosition;
    private final int player;
    private int imageIndex;
    private boolean winner = false;
    private int checkPoint = 0;
    private int lap = 0;
    private boolean crashed = false;
    private SoundsManager soundsManager;
    private final ImageIcon[] images = new ImageIcon[NUMBER_OF_IMAGES];
    public static final int NUMBER_OF_IMAGES = 16;
    public final int IMAGE_SIZE = 50;

    public Kart(String colour, int player) {
        // capitalise the first letter of the string (https://stackoverflow.com/a/3904607)
        String colourUppercase = colour.substring(0, 1).toUpperCase() + colour.substring(1);
        String image = "kart" + colourUppercase;

        this.speed = 0;
        this.player = player;

        // load images into an array
        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            this.images[i] = new ImageIcon(
                    getClass().getResource(HelperClass.images + image + i + ".png")
            );
        }
        // initial position of the car is pointing left
        this.imageIndex = 12;
        soundsManager = new SoundsManager("Speed0");
        soundsManager.playSoundInLoop();

    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
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

    public int getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(int checkPoint) {
        this.checkPoint = checkPoint;
    }

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public int getPlayer() {
        return player;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    public void setNextXPosition() {
        // multiple cases as shown in docs:
        // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
        switch (this.getImageIndex()) {
            case 1:
            case 7:
                this.setXPosition(this.getXPosition() + ((this.getSpeed() * 10) / 100)); // + 10%
                break;
            case 2:
            case 6:
                this.setXPosition(this.getXPosition() + ((this.getSpeed() * 20) / 100)); // + 20%
                break;
            case 3:
            case 4:
            case 5:
                this.setXPosition(this.getXPosition() + ((this.getSpeed() * 30) / 100)); // + 30%
                break;
            case 9:
            case 15:
                this.setXPosition(this.getXPosition() - ((this.getSpeed() * 10) / 100)); // - 10%
                break;
            case 10:
            case 14:
                this.setXPosition(this.getXPosition() - ((this.getSpeed() * 20) / 100)); // - 20%
                break;
            case 11:
            case 12:
            case 13:
                this.setXPosition(this.getXPosition() - ((this.getSpeed() * 30) / 100)); // - 30%
                break;
            default:
                // 0 and 8, no change in position
                break;
        }
    }

    public void setNextYPosition() {
        switch (this.getImageIndex()) {
            case 0:
            case 1:
            case 15:
                this.setYPosition(this.getYPosition() - ((this.getSpeed() * 30) / 100)); // -30%
                break;
            case 2:
            case 14:
                this.setYPosition(this.getYPosition() - ((this.getSpeed() * 20) / 100)); // - 20%
                break;
            case 3:
            case 13:
                this.setYPosition(this.getYPosition() - ((this.getSpeed() * 10) / 100)); // - 10%
                break;
            case 5:
            case 11:
                this.setYPosition(this.getYPosition() + ((this.getSpeed() * 10) / 100)); // + 10%
                break;
            case 6:
            case 10:
                this.setYPosition(this.getYPosition() + ((this.getSpeed() * 20) / 100)); // + 20%
                break;
            case 7:
            case 8:
            case 9:
                this.setYPosition(this.getYPosition() + ((this.getSpeed() * 30) / 100)); // + 30%
                break;
            default:
                // 4 and 12, no change in position
                break;
        }
    }

    public void accelerate() {
        this.setSpeed(this.getSpeed() + 10);
        playSpeedSound();
    }

    public void decelerate() {
        if (this.getSpeed() > -10) {
            // kart can only reverse at 10 speed
            this.setSpeed(this.getSpeed() - 10);
            playSpeedSound();
        }
    }

    public void stop() {
        this.setSpeed(0);
        soundsManager.stopSound();
    }

    public void playSpeedSound() {
        soundsManager.stopSound();
        soundsManager = new SoundsManager("Speed" + Math.abs(this.getSpeed())); //play speed10 when reversing
        soundsManager.playSoundInLoop();
    }

    public void stopSpeedSound() {
        soundsManager.stopSound();
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

    public void updateCheckpoint() {
        if (this.kartIsOnBottomRoad() && this.getXPosition() <= RaceTrack.CHECKPOINTS[0]) {
            if (this.getCheckPoint() == 0 || this.getCheckPoint() == 4) {
                // only update checkpoint if the kart is going through them in order
                this.setCheckPoint(1);
                // here should increment lap only once
                if (this.getLap() == HelperClass.NUMBER_OF_LAPS) {
                    // Stop the game, this go_kart_go.Kart won the race
                    this.setWinner(true);
                } else {
                    this.setLap(this.getLap() + 1);
                }
                return;
            }
        }
        // kart is in left road
        if (this.kartIsOnLeftRoad() && this.getYPosition() <= RaceTrack.CHECKPOINTS[1]) {
            if (this.getCheckPoint() == 1) {
                this.setCheckPoint(2);
            }
            return;
        }
        // kart is in top road
        if (this.kartIsOnTopRoad() && this.getXPosition() >= RaceTrack.CHECKPOINTS[0]) {
            if (this.getCheckPoint() == 2) {
                this.setCheckPoint(3);
            }
            return;
        }
        // kart is in right road
        if (this.kartIsOnRightRoad() && this.getYPosition() >= RaceTrack.CHECKPOINTS[1]) {
            if (this.getCheckPoint() == 3) {
                this.setCheckPoint(4);
            }
        }
    }

    public boolean kartIsOnBottomRoad() {
        return this.getYPosition() >= RaceTrack.INNER_BOTTOM_EDGE && this.getYPosition() <= RaceTrack.OUTER_BOTTOM_EDGE;
    }

    public boolean kartIsOnTopRoad() {
        return this.getYPosition() >= RaceTrack.OUTER_TOP_EDGE && this.getYPosition() <= RaceTrack.INNER_TOP_EDGE;
    }

    public boolean kartIsOnLeftRoad() {
        return this.getXPosition() >= RaceTrack.OUTER_LEFT_EDGE && this.getXPosition() <= RaceTrack.INNER_LEFT_EDGE;
    }

    public boolean kartIsOnRightRoad() {
        return this.getXPosition() <= RaceTrack.OUTER_RIGHT_EDGE && this.getXPosition() >= RaceTrack.INNER_RIGHT_EDGE;
    }

}
