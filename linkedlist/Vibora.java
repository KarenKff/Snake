/*Porqué no hacerlo con un ArryList?, porque la vibora comienza con pocos segmentos y luego va creciendo, en un ArrayList debe definir el tamaño antes.
 * Confeccionar el juego de la serpiente (snake) utilizando un objeto de la clase LinkedList para representar cada trozo de la misma.
 *https://www.youtube.com/watch?v=wnI40IVGgrU
https://www.tutorialesprogramacionya.com/javaya/detalleconcepto.php?punto=73&codigo=152&inicio=60
 */
package linkedlist;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.JFrame;
        
    public class Vibora extends JFrame implements Runnable, KeyListener {// Como va a ser una ventana, es un extends de la clase JFrame y debo importarla
// La interfaz KeyListener es para trabajar con el teclado en los juegos. Tiene 3 métodos.1) keyTyped- 2)keyReleased- 3)keyPressed
//Debemos poner la palabra implements (implementar)
        
    private LinkedList<Punto> lista = new LinkedList<Punto>(); // Vamos a almacenar los datos en una lista, de tipo Punto, ya que serán los elementos x e y
// Importar la clase LinkedList.   
// Definimos los atributos de la clase vibora
    private int columna, fila; // Dos variables- columna y fila donde se encuentra la cabeza de la vibora
    private int colfruta, filfruta; // 2 variables- columna y fila donde se encuentra la fruta (cuadradito azul)
    private boolean activo = true; // disponemos en false cuando finaliza el juego
    private Direccion direccion = Direccion.DERECHA;// Apenas comienza el juego la vibora avanza hacia la derecha
    private Thread hilo; // Hilo de nuestro programa de la clase Thread, es para que constantemente el programa esté en actividad, por ej 10 veces por segundo, que haga actividad, hasta que finalice el juego. (La vibora no se detiene)
    private int crecimiento = 0; // indica la cantidad de cuadraditos que debe crecer la vibora cuando come la fruta, la inicializamos en cero, porque no comió frutas aún.
    private Image imagen; // Para evitar el parpadeo del repaint()
    private Graphics bufferGraphics;// Se dibuja en memoria para evitar parpadeo// importar el método

    private  enum Direccion {
        IZQUIERDA, DERECHA, SUBE, BAJA // Este método guarda la dirección que toma la víbora, son 4.
    };

    class Punto {// clase interna para guardar los atributos x e y ( serían los cuadraditos de la víbora, los puntos que la componen)
        int x, y;

        public Punto(int x, int y) {// Constructor de la clase interna
            this.x = x;
            this.y = y;
        }
    }

    public Vibora() {// constructor sin parámetros
        // escuchamos los eventos de teclado para identificar cuando se presionan las
        // teclas de flechas, tiene que ir junto con los 3 métodos KeyListener. 
        this.addKeyListener(this);
        // la vibora comienza con cuatro cuadraditos
        lista.add(new Punto(4, 25)); //(la primera parte de la vibora va a estar en la columna 4, fila 25)
        lista.add(new Punto(3, 25));// ( la segunda parte de la vibora.)
        lista.add(new Punto(2, 25));//( la tercera parte de la vibora.)
        lista.add(new Punto(1, 25));//( la cuarta parte de la vibora.)
        // indicamos la ubicacion de la cabeza de la vibora
        columna = 4;
        fila = 25;
// generamos la coordenada de la fruta de forma aleatoria en nuestra cuadrícula de juego
        colfruta = (int) (Math.random() * 50);//( Hacemos una cuadrícula, de cero a 49 dentro del area de juego)
        filfruta = (int) (Math.random() * 50);
// creamos el hilo y lo arrancamos (con esto se ejecuta el metodo run()
        hilo = new Thread(this);// se crea un objeto de la clase Thread y hay pasarle por parámetro this,que es la referencia del objeto que va a escuchar el evento,
// apenas arranca  hilo.start() el objeto hilo, llama al método run y tengo que implementar la interfaz Runnable, antes de keyListener, en la class.Y se implementa el método Run
        hilo.start();
    }

    @Override
    public void run() {// Se implementa este método al importar Runnable, se ejecuta cuando se lanza el hilo.( hilo.Start)
        while (activo) {// genero un bucle infinito hasta que la bandera( activo) que definí en los atributos en true, se ponga en false.
            try {// Es una excepción que tengo que poner si o si, encerrar al método Thread.sleep, porque si no da error
                // dormimos el hilo durante una décima de segundo para que no se mueva tan
                // rapidamente la vibora
                Thread.sleep(100);
                // segun el valor de la variable direccion generamos la nueva posicion de la
                // cabeza de la vibora
                switch (direccion) {
                case DERECHA: //Si toma la dirección hacia la derecha, la cabeza debe avanzar hacia derecha ++
                    columna++;
                    break;
                case IZQUIERDA:
                    columna--;
                    break;
                case SUBE:
                    fila--;
                    break;
                case BAJA:
                    fila++;
                    break;
                }

                repaint();// borra todo y vuelve a ejecutar
                sePisa();// para ver si la cabeza de la vibora se pisa en algún segmento así misma
                // insertamos la coordenada de la cabeza de la vibora en la lista porque con el switch se incrementó o decrementó.
                lista.addFirst(new Punto(columna, fila));
// Para verificar si come fruta:
                if (this.verificarComeFruta() == false && this.crecimiento == 0) {
                    // si no estamos en la coordenada de la fruta y no debe crecer la vibora
                    // borramos el ultimo nodo de la lista
                    // esto hace que la lista siga teniendo la misma cantidad de nodos
                    // ls.borrarUltimo();
                    lista.remove(lista.size() - 1);// borro el último nodo de la lista
                } else {
                    // Si creciento es mayor a cero es que debemos hacer crecer la vibora
                    if (this.crecimiento > 0)
                        this.crecimiento--;
                }
                verificarFin();
            } catch (InterruptedException e) {//Termina la excepción try, abierta y que encierra al método Thread.sleep
                e.printStackTrace();  // try significa intentá ejecutar este bloque, si hay error se va a ejecutar con el catch, en esta línea nos dice en qué método hubo errores
            }
        }
    }

    // controlamos si la cabeza de la vibora se encuentra dentro de su cuerpo
    private void sePisa() {
        for (Punto p : lista) {
            if (p.x == columna && p.y == fila) {// Significa que se pisa
                activo = false;// y así le digo perdiste en un título en la ventana
                setTitle("Perdiste");
            }
        }
    }

    // controlamos si estamos fuera de la region del tablero
    private void verificarFin() {
        if (columna < 0 || columna >= 50 || fila < 0 || fila >= 50) {// se sale del área del juego
            activo = false;
            setTitle("Perdiste");
        }
    }

    private boolean verificarComeFruta() {
        if (columna == colfruta && fila == filfruta) {// para saber si come la fruta
            colfruta = (int) (Math.random() * 50);// cada vez que come la fruta hay que generar el valor aleatorio, para la nueva fruta, que cambie de lugar
            filfruta = (int) (Math.random() * 50);
            this.crecimiento = 10;// cada vez que come crece 10 cuadraditos
            return true;
        } else
            return false;
    }

    public void paint(Graphics g) { // un objeto de la clase Graphics como parámetro, es para dibujar
        super.paint(g);// llamamos al constructor de la clase padre ( super)
        if (!lista.isEmpty()) {// si la lista no está vacía vamos a hacer:
            if (imagen == null) {// primero va a ser null porque no está creada la imagen y luego se crea la imagen:
                imagen = createImage(this.getSize().width, this.getSize().height);// le doy el ancho y alto, igual que la pantalla
                bufferGraphics = imagen.getGraphics();// con esto guardo la referencia de getGraphics
            }
            // borramos la imagen de memoria y luego dibujamos en memoria, no sale nada en la pantalla aún
            bufferGraphics.clearRect(0, 0, getSize().width, getSize().width);
            // dibujar recuadro rojo de la ventana que muestra el juego
            bufferGraphics.setColor(Color.red);
            bufferGraphics.drawRect(20, 50, 500, 500);// le decimos el tamaño( 20 columna y 50 fila) y que sea rectángulo de 500 píxel *500
            // dibujar vibora
            for (Punto punto : lista) {// recorremos con un for
                bufferGraphics.fillRect(punto.x * 10 + 20, 50 + punto.y * 10, 8, 8); // en pixel el tamaño de los cuadraditos
            }
            // dibujar fruta
            bufferGraphics.setColor(Color.blue);// la fruta es azul
            bufferGraphics.fillRect(colfruta * 10 + 20, filfruta * 10 + 50, 8, 8);// en pixel el tamaño de la fruta
            g.drawImage(imagen, 0, 0, this);
        }
    }
// Analizo que tecla se presiona, si a la derecha o a la izquierda, si sube o baja
    public void keyPressed(KeyEvent arg0) {//KeyListener
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
            direccion = Direccion.DERECHA;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
            direccion = Direccion.IZQUIERDA;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_UP) {
            direccion = Direccion.SUBE;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
            direccion = Direccion.BAJA;
        }
    }

    public void keyReleased(KeyEvent arg0) {//KeyListener
    }

    public void keyTyped(KeyEvent arg0) {// Método de KeyListener
    }

     public static void main(String[] ar) {
        Vibora vibora1 = new Vibora();// creamos el objeto víbora1.
        vibora1.setBounds(0, 0, 600, 600);// La ubicamos con setBounds, en columna 0, fila 0, de 600 pixel *600
        vibora1.setVisible(true);// Con este método se hace visible el objeto
        vibora1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Cuando se presione la cruz de la ventana se cierre.
    }

}

