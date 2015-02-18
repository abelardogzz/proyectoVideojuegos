/**
 * Juego
 *
 * ????????????????????????????????????
 *
 * @author ????????
 * @version ???
 * @date ??/???/??
 */
 
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;

/**
 *
 * @author AntonioM
 */
public class Juego extends Applet implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basPrincipal;         // Objeto principal
    private Base basMalo;         // Objeto malo
    
    private int iDireccion; //Direccion de nena
    private int iVidas; // Vidas del juego
    private int iCont;
    private int iPuntos; // Vidas del juego
    private Image imagameover; //Despliega imagen de fin de juego
    private boolean bFin;
    private boolean bPausa;
    private LinkedList <Base>lklMalos; //Lista Encadenada de fantasmas
    private LinkedList <Base>lklMalos2; //Lista Encadenada de fantasmas
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private AudioClip adcSonidoChimpy;   // Objeto sonido de Chimpy
    private AudioClip adcSonidoChimpy2;   // Objeto sonido de Chimpy
    
	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(800,500);
        
        // defino la imagen del malo
	URL urlImagenMalo = this.getClass().getResource("chimpy.gif");
        
            // se crea el objeto para malo 
            int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
            int iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;        
            basMalo = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                    getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
        
	URL urlImagenPrincipal = this.getClass().getResource("juanito.gif");
        // creo la lista de animales
        lklMalos = new LinkedList();
        // genero un numero azar de 3 a 8
        int iAzar = (int) (Math.random() * 5) + 10;
        
        // genero cada juanito y lo añado a la lista
        for (int iI = 0; iI < iAzar; iI ++) {
            iPosY = -(int) (Math.random() * 1000);    
            iPosX = (int) (Math.random() * (3 * getWidth() / 4));    
            // se crea el objeto Fantasmita
            Base basPrincipal = new Base(iPosX, iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));
            //Se agrega el fantasma a la lista
            lklMalos.add(basPrincipal);
        }
        URL urlImagenFantasma = this.getClass().getResource("fantasmita.gif");
        // creo la lista de animales
        lklMalos2 = new LinkedList();
        // genero un numero azar de 3 a 8
        iAzar = (int) (Math.random() * 2) + 8;
        
        // genero cada juanito y lo añado a la lista
        for (int iI = 0; iI < iAzar; iI ++) {
            iPosX = -(int) (Math.random() * 100);    
            iPosY = (int) (Math.random() * (3 * getHeight() / 4));    
            // se crea el objeto Fantasmita
            Base basFantasma = new Base(iPosX, iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenFantasma));
            //Se agrega el fantasma a la lista
            lklMalos2.add(basFantasma);
        }

        
        //Carga los sonidos
        URL urlSonidoChimpy = this.getClass().getResource("monkey2.wav");
        adcSonidoChimpy = getAudioClip (urlSonidoChimpy);
        adcSonidoChimpy.play();
         URL urlSonidoChimpy2 = this.getClass().getResource("monkey2.wav");
        adcSonidoChimpy2 = getAudioClip (urlSonidoChimpy2);
        adcSonidoChimpy2.play();
        
        addKeyListener(this);
        iPuntos=0;
        iDireccion = 0;
        iVidas= (int) ((Math.random() * (2)) + 4);
        iCont=0;
        bFin= false;
        bPausa= false;
        URL goURL = this.getClass().getResource("gameOver2.png");
        imagameover = Toolkit.getDefaultToolkit().getImage(goURL);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (!bFin) {
            if(!bPausa){
                actualiza();
                checaColision();
            } 
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
        switch(iDireccion){
            case 1:
                basMalo.setY(basMalo.getY()-5);
                break;
            case 2:
                basMalo.setY(basMalo.getY()+5);
                break;
            case 3:
                basMalo.setX(basMalo.getX()-5);
                break;
            case 4:
                basMalo.setX(basMalo.getX()+5);
                break;
        }
        for (Base basPrincipal : lklMalos){
               basPrincipal.setY(basPrincipal.getY()+ (int)((Math.random()*(2))+3));
        }
        for (Base basFantasma : lklMalos2){
               basFantasma.setX(basFantasma.getX()+ (11-iVidas));
        }
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        if(basMalo.getY() < 0){
            basMalo.setY(0);
        }
        if(basMalo.getY()+ basMalo.getAlto() > getHeight()){
            basMalo.setY(getHeight()-basMalo.getAlto());
        }
        if(basMalo.getX()<0){
            basMalo.setX(0);
        }
        if(basMalo.getX() + basMalo.getAncho() > getWidth()) { 
            // se fija la posicion en limite
            basMalo.setX(getWidth() - basMalo.getAncho());
        }
        //Intersecta con la pared Juanito
        for(Base basPrincipal : lklMalos){
            if(basPrincipal.getY()+basPrincipal.getAlto() > getHeight()){
                basPrincipal.setY(-(int) (Math.random() * 100));
                basPrincipal.setX((int) (Math.random() * (3 * getWidth() / 4)));
            }   
        }
        //Intersecta con la pared Fantasma
        for(Base basFantasmita : lklMalos2){
            if(basFantasmita.getX()+basFantasmita.getAncho() > getWidth()){
                basFantasmita.setX(-(int) (Math.random() * 100));
                basFantasmita.setY((int) (Math.random() * (3 * getHeight() / 4)));
            }   
        }
        //Intersecta Juanito con chango
        for(Base basPrincipal : lklMalos){
            if(basMalo.intersecta(basPrincipal)){
                basPrincipal.setX(-(int) (Math.random() * 100));
                basPrincipal.setY((int) (Math.random() * (3 * getHeight() / 4)));
                
                adcSonidoChimpy.play();
                iCont = iCont + 1;
            }
        }
        //Intersecta Fantasma con chango
        for(Base basFantasma : lklMalos2){
            if(basMalo.intersecta(basFantasma)){
                basFantasma.setX(-(int) (Math.random() * 100));
                basFantasma.setY((int) (Math.random() * (3 * getHeight() / 4)));
                iPuntos = iPuntos +1 ;
                adcSonidoChimpy2.play();
            }
        }
        if(iVidas == 0){
            bFin = true;
        }
        if(iCont == 5){
            iVidas = iVidas -1;
            iCont =0;
        }
        
            
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void update (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics graDibujo) {
        if(!bFin){
            // si la imagen ya se cargo
            if (basMalo != null) {
                    //Dibuja la imagen de principal en el Applet
                    for (Base basPrincipal : lklMalos) {
                        //Dibuja la imagen de dumbo en el Applet
                        basPrincipal.paint(graDibujo, this);
                    }
                    //Dibuja la imagen de malo en el Applet
                    basMalo.paint(graDibujo, this);
                    for (Base basFantasma : lklMalos2) {
                        //Dibuja la imagen de dumbo en el Applet
                        basFantasma.paint(graDibujo, this);
                    }
                    graDibujo.setColor(Color.red);
                    graDibujo.drawString("Puntos: "+ iPuntos, 400 , 100);
                    graDibujo.drawString("Vidas: "+ iVidas, 400 , 50);

            } // sino se ha cargado se dibuja un mensaje 
            else {
                    //Da un mensaje mientras se carga el dibujo	
                    graDibujo.drawString("No se cargo la imagen..", 20, 20);
            }
        }
        else{
            graDibujo.drawImage(imagameover, 150, 150, this);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // si presiono flecha para arriba
        if(keyEvent.getKeyCode() == KeyEvent.VK_W) {    
                iDireccion = 1;  // cambio la dirección arriba
        }
        // si presiono flecha para abajo
        else if(keyEvent.getKeyCode() == KeyEvent.VK_S) {    
                iDireccion = 2;   // cambio la direccion para abajo
        }
        // si presiono flecha a la izquierda
        else if(keyEvent.getKeyCode() == KeyEvent.VK_A) {    
                iDireccion = 3;   // cambio la direccion a la izquierda
        }
        // si presiono flecha a la derecha
        else if(keyEvent.getKeyCode() == KeyEvent.VK_D){    
                iDireccion = 4;   // cambio la direccion a la derecha
        }
        
        if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
            bFin=true;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_P){
            if(!bPausa){
                bPausa=true;
            }
            else
                bPausa=false;
        }
    }
}