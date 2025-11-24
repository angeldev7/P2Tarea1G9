package Modelo;

/** Lista enlazada simple para almacenar películas */
public class ListaEnlazada {
    private Nodo cabeza;
    private int tamanio;

    public ListaEnlazada() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    // Insertar al inicio - O(1)
    public void insertarAlInicio(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("No se puede insertar película nula");
        }
        Nodo nuevoNodo = new Nodo(pelicula);
        nuevoNodo.setSiguiente(this.cabeza);
        this.cabeza = nuevoNodo;
        tamanio++;
    }

    // Insertar al final - O(n)
    public void insertarAlFinal(Pelicula pelicula) {
        if (pelicula == null) {
            throw new IllegalArgumentException("No se puede insertar película nula");
        }
        Nodo nuevoNodo = new Nodo(pelicula);
        if (estaVacia()) {
            this.cabeza = nuevoNodo;
        } else {
            Nodo actual = this.cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamanio++;
    }

    /** Elimina la primera película con el título dado (case-insensitive) */
    public boolean eliminarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }
        
        if (estaVacia()) {
            return false;
        }

        if (cabeza.getDato().getTitulo().equalsIgnoreCase(titulo.trim())) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return true;
        }
        
        Nodo anterior = cabeza;
        Nodo actual = cabeza.getSiguiente();
        while (actual != null) {
            if (actual.getDato().getTitulo().equalsIgnoreCase(titulo.trim())) {
                anterior.setSiguiente(actual.getSiguiente());
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        
        return false;
    }    /** Busca y retorna la primera película con el título dado (case-insensitive) */
    public Pelicula buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }
        
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getTitulo().equalsIgnoreCase(titulo.trim())) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    /** Retorna todas las películas en un array ordenado de inicio a fin */
    public Pelicula[] obtenerTodasLasPeliculas() {
        if (estaVacia()) {
            return new Pelicula[0];
        }
        
        Pelicula[] peliculas = new Pelicula[tamanio];
        Nodo actual = cabeza;
        int indice = 0;
        
        while (actual != null && indice < tamanio) {
            peliculas[indice++] = actual.getDato();
            actual = actual.getSiguiente();
        }
        
        return peliculas;
    }

    /**
     * Búsqueda aproximada por título usando distancia de Levenshtein.
     * @param query texto a buscar
     * @param maxDist distancia máxima permitida (e.g., 2 o 3)
     * @return arreglo con coincidencias en orden de la lista
     */
    public Pelicula[] buscarPorTituloAproximado(String query, int maxDist) {
        if (query == null || query.trim().isEmpty()) {
            return obtenerTodasLasPeliculas();
        }
        String q = normaliza(query);
        if (estaVacia()) return new Pelicula[0];

        // Recuento para tamaño
        int count = 0;
        Nodo actual = cabeza;
        while (actual != null) {
            String t = normaliza(actual.getDato().getTitulo());
            if (distanciaLevenshtein(q, t) <= maxDist || t.contains(q)) {
                count++;
            }
            actual = actual.getSiguiente();
        }
        Pelicula[] res = new Pelicula[count];
        int i = 0;
        actual = cabeza;
        while (actual != null) {
            String t = normaliza(actual.getDato().getTitulo());
            if (distanciaLevenshtein(q, t) <= maxDist || t.contains(q)) {
                res[i++] = actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return res;
    }

    private String normaliza(String s) {
        if (s == null) return "";
        String n = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return n.toLowerCase().trim();
    }

    // Distancia de Levenshtein iterativa O(m*n)
    private int distanciaLevenshtein(String a, String b) {
        int m = a.length(), n = b.length();
        if (m == 0) return n;
        if (n == 0) return m;
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        for (int j = 0; j <= n; j++) prev[j] = j;
        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[n];
    }

    /**
     * Ordena la lista en memoria usando el comparador dado.
     * Preserva las mismas instancias de Pelicula.
     */
    public void ordenar(java.util.Comparator<Pelicula> cmp) {
        if (cabeza == null || cabeza.getSiguiente() == null) return;
        
        // Copiar a lista temporal
        java.util.ArrayList<Pelicula> tmp = new java.util.ArrayList<>(tamanio);
        Nodo actual = cabeza;
        while (actual != null) {
            tmp.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        
        // Ordenar usando sort de Java (Timsort - O(n log n))
        tmp.sort(cmp);
        
        // Reconstruir enlaces con los mismos objetos
        cabeza = null;
        tamanio = 0;
        for (Pelicula p : tmp) {
            insertarAlFinal(p);
        }
    }

    /** Limpia todas las películas de la lista */
    public void limpiar() {
        cabeza = null;
        tamanio = 0;
    }

    /** Verifica si existe una película con el título dado */
    public boolean contiene(String titulo) {
        return buscarPorTitulo(titulo) != null;
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "ListaEnlazada[]";
        }
        
        StringBuilder sb = new StringBuilder("ListaEnlazada[");
        Nodo actual = cabeza;
        while (actual != null) {
            sb.append(actual.getDato().toString());
            if (actual.getSiguiente() != null) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }
        sb.append("]");
        return sb.toString();
    }
}
