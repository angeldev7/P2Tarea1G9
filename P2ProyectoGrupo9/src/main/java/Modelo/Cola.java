package Modelo;

/** Cola (FIFO - First In First Out) para lista de películas por ver */
public class Cola {

    private Nodo frente; // frente de la cola (primero en salir)
    private Nodo fin;    // final de la cola (último en salir)
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    /** Verifica si la cola está vacía */
    public boolean estaVacia() {
        return frente == null;
    }

    /** Retorna el número de películas en la cola */
    public int tamanio() {
        return tamanio;
    }

    /** Encola una película al final (FIFO) */
    public void encolar(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("No se puede encolar una película nula");
        }
        
        Nodo nuevoNodo = new Nodo(pelicula);
        if (estaVacia()) {
            frente = fin = nuevoNodo;
        } else {
            fin.setSiguiente(nuevoNodo);
            fin = nuevoNodo;
        }
        tamanio++;
    }

    /** Desencola y retorna la película del frente (FIFO) */
    public Pelicula desencolar() {
        if (estaVacia()) {
            return null;
        }
        
        Pelicula pelicula = frente.getDato();
        frente = frente.getSiguiente();
        
        if (frente == null) {
            fin = null;
        }
        
        tamanio--;
        return pelicula;
    }

    /** Retorna la película del frente sin removerla */
    public Pelicula verFrente() {
        return estaVacia() ? null : frente.getDato();
    }

    /** Retorna todas las películas como array (de frente a fin) */
    public Pelicula[] aArray() {
        if (estaVacia()) {
            return new Pelicula[0];
        }
        
        Pelicula[] arr = new Pelicula[tamanio];
        Nodo actual = frente;
        int indice = 0;
        
        while (actual != null && indice < tamanio) {
            arr[indice++] = actual.getDato();
            actual = actual.getSiguiente();
        }
        
        return arr;
    }

    /** Limpia todas las películas de la cola */
    public void limpiar() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "Cola[]";
        }
        
        StringBuilder sb = new StringBuilder("Cola[FRENTE -> ");
        Nodo actual = frente;
        while (actual != null) {
            sb.append(actual.getDato().toString());
            if (actual.getSiguiente() != null) {
                sb.append(" -> ");
            }
            actual = actual.getSiguiente();
        }
        sb.append(" <- FIN]");
        return sb.toString();
    }
}
