package pl.evelanblog.prawdaczyfalsz.screen;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import pl.evelanblog.prawdaczyfalsz.Prefs;
import pl.evelanblog.prawdaczyfalsz.R;
import pl.evelanblog.prawdaczyfalsz.HelperClass;
import pl.evelanblog.prawdaczyfalsz.object.Advert;
import pl.evelanblog.prawdaczyfalsz.object.Scoreboard;
import pl.evelanblog.prawdaczyfalsz.object.Sound;
import pl.evelanblog.prawdaczyfalsz.object.Stage;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.OnSignOutCompleteListener;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class Win extends BaseGameActivity implements View.OnClickListener, OnSignOutCompleteListener, GameHelperListener,
		GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {


	@Override
	public void onSignInSucceeded() {
		findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
		int stageInt = Stage.stageLevel - 1;

		Log.i("LEADERBOARD", "Tablica, Scoreboard.getTime()" + Scoreboard.time);
		Log.i("LEADERBOARD", "Osiągniecie, Scoreboard.getCurrentTime()" + Scoreboard.currentTime);
		Log.i("ACHIEVEMENT", "Pytania w sumie" + (Scoreboard.goodAnswers + Scoreboard.badAnswers));

		getGamesClient().submitScore(getString(R.string.leaderboard_najwicej_punktw), Scoreboard.score);
		getGamesClient().submitScore(getString(R.string.leaderboard_najwyszy_poziom), stageInt);
		getGamesClient().submitScore(getString(R.string.leaderboard_najduej_grane), Scoreboard.time);
		getGamesClient().submitScore(getString(R.string.leaderboard_najwicej_odpowiedzi), Prefs.getIntValue(this, "QuestionsAll", 0));
		if (((int) (Scoreboard.time / 1000)) > 0)
			getGamesClient().incrementAchievement(getResources().getString(R.string.achievement_wytrway_zawodnik), ((int) (Scoreboard.time / 1000)));
		Scoreboard.currentTime = 0;

		if (Scoreboard.goodAnswers > 0 && Scoreboard.badAnswers > 0) {

			getGamesClient().incrementAchievement(getResources().getString(R.string.achievement_podoba_ci_si_ta_gra),
					(Scoreboard.goodAnswers + Scoreboard.badAnswers));
		}

		if (HelperClass.achievementLate)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_spnialski_));

		if (HelperClass.achievementLife)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_obeznany));

		if (Prefs.getIntValue(this, "Highscores", 0) >= 5000)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_mistrz));

		if (Prefs.getIntValue(this, "Highscores", 0) >= 9000)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_over_9000));

		if (Scoreboard.goodAnswers > 0)
			getGamesClient().incrementAchievement(getResources().getString(R.string.achievement_sporo_wiesz), Scoreboard.goodAnswers);

		if (Scoreboard.badAnswers > 0)
			getGamesClient().incrementAchievement(getResources().getString(R.string.achievement_moe_nie_umiesz_czyta), Scoreboard.badAnswers);

		if (stageInt == 1)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_dobry_pocztek));
		else if (stageInt == 2)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_mw_mi_pocztkujcy));
		else if (stageInt == 3)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_dopiero_si_rozkrcam));
		else if (stageInt == 10)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_to_nie_przelewki));
		Scoreboard.goodAnswers = 0;
		Scoreboard.badAnswers = 0;
	}
}
