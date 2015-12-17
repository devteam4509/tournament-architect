package adesc062.uottawa.ca.tournamentdesigner.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import adesc062.uottawa.ca.tournamentdesigner.R;

/**
 * Used for the teams list view in the CreateTournament activity.
 * The list includes a small arrow in indicate  that
 * the rows are clickable.
 */
public class CustomMatchesListViewAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] team1Names;
    private final String[] team2Names;
    private final Integer[] team1Logos; // These integer correspond to the resource IDs of the drawables
    private final Integer[] team2Logos; // These integer correspond to the resource IDs of the drawables

    public CustomMatchesListViewAdapter(Activity context, String[] team1Names, Integer[] team1Logos,
                                        String[] team2Names, Integer[] team2Logos ) {

        super(context, R.layout.custom_teams_list_view, team1Names);

        this.context = context;
        this.team1Names = team1Names;
        this.team2Names = team2Names;
        this.team1Logos = team1Logos;
        this.team2Logos = team2Logos;
    }

    public View getView(int position, View view, ViewGroup parent) {

        // Set up the layout of the list
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_matches_list_view, null, true);

        // If the match is a bye
        if(team1Names[position].equals(team2Names[position])) {

            // Set up the name of the team
            TextView teamName1TextView = (TextView) rowView.findViewById(R.id.teamName1TextView);
            teamName1TextView.setText(team1Names[position]);

            // Remove the name os second team
            TextView teamName2TextView = (TextView) rowView.findViewById(R.id.teamName2TextView);
            teamName2TextView.setText("");

            // Set up the text saying the match is a bye
            TextView vsTextView = (TextView) rowView.findViewById(R.id.vsTextView);
            vsTextView.setText("Bye");

            // Set up the logo of the team
            ImageView team1LogoImageView = (ImageView) rowView.findViewById(R.id.team1LogoImageView);
            team1LogoImageView.setImageResource(team1Logos[position]);

            // Remove the logo of the second team
            ImageView team2LogoImageView = (ImageView) rowView.findViewById(R.id.team2LogoImageView);
            team2LogoImageView.setImageResource(-1);

            // Remove the arrow
            ImageView arrow = (ImageView) rowView.findViewById(R.id.arrowImageView);
            arrow.setImageResource(-1);

        }
        // If the match is not a bye
        else {

            // Set up the names of the first teams
            TextView teamName1TextView = (TextView) rowView.findViewById(R.id.teamName1TextView);
            teamName1TextView.setText(team1Names[position]);

            // Set up the names of the second teams
            TextView teamName2TextView = (TextView) rowView.findViewById(R.id.teamName2TextView);
            teamName2TextView.setText(team2Names[position]);

            // Set up the logos of the first teams
            ImageView team1LogoImageView = (ImageView) rowView.findViewById(R.id.team1LogoImageView);
            team1LogoImageView.setImageResource(team1Logos[position]);

            // Set up the logos of the second teams
            ImageView team2LogoImageView = (ImageView) rowView.findViewById(R.id.team2LogoImageView);
            team2LogoImageView.setImageResource(team2Logos[position]);
        }

        // Return the view of the row
        return rowView;
    }
}