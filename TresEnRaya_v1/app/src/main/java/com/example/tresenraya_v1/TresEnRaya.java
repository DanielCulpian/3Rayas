package com.example.tresenraya_v1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class TresEnRaya extends AppCompatActivity {

    //Elementos a utilizar
    Button btnNewGame;
    Button btnMode;
    TextView txtVictory;
    TextView userN;
    Integer[] btnZ;

    //Frases vs AI mode
    String[] winS = {"VICTORIAAA!","Lo conseguiste... \nNo me lo esperaba jajaja.","Superaste a la IA! \nMuy habil!","Gran victoria!","Ganastee! \nBuena tactica."};
    String[] looseS  = {"Perdiste!\nMuy triste...","Casi lo consigues!\nPero no...Eres muy malo.","Confiaba en ti \nno lo volvere ha hacer...","Eres muy malo \nMe das pena","Tienes un don! \n La cagas siempre..."};
    String[] tieS = {"Empate!\nY eso que la IA esta en modo facil jejeje","Tablas! \nCreo que necesitas un respiro.","Empataste con la IA\nPractica mas...","Deberias intentarlo mas tarde\nUn empate es inadmisible...","Un empate mas, lo anoto."};

    //Frases 2 Players Mode
    String[] win1 = {"Victoria del Jugador 1!", "Es obvio que el Jugador 1\n ha jugado bastante mejor...", "Victoria aplastante del\nJugador 1!!", "Si tuviera que elegir, \nvolveria a apostar por el Jugador 1", "El Jugador 1 ha ganado\nusando mejores tacticas..."};
    String[] win2 = {"El jugador 2 es superior\nen todos los aspectos","VICTORIA!\nEl Jugador 2 ha gando!", "Todos tenemos un don...\nEl del Jugador 2 es aplastar al Jugador 1.", "Victoria del Jugador 2\n Lo anoto...", "Buena esa Jugador 2\nLuego te invito a algo..."};
    String[] tie = {"Empate!\nNo os enfadeis, sois amigos","Tablas! \nCreo que necesitais un respiro.","Sois rivales...\nLuchad mas a fondo","Empatee!!\nNo os quereis traicionar...\nDebiles de corazon!","Un empate mas, lo anoto."};

    //Tablero vacio
    int[] tablero = new int[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
    };

    //Variables del juego
    int state = 0; //0 (empate), 1 (victoria A), 2 (vistoria B)
    int fichaSet = 0; //Contador de fichas colocadas, maximo 9
    int turn = 1; //1 o -1

    //Array para guardar la posicion ganadora
    int[] posWin = new int[]{
      -1,-1,-1
    };

    int elector = -1;
    int gameMode = 0; //0 == 1 player | 1 == 2 players

    //Control del boton atras
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            //Para mostar un mensajeBox
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir de 3Rayas?")
                    .setPositiveButton("si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finishAffinity(); //Para finalizar cualquier proceso de la aplicacion
                        }

                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show(); //Construimos el messageBox con los atributos expecificados

        }
        return super.onKeyDown(keyCode, event);
    }

    //Inicio de la app
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3rayas);
        //Orientacion vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Recogemos el nombre del usuario loggeado
        Intent intent = getIntent();
        String name = intent.getStringExtra("Username");

        //Damos la bienvenida
        Utilidades.mostrarToast(this, "Hola " + name + ", bienvenido!", 2000);

        //Hacemos un set al texto del TextView que almacena el nombre del usuario loggeado
        userN = (TextView) findViewById(R.id.user);
        userN.setText(name);

        //Ponemos el TextView de la victoria invisble
        txtVictory = (TextView) findViewById(R.id.txt1);
        txtVictory.setVisibility(View.INVISIBLE);

        //Array de botones --> El tablero pero en botones
        btnZ = new Integer[]{
                R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9,
        };

        //Boton para hacer una partida nueva
        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        //Le añadimos un OnClick()
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            //Resetea la partida
            public void onClick(View v) {
                for (int i = 0; i < btnZ.length; i++){
                    ImageView b = (ImageView) findViewById(btnZ[i]);
                    b.setBackgroundResource(R.drawable.nullselected);
                    tablero[i] = 0;
                }//for
                state = 0;
                turn = 1;
                fichaSet = 0;
                txtVictory.setVisibility((View.INVISIBLE));
                txtVictory.setText("");
            }
        });

        //Para cambiar el modo de juego
        btnMode = (Button) findViewById(R.id.btnMode);
        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < btnZ.length; i++){
                    ImageView b = (ImageView) findViewById(btnZ[i]);
                    b.setBackgroundResource(R.drawable.nullselected);
                    tablero[i] = 0;
                }//for
                state = 0;
                turn = 1;
                fichaSet = 0;
                txtVictory.setVisibility((View.INVISIBLE));
                txtVictory.setText("");
                //Modos de juego
                if(gameMode == 0){btnMode.setText("VS IA"); gameMode = 1;}
                else{btnMode.setText("2 Players"); gameMode = 0;}
            }
        });


    }

    //Metodos del jugador
    public void mvSetFicha(View v){
        if(gameMode == 0){ //VS IA
            if (state == 0){ //Si no hay ganador todavia...
                turn = 1; //Turno del jugador

                //Para posicionar las imagenes de X y O
                int numBtn = Arrays.asList(btnZ).indexOf(v.getId());

                if (tablero[numBtn] == 0){ //Si la posicion a jugar esta vacia...
                    v.setBackgroundResource(R.drawable.cross);
                    tablero[numBtn] = 1; //Jugador pone su ficha
                    fichaSet += 1;
                    //Siempre se comprueba el estado de la partida y luego se intenta terminar la partida
                    state = comprobarState();
                    finish();
                    if (state == 0){ //Solo si no hay ganador
                        turn = -1; //Turno de la IA
                        ia(); //La IA empieza a moverse
                        fichaSet += 1;
                        state = comprobarState();
                        finish();
                    }
                }

            }
        }else{
            if(state == 0 && turn == 1){
                turn = 1;
                int numBtn = Arrays.asList(btnZ).indexOf(v.getId());
                if(tablero[numBtn] == 0){
                    v.setBackgroundResource(R.drawable.cross);
                    tablero[numBtn] = 1;
                    fichaSet += 1;
                    state = comprobarState();
                    finish();
                }
                turn = -1;
            }else if(turn == -1 && state == 0){
                turn = -1;
                int numBtn = Arrays.asList(btnZ).indexOf(v.getId());
                if(tablero[numBtn] == 0){
                    v.setBackgroundResource(R.drawable.circulo);
                    tablero[numBtn] = -1;
                    fichaSet += 1;
                    state = comprobarState();
                    finish();
                }
                turn = 1;
            }
        }

    }//mvSetFicha
    public void mvSetFichaB(View v){

    }//mvSetFichaB

    //Metodos de la IA
    public void ia(){
        int lvl = dropRand(); //Saca un numero aleatorio para definir su dificultad
        if(lvl == 1){ //Si el numero al azar es 1, la IA se equivocará y posicionará en una posicion aleatoria
                Random rand = new Random();
                int pos = rand.nextInt(tablero.length);
                while(tablero[pos] != 0){
                    pos = rand.nextInt(tablero.length);
                }//while
                ImageView b = (ImageView) findViewById(btnZ[pos]);
                b.setBackgroundResource(R.drawable.circulo);
                tablero[pos] = -1; //Sustituye la posicion '0' por -1 (ficha de la ia)
        }else{
            //Si no ha salido 1, la IA funcionará correctamente e intentará ganar
           iaWins();
        }
    }//ia()
    public void iaWins(){
        //Primera linea
        if(tablero[0] == -1 && tablero[1] == -1 && tablero[2] == 0){
            ImageView b = (ImageView) findViewById(btnZ[2]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[2] = -1;
        }else if(tablero[0] == -1 && tablero[2] == -1 && tablero[1] == 0){
            ImageView b = (ImageView) findViewById(btnZ[1]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[1] = -1;
        }else if(tablero[1] == -1 && tablero[2] == -1 && tablero[0] == 0){
            ImageView b = (ImageView) findViewById(btnZ[0]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[0] = -1;
            //Segunda linea
        }else if(tablero[3] == -1 && tablero[4] == -1 && tablero[5] == 0){
            ImageView b = (ImageView) findViewById(btnZ[5]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[5] = -1;
        }else if(tablero[3] == -1 && tablero[5] == -1 && tablero[4] == 0){
            ImageView b = (ImageView) findViewById(btnZ[4]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[4] = -1;
        }else if(tablero[4] == -1 && tablero[5] == -1 && tablero[3] == 0){
            ImageView b = (ImageView) findViewById(btnZ[3]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[3] = -1;
            //Tercera linea
        }else if(tablero[6] == -1 && tablero[7] == -1 && tablero[8] == 0){
            ImageView b = (ImageView) findViewById(btnZ[8]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[8] = -1;
        }else if(tablero[6] == -1 && tablero[8] == -1 && tablero[7] == 0){
            ImageView b = (ImageView) findViewById(btnZ[7]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[7] = -1;
        }else if(tablero[7] == -1 && tablero[8] == -1 && tablero[6] == 0){
            ImageView b = (ImageView) findViewById(btnZ[6]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[6] = -1;
            //Primera columna
        }else if(tablero[0] == -1 && tablero[3] == -1 && tablero[6] == 0){
            ImageView b = (ImageView) findViewById(btnZ[6]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[6] = -1;
        }else if(tablero[0] == -1 && tablero[6] == -1 && tablero[3] == 0){
            ImageView b = (ImageView) findViewById(btnZ[3]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[3] = -1;
        }else if(tablero[3] == -1 && tablero[6] == -1 && tablero[0] == 0){
            ImageView b = (ImageView) findViewById(btnZ[0]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[0] = -1;
            //SSegunda columna
        }else if(tablero[1] == -1 && tablero[4] == -1 && tablero[7] == 0){
            ImageView b = (ImageView) findViewById(btnZ[7]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[7] = -1;
        }else if(tablero[1] == -1 && tablero[7] == -1 && tablero[4] == 0){
            ImageView b = (ImageView) findViewById(btnZ[4]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[4] = -1;
        }else if(tablero[7] == -1 && tablero[4] == -1 && tablero[1] == 0){
            ImageView b = (ImageView) findViewById(btnZ[1]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[1] = -1;
            //Tercera columna
        }else if(tablero[2] == -1 && tablero[5] == -1 && tablero[8] == 0){
            ImageView b = (ImageView) findViewById(btnZ[8]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[8] = -1;
        }else if(tablero[2] == -1 && tablero[8] == -1 && tablero[5] == 0){
            ImageView b = (ImageView) findViewById(btnZ[5]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[5] = -1;
        }else if(tablero[5] == -1 && tablero[8] == -1 && tablero[2] == 0){
            ImageView b = (ImageView) findViewById(btnZ[2]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[2] = -1;
            //Diagonales
        }else if(tablero[0] == -1 && tablero[4] == -1 && tablero[8] == 0){
            ImageView b = (ImageView) findViewById(btnZ[8]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[8] = -1;
        }else if(tablero[0] == -1 && tablero[8] == -1 && tablero[4] == 0){
            ImageView b = (ImageView) findViewById(btnZ[4]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[4] = -1;
        }else if(tablero[8] == -1 && tablero[4] == -1 && tablero[0] == 0){
            ImageView b = (ImageView) findViewById(btnZ[0]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[0] = -1;
        }else if(tablero[2] == -1 && tablero[4] == -1 && tablero[6] == 0){
            ImageView b = (ImageView) findViewById(btnZ[6]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[6] = -1;
        }else if(tablero[2] == -1 && tablero[6] == -1 && tablero[4] == 0){
            ImageView b = (ImageView) findViewById(btnZ[4]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[4] = -1;
        }else if(tablero[6] == -1 && tablero[4] == -1 && tablero[2] == 0){
            ImageView b = (ImageView) findViewById(btnZ[2]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[2] = -1;
            //Si no encuentra una posicion en la cual pueda ganar...
        }else{
            //Intenta bloquear
            iaBlocks();
        }
    }//iaWins()
    public void iaBlocks(){
        //Primera linea
        if(tablero[0] == 1 && tablero[1] == 1 && tablero[2] == 0){
            ImageView b = (ImageView) findViewById(btnZ[2]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[2] = -1;
        }else if(tablero[0] == 1 && tablero[2] == 1 && tablero[1] == 0){
            ImageView b = (ImageView) findViewById(btnZ[1]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[1] = -1;
        }else if(tablero[1] == 1 && tablero[2] == 1 && tablero[0] == 0){
            ImageView b = (ImageView) findViewById(btnZ[0]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[0] = -1;
            //Segunda linea
        }else if(tablero[3] == 1 && tablero[4] == 1 && tablero[5] == 0){
            ImageView b = (ImageView) findViewById(btnZ[5]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[5] = -1;
        }else if(tablero[3] == 1 && tablero[5] == 1 && tablero[4] == 0){
            ImageView b = (ImageView) findViewById(btnZ[4]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[4] = -1;
        }else if(tablero[4] == 1 && tablero[5] == 1 && tablero[3] == 0){
            ImageView b = (ImageView) findViewById(btnZ[3]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[3] = -1;
            //Tercera linea
        }else if(tablero[6] == 1 && tablero[7] == 1 && tablero[8] == 0){
            ImageView b = (ImageView) findViewById(btnZ[8]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[8] = -1;
        }else if(tablero[6] == 1 && tablero[8] == 1 && tablero[7] == 0){
            ImageView b = (ImageView) findViewById(btnZ[7]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[7] = -1;
        }else if(tablero[7] == 1 && tablero[8] == 1 && tablero[6] == 0){
            ImageView b = (ImageView) findViewById(btnZ[6]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[6] = -1;
            //Primera columna
        }else if(tablero[0] == 1 && tablero[3] == 1 && tablero[6] == 0){
            ImageView b = (ImageView) findViewById(btnZ[6]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[6] = -1;
        }else if(tablero[0] == 1 && tablero[6] == 1 && tablero[3] == 0){
            ImageView b = (ImageView) findViewById(btnZ[3]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[3] = -1;
        }else if(tablero[3] == 1 && tablero[6] == 1 && tablero[0] == 0){
            ImageView b = (ImageView) findViewById(btnZ[0]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[0] = -1;
            //Segunda columna
        }else if(tablero[1] == 1 && tablero[4] == 1 && tablero[7] == 0){
            ImageView b = (ImageView) findViewById(btnZ[7]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[7] = -1;
        }else if(tablero[1] == 1 && tablero[7] == 1 && tablero[4] == 0){
            ImageView b = (ImageView) findViewById(btnZ[4]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[4] = -1;
        }else if(tablero[7] == 1 && tablero[4] == 1 && tablero[1] == 0){
            ImageView b = (ImageView) findViewById(btnZ[1]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[1] = -1;
            //Tercera columna
        }else if(tablero[2] == 1 && tablero[5] == 1 && tablero[8] == 0){
            ImageView b = (ImageView) findViewById(btnZ[8]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[8] = -1;
        }else if(tablero[2] == 1 && tablero[8] == 1 && tablero[5] == 0){
            ImageView b = (ImageView) findViewById(btnZ[5]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[5] = -1;
        }else if(tablero[5] == 1 && tablero[8] == 1 && tablero[2] == 0){
            ImageView b = (ImageView) findViewById(btnZ[2]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[2] = -1;
            //Diagonales
        }else if(tablero[0] == 1 && tablero[4] == 1 && tablero[8] == 0){
            ImageView b = (ImageView) findViewById(btnZ[8]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[8] = -1;
        }else if(tablero[0] == 1 && tablero[8] == 1 && tablero[4] == 0){
            ImageView b = (ImageView) findViewById(btnZ[4]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[4] = -1;
        }else if(tablero[8] == 1 && tablero[4] == 1 && tablero[0] == 0){
            ImageView b = (ImageView) findViewById(btnZ[0]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[0] = -1;
        }else if(tablero[2] == 1 && tablero[4] == 1 && tablero[6] == 0){
            ImageView b = (ImageView) findViewById(btnZ[6]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[6] = -1;
        }else if(tablero[2] == 1 && tablero[6] == 1 && tablero[4] == 0){
            ImageView b = (ImageView) findViewById(btnZ[4]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[4] = -1;
        }else if(tablero[6] == 1 && tablero[4] == 1 && tablero[2] == 0){
            ImageView b = (ImageView) findViewById(btnZ[2]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[2] = -1;
            //Si no puede bloquear...
        }else{
            //Posicionará su ficha en un lugar aleatorio
            Random rand = new Random();
            int pos = rand.nextInt(tablero.length);
            while(tablero[pos] != 0){
                pos = rand.nextInt(tablero.length);
            }//while
            ImageView b = (ImageView) findViewById(btnZ[pos]);
            b.setBackgroundResource(R.drawable.circulo);
            tablero[pos] = -1; //Sustituye la posicion '0' por -1 (ficha de la ia)
        }
    }//iaBlocks()

    public int dropRand(){
        //Para añadir una posiblidad de fallo
        int maxLvl = 100;
        Random wiRand = new Random();
        int lvl = wiRand.nextInt(maxLvl);
        return lvl;
    }//dropRand()

    //Metodos de control
    public int comprobarState(){
        int newState = 0;
        if (Math.abs(tablero[0] + tablero[1] + tablero[2]) == 3){
            posWin = new int[]{0,1,2};
            newState = 1*turn;
        }else if(Math.abs(tablero[3] + tablero[4] + tablero[5]) == 3){
            posWin = new int[]{3,4,5};
            newState = 1*turn;
        }else if(Math.abs(tablero[6] + tablero[7] + tablero[8]) == 3){
            posWin = new int[]{6,7,8};
            newState = 1*turn;
        }else if(Math.abs(tablero[0] + tablero[3] + tablero[6]) == 3){
            posWin = new int[]{0,3,6};
            newState = 1*turn;
        }else if(Math.abs(tablero[1] + tablero[4] + tablero[7]) == 3){
            posWin = new int[]{1,4,7};
            newState = 1*turn;
        }else if(Math.abs(tablero[2] + tablero[5] + tablero[8]) == 3){
            posWin = new int[]{2,5,8};
            newState = 1*turn;
        }else if(Math.abs(tablero[0] + tablero[4] + tablero[8]) == 3){
            posWin = new int[]{0,4,8};
            newState = 1*turn;
        }else if(Math.abs(tablero[2] + tablero[4] + tablero[6]) == 3){
            posWin = new int[]{2,4,6};
            newState = 1*turn;
        }else if(fichaSet == 9){
            newState = 2;
        }else{
            newState = 0;
        }
        return newState;
    }//comprobarState()

    public void finish(){
        int fichaWin = R.drawable.crosswin;
        if (state == 1 || state == -1){
            if(state == 1){
                if(gameMode == 0){ //Pierde IA
                    txtVictory.setVisibility((View.VISIBLE));
                    txtVictory.setText(winS[(elector = elector())]);
                    txtVictory.setTextColor(Color.GREEN);
                }else{ //Gana jugador 1
                    txtVictory.setVisibility((View.VISIBLE));
                    txtVictory.setText(win1[(elector = elector())]);
                    txtVictory.setTextColor(Color.RED);
                }
            }else{
                if(gameMode == 0){ //Gana IA
                    txtVictory.setVisibility((View.VISIBLE));
                    txtVictory.setText(looseS[(elector = elector())]);
                    txtVictory.setTextColor(Color.RED);
                    fichaWin = R.drawable.circulowin;
                }else{ //Gana jugador 2
                    txtVictory.setVisibility((View.VISIBLE));
                    txtVictory.setText(win2[(elector = elector())]);
                    txtVictory.setTextColor(Color.BLUE);
                    fichaWin = R.drawable.circulowin;
                }
            }
            //Cambia las imagenes de X y O a su version en verde para resaltar las posiciones ganadoras
            for(int i = 0; i < posWin.length; i++){
                ImageView b =findViewById(btnZ[posWin[i]]);
                b.setBackgroundResource(fichaWin);
            }//for
        }else if(state == 2){ //Empate
            if(gameMode == 0){
                txtVictory.setVisibility((View.VISIBLE));
                txtVictory.setTextColor(Color.GRAY);
                txtVictory.setText(tieS[(elector = elector())]);
            }else{
                txtVictory.setVisibility((View.VISIBLE));
                txtVictory.setTextColor(Color.GRAY);
                txtVictory.setText(tie[(elector = elector())]);
            }
        }
    }//finish

    //Selector de numero random
    public int elector(){
        int maxLvl = 5;
        Random wiRand = new Random();
        int elector = wiRand.nextInt(maxLvl);
        return elector;
    }//elector()
}