package Modelo;

/** Pila (LIFO - Last In First Out) para películas vistas recientemente */
public class Pila {

    private Nodo tope;
    private int tamanio;

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    /** Verifica si la pila está vacía */
    public boolean estaVacia() {
        return tope == null;
    }

    /** Retorna el número de películas en la pila */
    public int tamanio() {
        return tamanio;
    }

    /** Apila una película en el tope (LIFO) */
    public void apilar(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("No se puede apilar una película nula");
        }
        Nodo nuevoNodo = new Nodo(pelicula);
        nuevoNodo.setSiguiente(tope);
        tope = nuevoNodo;
        tamanio++;
    }

    /** Desapila y retorna la película del tope (LIFO) */
    public Pelicula desapilar() {
        if (estaVacia()) {
            return null;
        }
        Pelicula pelicula = tope.getDato();
        tope = tope.getSiguiente();
        tamanio--;
        return pelicula;
    }

    /** Retorna la película del tope sin removerla */
    public Pelicula verTope() {
        return estaVacia() ? null : tope.getDato();
    }

    /** Retorna todas las películas como array (de tope a fondo) */
    public Pelicula[] aArray() {
        if (estaVacia()) {
            return new Pelicula[0];
        }
        
        Pelicula[] arr = new Pelicula[tamanio];
        Nodo actual = tope;
        int indice = 0;
        
        while (actual != null && indice < tamanio) {
            arr[indice++] = actual.getDato();
            actual = actual.getSiguiente();
        }
        
        return arr;
    }

    /** Limpia todas las películas de la pila */
    public void limpiar() {
        tope = null;
        tamanio = 0;
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "Pila[]";
        }
        
        StringBuilder sb = new StringBuilder("Pila[TOPE -> ");
        Nodo actual = tope;
        while (actual != null) {
            sb.append(actual.getDato().toString());
            if (actual.getSiguiente() != null) {
                sb.append(" -> ");
            }
            actual = actual.getSiguiente();
        }
        sb.append("]");
        return sb.toString();
    }
}
