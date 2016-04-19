package com.vroy.trapper.gameContent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vroy.trapper.R;
import com.vroy.trapper.menuscreens.DeathScreenActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends View implements View.OnTouchListener {

    /*
    OVERVIEW:

    array of points "linePoints" stores all locations of user touching screen
    that are captured by system.

    Each time a new point is added to "linePoints" I draw a path from the previous point
    to the new point. (Sidenote: While this does make the line look very smooth it can still look odd sometimes)

    The game also checks for intersections in the line to see if the line has made a
    polygon. I do this because this is important for a feature that will be implemented.

    The system then draws the path on screen.

    The system also checks if the user has lifted their finger off the screen,
    if the user has then the system deletes the current line on screen and resets all variables.

    TO BE IMPLEMENTED:

    If the line has formed a polygon then the game will check if that polygon contains certain
    objects that will randomly spawn onscreen.


    PROBLEMS:


    */
    //endregion


    // General variables.
    private static int screenWidth;
    private static int screenHeight;
    public static boolean screenPressed; //Might not need.
    private static boolean callOnDraw;
    private static int horizontalMargin;
    private static int verticalMargin;

        //Time variables.
        private static long startTime; //This variable is used in conjunction with the
                                       //elapsedTime() method to determine if the user
                                       // has been drawing a line for more then "X" seconds.
    //Game variables.
    private static int orbRadius; 

    private List<TimeStampedPoint> linePoints; //The list holds all captured points.
    private List<TimeStampedPoint> validPoints;
    private static List<Polygon> polygons; //This list stores all the polygons created by the line.
    public static List<Orb> friendlyOrbs;
    public static List<Orb> enemyOrbs;
    private static List<Long> friendlyTimesTillAdd;
    private static List<Long> enemyTimesTillAdd;
    public static int forbsKilled;


    private int listIndex;

    private static long drawCallTime;
    public static long pauseStartTime;
    private static int lifePoints;
    public static int startOrbs;



    public static Timer TIMER = new Timer();
    public static int friendlyOrbSpawnInterval;
    public static int enemyOrbSpawnInterval;
    private ListIterator<Orb> orbIterator;
    private ListIterator<Long> timeIterator;

    private boolean firstPoint; //If firstPoint is true then that means is 1st point in current line.
    private Path linePath; //The path that the canvas draws.

    private static int orbLifeMeterStartPos;
    private static int orbLifeMeterXGap;

    public static int totalOrbs;

    private static Random rand;

    //Paints
    Paint black = new Paint();
    Paint blue = new Paint();
    Paint yellow = new Paint();
    Paint red = new Paint();
    public static Paint green = new Paint();
    Paint whitePathPaint = new Paint();
    Paint whitePathFill = new Paint();
    Paint bluePathPaint = new Paint();
    Paint bluePathFill = new Paint();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //region
        whitePathPaint.setARGB(255, 255, 255, 255);
        whitePathPaint.setStyle(Paint.Style.STROKE);
        whitePathPaint.setStrokeWidth(3);



        whitePathFill.setARGB(125, 255, 255, 255);
        whitePathFill.setStyle(Paint.Style.FILL);



        bluePathPaint.setARGB(255, 0, 128, 255);
        bluePathPaint.setStyle(Paint.Style.STROKE);
        bluePathPaint.setStrokeWidth(3);

        bluePathFill.setARGB(125, 0, 128, 255);
        bluePathFill.setStyle(Paint.Style.FILL);



        blue.setARGB(125, 0, 191, 255);
        yellow.setARGB(125, 255, 255, 0);
        red.setARGB(125, 255, 51, 51);
        green.setARGB(255,0,255,0);
        //endregion
        linePoints = new ArrayList();
        polygons = new ArrayList();
        friendlyOrbs = new ArrayList();
        enemyOrbs  = new ArrayList();
        friendlyTimesTillAdd = new ArrayList();
        enemyTimesTillAdd = new ArrayList();




        rand = new Random();
        linePath = new Path(); //Setting up initial path.


        GameView gameView = (GameView) findViewById(R.id.GameScreen); //Setting up onTouch listener.
        gameView.setOnTouchListener(this);


        forbsKilled = 0;
        drawCallTime = 0;
        friendlyOrbSpawnInterval = 4000;
        enemyOrbSpawnInterval = 5000;
        firstPoint = true;
        lifePoints = 3;
        callOnDraw = true;
        forbsKilled = 0;

        for(int i = 0; i <startOrbs; i ++) {
            enemyTimesTillAdd.add(SystemClock.uptimeMillis() + (500 + rand.nextInt((3000 - 500) + 1)));
            friendlyTimesTillAdd.add(SystemClock.uptimeMillis() + (500 + rand.nextInt((3000 - 500) + 1)));
        }


    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);




        screenHeight = getHeight();
        screenWidth = getWidth();
        orbRadius = screenHeight / 80;
        orbLifeMeterStartPos = screenWidth / 2 - orbRadius * 2;
        orbLifeMeterXGap = (orbRadius * 2) + orbRadius / 3;
        horizontalMargin = orbRadius * 7;
        verticalMargin = orbRadius * 10;


    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (callOnDraw) {

        drawCallTime = SystemClock.uptimeMillis();
        linePath.rewind();
        updatePoints();

        for (Polygon p : polygons) {
            canvas.drawPath(p.polyP, whitePathFill);
            //canvas.drawRect(p.minX,p.minY,p.maxX,p.maxY,red);
        }

        //region
        if (lifePoints <= 0) {
            callOnDraw = false;
            Context ctx = getContext();
            Intent intent = new Intent(ctx, DeathScreenActivity.class);
            intent.putExtra("gameScore", forbsKilled); //Might need to update later
            ctx.startActivity(intent);
        } else {
            int tempint = orbLifeMeterStartPos;
            for (int i = 0; i < lifePoints; i++) {
                canvas.drawCircle(tempint, orbRadius + orbRadius / 6, orbRadius, green);
                tempint = tempint + orbLifeMeterXGap;
            }

        }
        //endregion

        //region
        for (timeIterator = friendlyTimesTillAdd.listIterator(); timeIterator.hasNext(); ) {
            long l = timeIterator.next();

           outerIf: if (drawCallTime >= l) {

                int tempx = horizontalMargin + rand.nextInt(screenWidth - horizontalMargin*2);
                int tempy =  verticalMargin + rand.nextInt(screenHeight - verticalMargin*2);

                if(enemyOrbs.size() > 0) {

                    for(int i = 0; i < enemyOrbs.size(); i++) {
                        if(!orbSpawnComparer(enemyOrbs.get(i),tempx,tempy)) {
                            break  outerIf;
                        }
                    }

                    Point point = new Point(tempx,tempy);
                    for(Polygon p: polygons) {
                        if(IsPointInPolygon(point,p)) {
                            break outerIf;
                        }
                    }
                    friendlyOrbs.add(new Orb(tempx, tempy));
                    timeIterator.remove();


                } else {

                    Point point = new Point(tempx,tempy);
                    for(Polygon p: polygons) {
                        if(IsPointInPolygon(point,p)) {
                            break outerIf;
                        }
                    }
                    friendlyOrbs.add(new Orb(tempx,tempy));
                    timeIterator.remove();
                }
            }

        }
        for (timeIterator = enemyTimesTillAdd.listIterator(); timeIterator.hasNext(); ) {
            long l = timeIterator.next();

           outerIf: if (drawCallTime >= l) {

                int tempX = horizontalMargin + rand.nextInt(screenWidth - horizontalMargin*2);
                int tempY =  verticalMargin + rand.nextInt(screenHeight - verticalMargin*2);

                if(friendlyOrbs.size() > 0) {
                    for(int i = 0; i < friendlyOrbs.size(); i++) {
                        if (!orbSpawnComparer(friendlyOrbs.get(i), tempX, tempY)) {
                            break outerIf;
                        }
                    }


                    Point point = new Point(tempX,tempY);
                    for(Polygon p: polygons) {
                        if(IsPointInPolygon(point,p)) {
                            break outerIf;
                        }
                    }
                    enemyOrbs.add(new Orb(tempX, tempY));
                    timeIterator.remove();

                } else {
                    Point point = new Point(tempX,tempY);
                    for(Polygon p: polygons) {
                        if(IsPointInPolygon(point,p)) {
                            break outerIf;
                        }
                    }
                    enemyOrbs.add(new Orb(tempX, tempY));
                    timeIterator.remove();

                }

            }
        }
        //endregion

        //region
        for (orbIterator = friendlyOrbs.listIterator(); orbIterator.hasNext(); ) {
            Orb tempOrb = orbIterator.next();



           final float arcAngle = 360 -  ((tempOrb.totalExistenceTime - 4000.0f) / 4000.0f) * 360;
        //    final float arcAngle = 360 -  (tempOrb.totalExistenceTime  / 8000.0f) * 360;

            RectF arcRect = new RectF(tempOrb.x - orbRadius, tempOrb.y - orbRadius, tempOrb.x + orbRadius, tempOrb.y + orbRadius);



            //   canvas.drawRect(arcRect,red);
        //    Log.i("DEBUG_TAG",Float.toString(arcAngle));

            if (tempOrb.phase == 'B') {
                canvas.drawCircle(tempOrb.x, tempOrb.y, orbRadius, blue);
              //  canvas.drawArc(arcRect,270,arcAngle,true,blue);
            } else {
             //   canvas.drawCircle(tempOrb.x, tempOrb.y, orbRadius, yellow);
                canvas.drawArc(arcRect,270,arcAngle,true,yellow);
            }

            int orbResult = tempOrb.Update();

            if (orbResult == 2) {
                lifePoints--;
                orbIterator.remove();
            } else if (orbResult == 3) {
                forbsKilled++;
                GameActivity.setScore();
                orbIterator.remove();
                friendlyTimesTillAdd.add(SystemClock.uptimeMillis() + (500 + rand.nextInt((2500 - 500) + 1)));
            }
        }
        //endregion
        //region
        for (orbIterator = enemyOrbs.listIterator(); orbIterator.hasNext(); ) {
            Orb tempOrb = orbIterator.next();

            int orbResult = tempOrb.Update();

            if (orbResult == 2) {
                //ENEMY ORB DIED A NATURAL DEATH.

                orbIterator.remove();
                enemyTimesTillAdd.add(SystemClock.uptimeMillis() + (500 + rand.nextInt((2500 - 500) + 1)));
            } else if (orbResult == 3) {
                //ENEMY ORB KILLED BY PLAYER.

                lifePoints--;
                orbIterator.remove();
            } else {
                canvas.drawCircle(tempOrb.x, tempOrb.y, orbRadius, red);
            }

        }
        //endregion
        canvas.drawPath(linePath, whitePathPaint);

            invalidate();

    }
        //I don't know if this is the best way to refresh the screen
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //Sets up starting point of path
        if(firstPoint) {
            firstPoint = false;
            linePath.moveTo(event.getX(),event.getY());
        }

        listIndex = linePoints.size() - 1;
        //Adds points to path & linePoints that were missed.
        for(int i = 0; i < event.getHistorySize(); i++) {
            linePoints.add(new TimeStampedPoint((int) event.getHistoricalX(i), (int) event.getHistoricalY(i), event.getHistoricalEventTime(i)));
            listIndex++;
            linePath.lineTo(linePoints.get(listIndex).x, linePoints.get(listIndex).y);
            if (listIndex >= 1) {
                checkForIntersections(linePoints.get(listIndex - 1), linePoints.get(listIndex));
            }
        }

        //Adds current point to path & linePath();
        listIndex = linePoints.size() - 1;
        linePoints.add(new TimeStampedPoint((int) event.getX(), (int) event.getY(), event.getEventTime()));
        listIndex++;
        if (listIndex >= 1) {
            checkForIntersections(linePoints.get(listIndex - 1), linePoints.get(listIndex));
        }
        linePath.lineTo(linePoints.get(listIndex).x,linePoints.get(listIndex).y);

        //This switch statements creates initial actions for when the finger is pressed/lifted.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                screenPressed = true;
                setEventTime(); //This starts the timer that will eventually reach "X" seconds.
                break;
            case MotionEvent.ACTION_UP: //The primary purpose of this "switch" is to delete the old line
                                        // & reset variables in preparation for new line
                screenPressed = false;
                linePoints = new ArrayList(); //Possibly filling heap with empty arrays.
               polygons = new ArrayList();
                linePath = new Path();
                listIndex = 0;
                firstPoint = true;
                break;
        }

        return true;
    }


    private void checkForIntersections(Point p, Point p2) {

        for(int i = linePoints.size() - 3; i > 0; i--) {
            if(intersect(p,p2,linePoints.get(i-1),linePoints.get(i))) {
                //RETURN POINTS IN THE POLYGON THAT WILL BE USED TO DETERMINE IF "TOKENS"
                // ARE IN POLYGON.

                List<TimeStampedPoint> tempList;

                if(linePoints.indexOf(p) > i) {
                    tempList = linePoints.subList(i,linePoints.indexOf(p));
                    if(tempList.size() > 3) {
                        polygons.add(new Polygon(Arrays.copyOf(tempList.toArray(), tempList.toArray().length, Point[].class), SystemClock.uptimeMillis()));
                    }


                } else {
                    tempList = linePoints.subList(linePoints.indexOf(p),i);
                    if(tempList.size() > 3) {
                        polygons.add(new Polygon(Arrays.copyOf(tempList.toArray(), tempList.toArray().length, Point[].class), SystemClock.uptimeMillis()));
                    }
                }

            }
        }
    }

    private void setEventTime() {
    startTime = System.nanoTime();
    }

    //Checks current time since setEventTime


    // Things used to determine intersections.

    //Used to determine orientation of <something>
    private static int orientation(Point p, Point q, Point r) {
        double val = (q.y - p.y) * (r.x - q.x)
                - (q.x - p.x) * (r.y - q.y);

        if (val == 0.0)
            return 0; // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    //Determines intersection of 2 lines (P1,Q1) & (P2,Q2).
    private static boolean intersect(Point p1, Point q1, Point p2, Point q2) {

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        return false;
    }

    //Will shorten checking process by determining if 2 lines do/don't have the same bounding box.
    //Not yet implemented.
    private static boolean lineBoundBoxCheck(Point p1, Point q1, Point p2, Point q2) {
       //STILL NEEDS TO BE FINISHED.


        int OMIX = Math.min(p1.x,q1.x);
        int OMIY = Math.min(p1.y,q1.y);

        int TOX = Math.min(p2.x,q2.x);
        int TOY = Math.min(p2.y,q2.y);

        return true; //Placeholder code
    }

    //REMINDER: FIX BOUNDING BOX CHECK.

    private static Boolean IsPointInPolygon( Point p, Polygon pol)
    {
        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        Boolean inside = false;

        //REMINDER: Fix Bounding box check


        /*
        if(p.x < pol.minX || p.x > pol.maxX || p.y < pol.minY || p.y > pol.maxX) {
            Log.i("BB STAT:","STOPPED");

            return false;
        }

        */


        for ( int i = 0, j = pol.points.length - 1 ; i < pol.points.length ; j = i++ )
        {
            if ( ( pol.points[ i ].y > p.y ) != ( pol.points[ j ].y > p.y ) &&
                    p.x < ( pol.points[ j ].x - pol.points[ i ].x ) * ( p.y - pol.points[ i ].y ) / ( pol.points[ j ].y - pol.points[ i ].y ) + pol.points[ i ].x )
            {
                inside = !inside;
            }
        }
        return inside;
    }

    public static class Orb {

        public long startTime;
        private final int x;
        private final int y;
        private long totalExistenceTime; //Public only for debugging purposes otherwise private.
       // private final boolean type;
        private final int phaseOneTime;
        private boolean firstUpdate;

        private char phase;




        Orb(int a, int b) {
            startTime = SystemClock.uptimeMillis(); //This might need to be updated if have things like "History"
            x = a;
            y = b;
            phase = 'B';
            phaseOneTime = 4000;

            //Place holder
         //   type = ptype;

            firstUpdate = true;
        }

        public int Update() {
            //REMINDER: Change to 50 milliseconds
            if(firstUpdate) {
                firstUpdate = false;
                return 0;
            }


            totalExistenceTime = SystemClock.uptimeMillis() - startTime;

            if(totalExistenceTime <= phaseOneTime) {
                //Phase is already blue

                for (Polygon p : polygons ) {

                    if(IsPointInPolygon(new Point(x,y), p)) {
                        return 3;
                        // this.KilledByPlayer();
                    }
                }

            }
            else if(totalExistenceTime > phaseOneTime && totalExistenceTime <= 8000) {
                //Start Yellow Stage

                if(phase != 'Y') {
                    phase = 'Y';
                }

                for (Polygon p: polygons ) {

                    if(GameView.IsPointInPolygon(new Point(x,y), p)) {
                        return 3;
                       // this.KilledByPlayer();
                    }
                }
            } else {
                //Die!
                return 2;
               // this.NaturalDeath();

            }
            return 1;
        }



    }


    //Will eventually turn Polygons list into actual array of "Polygons"
    private class Polygon {

        public final Point[] points;

        public Path polyP;

        private final long startTime;


        public final int minX; //Bottom Left
        public final int maxX; // Bottom Right


        public final int minY; //Top Left
        public final int maxY; // Top Right



        Polygon( Point[] pPoints, long pstartTime) {

            polyP = new Path();

            points = pPoints;

            double tminX = points[0].x;
            double tmaxX = points[0].x;
            double tminY = points[0].y;
            double tmaxY = points[0].y;
            for ( int i = 1 ; i < points.length ; i++ )  {
                Point q = points[i];
                tminX = Math.min( q.x, tminX );
                tmaxX = Math.max( q.x, tmaxX );
                tminY = Math.min( q.y, tminY );
                tmaxY = Math.max( q.y, tmaxY );
            }

            minX = (int) tminX;
            maxX = (int) tmaxX;
            minY = (int) tminY;
            maxY = (int) tmaxY;

        startTime = pstartTime;


            //Create the custom path:

            polyP.moveTo(points[0].x,points[0].y);
            for (Point p: points) {
                polyP.lineTo(p.x,p.y);
            }

        }

    }

    @SuppressLint("ParcelCreator")
    private class TimeStampedPoint extends Point {

        private final long timeStamp;

        private TimeStampedPoint(final int x, final int y, final long timeStamp) {
            super(x, y);
            this.timeStamp = timeStamp;
        }
    }

    //Possible replacement for removeExpired Points
    private void updatePoints() {

        if(linePoints.size() < 2) {
            return;
        }

       int i = 0;
       while(true) {

           if(i == linePoints.size()) {
               break;
           }

           TimeStampedPoint point = linePoints.get(i);
           if (SystemClock.uptimeMillis() - point.timeStamp >= 800) {
               // We only include points in the result if they are not expired.
               polyUpdate(point);
               linePoints.remove(point);
               i--;
           }
           i++;
       }


    if(linePoints.size() < 2) {
        return;
    }

        linePath.moveTo( linePoints.get(0).x,linePoints.get(0).y);

        for (int it = 1; it < linePoints.size(); it++) {
            final Point targetPoint = linePoints.get(it);
            linePath.lineTo(targetPoint.x, targetPoint.y);
        }

    }

    private void polyUpdate(TimeStampedPoint p) {

        int i = 0;
        while(true) {


            if(i == polygons.size()) {
                break;
            }

            if(p.equals(polygons.get(i).points[0])) {
                polygons.remove(i);
                i--;
            }

            i++;
        }

    }


    private static boolean orbSpawnComparer(Orb o, int pX, int pY) {

    double distance =  Math.hypot(o.x - pX, o.y - pY);
        return distance >= orbRadius * 5;

    }



    public static class FriendlyOrbTimerTask extends TimerTask implements Cloneable {

        @Override
        public void run() {

            if(SystemClock.uptimeMillis() - drawCallTime < 100) {
                if(friendlyTimesTillAdd.size() + friendlyOrbs.size() < totalOrbs) {
                    friendlyTimesTillAdd.add(SystemClock.uptimeMillis() + (500 + rand.nextInt((2500 - 500) + 1)));
                }
                TIMER.schedule(this.clone(), friendlyOrbSpawnInterval);
            }
        }

        @Override
        public FriendlyOrbTimerTask clone(){
            return new FriendlyOrbTimerTask(); //add parameters from the current contextif needed.
        }
    }

    public static class EnemyOrbTimerTask extends TimerTask implements Cloneable {



        @Override
        public void run() {

            if(SystemClock.uptimeMillis() - drawCallTime < 50) {
                if(enemyTimesTillAdd.size() + enemyOrbs.size() < totalOrbs) {
                    enemyTimesTillAdd.add(SystemClock.uptimeMillis() + (500 + rand.nextInt((2500 - 500) + 1)));
                }
                TIMER.schedule(this.clone(), enemyOrbSpawnInterval);
            }
        }

        @Override
        public EnemyOrbTimerTask clone(){
            return new EnemyOrbTimerTask(); //add parameters from the current contextif needed.
        }
    }







}
