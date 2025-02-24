import java.io.*;

public class LeaderBoard {
    private static final int MAX_ENTRIES = 5;
    private String[] names;
    private int[] scores;
    private FileWriter fileWriter ;
    private int scoresCount = 0;
    private String fileName;

    public LeaderBoard(){
        this.names = new String[MAX_ENTRIES];
        this.scores = new int[MAX_ENTRIES];
        this.fileName = "scores.csv";

        try{
            File boardFile = new File(fileName);
            boardFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupScoresFromFile();
    }

    public void registerScore(String name, int score){
        // add player score

        int existingPlayerIndex = -1;
        for (int i =0; i<this.names.length ; i++){
            if (this.names[i]!= null && this.names[i] .equals(name)){
                existingPlayerIndex = i;
                break;
            }
        }

        if (existingPlayerIndex == -1){
            existingPlayerIndex = this.scoresCount;
        }

        if (score <  this.scores[existingPlayerIndex]){
            return;
        }

        this.scores[existingPlayerIndex] = score;
        this.names[existingPlayerIndex] = name;

        if (this.scoresCount < MAX_ENTRIES -1){
            this.scoresCount++;
        }


        // Sort Scores
        for (int i =0; i<this.scores.length ; i++){
            for (int j=0; j<this.scores.length -1-i;j++){
                if (this.scores[j] < this.scores[j+1]){
                    int tempScore = this.scores[j]; // 100
                    String tempName = this.names[j]; // name for 100

                    this.scores[j] = this.scores[j+1];
                    this.names[j] = this.names[j+1];

                    this.scores[j+1] = tempScore;
                    this.names[j+1] = tempName;
                }
            }
        }


        // write scores to the file
        try {
            File leaderBoardFile = new File(this.fileName);
            FileWriter fileWriter = new FileWriter(leaderBoardFile, false);

            for (int i=0;i<this.names.length;i++){
                fileWriter.write(this.names[i]+", "+String.valueOf(this.scores[i])+"\n");
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void setupScoresFromFile(){
        try {
            BufferedReader br = new BufferedReader( new FileReader(this.fileName));

            while(true){
                String csvLine = br.readLine();
                if (csvLine == null){
                    break;
                }

                String[] values = csvLine.split(",");

                String playerName = values[0].trim();
                int playerScore = Integer.parseInt(values[1].trim());


                this.names[this.scoresCount] = playerName;
                this.scores[this.scoresCount] = playerScore;
                if (this.scoresCount < 4){
                    this.scoresCount++;
                }
            }
        }catch (IOException e) {
            System.out.printf("failed opening %s file\n",fileName);
            e.printStackTrace();
        }
    }

    public void display(boolean isFromCommandLine){
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.5, 0.9, "Leaderboard");

        for (int i = 0; i < 5; i++){
            if (this.names[i] != null && !this.names[i].equals("null")) {
                StdDraw.text(0.3, 0.8 - 0.1 * i, this.names[i]);
                StdDraw.text(0.7, 0.8 - 0.1 * i, String.valueOf(this.scores[i]));
            }
        }

        if (isFromCommandLine){
            StdDraw.text(0.5, 0.1, "Press any key to close the leaderboard");
        }
        else {
            StdDraw.text(0.5, 0.1, "Press any key to restart the game");
        }
        StdDraw.show();

        while(!StdDraw.hasNextKeyTyped() && !StdDraw.isMousePressed()) {
            StdDraw.pause(100);
        }
    }
}