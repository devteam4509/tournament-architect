package adesc062.uottawa.ca.tournamentdesigner.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.*;

import org.w3c.dom.Text;

import adesc062.uottawa.ca.tournamentdesigner.R;
import adesc062.uottawa.ca.tournamentdesigner.database.DBAdapter;
import adesc062.uottawa.ca.tournamentdesigner.domain.Match;
import adesc062.uottawa.ca.tournamentdesigner.domain.MatchTeamScore;

public class MatchViewActivity extends Activity {

    int match_id;
    String team1Name;
    String team2Name;
    int team1Logo;
    int team2Logo;
    int roundNum;
    EditText team1ScoreEditText;
    EditText team2ScoreEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_view);

        // Initialize the intent
        Intent intent = getIntent();
        roundNum = intent.getIntExtra("roundNum", -2);


        // Initialize the views
        TextView teamName1TextView = (TextView) findViewById(R.id.teamName1TextView);
        TextView teamName2TextView = (TextView) findViewById(R.id.teamName2TextView);
        ImageView team1ImageView = (ImageView) findViewById(R.id.team1ImageView);
        ImageView team2ImageView = (ImageView) findViewById(R.id.team2ImageView);
        EditText team1ScoreEditText = (EditText) findViewById(R.id.team1ScoreEditText);
        EditText team2ScoreEditText = (EditText) findViewById(R.id.team2ScoreEditTExt);

        // Get the information
        team1Name = intent.getStringExtra("team1Name");
        team2Name = intent.getStringExtra("team2Name");
        team1Logo = intent.getIntExtra("team1Logo", 0);
        team2Logo = intent.getIntExtra("team2Logo", 0);

        // Set the team logos, names and the match id
        match_id = intent.getIntExtra("match_id", 0);
        teamName1TextView.setText(team1Name);
        teamName2TextView.setText(team2Name);
        team1ImageView.setImageResource(team1Logo);
        team2ImageView.setImageResource(team2Logo);

        // If the match was updated, then get the scores for both teams
        // And disable score saving
        int updated = DBAdapter.getMatchUpdated(getApplicationContext(), match_id);
        if(updated == 1) {

            // Get the team IDs for both teams
            int team1ID = DBAdapter.getTeamId(getApplicationContext(), team1Name,
                    DBAdapter.getTournamentId(getApplicationContext(), match_id));
            int team2ID = DBAdapter.getTeamId(getApplicationContext(), team2Name,
                    DBAdapter.getTournamentId(getApplicationContext(), match_id));

            // Get the match team score IDs for both teams
            int match_team_score1 = DBAdapter.getMatchTeamScore(getApplicationContext(), team1ID, match_id);
            int match_team_score2 = DBAdapter.getMatchTeamScore(getApplicationContext(), team2ID, match_id);

            // Set the match team scores on the page
            team1ScoreEditText.setText(String.valueOf(match_team_score1));
            team2ScoreEditText.setText(String.valueOf(match_team_score2));

            // Disable the edit texts
            team1ScoreEditText.setEnabled(false);
            team2ScoreEditText.setEnabled(false);
        }
    }

    public void saveOnClickInMatchView(View view) {

        // If the match had already been updated,
        // inform the user that he cannot save the scores
        if(DBAdapter.getMatchUpdated(getApplicationContext(), match_id) == 1) {

            // Pop up a dialog to inform the user
            final Dialog alertTournamentNameAlreadyInUse = new Dialog(MatchViewActivity.this);
            alertTournamentNameAlreadyInUse.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertTournamentNameAlreadyInUse.setContentView(R.layout.custom_alert_ok);

            // Set the message
            TextView messageTournamentNameAlreadyInUse = (TextView) alertTournamentNameAlreadyInUse.findViewById(R.id.messageOkTextView);
            messageTournamentNameAlreadyInUse.setText("This match has already been saved and updated.");

            Button okButton = (Button) alertTournamentNameAlreadyInUse.findViewById(R.id.okButton);

            okButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    alertTournamentNameAlreadyInUse.dismiss();
                }
            });
            alertTournamentNameAlreadyInUse.show();
        }
        // If the match had not been updated
        else {
            // Initialize the edit texts
            team1ScoreEditText = (EditText) findViewById(R.id.team1ScoreEditText);
            team2ScoreEditText = (EditText) findViewById(R.id.team2ScoreEditTExt);

            // Get the strings from the edit text
            String team1Score = team1ScoreEditText.getText().toString();
            String team2Score = team2ScoreEditText.getText().toString();

            // If the user entered scores for both teams, save them
            if (!team1Score.equals("") && !team2Score.equals("")) {

                // If the match is a tie, do not let ther user save
                if(team1Score.equals(team2Score)) {
                    // Pop up a dialog to inform the user
                    final Dialog alertTournamentNameAlreadyInUse = new Dialog(MatchViewActivity.this);
                    alertTournamentNameAlreadyInUse.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertTournamentNameAlreadyInUse.setContentView(R.layout.custom_alert_ok);

                    // Set the message
                    TextView messageTournamentNameAlreadyInUse = (TextView) alertTournamentNameAlreadyInUse.findViewById(R.id.messageOkTextView);
                    messageTournamentNameAlreadyInUse.setText("Matches cannot end in ties.");

                    Button okButton = (Button) alertTournamentNameAlreadyInUse.findViewById(R.id.okButton);

                    okButton.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            alertTournamentNameAlreadyInUse.dismiss();
                        }
                    });
                    alertTournamentNameAlreadyInUse.show();
                }
                else {

                    // Get the scores from the edit texts
                    int team1ScoreInt = Integer.parseInt(team1Score);
                    int team2ScoreInt = Integer.parseInt(team2Score);

                    // Get the tournament id
                    int tournament_id = DBAdapter.getTournamentId(getApplicationContext(), match_id);

                    // Update the match
                    Match.updateMatch(getApplicationContext(), match_id, team1Name, team1ScoreInt,
                            team2Name, team2ScoreInt, tournament_id);

                    // Open the standings page and clear previous activities
                    Intent intent = new Intent(MatchViewActivity.this, StandingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("tournament_id", tournament_id);
                    intent.putExtra("editedRoundNum", roundNum);
                    startActivity(intent);
                }
            }
            // If the user did not enter scores for both teams, pop up a warning
            else {

                // Pop up a dialog to inform the user
                final Dialog alertTournamentNameAlreadyInUse = new Dialog(MatchViewActivity.this);
                alertTournamentNameAlreadyInUse.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertTournamentNameAlreadyInUse.setContentView(R.layout.custom_alert_ok);

                // Set the message
                TextView messageTournamentNameAlreadyInUse = (TextView) alertTournamentNameAlreadyInUse.findViewById(R.id.messageOkTextView);
                messageTournamentNameAlreadyInUse.setText("You must enter a score for both teams to save.");

                Button okButton = (Button) alertTournamentNameAlreadyInUse.findViewById(R.id.okButton);

                okButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        alertTournamentNameAlreadyInUse.dismiss();
                    }
                });
                alertTournamentNameAlreadyInUse.show();
            }
        }

    }
}