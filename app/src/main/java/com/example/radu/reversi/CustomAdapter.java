package com.example.radu.reversi;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter {
    private final Context context;
    private Game game;
    private int size;
    private int number = 1;
    int BLOCK = 0;
    int WIN = 1;
    int LOSE = 2;
    int DRAW = 3;
    int TIMER = 4;

    public CustomAdapter (Context c, Game game){
        this.context = c;
        this.game = game;
        this.size = game.getBoard().size();
    }

    @Override
    public int getCount() {
        //return 0;
        return this.game.getBoard().totalCells();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageButton cell;
        int i,j;

        if (convertView == null) {
            cell = new ImageButton(context);
            cell.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / game.getBoard().size(), parent.getWidth() / game.getBoard().size()));
            cell.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cell.setScaleType(ImageButton.ScaleType.FIT_XY);
            cell.setPadding(0, 0, 0, 0);
        } else {
            cell = (ImageButton) convertView;
        }

        i = position % size;
        j = position / size;



        if (game.getBoard().isBlack(new Position(i,j))){
            cell.setImageResource(R.drawable.black);
        } else if (game.getBoard().isWhite(new Position(i,j))){
            cell.setImageResource(R.drawable.white);
        } else if (game.getBoard().isEmpty(new Position(i,j))){
            cell.setImageResource(R.drawable.green);
        } else if (game.getBoard().isObjective(new Position(i,j))){
            cell.setImageResource(R.drawable.whitegreen);

        }

        cell.setTag(position);

        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                int i = pos % size;
                int j = pos / size;


                if(game.getBoard().isObjective(new Position(i,j))){


                    // System.out.println("CUANTASNEGRAS: "+ game.getBoard().getCountBlack());
                    if(game.getState()==State.BLACK){
                        System.out.println("ENTRO EN EL BLACK");
                        game.move(new Position(i,j));
                        game.setObjectives(size);
                        game.getBoard().countAll(size);
                    }

                    //MARCA OBJETIVOS PARA LA MAQUINA
                    /*game.setObjectives(grid_dimension);
                    // System.out.println("CUANTASNEGRAS: "+ game.getBoard().getCountBlack());

                    game.getBoard().countAll(grid_dimension);*/

                    //CustomAdapter gnew = (CustomAdapter) parent.getAdapter();
                    notifyDataSetChanged();
                    //gv.setAdapter(gnew);

                    //adapter.UpdateGame(game);
                    //gv.setAdapter(adapter);
                    //TURNO MAKINA
                    if(game.getState()==State.WHITE){
                        System.out.println("ENTRO O KELOKE phone turn");
                        game.phoneTurn();
                        //MARCA OBJETIVOS PARA EL PLAYER
                        game.setObjectives(size);

                        notifyDataSetChanged();
                       // gnew.notifyDataSetChanged();
                        //gv.setAdapter(gnew);

                    }
                    System.out.println("Salio del turno makina");
                    //game.comprove();

                    if(game.getState() == State.FINISHED){
                        System.out.println("Entro en la condicion final");
                        game.getBoard().countAll(size);

                        if(game.blockFinished()){
                            makeToast(BLOCK);
                        } else {
                            if(game.getBoard().getCountBlack() > game.getBoard().getCountWhite()) {
                                makeToast(WIN);
                            } else if (game.getBoard().getCountBlack() < game.getBoard().getCountWhite()) {
                                makeToast(LOSE);
                            } else {
                                makeToast(DRAW);
                            }
                        }

                        /*Controlar tiempo*/
                        /*if(){
                             Toast.makeText(DesarrolloJuego.this, R.string.final_tiempo, Toast.LENGTH_SHORT).show();
                        }*/


                    }




/*
                    if(game.getBoard().isObjective(new Position(0, 1))){
                        System.out.println("objetivo MECAGOENTODO\n\n\n");

                    }else{
                        System.out.println("ALFINNNN");
                    }
*/


                    // System.out.println("Llego y salio del phonwTurn");

                   /* for (int x = 0; x < grid_dimension; x++){
                        for (int y = 0; y < grid_dimension; y++){
                            game.getBoard().getCountBlack();
                            game.getBoard().getCountWhite();
                        }
                    })*/

                    //adapter.notifyDataSetChanged();

                    //adapter.UpdateGame(game);

                    //gv.setAdapter(adapter);
                    //updateAdapter();
                } else {
                    /*No es una casilla valida, sonido incorrecto*/
                    System.out.println("Casilla incorrecta puslada");
                }
            }
        });
        return cell;
    }
    public void makeToast(int i){

        Toast imageToast = new Toast(context);
        LinearLayout toastLayout = new LinearLayout(context);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView image = new ImageView(context);
        TextView text = new TextView(context);

        if (i == BLOCK){
            image.setImageResource(R.drawable.block_icon);
            text.setText(R.string.bloqueo);
        } else if (i == WIN){
            //MediaPlayer ring= MediaPlayer.create(DesarrolloJuego.this, R.raw.win_sound);
            //ring.start();
            //System.out.println("Salio del ring");
            image.setImageResource(R.drawable.like_icon);
            text.setText(R.string.victoria);
        } else if (i == LOSE){
            //MediaPlayer ring= MediaPlayer.create(DesarrolloJuego.this, R.raw.win_sound);
            //ring.start();
            image.setImageResource(R.drawable.dislike_icon);
            text.setText(R.string.perdida);
        } else if (i == DRAW){
            image.setImageResource(R.drawable.balance_icon);
            text.setText(R.string.empate);
        } else if (i == TIMER){
            image.setImageResource(R.drawable.chrono_icon);
            text.setText(R.string.final_tiempo);
        }
        toastLayout.addView(image);
        toastLayout.addView(text);
        imageToast.setView(toastLayout);
        imageToast.setDuration(Toast.LENGTH_SHORT);
        imageToast.show();


    }
/*
    public void UpdateGame(Game game) {
        this.game = game;
        notifyDataSetChanged();
    }*/

}
