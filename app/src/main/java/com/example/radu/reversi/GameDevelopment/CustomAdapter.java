package com.example.radu.reversi.GameDevelopment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radu.reversi.GameLogic.Game;
import com.example.radu.reversi.GameLogic.GameType;
import com.example.radu.reversi.GameLogic.Position;
import com.example.radu.reversi.GameLogic.State;
import com.example.radu.reversi.GameResult.ResultadosActivity;
import com.example.radu.reversi.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private Game game;
    private int size;
    private TextView et, tv, count;
    private int timer;
    Timer time;
    TimerTask timerTask;
    MediaPlayer ring;
    final Handler handler = new Handler();

    private static final int BLOCK = 0;
    private static final int WIN = 1;
    private static final int LOSE = 2;
    private static final int DRAW = 3;
    private static final int TEMPUS = 4;
    private static final int WRONGCELL = 5;
    private static final int SECONDINMILISECONDS = 1000;

    int firstMove = 0;


    Activity activity;

    String alias, numbers;

    ParrillaFrag.MyOnClickListener listener;

    String tempini, tempfin;



    public CustomAdapter (Context c,  Game game, int timer, String alias){
        this.context = c;
        this.game = game;
        this.timer = timer;
        this.size = game.getBoard().size();
        this.alias = alias;
        this.activity = (Activity) c;
        createViews();
    }


    public void setMyOnClickListener(ParrillaFrag.MyOnClickListener listener) {
        this.listener=listener;
    }

    public void createViews(){
        et = (TextView) ((Activity)context).findViewById(R.id.text);
        tv = (TextView) ((Activity)context).findViewById(R.id.text_fichas);
    }


    @Override
    public int getCount() {
        return this.game.getBoard().totalCells();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageButton cell;
        int i,j;


        i = position % size;
        j = position / size;

        if (convertView == null) {
            cell = new ImageButton(context);
            cell.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / size, parent.getWidth() / size));
            cell.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cell.setScaleType(ImageButton.ScaleType.FIT_XY);
            cell.setPadding(0, 0, 0, 0);
        } else {
            cell = (ImageButton) convertView;
        }


        cell.setTag(position);
        changeCellImage(cell, new Position(i, j));

        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                int i = pos % size;
                int j = pos / size;
                if(game.getBoard().isObjective(new Position(i,j))){
                    isObjectiveProcess(new Position(i,j));
                    if (listener!=null) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:s");
                        tempfin = sdf.format(c.getTime());

                        TextView txt = activity.findViewById(R.id.TxtDetalleLog);




                        listener.onLogSeleccionado(String.format(context.getString(R.string.temp_onclick), String.valueOf(i), String.valueOf(j), String.valueOf(game.getRemainingCells()), tempini, tempfin));
                    }
                } else {
                    makeToast(WRONGCELL);
                }

            }
        });

        return cell;
    }

    public void changeCellImage(ImageButton cell, Position p){
        if (game.getBoard().isBlack(p)){
            cell.setImageResource(R.drawable.black);
        } else if (game.getBoard().isWhite(p)){
            cell.setImageResource(R.drawable.white);
        } else if (game.getBoard().isEmpty(p)){
            cell.setImageResource(R.drawable.green);
        } else if (game.getBoard().isObjective(p)){
            if (game.getGameType() == GameType.EASY){
                if(game.getBoard().getTransform(p)  == 1){
                    cell.setImageResource(R.drawable.one_icon);
                }else if(game.getBoard().getTransform(p)  == 2){
                    cell.setImageResource(R.drawable.two_icon);
                }else if(game.getBoard().getTransform(p)  == 3){
                    cell.setImageResource(R.drawable.three_icon);
                }else if(game.getBoard().getTransform(p)  == 4){
                    cell.setImageResource(R.drawable.four_icon);
                }else if(game.getBoard().getTransform(p)  == 5){
                    cell.setImageResource(R.drawable.five_icon);
                }else if(game.getBoard().getTransform(p)  == 6){
                    cell.setImageResource(R.drawable.six_icon);
                }else if(game.getBoard().getTransform(p)  == 7){
                    cell.setImageResource(R.drawable.seven_icon);
                }else if(game.getBoard().getTransform(p)  == 8){
                    cell.setImageResource(R.drawable.eight_icon);
                }else if(game.getBoard().getTransform(p)  == 9){
                    cell.setImageResource(R.drawable.nine_icon);
                }else if(game.getBoard().getTransform(p)  == 10){
                    cell.setImageResource(R.drawable.ten_icon);
                }else{
                    cell.setImageResource(R.drawable.whitegreen);
                }
            } else {
                cell.setImageResource(R.drawable.whitegreen);
            }
        }
    }

    public void isObjectiveProcess(Position p){
        if(firstMove == 0){
            countTime();

        }

        /*User Turn*/
        if(game.getState()== State.BLACK){
            game.move(p);
            /* Mobile objectives set*/
            game.setObjectives(size);
            notifyDataSetChanged();

        }

        /*Mobile Turn*/
        if(game.getState()==State.WHITE){

            if(game.getGameType() != GameType.MULTIPLAYER){
                game.phoneTurn();
            } else {
                game.move(p);
            }
            /* Player objectives set */
            game.setObjectives(size);
            notifyDataSetChanged();

        }
        if(game.getFinished()){
            finish();
        }
        /* Updates the EditText's of the activity*/
        updateNumbers();
    }


    public void finish(){

        Bundle b = new Bundle();
        updateNumbers();

        int win;
        int diferencia = this.game.getBoard().getCountWhite() - this.game.getBoard().getCountBlack();

        if (game.getGameDuration() == 25 && timer == 1){
            makeToast(TEMPUS);
            win = 3;
        } else if (((game.getBoard().getCountBlack() + game.getBoard().getCountWhite()) != getCount())){
            makeToast(BLOCK);
            win = 2;
        } else {
            if(game.getBoard().getCountBlack() > game.getBoard().getCountWhite()) {
                makeToast(WIN);
                win = 1;
                diferencia = this.game.getBoard().getCountBlack() - this.game.getBoard().getCountWhite();
            } else if (game.getBoard().getCountBlack() < game.getBoard().getCountWhite()) {
                makeToast(LOSE);
                win = 0;
            } else {
                makeToast(DRAW);
                win = -1;
            }
        }

        android.content.Intent in = new android.content.Intent(context, ResultadosActivity.class);
        b.putInt(context.getString(R.string.duration_key), this.game.getGameDuration());
        b.putInt(context.getString(R.string.black_key), this.game.getBoard().getCountBlack());
        b.putInt(context.getString(R.string.white_key), this.game.getBoard().getCountWhite());
        b.putInt(context.getString(R.string.diferencia_key), diferencia);
        b.putInt(context.getString(R.string.win_key), win);

        in.putExtras(b);
        context.startActivity(in);
        activity.finish();

    }

    public void countTime(){
        startTimer();
        firstMove++;
    }

    public void startTimer() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh,mm,s");
        tempini = sdf.format(c.getTime());

        time = new Timer();
        initializeTimerTask();
        time.schedule(timerTask, 0, SECONDINMILISECONDS);
    }

    public void stopTimerTask(View v) {

        if (time != null) {
            time.cancel();
            time = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if(game.getFinished()){
                            stopTimerTask(count);
                        } else{
                            game.increaseDuration();
                            updateCount();
                            if (game.getGameDuration() == 25 && timer == 1){
                                game.setState(State.FINISHED);
                                stopTimerTask(count);
                                finish();
                            }
                        }
                    }
                });
            }
        };
    }

    public void updateCount(){
        final TextView textView = (TextView) activity.findViewById(R.id.timer_text);
        textView.setText(Integer.valueOf(game.getGameDuration()).toString());
        if (timer == 1){
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.BLUE);
        }
        textView.postInvalidate();
    }

    public  void updateNumbers(){
        String state, auxiliar;
        int toFill = getCount() -
                (game.getBoard().getCountBlack() + game.getBoard().getCountWhite());
        numbers = String.format(context.getString(R.string.info_caselles),
                Integer.valueOf(game.getBoard().getCountBlack()).toString(),
                Integer.valueOf(game.getBoard().getCountWhite()).toString(),
                Integer.valueOf(toFill).toString());
        tv.setText(numbers);
        if(game.getState() == State.FINISHED){
            auxiliar = context.getString(R.string.finalitzada);
        } else {
            auxiliar = context.getString(R.string.en_marxa);
        }
        state = String.format(context.getString(R.string.estat), auxiliar);
        et.setText(state);
    }


    public void makeToast(int i){

        Toast imageToast = new Toast(context);
        imageToast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastLayout = new LinearLayout(context);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        toastLayout.setBackgroundColor(Color.WHITE);
        ImageView image = new ImageView(context);
        TextView text = new TextView(context);

        if (i == BLOCK){
            image.setImageResource(R.drawable.block_icon);
            text.setText(R.string.bloqueo);
        } else if (i == WIN){
            makeSound(WIN);
            image.setImageResource(R.drawable.like_icon);
            text.setText(R.string.victoria);
        } else if (i == LOSE){
            makeSound(LOSE);
            image.setImageResource(R.drawable.dislike_icon);
            text.setText(R.string.perdida);
        } else if (i == DRAW){
            image.setImageResource(R.drawable.balance_icon);
            text.setText(R.string.empate);
        } else if (i == TEMPUS){
            makeSound(TEMPUS);
            image.setImageResource(R.drawable.chrono_icon);
            text.setText(R.string.final_tiempo);
        } else if (i == WRONGCELL){
            makeSound(WRONGCELL);
            image.setImageResource(R.drawable.wrong_icon);
            text.setText(R.string.casella_incorrecta);
        }
        toastLayout.addView(image);
        toastLayout.addView(text);
        imageToast.setView(toastLayout);
        imageToast.setDuration(Toast.LENGTH_SHORT);
        imageToast.show();

    }

    public void makeSound(int i){
        if (i == WIN || i == DRAW){
            ring= MediaPlayer.create(context, R.raw.win_sound);
            ring.start();
        } else if (i == LOSE){
            ring= MediaPlayer.create(context, R.raw.lose_sound);
            ring.start();
        } else if (i == WRONGCELL){
            ring= MediaPlayer.create(context, R.raw.wrong_cell_sound);
            ring.start();
        } else if (i == TEMPUS) {
            ring = MediaPlayer.create(context, R.raw.timer_sound);
            ring.start();
        }

    }
}
