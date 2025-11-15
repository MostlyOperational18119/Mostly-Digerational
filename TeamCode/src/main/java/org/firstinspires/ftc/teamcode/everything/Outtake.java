package org.firstinspires.ftc.teamcode.everything;

public class Outtake {
    private final Methods methods;
    private final Indexer indexer;

    private int maxClicks =  6000;
    private int targetClicks = 3000;
    private int tolerance = 50;

    public Outtake(Methods methods, Indexer indexer)  {
        this.methods = methods;
        this.indexer = indexer;
    }

    public void update() {
        int clicks = methods.motorFR.getCurrentPosition();

        if (Math.abs(targetClicks - clicks) <= tolerance) {
            methods.launcherYaw.setPower(0);
        } else {
            methods.outtakePower = -(double) (targetClicks - clicks)/3000;
            methods.launcherYaw.setPower(methods.outtakePower);
        }
    }

    public void setRotationPosition(double target) {
        target = Math.max(0, Math.min(1, target));
        targetClicks = (int) (maxClicks * target);
    }
}
