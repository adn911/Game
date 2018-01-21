package org.galib.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bakhtiar.Galib on 21/01/2018.
 */
public class Animation {

    private String name;
    private long frameTime;
    private int currentFrameIndex;
    private Direction animationDirection;
    private int totalFrames;

    private boolean running, loop;

    private List<Frame> frames;

    public Animation(String name, List<Frame> frames) {
        this(name, frames, Direction.FORWARD, false);
    }

    public Animation(String name, List<Frame> frames, Direction animationDirection, boolean loop) {
        this.name = name;
        this.frames = frames;
        this.totalFrames = frames.size();
        this.animationDirection = animationDirection;
        this.loop = loop;
        this.frameTime = 0;
        this.currentFrameIndex = (animationDirection == Direction.FORWARD) ? 0 : totalFrames - 1;
    }

    public Direction getAnimationDirection() {
        return animationDirection;
    }

    public void setAnimationDirection(Direction animationDirection) {
        this.animationDirection = animationDirection;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void start() {
        if (running || frames.size() == 0) {
            return;
        }

        running = true;
    }

    public void stop() {
        if (!running || frames.size() == 0) {
            return;
        }

        running = false;
    }

    public void restart() {
        if (frames.size() == 0) {
            return;
        }

        this.running = true;
        this.frameTime = 0;
        this.currentFrameIndex = (animationDirection == Direction.REVERSE) ? 0 : totalFrames - 1;
    }

    public BufferedImage getSprite() {
        return frames.get(currentFrameIndex).getImage();
    }

    public void update(long elapsedTime) {
        if (running) {
            frameTime += elapsedTime;

            while (frameTime > frames.get(currentFrameIndex).duration) {
                frameTime -= frames.get(currentFrameIndex).duration;
                currentFrameIndex += animationDirection.value;
            }

            if (currentFrameIndex > totalFrames - 1 || currentFrameIndex < 0) {
                if (loop) {
                    currentFrameIndex = (animationDirection == Direction.FORWARD) ? 0 : totalFrames - 1;
                } else {
                    stop();
                }
            }
        }
    }

    public static Animation generateFormSpriteSheet(String name, BufferedImage spriteSheet,
                                                    int rows, int cols,
                                                    int frameWidth, int frameHeight,
                                                    int duration) {
        List<Animation.Frame> frames = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BufferedImage subimage = spriteSheet.getSubimage(j * frameWidth, i * frameHeight, frameWidth, frameHeight);
                Animation.Frame frame = new Animation.Frame(subimage, duration);
                frames.add(frame);
            }
        }

        return new Animation(name, frames);
    }

    public static class Frame {
        private BufferedImage image;
        private int duration;

        public Frame(BufferedImage image, int duration) {
            this.image = image;
            this.duration = duration;
        }

        public BufferedImage getImage() {
            return image;
        }

        public void setImage(BufferedImage image) {
            this.image = image;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }

    enum Direction {
        FORWARD(1), REVERSE(-1);

        int value;

        Direction(int value) {
            this.value = value;
        }
    }
}