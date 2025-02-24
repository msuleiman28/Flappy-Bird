public class Project {
    private static final double GRAVITY = -0.002;
    private static final double LIFT = 0.02;
    private static final double PIPE_WIDTH = 0.1;
    private static final double PIPE_GAP = 0.5;
    private static final double BIRD_RADIUS = 0.05;
    private static double birdY = 0.5;
    private static double birdVelocity = 0;


    private static double[] pipeX = {1.2, 1.8, 2.4};
    private static double[] pipeGapCenter = {0.4, 0.6, 0.5};
    private static final double MIN_PIPE_DISTANCE = 0.6;
    private static boolean isSpaceOrMousePressed = false;

    private static boolean[] pipePassed = new boolean[pipeX.length];

    private static int score = 0;
    private static boolean gameOver = false;
    private static String playerName;
    private static LeaderBoard leaderBoard;


    public static void main(String[] args) {
        leaderBoard = new LeaderBoard();


        if (args.length != 1) {
            System.err.println("Error: There must be one command line argument, your name, or '1' to view leaderboard");
            System.exit(1);
        }

        playerName = args[0];
        if (playerName.equals("1")) {
            leaderBoard.display(true);
            System.exit(0);
        }


        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.enableDoubleBuffering();

        String gameState = "playing";
        boolean scoreSet = false;

        while (true) {

            if (gameState.equals("playing")) {
                if (!gameOver) {
                    scoreSet = false;
                    updateGame();
                    checkCollision();
                } else {
                    gameState = "gameOver";
                }
            } else if (gameState.equals("gameOver")) {
                if (!scoreSet){
                    showGameOver();
                    scoreSet = true;
                }


            while(!StdDraw.hasNextKeyTyped() && !StdDraw.isMousePressed()) {

            }

                while (StdDraw.hasNextKeyTyped()) {
                    StdDraw.nextKeyTyped();
                }
                if (StdDraw.isKeyPressed(32)) {
                    gameState = "leaderboard";
                    while (StdDraw.hasNextKeyTyped()) {
                        StdDraw.nextKeyTyped();
                    }

                }

            } else {
                leaderBoard.display(false);
                if (StdDraw.hasNextKeyTyped() || StdDraw.isMousePressed()) {
                    gameState = "playing";
                    resetGame();
                    while (StdDraw.hasNextKeyTyped()) {
                        StdDraw.nextKeyTyped();
                    }
                }


            }
            StdDraw.show();
            StdDraw.pause(20);
        }
    }


    private static void playSoundAsync(String filePath) {
        new Thread(() -> StdAudio.play(filePath)).start();
    }


    public static void updateGame() {
        StdDraw.clear();
        StdDraw.picture(0.5, 0.7, "backgorund.jpg");
        StdDraw.picture(0.5, 0.35, "backgorund.jpg");


        birdVelocity += GRAVITY;
        birdY += birdVelocity;

        if ((StdDraw.isKeyPressed(32) || StdDraw.isMousePressed()) && !isSpaceOrMousePressed) {
            birdVelocity = LIFT;
            playSoundAsync("flap.wav");
            isSpaceOrMousePressed = true;
        }


        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        double birdAngle = Math.toDegrees(Math.atan(birdVelocity * 10));
        birdAngle = Math.max(-70, Math.min(70, birdAngle));

        StdDraw.picture(0.2, birdY, "bird1.png", 0.2, 0.2, birdAngle);


        if (!StdDraw.isKeyPressed(32) && !StdDraw.isMousePressed()) {
            isSpaceOrMousePressed = false;
        }

        for (int i = 0; i < pipeX.length; i++) {
            pipeX[i] = pipeX[i] - 0.015;
            if (pipeX[i] < -PIPE_WIDTH) {
                double lastPipeX;
                if (i == 0) {
                    lastPipeX = pipeX[pipeX.length - 1];
                } else {
                    lastPipeX = pipeX[i - 1];
                }

                pipeX[i] = lastPipeX + MIN_PIPE_DISTANCE;
                pipeGapCenter[i] = 0.3 + Math.random() * 0.4;
                pipePassed[i] = false;
            }


            if (pipeX[i] + PIPE_WIDTH / 2 < 0.2 && !pipePassed[i]) {
                score++;
                pipePassed[i] = true;
                playSoundAsync("point.wav");
            }


            StdDraw.setPenColor(StdDraw.GREEN);

            double topPipeHeight = (1 - pipeGapCenter[i] - PIPE_GAP / 2);
            double bottomPipeHeight = pipeGapCenter[i] / 2;

            double topPipeY = pipeGapCenter[i] + PIPE_GAP / 2 + topPipeHeight / 2;
            StdDraw.filledRectangle(pipeX[i], topPipeY, PIPE_WIDTH / 2, topPipeHeight);

            double bottomPipeY = pipeGapCenter[i] - PIPE_GAP / 2 - bottomPipeHeight / 2;
            StdDraw.filledRectangle(pipeX[i], bottomPipeY, PIPE_WIDTH / 2, bottomPipeHeight);
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.9, 0.9, "score: " + score);
    }


    private static void checkCollision() {
        double horizontalCollisionBuffer = BIRD_RADIUS;
        double verticalCollisionBuffer = BIRD_RADIUS / 0.35;
        if (birdY - horizontalCollisionBuffer < 0 || birdY + horizontalCollisionBuffer > 1) {
            playSoundAsync("death.wav");
            gameOver = true;
            return;
        }

        for (int i = 0; i < pipeX.length; i++) {
            if (pipeX[i] - PIPE_WIDTH / 2 < 0.2 + horizontalCollisionBuffer && pipeX[i] + PIPE_WIDTH / 2 > 0.2 - horizontalCollisionBuffer) {
                if (birdY - verticalCollisionBuffer < pipeGapCenter[i] - PIPE_GAP / 2 || birdY + verticalCollisionBuffer > pipeGapCenter[i] + PIPE_GAP / 2) {
                    playSoundAsync("death.wav");
                    gameOver = true;
                    return;

                }
            }
        }
    }

    private static void showGameOver() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(0.5, 0.6, "Game Over!");
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.5, 0.5, "Score: " + score);
        leaderBoard.registerScore(playerName, score);
        StdDraw.text(0.5, 0.4, "Press the Space bar to view the leaderboard");
        StdDraw.show();

    }

    private static void resetGame() {
        birdY = 0.5;
        birdVelocity = 0;
        gameOver = false;
        score = 0;
        pipeX = new double[]{1.2, 1.8, 2.4};
        pipeGapCenter = new double[]{0.4, 0.6, 0.5};

    }
}